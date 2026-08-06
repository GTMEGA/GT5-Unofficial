package gregtech.api.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import com.github.matt159.mcqlite.api.Database;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A utility to save all kinds of data that is a function of any chunk.
 * <p>
 * GregTech takes care of saving and loading the data from disk, and an efficient mechanism to locate it.
 * Subclass only need to define the exact scheme of each element data (by overriding the three protected abstract method)
 * <p>
 * Oh, there is no limit on how large your data is, though you'd not have the familiar NBT interface, but DataOutput
 * should be reasonably common anyway.
 * <p>
 * It should be noted this class is NOT thread safe.
 * <p>
 * Element cannot be null.
 * <p>
 * TODO: Implement automatic region unloading.
 *
 * @param <T> data element type
 * @author glease
 */
@ParametersAreNonnullByDefault
public abstract class GT_ChunkAssociatedData<T extends GT_ChunkAssociatedData.IData> {
	private static final ThreadLocal<Map<String, GT_ChunkAssociatedData<?>>> instances = ThreadLocal.withInitial(ConcurrentHashMap::new);
	protected static final int PAGE_SIZE = 250;

	static {
		// register event handler
		new EventHandler();
	}

	/**
	 * Data is stored as a `(world id -> (super region id -> super region data))` hash map.
	 * where super region's size is determined by regionSize.
	 * Here it is called super region, to not confuse with vanilla's regions.
	 */
	protected final Map<Integer, Map<ChunkCoordIntPair, T>> masterMap = new ConcurrentHashMap<>();
	private final Queue<T> writeQueue = new ConcurrentLinkedQueue<>();
	private final Queue<Long> readQueue = new ConcurrentLinkedQueue<>();

	protected final String mId;

	/**
	 * Initialize this instance.
	 *
	 * @param aId          An arbitrary, but globally unique identifier for what this data is
	 * @param elementType The class of this element type. Used to create arrays.
	 */
	protected GT_ChunkAssociatedData(String aId, Class<T> elementType) {
		if (!IData.class.isAssignableFrom(elementType))
			throw new IllegalArgumentException("Data type invalid");

		if (aId.contains("."))
			throw new IllegalArgumentException("ID cannot contains dot");

		this.mId = aId;

		if (instances.get().putIfAbsent(aId, this) != null)
			throw new IllegalArgumentException("Duplicate GT_ChunkAssociatedData: " + aId);
	}

	protected abstract void writeElements(Connection connection, Queue<T> writeQueue) throws SQLException;

	protected abstract void readAllElementsInWorld(Connection connection, int dimId) throws SQLException;

	protected abstract void readElements(Connection connection, Queue<Long> keys) throws SQLException;

	//	protected abstract T readElement(Connection connection, World world, int chunkX, int chunkZ) throws SQLException;

	protected abstract T createElement(World world, int chunkX, int chunkZ);

	/**
	 * Get a reference to data of the chunk that tile entity is in.
	 * The returned reference should be mutable.
	 */
	@NotNull
	public final T get(IGregTechTileEntity tileEntity) {
		return this.get(tileEntity.getWorld(), tileEntity.getXCoord() >> 4, tileEntity.getZCoord() >> 4);
	}

	@NotNull
	public final T get(Chunk chunk) {
		return this.get(chunk.worldObj, chunk.xPosition, chunk.zPosition);
	}

	@NotNull
	public final T get(World world, ChunkCoordIntPair coord) {
		return this.get(world, coord.chunkXPos, coord.chunkZPos);
	}

	@NotNull
	public final T get(World world, int chunkX, int chunkZ) {
		val dimId = world.provider.dimensionId;
		val chunkCoord = new ChunkCoordIntPair(chunkX, chunkZ);

		return this.masterMap.computeIfAbsent(dimId, key -> new ConcurrentHashMap<>())
							 .computeIfAbsent(chunkCoord, key -> this.createElement(world, chunkX, chunkZ));
	}

	protected final boolean isCreated(int dimId, int chunkX, int chunkZ) {
		val worldChunkData = this.masterMap.get(dimId);

		if (worldChunkData == null) {
			return false;
		}

		return worldChunkData.containsKey(new ChunkCoordIntPair(chunkX, chunkZ));
	}

	public void clear() {
		if (GT_Values.debugWorldData) {
			long dirtyObjects = masterMap.values().stream()
										 .map(Map::values)
										 .flatMap(Collection::stream)
										 .filter(T::isDirty)
										 .count();
			if (dirtyObjects > 0) {
                GT_Log.out.println("Clearing ChunkAssociatedData with " + dirtyObjects + " regions dirty. Data might have been lost!");
            }
		}
		masterMap.clear();
	}

	public void save(World world) {
		val dimId = world.provider.dimensionId;
		val worldChunkData = masterMap.get(dimId);

		if (worldChunkData == null) {
			return;
		}

		this.masterMap.getOrDefault(dimId, Collections.emptyMap())
					  .values()
					  .stream()
					  .filter(T::isDirty)
					  .forEach(this.writeQueue::offer);

		val workerThread = CompletableFuture.runAsync(() -> {
			val currentThread = Thread.currentThread();
			val currentName = currentThread.getName();

			currentThread.setName("Server thread");
            try (val connection = Database.getConnection()) {
				this.writeElements(connection, this.writeQueue);

				this.writeQueue.clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
				currentThread.setName(currentName);
			}
        });
	}

	/**
	 * Clear all mappings, regardless of whether they are dirty
	 */
	public static void clearAll() {
		for (GT_ChunkAssociatedData<?> d : instances.get().values()) {
			d.clear();
		}
	}

	/**
	 * Load data for all chunks for a given world.
	 * Current data for that world will be discarded. If this is what you intended, call {@link #save(World)} beforehand.
	 * <p>
	 * Be aware of the memory consumption though.
	 */
	public void loadAll(World world) {
		val dimId = world.provider.dimensionId;

		val workerThread = CompletableFuture.runAsync(() -> {
			val currentThread = Thread.currentThread();
			val currentName = currentThread.getName();

			currentThread.setName("Server thread");

			try (val connection = Database.getConnection()) {
				this.readAllElementsInWorld(connection, dimId);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				currentThread.setName(currentName);
			}
		});
	}

	protected static long makeKey(int dimId, int chunkX, int chunkZ) {
		val mask = 0x07FFFFFF;

		// create a long primary key. dimId gets 10 bits and chunk x/z each get 27 bits.
		// since chunkX and chunkZ are in chunk coord space, that effectively grants
		// another 4 bits of info to those sections. So this *will* fail if you go out far enough,
		// since we're only able to account for 31/32 bits of info, but normal minecraft terrain
		// fails far before that point, so it's a non-issue

		return ((long) dimId << 54) | (((long) chunkX & mask) << 27) | (chunkZ & mask);
	}

	protected static ChunkCoordIntPair keyToChunkCoord(long key) {
		val xMask = 0x07FFFFFFL << 27;
		val zMask = 0x07FFFFFFL;

		val chunkX = (key & xMask) >> 27;
		val chunkZ = key & zMask;

		return new ChunkCoordIntPair((int) chunkX, (int) chunkZ);
	}

	@Setter
	@Accessors(fluent = true)
	@SuperBuilder(toBuilder = true)
	public static abstract class IData {
		@Getter
		protected long location;
		protected boolean isDirty;

		/**
		 * mark the chunk data such that {@link #isDirty()} returns true
		 */
		public final void markDirty() {
			this.isDirty = true;
		}

		/**
		 * @return Whether the chunk data has been altered and requires saving
		 */
		public boolean isDirty() {
			return this.isDirty;
		}
	}

	public static class EventHandler {
		private EventHandler() {
			MinecraftForge.EVENT_BUS.register(this);
		}

		@SubscribeEvent
		public void onWorldSave(WorldEvent.Save e) {
			if (e.world.isRemote) {
				return;
			}

			for (GT_ChunkAssociatedData<?> d : instances.get().values()) {
				d.save(e.world);
			}
		}

		@SubscribeEvent
		public void onWorldUnload(WorldEvent.Unload e) {
			if (e.world.isRemote) {
				return;
			}

			for (GT_ChunkAssociatedData<?> d : instances.get().values()) {
				// there is no need to explicitly do a save here
				// forge will send a WorldEvent.Save on server thread before this event is distributed
				d.masterMap.remove(e.world.provider.dimensionId);
			}
		}
	}
}

package gregtech.common.misc.explosions;


import gregtech.common.entities.explosives.GT_Entity_Explosive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.World;


@Getter
@RequiredArgsConstructor
public abstract class GT_Explosion_PreCalculation {

    private final GT_Entity_Explosive explosionSource;

    private final World world;

    private final int sourceX, sourceY, sourceZ;

    public void tick() {

    }

}

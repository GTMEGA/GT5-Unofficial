package gregtech.api.graphs;


import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.NodePath;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;


// base Node class
public class Node {

    public class ReturnPair {

        public NodePath mReturnPath;

        public Lock returnLock;

    }

    public final TileEntity mTileEntity;

    public Node[] mNeighbourNodes = new Node[6];

    public NodePath[] mNodePaths = new NodePath[6];

    public Lock[] locks = new Lock[6];

    public ReturnPair returnValues = new ReturnPair();

    public NodePath mSelfPath;

    public ArrayList<ConsumerNode> mConsumers;

    public int mCreationTime;

    public int mNodeValue;

    public int mHighestNodeValue;


    public Node(int aNodeValue, TileEntity aTileEntity, ArrayList<ConsumerNode> aConsumers) {
        this.mNodeValue = aNodeValue;
        this.mTileEntity = aTileEntity;
        this.mConsumers = aConsumers;
        mHighestNodeValue = aNodeValue;
        // you don't want to generate map multiple times in the same tick
        mCreationTime = MinecraftServer.getServer().getTickCounter();
    }

}

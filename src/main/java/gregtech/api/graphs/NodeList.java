package gregtech.api.graphs;

// keep track on which node is being looked for across the recursive functions
public class NodeList {
    protected Node[] mNodes;
    protected int mCounter = 0;
    public NodeList(Node[] mNodes) {
        this.mNodes = mNodes;
    }

    public Node getNextNode() {
        if (++mCounter < mNodes.length)
            return mNodes[mCounter];
        else
            return null;
    }

    public Node getNode() {
        if (mCounter < mNodes.length)
            return mNodes[mCounter];
        else
            return null;
    }
}

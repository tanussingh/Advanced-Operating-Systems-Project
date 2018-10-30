import java.io.Serializable;

public class Packet implements Serializable {
    private int broadcastNode;
    private int sourceId;
    private String msg;

    public void buildPacket(int broadcast, int sourceId, String msg) {
        this.broadcastNode = broadcast;
        this.sourceId = sourceId;
        this.msg = msg;
    }

    public int getBroadcastNode() {
        return this.broadcastNode;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "Message [broadcastNode=" + this.broadcastNode + ", sourceId=" + this.sourceId + ", msg=" + this.msg + "]";
    }
}
import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
    private int sourceId;
    private int destId;
    private String msg;

    public void buildPacket(int sourceId, int destId, String msg) {
        this.sourceId = sourceId;
        this.destId = destId;
        this.msg = msg;
    }

    public void setSourceId (int i) {
        this.sourceId = i;
    }

    public void setDestId (int i) {
        this.destId = i;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public int getDestId() {
        return this.destId;
    }

    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "Message [sourceId=" + sourceId + ", destId=" + this.destId + ", msg=" + this.msg + "]";
    }
}
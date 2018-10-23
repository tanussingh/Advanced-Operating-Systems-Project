import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
    private int sourceId;
    private String msg;

    public void buildPacket(int sourceId, String msg) {
        this.sourceId = sourceId;
        this.msg = msg;
    }

    public void setSourceId (int i) {
        this.sourceId = i;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "Message [sourceId=" + sourceId + ", msg=" + this.msg + "]";
    }
}
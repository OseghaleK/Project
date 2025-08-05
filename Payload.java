// UCID: oka
// Date: 2025-07-29
// Summary: Milestone-3 updates - adds away/spectate payloads and board export.
package Common;

import java.io.Serializable;

public class Payload implements Serializable {
    private static final long serialVersionUID = 3460669336904937431L;

    private PayloadType payloadType = PayloadType.MESSAGE;
    private String message;
    private String from;      // sender name
    private long fromId;      // sender id
    private String room;

    public Payload() { }

    public Payload(PayloadType type) {
        this.payloadType = type;
    }

    // Many call sites use (type, message)
    public Payload(PayloadType type, String message) {
        this.payloadType = type;
        this.message = message;
    }

    // Some code calls p.getType()
    public PayloadType getType() { return payloadType; }
    public PayloadType getPayloadType() { return payloadType; }
    public void setPayloadType(PayloadType payloadType) { this.payloadType = payloadType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public long getFromId() { return fromId; }
    public void setFromId(long fromId) { this.fromId = fromId; }

    // Compatibility with code that uses "sender"
    public void setSenderId(int id) { this.fromId = id; }
    public void setSenderName(String name) { this.from = name; }
    public int getSenderId() { return (int) this.fromId; }
    public String getSenderName() { return this.from; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    /** Convenience factory for server/system notices. */
    public static Payload systemMessage(String msg) {
        Payload p = new Payload(PayloadType.SERVER_NOTIFICATION, msg);
        return p;
    }
}





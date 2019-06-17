package pitcher;

import java.nio.ByteBuffer;

public class Message {
    private int id;
    private int size;
    private long sentTimestamp;
    private long catcherTimestamp;
    private long receivedTimestamp;

    public Message(int id,
                   int size,
                   long sentTimestamp,
                   long catcherTimestamp,
                   long receivedTimestamp) {
        this.id = id;
        this.size = size;
        this.sentTimestamp = sentTimestamp;
        this.catcherTimestamp = catcherTimestamp;
        this.receivedTimestamp = receivedTimestamp;
    }

    public long getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public void setReceivedTimestamp(long receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    public int getId() {
        return this.id;
    }

    public int getSize() {
        return this.size;
    }

    public long getSentTimestamp() {
        return this.sentTimestamp;
    }

    public long getCatcherTimestamp() {
        return catcherTimestamp;
    }

    public void setCatcherTimestamp(long catcherTimestamp) {
        this.catcherTimestamp = catcherTimestamp;
    }

    public long getAtoBTimeInMillis(){
        return catcherTimestamp - sentTimestamp;
    }

    public long getBtoATimeInMillis() {
        return receivedTimestamp - catcherTimestamp;
    }

    public long getRTTInMillis() {
        return getAtoBTimeInMillis() + getBtoATimeInMillis();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            return ((Message) obj).getId() == this.getId();
        } else {
            return false;
        }
    }

    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(this.size);
        buffer.putInt(this.id);
        buffer.putInt(this.size);
        buffer.putLong(this.sentTimestamp);
        buffer.putLong(this.catcherTimestamp);
        buffer.putLong(this.receivedTimestamp);
        return buffer.array();
    }

    public static Message deserialize(ByteBuffer byteBuffer) {
        return new Message(byteBuffer.getInt(),
                byteBuffer.getInt(),
                byteBuffer.getLong(),
                byteBuffer.getLong(),
                byteBuffer.getLong());
    }
}

package clientTwo.model;

/**
 * This class represents a record.
 */
public class Record {
    private long startTime;
    private String resType;
    private float latency;
    private int status;

    /**
     * Class constructor.
     *  @param startTime of the request
     * @param resType of the HTTP response
     */
    public Record(long startTime, String resType, float latency, int status) {
        this.startTime = startTime;
        this.resType = resType;
        this.latency = latency;
        this.status = status;
    }

    /**
     * Getter method.
     *
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Getter method.
     *
     * @return the resType
     */
    public String getReqType() {
        return resType;
    }

    /**
     * Getter method.
     *
     * @return the latency
     */
    public float getLatency() {
        return latency;
    }

    /**
     * Getter method.
     *
     * @return the status code
     */
    public int getCode() {
        return status;
    }
}

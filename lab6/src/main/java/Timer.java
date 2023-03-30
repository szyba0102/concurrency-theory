public class Timer{
    private final long startTime;
    private long stopTime;
    private final long startTimeN;
    private long stopTimeN;
    private long time;
    private long timeN;
    private boolean stopped;

    public Timer(){
        this.startTime = System.currentTimeMillis();
        this.stopTime = 0;
        this.startTimeN = System.nanoTime();
        this.stopTimeN = 0;
        this.time = 0;
        this.stopped = false;
    }

    public void stop(){
        if (!stopped) {
            stopTime = System.currentTimeMillis();
            stopTimeN = System.nanoTime();
            time = (stopTime - startTime);
            timeN = (stopTimeN - startTimeN);
            stopped = true;
        }
    }

    public String resultTime(){
        return String.valueOf(time);
    }

    public String resultNanoTime(){
        return String.valueOf(timeN);
    }
}

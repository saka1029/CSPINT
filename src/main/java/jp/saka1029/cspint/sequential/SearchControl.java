package jp.saka1029.cspint.sequential;

public class SearchControl {

    private boolean stopped = false;
    public boolean isStopped() { return stopped; }
    public void stop() { this.stopped = true; }

}

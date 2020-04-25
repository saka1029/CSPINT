package jp.saka1029.cspint.sequential;

import java.util.concurrent.atomic.AtomicBoolean;

public class SerchControl {
    
    private AtomicBoolean value = new AtomicBoolean(true);
    public boolean value() { return value.get(); }
    public void stop() { this.value.set(false); }

}

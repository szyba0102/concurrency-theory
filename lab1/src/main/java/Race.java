import javax.swing.plaf.TableHeaderUI;
import java.io.*;

class Counter {
    private int _val;
    public Counter(int n) {
        _val = n;
    }
    public void inc() {
        _val++;
    }
    public void dec() {
        _val--;
    }
    public int value() {
        return _val;
    }
}

// Watek, ktory inkrementuje licznik 100.000 razy
class IThread extends Thread {
    public Counter cnt;

    public IThread(Counter cnt){this.cnt = cnt;}

    public void run() {
        for (int i=0; i<100000; i++){
            cnt.inc();
        }
    }
}

// Watek, ktory dekrementuje licznik 100.000 razy
class DThread extends Thread {
    public Counter cnt;

    public DThread(Counter cnt){this.cnt = cnt;}

    public void run() {
        for (int i=0; i<100000; i++){
            cnt.dec();
        }
    }
}

public class Race {
    public static void main(String[] args) throws InterruptedException, IOException {
        Counter cnt = new Counter(0);
        IThread incThread = new IThread(cnt);
        DThread decThread = new DThread(cnt);

        incThread.start();
        decThread.start();

        incThread.join();
        decThread.join();

        System.out.println("stan=" + cnt.value());
    }
}


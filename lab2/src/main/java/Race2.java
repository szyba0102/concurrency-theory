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

class Semafor {
    //private boolean _stan = true;
    private int _czeka;

    public Semafor(int availableResources) {
        _czeka = availableResources;
    }


    public synchronized void P() throws InterruptedException {
        while(_czeka <= 0){
            wait();
        }
        _czeka -= 1;
    }

    public synchronized void V() throws InterruptedException {
        _czeka += 1;
        notify();
    }
}

class IThread extends Thread {
    private Counter _cnt;
    public Semafor semafor;
    public IThread(Counter c, Semafor semafor) {
        _cnt = c;
        this.semafor = semafor;
    }
    public void run() {
        for (int i = 0; i < 1000000; ++i) {
            try {
                semafor.P();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _cnt.inc();
            try {
                semafor.V();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class DThread extends Thread {
    private Counter _cnt;
    public Semafor semafor;
    public DThread(Counter c, Semafor semafor) {
        _cnt = c;
        this.semafor = semafor;
    }

    public void run() {
        for (int i = 0; i < 1000000; ++i) {
            try {
                semafor.P();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _cnt.dec();
            try {
                semafor.V();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Race2 {
    public static void main(String[] args) {
//        Counter cnt = new Counter(0);
//        Semafor semafor = new Semafor(1);
//        IThread it = new IThread(cnt,semafor);
//        DThread dt = new DThread(cnt,semafor);
//
//
//
//        it.start();
//        dt.start();
//
//        try {
//            it.join();
//            dt.join();
//        } catch(InterruptedException ignored) { }
//
//        System.out.println("value=" + cnt.value());
        final byte[] bytes = new byte[100];
        bytes[0] = 1;
        bytes[99] = 1;
        System.out.println(bytes[1]);
        System.out.println(bytes[2]);
        System.out.println(bytes[3]);
        System.out.println(bytes[99]);

    }
}

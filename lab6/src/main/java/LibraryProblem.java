import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Reader extends Thread {
    private Library library;
    public Reader(Library library) {
        this.library = library;
    }
    @Override
    public void run() {
        try {
            library.Read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Writer extends Thread {
    private Library library;
    public Writer(Library library) {
        this.library = library;
    }
    @Override
    public void run() {
        try {
            library.Write();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Library {
    private final Lock lock = new ReentrantLock();
    private final Semaphore semaphore ;
    private final int readerAmount;

    public Library(int readerAmount){
        this.readerAmount = readerAmount;
        semaphore = new Semaphore(readerAmount);
    }

    public void Read() throws InterruptedException {
        semaphore.acquire();
        System.out.println("read");
        semaphore.release();

    }

    public void Write() throws InterruptedException {
        lock.lock();
        try {
            for (int i = 0; i < readerAmount; i++) {
                semaphore.acquire();
            }
            System.out.println("write");
            semaphore.release(readerAmount);
        } finally {
            lock.unlock();
        }
    }
}

public class LibraryProblem{
//    public static void main(String[] args) throws InterruptedException {
//        int readersAmount = 100;
//        int writersAmount = 10;
//        int i;
//        final Library library = new Library(readersAmount);
//        Reader[] reader = new Reader[readersAmount];
//        Writer[] writer = new Writer[writersAmount];
//
//
//        for (i = 0; i < readersAmount; ++i) {
//            reader[i] = new Reader(library);
//        }
//        for (i = 0; i < writersAmount; ++i) {
//            writer[i] = new Writer(library);
//        }
//
//        long millisActualTime = System.currentTimeMillis();
//        for (i = 0; i < readersAmount; ++i) {
//            reader[i].start();
//        }
//        for (i = 0; i < writersAmount; ++i) {
//            writer[i].start();
//        }
//        for (i = 0; i < readersAmount; ++i) {
//            try {
//                reader[i].join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        for (i = 0; i < writersAmount; ++i) {
//            try {
//                writer[i].join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        long executionTime = System.currentTimeMillis() - millisActualTime;
//        System.out.println(executionTime);
//    }
    public static File file = new File("resultsSem.csv");
    public static void main(String[] args) {
        String nl = "\n";
        for (int i = 1; i <= 10; i++){
            for (int j = 10; j <= 100; j = j + 10){
                iteration(i, j);
            }
            try {
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(nl.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void iteration(int x , int y) {
        Timer timer = new Timer();
        Library library = new Library(y);
        Reader[] reader = new Reader[y];
        Writer[] writer = new Writer[x];
        for (int i = 0; i < y; ++i) {
            reader[i] = new Reader(library);
            reader[i].start();
        }
        for (int i = 0; i < x; ++i) {
            writer[i] = new Writer(library);
            writer[i].start();
        }
        for (int i = 0; i < y; ++i) {
            try {
                reader[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < x; ++i) {
            try {
                writer[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        timer.stop();
        String result = timer.resultNanoTime()+";";
        //System.out.println(result);
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(result.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

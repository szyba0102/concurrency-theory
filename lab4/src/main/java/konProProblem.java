import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Producer extends Thread {
    private final Buffer _buf;
    private final int quantity;
    private final int value;

    public Producer(Buffer _buf, int quantity, int value){
        this._buf = _buf;
        this.quantity = quantity;
        this.value = value;
    }

    public void run() {
        _buf.put(value,quantity);
    }
}

class Consumer extends Thread {
    private final Buffer _buf;
    private final int quantity;

    public Consumer(Buffer _buf, int quantity){
        this._buf = _buf;
        this.quantity = quantity;
    }

    public void run() {

        ArrayList<Integer> x = _buf.get(quantity);
        for(int i=0;i <x.size(); i++){
            System.out.println("value: " + x.get(i));
        }
    }
}

class Buffer{
    private final int[] table;
    private int putInx = 0;
    private int getInx = 0;
    private int counter = 0;
    private int producentsQuantity;
    private int consumersQuantity;

    private final ReentrantLock lock = new ReentrantLock();
    private final ReentrantLock producerLock = new ReentrantLock(true);
    private final ReentrantLock consumerLock = new ReentrantLock(true);
    private final Condition Empty = lock.newCondition();
    private final Condition Full  = lock.newCondition();

    public Buffer(int size, int producentsQuantity, int consumersQuantity){
        this.table = new int[size];
        this.producentsQuantity = producentsQuantity;
        this.consumersQuantity = consumersQuantity;
    }

    public void put(int value, int quantity) {
        producerLock.lock();
        lock.lock();
        try {
            while (table.length - counter < quantity) {
                if(consumersQuantity == 0){
                    return;
                }
                try {
                    Full.await();
                } catch (InterruptedException e) {
                    return;
                }
            }
            for (int i = 0; i < quantity; i++) {
                table[putInx] = value;
                putInx++;
                if (putInx == table.length) {
                    putInx = 0;
                }
            }
            counter += quantity;
            Empty.signal();
        } finally {
            lock.unlock();
            producentsQuantity -= 1;
            producerLock.unlock();
        }
    }

    public ArrayList<Integer> get(int quantity){
        consumerLock.lock();
        lock.lock();
        try {
            ArrayList<Integer> result = new ArrayList();
            while (counter < quantity) {
                if(producentsQuantity == 0){
                    result.add(-1);
                    return result;
                }
                try {
                    Empty.await();
                } catch (InterruptedException e) {
                    return result;
                }
            }
            for (int i = 0; i < quantity; i++) {
                result.add(table[getInx]);
                table[getInx] = -1;
                getInx++;
                if (getInx == table.length) {
                    getInx = 0;
                }
            }
            counter -= quantity;
            Full.signal();
            return result;
        } finally {
            consumersQuantity -= 1;
            lock.unlock();
            consumerLock.unlock();
        }
    }
}

public class konProProblem {
    public static void main(String[] args) throws InterruptedException {
        int pr = 75;
        int con = 50;
        int M =15;
        Buffer buffer = new Buffer(2*M,pr,con);
        long millisActualTime = System.currentTimeMillis();
        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();


        for(int i=0;i < pr; i++){
            producers.add(new Producer(buffer,i%M + 1, 2));
        }

        for(int i=0; i<con; i++){
            consumers.add(new Consumer(buffer,i%M+1));
        }

        for(int i=0;i < pr; i++){
            producers.get(i).start();
        }

        for(int i=0; i<con; i++){
            consumers.get(i).start();
        }

        for(int i=0;i < pr; i++){
            producers.get(i).join();
        }

        for(int i=0; i<con; i++){
            consumers.get(i).join();
        }

        long executionTime = System.currentTimeMillis() - millisActualTime; // czas wykonania programu w milisekundach.
        System.out.println(executionTime);
    }
}



package ex1a;

import java.util.ArrayList;

class Producer extends Thread {
    private Buffer _buf;

    public Producer(Buffer _buf){this._buf = _buf;}

    public void run() {
        for (int i = 0; i < 100; ++i) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _buf.put(i);
        }
    }
}

class Consumer extends Thread {
    private Buffer _buf;
    private int[] tab;
    public Consumer(Buffer _buf, int[] tab){
        this.tab = tab;
        this._buf = _buf;
    }
    public Consumer(Buffer _buf){
        this._buf = _buf;}

    public void run() {
        for (int i = 0; i < 100; ++i) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int x = _buf.get();
            System.out.println(x);
            tab[x] += 1;
        }
    }
}

class Buffer {
    private final int[] table;
    private int putInx = 0;
    private int getInx = 0;
    private int counter = 0;

    public Buffer(int size){
        this.table = new int[size];
    }

    public synchronized void put(int i) {
        while (counter == table.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        table[putInx] = i;
        putInx ++;
        if(putInx == table.length){
            putInx = 0;
        }
        counter++;
        notify();
    }

    public synchronized int get(){
        while(counter == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int var = table[getInx];
        table[getInx] = -1;
        getInx ++;
        if(getInx == table.length){
            getInx = 0;
        }
        counter --;
        notify();
        return var;

    }


}

public class PKmon {
    public static void main(String[] args) throws InterruptedException {
        //Buffer buffer = new Buffer(8);

        /*Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}*/



        int[] count = new int[100];
        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();
        int producersNumber = 10;
        int consumersNUmber = 5;

        Buffer buffer = new Buffer(8);

        for(int i=0;i<producersNumber;i++){
            producers.add(new Producer(buffer));
            producers.get(i).setName("Producer: "+ i );
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.add(new Consumer(buffer,count));
            consumers.get(i).setName("Consumer: " + i);
        }

        for(int i=0;i<producersNumber;i++){
            producers.get(i).start();
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.get(i).start();
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.get(i).join();
        }

        /*for(int i = 0; i<count.length; i++){
                System.out.println(count[i]);

        }*/
        for(int i=0;i<producersNumber;i++){
            producers.get(i).join();
        }



    }
}

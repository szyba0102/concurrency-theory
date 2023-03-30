package ex2b;

import java.util.ArrayList;

class Semafor2b extends Thread {
    //private boolean _stan = true;
    private int _czeka;
    private final int maxAvailableResources;


    public Semafor2b(int availableResources, int startValue) {
        _czeka = startValue;
        this.maxAvailableResources = availableResources;
    }


    public synchronized void P() throws InterruptedException {
        while(_czeka <= 0){
            System.out.println("Czekam w P, czeka: " + _czeka + " " + this.getName());
            wait();
        }
        _czeka -= 1;
        notifyAll();
    }

    public synchronized void V() throws InterruptedException {
        //while(_czeka == maxAvailableResources){
            //wait();
        //}
        _czeka += 1;
        notifyAll();
    }

    public void setValue(int i) {
        _czeka = i;
    }

    public synchronized int getczek(){
        return _czeka;
    }
}
class Producer2b extends Thread {
    private Buffer2b _buf;
    //private Semafor semafor;


    public Producer2b(Buffer2b _buf){
        this._buf = _buf;
        //this.semafor = semafor;
    }

    public void run() {
        for (int i = 0; i <10; ++i) {

            try {
                _buf.put(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println(this.getName() + " puts: " + i + " size: " + _buf.getList().size());

        }


        //System.out.println(this.getName() + " is done. ALive: " + _buf.getProducersAlive());
    }
}

class Consumer2b extends Thread {
    private Buffer2b _buf;
    //private Semafor semafor;

    public Consumer2b(Buffer2b _buf){
        this._buf = _buf;
    }

    public void run() {
        for (int i = 0; i < 10; ++i) {
            System.out.println("Petlna i: " + i + " nazwa: " + this.getName());
            try {
                int k = _buf.get();
                System.out.println(this.getName() + " gets: " + k );
            } catch (InterruptedException e) {
                System.out.println("jebac to goowno");
                e.printStackTrace();
            }
            System.out.println("Petlna wychodze i: " + i + " nazwa: " + this.getName());
        }
        //System.out.println(this.getName() + " is done");

    }
}

class Buffer2b extends Thread{
    //private int maxSize;
    private int[] table;
    private Semafor2b semafor1;
    private Semafor2b semafor2;
    private Semafor2b semaforputtInx;
    private Semafor2b semaforgetInx;
    private int putInx = 0;
    private int getInx = 0;
    private int counter = 0;

    public Buffer2b(int maxSize, Semafor2b semafor1, Semafor2b semafor2, Semafor2b semaforputtInx, Semafor2b semaforgetInx){
        //this.maxSize = maxSize;
        this.semafor1 = semafor1;
        this.semafor2 = semafor2;
        this.semaforputtInx = semaforputtInx;
        this.semaforgetInx = semaforgetInx;
        table = new int[maxSize];
    }

    public synchronized int getPutInx(){
        int result = putInx;
        putInx ++;
        if(putInx == table.length){
            putInx = 0;
        }
        //counter++;
        return result;
    }
    public synchronized void  put(int i) throws InterruptedException {
        semafor1.P();
        System.out.println(getName());
        //System.out.println("jestem w put i: " + i);
        semaforputtInx.P();
        //System.out.println("Szukam putInx");
        int inx = getPutInx();
        //System.out.println("znalazlem putINx");
        semaforputtInx.V();
        table[inx] = i;

        semafor2.V();
        System.out.println("wartosc sema " + semafor2.getczek());
    }

    public synchronized int getGetInx(){
        int result = getInx;
        getInx ++;
        if(getInx == table.length){
            getInx = 0;
        }
        //counter --;
        return result;
    }
    public synchronized int get() throws InterruptedException {
        System.out.println("wartosc sema w get  " + semafor2.getczek());
        semafor2.P();
        //System.out.println("jestem w get ");
        semaforgetInx.P();
        //System.out.println("Szukam getInx");
        int inx = getGetInx();
        //System.out.println("znalazlem getINx");
        semaforgetInx.V();
        int var = table[inx];
        table[inx] = -1;
        semafor1.V();

        return var;
    }
}

public class PKmon2 {
    public static void main(String[] args) throws InterruptedException {


        //Producer producer = new Producer(buffer);
        //Consumer consumer = new Consumer(buffer);

        ArrayList<Producer2b> producers = new ArrayList<>();
        ArrayList<Consumer2b> consumers = new ArrayList<>();
        int producersNumber = 2;
        int consumersNUmber = 2;

        Semafor2b semafor1 = new Semafor2b(10,10);
        Semafor2b semafor2 = new Semafor2b(10,0);
        //semafor2.setValue(1);
        Semafor2b semaforputInx = new Semafor2b(1,1);
        Semafor2b semaforgetInx = new Semafor2b(1,1);
        Buffer2b buffer = new Buffer2b(10,semafor1,semafor2,semaforputInx,semaforgetInx);


        for(int i=0;i<producersNumber;i++){
            producers.add(new Producer2b(buffer));
            producers.get(i).setName("Producer: "+ i );
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.add(new Consumer2b(buffer));
            consumers.get(i).setName("Consumer: " + i);
        }

        //producer.start();
        //consumer.start();

        for(int i=0;i<producersNumber;i++){
            producers.get(i).start();
            consumers.get(i).start();
        }
        for(int i=0;i<consumersNUmber;i++){
            //consumers.get(i).start();
        }

        //producer.join();
        //consumer.join();

        for(int i=0;i<producersNumber;i++){
            producers.get(i).join();
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.get(i).join();
        }

        //System.out.println(buffer.getList().isEmpty());

    }
}


import java.util.ArrayList;

class Semafor {
    private int _czeka;

    public Semafor(int availableResources) {
        _czeka = availableResources;
    }


    public synchronized void P() throws InterruptedException {
        while(_czeka <= 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        _czeka -= 1;
    }

    public synchronized void V() throws InterruptedException {
        _czeka += 1;
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
class Producer2 extends Thread {
    private Buffer2 _buf;
    public Producer2(Buffer2 _buf){
        this._buf = _buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            try {
                _buf.put(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class Consumer2 extends Thread {
    private Buffer2 _buf;
    private int[] tab;
    public Consumer2(Buffer2 _buf){
        this._buf = _buf;
    }
    public Consumer2(Buffer2 _buf, int[] tab){
        this.tab = tab;
        this._buf = _buf;
    }
    public void run() {
        for (int i = 0; i < 100; ++i) {
            try {
                int x = _buf.get();
                //System.out.println(this.getName() + " gets: " + _buf.get() );
                System.out.println(this.getName() + " gets: " + x );
                tab[x] += 1;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class Buffer2 {
    private final int[] table;
    private final Semafor semafor1;
    private final Semafor semafor2;
    private int putInx = 0;
    private int getInx = 0;

    public Buffer2(int maxSize, Semafor semafor1, Semafor semafor2){
        this.semafor1 = semafor1;
        this.semafor2 = semafor2;
        table = new int[maxSize];
    }
    public synchronized void  put(int i) throws InterruptedException {
        semafor1.P();
        table[putInx] = i;
        putInx ++;
        if(putInx == table.length){
            putInx = 0;
        }
        semafor2.V();
    }

    public int get() throws InterruptedException {
        semafor2.P();
        int var = table[getInx];
        table[getInx] = -1;
        getInx ++;
        if(getInx == table.length){
            getInx = 0;
        }
        semafor1.V();
        return var;
    }
}

public class PKmon2 {
    public static void main(String[] args) throws InterruptedException {


        //Producer producer = new Producer(buffer);
        //Consumer consumer = new Consumer(buffer);
        int[] count = new int[100];
        ArrayList<Producer2> producers = new ArrayList<>();
        ArrayList<Consumer2> consumers = new ArrayList<>();
        int producersNumber = 6;
        int consumersNUmber = 8;


        Semafor semaforProducer = new Semafor(6);
        Semafor semaforConsumer = new Semafor(0);
        Buffer2 buffer = new Buffer2(10, semaforProducer, semaforConsumer);

        for(int i=0;i<producersNumber;i++){
            producers.add(new Producer2(buffer));
            producers.get(i).setName("Producer: "+ i );
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.add(new Consumer2(buffer,count));
            consumers.get(i).setName("Consumer: " + i);
        }

        //producer.start();
        //consumer.start();

        for(int i=0;i<producersNumber;i++){
            producers.get(i).start();
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.get(i).start();
        }

        //producer.join();
        //consumer.join();

        for(int i=0;i<producersNumber;i++){
            producers.get(i).join();
        }

        for(int i = 0; i<count.length; i++){
            System.out.println(count[i]);

        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.get(i).join();
        }



        //System.out.println(buffer.getList().isEmpty());

    }
}



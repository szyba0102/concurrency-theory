import java.util.ArrayList;
import java.util.Arrays;
import java.util.PrimitiveIterator;

/// jak zrobic aby w danym momencie dany indeks modyfikował jeden producent/konsumer
class Producer extends Thread {
    private Buffer _buf;
    private int quantityOfProducts;

    public Producer(Buffer _buf,int quantityOfProducts){
        this._buf = _buf;
        this.quantityOfProducts = quantityOfProducts;
    }

    public void run() {
        for (int i = 0; i < quantityOfProducts; ++i) {
            /*if(_buf.getProducersAlive() == 1 && i == quantityOfProducts-1){
                _buf.setKillAllConsumers();
                _buf.put(i);
            }
            else {
                _buf.put(i);
            }*/
            _buf.put(i);

        }
        _buf.decreaseProducersAlive();
        System.out.println(this.getName() + " is done");
    }
}

class Consumer extends Thread {
    private Buffer _buf;
    private int quantityOfProducts;

    public Consumer(Buffer _buf,int quantityOfProducts){
        this._buf = _buf;
        this.quantityOfProducts = quantityOfProducts;
    }

    public void run() {
        for (int i = 0; i < quantityOfProducts; ++i) {
            /*if(_buf.getConsumersAlive() == 1 && i == quantityOfProducts-1){
                _buf.setKillAllProducers();
                System.out.println("wysetowne");
            }*/
            System.out.println(this.getName() + " gets: " + _buf.get(i));
        }
        System.out.println(this.getName() + " is done");
        _buf.decreaseConsumersAlive();
    }
}

class Buffer {
    private int[] table;
    private int putInx = 0;
    private int getInx = 0;
    private int counter = 0;
    private boolean putInxOccupied = false;
    private boolean getInxOccupied = false;
    private int quantityOfProducts;

    private int consumersAlive;
    private int producersAlive;
    private boolean killAllProducers = false;
    private boolean killAllConsumers = false;

    public Buffer(int size,int producersAlive, int consumersAlive,int quantityOfProducts){
        this.consumersAlive = consumersAlive;
        this.producersAlive = producersAlive;
        this.table = new int[size];
        this.quantityOfProducts = quantityOfProducts;
    }

    public synchronized void put(int i) {
        while (counter == table.length || putInxOccupied) {
            if (killAllProducers) {
                notifyAll();
                return;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                }
        }
        putInxOccupied = true;
        table[putInx] = i;
        putInx++;
        if (putInx == table.length) {
            putInx = 0;
        }
        counter++;
        putInxOccupied = false;

        if(producersAlive == 1 && quantityOfProducts-1 == i){
            setKillAllConsumers();
        }
        notify();
    }


    public synchronized int get(int i){
        while(counter == 0 || getInxOccupied){
            if(killAllConsumers && counter == 0){
                System.out.println("chuju działaj " + counter);
                notifyAll();
                return -1;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getInxOccupied = true;
        int var = table[getInx];
        table[getInx] = -1;
        getInx ++;
        if(getInx == table.length){
            getInx = 0;
        }
        counter --;
        getInxOccupied = false;
        if(consumersAlive == 1 && quantityOfProducts-1 == i){
            setKillAllProducers();
        }
        notify();
        return var;

    }

    public void setKillAllProducers(){
        killAllProducers = true;
        System.out.println("setKillAllProducers");
    }

    public void setKillAllConsumers(){
        killAllConsumers = true;
        System.out.println("setKillAllConsumers");
    }

    public void decreaseConsumersAlive(){
        consumersAlive -= 1;
    }

    public void decreaseProducersAlive(){
        producersAlive -= 1;
    }

    public boolean getKillAllProducers(){
        return killAllProducers;
    }

    public boolean getKillAllConsumers(){
        return killAllConsumers;
    }

    public int getProducersAlive(){
        return producersAlive;
    }

    public int getConsumersAlive(){
        return consumersAlive;
    }

    public int[] getTable(){
        return table;
    }

}

public class PKmon {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();
        int producersNumber = 8;
        int consumersNUmber = 8;
        int quantityOfProducts = 10;

        Buffer buffer = new Buffer(8,producersNumber,consumersNUmber,quantityOfProducts);

        for(int i=0;i<producersNumber;i++){
            producers.add(new Producer(buffer,quantityOfProducts));
            producers.get(i).setName("Producer: "+ i );
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.add(new Consumer(buffer,quantityOfProducts));
            consumers.get(i).setName("Consumer: " + i);
        }

        for(int i=0;i<producersNumber;i++){
            producers.get(i).start();
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.get(i).start();
        }

        for(int i=0;i<producersNumber;i++){
            producers.get(i).join();
        }
        for(int i=0;i<consumersNUmber;i++){
            consumers.get(i).join();
        }

        for(int i = 0; i<buffer.getTable().length; i++){
            System.out.println(buffer.getTable()[i]);
        }

    }
}
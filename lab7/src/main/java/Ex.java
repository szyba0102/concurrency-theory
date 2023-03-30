import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

class ActiveObject {
    private Buffer buffer;
    private Scheduler scheduler;
    private Proxy proxy;

    public ActiveObject(int queueSize){
        buffer = new Buffer(queueSize);
        scheduler = new Scheduler();
        proxy = new Servant(buffer, scheduler);
        scheduler.start();
    }

    public Proxy getProxy(){
        return this.proxy;
    }
}

class Buffer {
    private int bufSize;
    private Queue<Object> buffer;
    public Buffer(int bufSize){
        this.bufSize = bufSize;
        this.buffer = new LinkedList<Object>();
    }
    public void add(Object object) {
        if(!this.isFull()){
            this.buffer.add(object);
        }
    }
    public Object remove() {
        if(this.isEmpty()){
            return null;
        }
        else{
            return buffer.remove();
        }
    }
    public boolean isFull() {
        return buffer.size() == bufSize;
    }
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
}

interface Proxy {
    void add(Object object);
    Future remove();
}

class Servant implements Proxy{
    Buffer buffer;
    Scheduler scheduler;

    public Servant(Buffer buffer, Scheduler scheduler){
        this.buffer = buffer;
        this.scheduler = scheduler;
    }

    public void add(Object object){
        scheduler.addToQueue(new AddRequest(buffer, object));
    }

    public Future remove(){
        Future future = new Future();
        scheduler.addToQueue(new RemoveRequest(buffer, future));
        return future;
    }
}

interface IMethodRequest {
    boolean guard();
    void execute();
}

class AddRequest implements IMethodRequest{
    private final Buffer buffer;
    private final Object object;

    public AddRequest(Buffer buffer, Object object){
        this.buffer = buffer;
        this.object = object;
    }

    public void execute() {
        buffer.add(object);
    }

    public boolean guard() {
        return !buffer.isFull();
    }
}

class RemoveRequest implements IMethodRequest{
    private Buffer buffer;
    private Future future;

    public RemoveRequest(Buffer buffer, Future future){
        this.buffer = buffer;
        this.future = future;
    }

    public void execute() {
        future.setObject(buffer.remove());
    }

    public boolean guard() {
        return !buffer.isEmpty();
    }
}

class Scheduler extends Thread {
    private Queue<IMethodRequest> activationQueue;

    public Scheduler() {
        activationQueue = new ConcurrentLinkedQueue<IMethodRequest>();
    }

    public void addToQueue(IMethodRequest request) {
        activationQueue.add(request);
    }

    public void run() {
        while (true) {
            IMethodRequest iMethodRequest = activationQueue.poll();
            if (iMethodRequest != null) {
                if (iMethodRequest.guard()) {
                    iMethodRequest.execute();
                } else {
                    activationQueue.add(iMethodRequest);
                }
            }
        }
    }
}

class Future {
    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public boolean isReady() {
        return object != null;
    }
}


class Consumer extends Thread{
    private int id;
    private Proxy proxy;

    public Consumer(int id, Proxy proxy){
        this.id = id;
        this.proxy = proxy;
    }
    public void run(){
        while(true){
            Future result = proxy.remove();
            while(!result.isReady()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Consumer " + id  + " ate: " + result.getObject());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Producer extends Thread{
    private int id;
    private Proxy proxy;
    private Random rand;

    public Producer(int id, Proxy proxy){
        this.id = id;
        this.proxy = proxy;
        rand = new Random();
    }

    public void run(){
        while(true){
            int temp = rand.nextInt(100);
            proxy.add(temp);
            System.out.println("Producer " + id + " added: " + temp);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Ex{
    public static void main(String[] args) throws InterruptedException {
        int Objects = 5;
        int numOfConsumers = 3;
        int numOfProducers = 3;
        ActiveObject activeObject = new ActiveObject(Objects);
        Proxy proxy = activeObject.getProxy();
        Producer[] producers = new Producer[numOfProducers];
        Consumer[] consumers = new Consumer[numOfConsumers];

        for (int i = 0; i < numOfProducers; i++) {
            producers[i] = new Producer(i, proxy);
        }

        for (int i = 0; i < numOfConsumers; i++) {
            consumers[i] = new Consumer(i, proxy);
        }

        for (int i = 0; i < numOfConsumers; i++) {
            consumers[i].start();
        }

        for (int i = 0; i < numOfProducers; i++) {
            producers[i].start();
        }

        for (int i = 0; i < numOfProducers; i++) {
            producers[i].join();
        }

        for (int i = 0; i < numOfConsumers; i++) {
            consumers[i].join();
        }
    }

}




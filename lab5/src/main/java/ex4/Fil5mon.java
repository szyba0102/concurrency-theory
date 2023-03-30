package ex4;
// philosphers are divedied into two groups; one group try to take left fork first and another try to take right first
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class Widelec {
    private final ReentrantLock forkLock = new ReentrantLock();
    public boolean podnies() {return forkLock.tryLock();}
    public void odloz() {
        forkLock.unlock();
    }
}

class Filozof extends Thread {
    private int _licznik = 0;
    private boolean firstForkInPossesion = false;
    private final Widelec firstToGrab;
    private final Widelec secondToGrab;

    private final Random rand = new Random();

    public Filozof(Widelec leftFork, Widelec rightFork,int philosopherNumber){
        if(philosopherNumber % 2 == 0){
            firstToGrab = rightFork;
            secondToGrab = leftFork;
        }
        else{
            firstToGrab = leftFork;
            secondToGrab = rightFork;
        }
    }
    public void run() {
        while (true) {
            while(!firstForkInPossesion){
                firstForkInPossesion  = firstToGrab.podnies();
            }
            while(firstForkInPossesion){
                if(secondToGrab.podnies()){
                    firstForkInPossesion = false;
                    try{
                        sleep(rand.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try{
                sleep(rand.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++_licznik;
            if (_licznik % 10 == 0) {
                System.out.println("Filozof: " + Thread.currentThread() +
                        "jadlem " + _licznik + " razy");
            }
            firstToGrab.odloz();
            secondToGrab.odloz();
        }
    }
}

class Lokaj {
    private int[] philosophers = new int[5];
    private Semaphore philosophersBlock =  new Semaphore(4);

    public void eat(){

    }
}

public class Fil5mon {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Filozof> philosophers = new ArrayList<>();
        ArrayList<Widelec> forks = new ArrayList<>();

        for(int i=0; i<5; i++){
            forks.add(new Widelec());
        }

        for(int i=0; i<5; i++){
            philosophers.add(new Filozof(forks.get(i),forks.get((i+1)%5),i));
        }

        for(int i=0; i<5; i++){
            philosophers.get(i).start();
        }

        for(int i=0; i<5; i++){
            philosophers.get(i).join();
        }

    }
}

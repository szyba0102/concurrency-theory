package ex2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class Widelec {
    private final ReentrantLock forkLock = new ReentrantLock();
    public boolean podnies() {
        //System.out.println("im blocked");
        return forkLock.tryLock();
    }
    public void odloz() {
        forkLock.unlock();
    }
}

class Filozof extends Thread {
    private int _licznik = 0;
    private final Widelec leftFork;
    private final Widelec rightFork;
    private final Random rand = new Random();
    private boolean check = false;

    public Filozof(Widelec leftFork, Widelec rightFork){
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }
    public void run() {
        while (true) {
            while(true){
                boolean leftForksInPossesion = leftFork.podnies();
                boolean rightForksInPossesion = rightFork.podnies();
                if(leftForksInPossesion && rightForksInPossesion){
                    break;
                }
                else if(leftForksInPossesion){
                    leftFork.odloz();
                }
                else if(rightForksInPossesion){
                    rightFork.odloz();
                }
            }
            try{
                sleep(10);
            } catch (InterruptedException e) {
                        e.printStackTrace();
            }
            ++_licznik;
            if (_licznik % 100 == 0) {
                System.out.println("Filozof: " + Thread.currentThread() +
                        "jadlem " + _licznik + " razy");
                if ( _licznik == 6000){
                    check = true;
                }
            }
            leftFork.odloz();
            rightFork.odloz();
            if(check){
                return;
            }
        }
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
            philosophers.add(new Filozof(forks.get(i),forks.get((i+1)%5)));
        }
        long millisActualTime = System.currentTimeMillis();
        for(int i=0; i<5; i++){
            philosophers.get(i).start();
        }

        for(int i=0; i<5; i++){
            philosophers.get(i).join();
        }
        long executionTime = System.currentTimeMillis() - millisActualTime;
        System.out.println(executionTime);

    }
}
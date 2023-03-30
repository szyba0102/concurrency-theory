//import com.sun.java.accessibility.util.EventQueueMonitor;
//import java.util.concurrent.ConcurrentLinkedDeque
//import java.util.concurrent.LinkedBlockingDeque;
//
//class Bufor {
//    // ogarnac rozmiar
//    private int liczba = 0;
//
//    public int zwieksz(int ilość) {
//        liczba += ilość;
//        return liczba;
//    }
//
//    public int zmniejsz(int ilość) {
//        liczba -= ilość;
//        return liczba;
//    }
//}
//
//// PROXY
//class BuforProxy {
//    private Scheduler planista;
//    private Bufor aktywnyObiekt;
//
//    public BuforProxy {
//        utwórz obiekt planisty;
//        utwórz właściwy bufor w aktywnyObiekt (Servant);
//    }
//
//    public Future zwieksz(int ilość) {
//        utwórz obiekt Future: future;
//        utwórz obiekt żądania dla metody "zwiększ": żądanie;
//        przypisz do żądania argument ilość;
//        przypisz do żądania obiekty future oraz aktywnyObiekt;
//        dodaj żądanie do kolejki planisty;
//        zwróć future
//    }
//
//    public Future zmniejsz(int ilość) {
//        utwórz obiekt Future: future;
//        utwórz obiekt żądania dla metody "zmniejsz": żądanie;
//        przypisz do żądania argument ilość;
//        przypisz do żądania obiekty future oraz aktywnyObiekt;
//        dodaj żądanie do kolejki planisty;
//        zwróć future
//    }
//}
//
//// METHOD REQUEST
//class ZwiekszMethodRequest {
//    private Future future;
//    private Bufor aktywnyObiekt;
//    private int ilość;
//
//    public bool guard() {
//        jeśli metoda zwieksz() może być wywołana:
//        zwróć prawdę
//        w przeciwnym wypadku:
//        zwróć fałsz
//    }
//
//    public void execute() {
//        zaalokuj pamięć na wynik;
//        wywołaj
//
//
//        {\displaystyle aktywnyObiekt.zwieksz}
//
//        aktywnyObiekt.zwieksz z argumentem ilość;
//        skopiuj wynik do zaalokowanej pamięci;
//        przekaż wskaźnik zaalokowanej pamięci do obiektu future;
//        zapisz w obiekcie future informację, że żądanie zostało obsłużone;
//    }
//}
//
//class BuforScheduler{
//    // trzeba swtorzyc pewnie interferjs do method requests
//    private LinkedBlockingDeque queueu = new LinkedBlockingDeque();
//
//    public enqueue(){
//        while(true){
//            ZwiekszMethodRequest r = queueu.getLast();
//            if(!r.guard()){
//                r.execute();
//            }
//            else{
//                queueu.add(r);
//            }
//        }
//
//    }
//}
//
//public class ActiveObject {
//}
//
//class ActivationQueue{
//// Musi ona być zaimplementowana jako monitor,
//// ponieważ dodawanie żądania do kolejki może być wykonane zarówno przez planistę,
//// jak i w obrębie wątku wywołującego metodę, gdy pragnie on wysłać żądanie.
//    public void dequeue(){
//
//    }
//
//    public void enqueue(){
//
//    }
//}
//
//class Future{
//
//}

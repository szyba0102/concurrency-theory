package zad22;

//class Node {
//    int
//}
class List1 {


}
//        List1 prev = null, tmp = this;
//        if(tmp == o){
//            return true;
//        }
//        if(tmp.next != null){
//            prev = tmp;
//            tmp = tmp.next;
//        }
//        else{
//            return false;
//        }
//        prev.lock.lock();
//        tmp.lock.lock();
//
//        while(tmp != null) {
//            if (prev == o) {
//                return true;
//            }
//            prev.lock.unlock();
//            prev = tmp;
//            prev.lock.lock();
//            tmp.lock.unlock();
//            tmp = tmp.next;
//            if(tmp != null){
//                tmp.lock.lock();
//            }
//        }
//        return false;
public class Ex2{
    public static void main(String[] args) throws InterruptedException {
        Object[] o = { "ala ma kota", 10L, 10.0, "ola ma kota", 11L, 11.0,
                "ala ma kata", 11L, 11.0 };
        //List1 list = new List1("ala ma kota", null);
        //List2 list2 = new List2("ala ma kota", null);

        long sleepTime;

//        for (int i = 0, n = o.length; i < 10; ++i) {
//            try {
//                list2.add(o[i % n]);
//                list2.contains(o[(i + 1) % n]);
//                list2.remove(o[(i + 2) % n]);
//                list2.contains(o[(i + 3) % n]);
//                list2.add(o[(i + 4) % n]);
//                list2.remove(o[(i + 5) % n]);
//                list2.add(o[(i + 6) % n]);
//                list2.remove(o[(i + 7) % n]);
//                list2.contains(o[(i + 8) % n]);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0, n = o.length; i < 10; ++i) {
//            try {
//                list.add(o[i % n]);
//                list.contains(o[(i + 1) % n]);
//                list.remove(o[(i + 2) % n]);
//                list.contains(o[(i + 3) % n]);
//                list.add(o[(i + 4) % n]);
//                list.remove(o[(i + 5) % n]);
//                list.add(o[(i + 6) % n]);
//                list.remove(o[(i + 7) % n]);
//                list.contains(o[(i + 8) % n]);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        //System.out.println(list.contains("m"));



    }
}

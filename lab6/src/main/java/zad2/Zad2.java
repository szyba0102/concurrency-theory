package zad2;

import java.util.concurrent.locks.Lock;
        import java.util.concurrent.locks.ReentrantLock;

 class FirstList {
    private Object value;
    private FirstList next;
    private Lock lock;

    public FirstList(Object val, FirstList next) {
        this.value = val;
        this.next = next;
        lock = new ReentrantLock();
    }

    public boolean contains(Object o) throws InterruptedException {
        FirstList prev = null;
        FirstList curr = this;
        curr.lock.lock();

        while (curr != null) {
            if (curr.value == o) {
                return true;
            }
            if (prev != null) {
                prev.lock.unlock();
            }
            prev = curr;
            prev.lock.lock();
            curr.lock.unlock();
            curr = curr.next;
            if (curr != null) {
                curr.lock.lock();
            }
        }
        return false;
    }


     public boolean remove(Object o) throws InterruptedException {
         FirstList prev = null;
         FirstList curr = this;
         curr.lock.lock();
         if(curr.next != null){
             prev = curr;
             prev.lock.lock();
             curr.lock.unlock();
             curr = curr.next;
         }
         else if(curr.value == o){
             curr.value = null;
             return true;
         }
         else{
             return false;
         }
         while (curr.next != null) {
             if (prev.value == o) {
                 prev.next = curr.next;
                 return true;
             }
             prev = curr;
             prev.lock.lock();
             curr.lock.unlock();
             curr = curr.next;
             if (curr != null) {
                 curr.lock.lock();
             }
         }
         if(curr.value == o){
             prev.next = null;
         }
         return false;
    }

    public boolean add(Object o) throws InterruptedException {
        if (o == null) {
            return false;
        }
        FirstList curr = this;
        FirstList next = this.next;
        if(next == null){
            curr.next = new FirstList(o,null);
            return true;
        }
        curr.lock.lock();
        next.lock.lock();
        while (next != null) {
            curr.lock.unlock();
            curr = next;
            curr.lock.lock();
            next.lock.unlock();
            next = next.next;
            if(next != null) {
                next.lock.lock();
            }
        }
        curr.next = new FirstList(o, null);
        return true;

    }

}

class SecondList {
    private Object val;
    private SecondList next;
    private static Lock lock = new ReentrantLock();
    private static long sleepTime;

    public SecondList(Object val, SecondList next) {
        this.val = val;
        this.next = next;
    }

    public boolean contains(Object o) throws InterruptedException {
        SecondList curr = this;
        lock.lock();
        try {
            while (curr != null) {
                if (val == o) {
                    return true;
                }
                curr = curr.next;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    public boolean remove(Object o) throws InterruptedException {
        SecondList prev = null;
        SecondList curr = this;
        lock.lock();
        try {
            while (curr != null) {
                if (val == o) {
                    if (prev != null) {
                        prev.next = curr.next;
                        curr.next = null;
                    }
                    Thread.sleep(sleepTime / 3);
                    return true;
                }
                prev = curr;
                curr = curr.next;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    public boolean add(Object o) throws InterruptedException {
        if (o == null) {
            return false;
        }
        SecondList curr = this;
        SecondList next = this.next;
        lock.lock();
        try {
            while (next != null) {
                curr = next;
                next = next.next;
            }
            curr.next = new SecondList(o, null);
            Thread.sleep(sleepTime);
            return true;
        } finally {
            lock.unlock();
        }
    }

}

public class Zad2{
    public static void main(String[] args) throws InterruptedException {
        Object[] o = { "ala ma kota", 10L, 10.0, "ola ma kota", 11L, 11.0,
                "ala ma kata", 11L, 11.0 };
        FirstList list = new FirstList("ala", null);
        SecondList list2 = new SecondList("ala", null);

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
        long millisActualTime = System.nanoTime();
        for (int i = 0, n = o.length; i < 10; ++i) {
            try {
                list2.add(1);
                list2.add(2);
                list2.add(3);
                list2.add(4);
                list2.add(5);
                list2.add(6);
                list2.add(7);
                list2.add(8);
                list2.add(9);
                list2.add(10);
                list2.add(11);
                list2.add(12);
                list2.add(13);
                list2.add(14);
                list2.add(15);
                list2.add(16);
                list2.add(17);
                list2.add(18);
                list2.add(19);
                list2.add(20);

                list2.add(1);
                list2.add(2);
                list2.add(3);
                list2.add(4);
                list2.add(5);
                list2.add(6);
                list2.add(7);
                list2.add(8);
                list2.add(9);
                list2.add(10);
                list2.add(11);
                list2.add(12);
                list2.add(13);
                list2.add(14);
                list2.add(15);
                list2.add(16);
                list2.add(17);
                list2.add(18);
                list2.add(19);
                list2.add(20);

                list2.add(1);
                list2.add(2);
                list2.add(3);
                list2.add(4);
                list2.add(5);
                list2.add(6);
                list2.add(7);
                list2.add(8);
                list2.add(9);
                list2.add(10);
                list2.add(11);
                list2.add(12);
                list2.add(13);
                list2.add(14);
                list2.add(15);
                list2.add(16);
                list2.add(17);
                list2.add(18);
                list2.add(19);
                list2.add(20);

                list2.contains(2);
                list2.contains(4);
                list2.contains(123);
                list2.contains("asda");
                list2.contains("asd");
                list2.contains(234);
                list2.contains("aj2");
                list2.contains(3);
                list2.contains(4);
                list2.contains(5);
                list2.contains(8);
                list2.contains("asd");
                list2.contains(1233412214);
                list2.contains(12323);
                list2.contains("asfddgffmgfhdsasddf");
                list2.contains("qweqe");
                list2.contains(12323132);
                list2.contains(124131);
                list2.contains("qweqweq");
                list2.contains(123123);

                list2.contains(2);
                list2.contains(4);
                list2.contains(123);
                list2.contains("asda");
                list2.contains("asd");
                list2.contains(234);
                list2.contains("aj2");
                list2.contains(3);
                list2.contains(4);
                list2.contains(5);
                list2.contains(8);
                list2.contains("asd");
                list2.contains(1233412214);
                list2.contains(12323);
                list2.contains("asfddgffmgfhdsasddf");
                list2.contains("qweqe");
                list2.contains(12323132);
                list2.contains(124131);
                list2.contains("qweqweq");
                list2.contains(123123);

                list2.contains(2);
                list2.contains(4);
                list2.contains(123);
                list2.contains("asda");
                list2.contains("asd");
                list2.contains(234);
                list2.contains("aj2");
                list2.contains(3);
                list2.contains(4);
                list2.contains(5);
                list2.contains(8);
                list2.contains("asd");
                list2.contains(1233412214);
                list2.contains(12323);
                list2.contains("asfddgffmgfhdsasddf");
                list2.contains("qweqe");
                list2.contains(12323132);
                list2.contains(124131);
                list2.contains("qweqweq");
                list2.contains(123123);

                list2.remove(2);
                list2.remove(4);
                list2.remove(123);
                list2.remove("asda");
                list2.remove("asd");
                list2.remove(234);
                list2.remove("aj2");
                list2.remove(3);
                list2.remove(4);
                list2.remove(5);
                list2.remove(8);
                list2.remove("asd");
                list2.remove(1233412214);
                list2.remove(12323);
                list2.remove("asfddgffmgfhdsasddf");
                list2.remove("qweqe");
                list2.remove(12323132);
                list2.remove(124131);
                list2.remove("qweqweq");
                list2.remove(123123);

                list2.remove(2);
                list2.remove(4);
                list2.remove(123);
                list2.remove("asda");
                list2.remove("asd");
                list2.remove(234);
                list2.remove("aj2");
                list2.remove(3);
                list2.remove(4);
                list2.remove(5);
                list2.remove(8);
                list2.remove("asd");
                list2.remove(1233412214);
                list2.remove(12323);
                list2.remove("asfddgffmgfhdsasddf");
                list2.remove("qweqe");
                list2.remove(12323132);
                list2.remove(124131);
                list2.remove("qweqweq");
                list2.remove(123123);

                list2.remove(2);
                list2.remove(4);
                list2.remove(123);
                list2.remove("asda");
                list2.remove("asd");
                list2.remove(234);
                list2.remove("aj2");
                list2.remove(3);
                list2.remove(4);
                list2.remove(5);
                list2.remove(8);
                list2.remove("asd");
                list2.remove(1233412214);
                list2.remove(12323);
                list2.remove("asfddgffmgfhdsasddf");
                list2.remove("qweqe");
                list2.remove(12323132);
                list2.remove(124131);
                list2.remove("qweqweq");
                list2.remove(123123);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(list.contains("ala ma kota"));
        long executionTime = System.nanoTime() - millisActualTime;
        System.out.println(executionTime);

        }
}


package variant1;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Guard;
import org.jcsp.lang.Alternative;

public class Consumer implements CSProcess
{
    private One2OneChannelInt consumption[];
    private int items;
    public Consumer (final One2OneChannelInt consumption[], final int items)
    {
        this.consumption = consumption;
        this.items = items;
    }
    public void run ()
    {
        long start = System.nanoTime();
        final Guard[] guards = new Guard[consumption.length];
        for (int i = 0; i < consumption.length; i++)
            guards[i] = consumption[i].in();
        final Alternative alternative = new Alternative(guards);
        for (int i = 0; i < items; i++)
        {
            int index = alternative.select();
            int item = consumption[index].in().read();
// System.out.println(index + ": " + item);
        }
        long end = System.nanoTime();
        System.out.println((end - start) + "ns");
        System.exit(0);
    }
}

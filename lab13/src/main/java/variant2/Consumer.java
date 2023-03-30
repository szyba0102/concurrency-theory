package variant2;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
public class Consumer implements CSProcess
{
    private One2OneChannelInt production;
    private int items;
    public Consumer (final One2OneChannelInt production, final int items)
    {
        this.production = production;
        this.items = items;
    }
    public void run ()
    {
        long start = System.nanoTime();
        for (int i = 0; i < items; i++)
        {
            int item = production.in().read();
// System.out.println(item);
        }
        long end = System.nanoTime();
        System.out.println((end - start) + "ns");
        System.exit(0);
    }
}

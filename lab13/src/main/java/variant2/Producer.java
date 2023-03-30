package variant2;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
public class Producer implements CSProcess
{
    private final One2OneChannelInt consumption;
    private final int items;
    public Producer (final One2OneChannelInt consumption, final int items)
    {
        this.consumption = consumption;
        this.items = items;
    }
    public void run ()
    {
        for (int i = 0; i < items; i++)
        {
            int item = (int)(Math.random()*100)+1;
            consumption.out().write(item);
        }
    }
}


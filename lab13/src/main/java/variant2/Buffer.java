package variant2;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess
{
    private final One2OneChannelInt production;
    private final One2OneChannelInt consumption;
    public Buffer (final One2OneChannelInt production,
                   final One2OneChannelInt consumption)
    {
        this.consumption = consumption;
        this.production = production;
    }
    public void run ()
    {
        while (true)
            consumption.out().write(production.in().read());
    }
}

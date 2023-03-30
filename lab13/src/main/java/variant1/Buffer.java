package variant1;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
public class Buffer implements CSProcess
{
    private final One2OneChannelInt production;
    private final One2OneChannelInt consumption;
    private final One2OneChannelInt toGO;
    public Buffer (final One2OneChannelInt production,
                   final One2OneChannelInt consumption,
                   final One2OneChannelInt toGO)
    {
        this.consumption = consumption;
        this.production = production;
        this.toGO = toGO;
    }
    public void run ()
    {
        while (true)
        {
            toGO.out().write(0);
            consumption.out().write(production.in().read());
        }
    }
}


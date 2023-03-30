package variant1;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Guard;
import org.jcsp.lang.Alternative;

public class Producer implements CSProcess
{
    private final One2OneChannelInt[] production;
    private final One2OneChannelInt[] toGo;
    private final int items;
    public Producer (final One2OneChannelInt[] production, final One2OneChannelInt[] toGo, final int items)
    {
        this.production = production;
        this.toGo = toGo;
        this.items = items;
    }
    public void run ()
    {
        final Guard[] guards = new Guard[toGo.length];
        for (int i = 0; i < toGo.length; i++)
            guards[i] = toGo[i].in();
        final Alternative alternative = new Alternative(guards);
        for (int i = 0; i < items; i++)
        {
            int index = alternative.select();
            toGo[index].in().read();
            int item = (int)(Math.random()*100)+1;
            production[index].out().write(item);
        }
    }
}


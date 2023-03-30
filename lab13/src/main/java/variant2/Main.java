package variant2;

import org.jcsp.lang.Parallel;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.StandardChannelIntFactory;
public final class Main
{
    public static void main (String[] args)
    {
        final int bufferSize = 10;
        final int items = 10000;
        StandardChannelIntFactory factory = new StandardChannelIntFactory();
        final One2OneChannelInt[] channels = factory.createOne2One(bufferSize + 1);
        CSProcess[] processes = new CSProcess[bufferSize + 2];
        processes[0] = new Producer(channels[0], items);
        processes[1] = new Consumer(channels[bufferSize], items);
        for (int i = 0; i < bufferSize; i++) {
            processes[i + 2] = new Buffer(channels[i], channels[i + 1]);
        }
        Parallel par = new Parallel(processes);
        par.run();
    }
}

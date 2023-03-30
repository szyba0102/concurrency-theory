package variant1;

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
        final One2OneChannelInt[] channels_producer = factory.createOne2One(bufferSize);
        final One2OneChannelInt[] channels_toGo = factory.createOne2One(bufferSize);
        final One2OneChannelInt[] channels_consumer = factory.createOne2One(bufferSize);

        CSProcess[] processes = new CSProcess[bufferSize + 2];
        processes[0] = new Producer(channels_producer, channels_toGo, items);
        processes[1] = new Consumer(channels_consumer, items);
        for (int i = 0; i < bufferSize; i++) {
            processes[i + 2] = new Buffer(channels_producer[i], channels_consumer[i], channels_toGo[i]);
        }

        Parallel par = new Parallel(processes);
        par.run();
    }
}

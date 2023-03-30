import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.*;
import javax.swing.JFrame;

import com.sun.source.doctree.InlineTagTree;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;

class MandelbrotCalculations implements Callable<Integer[]> {

    private final double ZOOM = 150;
    private final int max_iter;
    private final int width;
    private final int height;

    public MandelbrotCalculations(int max_iter, int width, int height){
        this.max_iter = max_iter;
        this.width = width;
        this.height = height;
    }

    @Override
    public Integer[] call(){
        Integer[] x_rgb = new Integer[width];
        for (int x = 0; x < width; x++) {
            double zy;
            double zx = zy = 0;
            double cX = (x - 400) / ZOOM;
            double cY = (height - 300) / ZOOM;
            int iter = max_iter;
            while (zx * zx + zy * zy < 4 && iter > 0) {
                double tmp = zx * zx - zy * zy + cX;
                zy = 2.0 * zx * zy + cY;
                zx = tmp;
                iter--;
            }
            x_rgb[x] = iter | (iter << 8);
        }
        return x_rgb;
    }
}


class Mandelbrot extends JFrame {

    private int MAX_ITER;
    private final BufferedImage I;
    private long timeOfExecution;

    public Mandelbrot(int itter) throws ExecutionException, InterruptedException {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        MAX_ITER = itter;
        }

    public void run(ExecutorService executorService) throws ExecutionException, InterruptedException {
        var start = System.currentTimeMillis();
        Future<Integer[]>[] futures = new Future[getHeight()];

        for (int y = 0; y < getHeight(); y++) {
            var task = new MandelbrotCalculations(MAX_ITER, getWidth(), y);
            futures[y] = (executorService.submit(task));
        }

        for( int i=0; i < futures.length; i++){
            Integer[] element = futures[i].get();
            for(int j=0; j < element.length; j++){
                I.setRGB(j, i, element[j]);
            }
        }
        timeOfExecution = System.currentTimeMillis() - start;

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public long getTimeOfExecution() {
        return timeOfExecution;
    }

}



public class Main{

    private static final int[] THREADS = {1,2,5,10,15,20} ;
    private static final int[] ITERRATIONS = {100,250,500,750,1000,1500,2000,3000};
    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        for (int itter : ITERRATIONS) {
////            for(int thread : THREADS){
//                Mandelbrot mandelbrot = new Mandelbrot(itter);
//                ExecutorService executorService = Executors.newSingleThreadExecutor();
//                mandelbrot.run(executorService);
//                var time = mandelbrot.getTimeOfExecution();
//                // mandelbrot.setVisible(true);
//                System.out.println(
//                        Long.toString(time) );
////            }
//        }
        openWebpage("https://github.com/szyba0102/LOGOv2");
    }
}
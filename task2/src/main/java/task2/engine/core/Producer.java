package task2.engine.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.BlockingQueue;

//public class Producer implements Runnable {
//
//    private final List<BlockingQueue<String>> queues;
//    private final String resourcePath;
//
//    public Producer(List<BlockingQueue<String>> queues, String resourcePath) {
//        this.queues = queues;
//        this.resourcePath = resourcePath;
//    }
//
//    @Override
//    public void run() {
//        try {
////            System.out.println("Producer started");
//            InputStream in = getClass()
//                    .getClassLoader()
//                    .getResourceAsStream(resourcePath);
////            System.out.println(in);
//
//            if (in == null) {
//                throw new RuntimeException("Input file not found in classpath: " + resourcePath);
//            }
//
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//                String line;
//                while ((line = br.readLine()) != null) {
////                    System.out.println("Producing record: " + line);
//                    for (BlockingQueue<String> q : queues) {
//                        q.put(line);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Producer failed", e);
//        }
//    }
//}
public class Producer implements Runnable {

    private final List<BlockingQueue<String>> queues;

    public Producer(List<BlockingQueue<String>> queues) {
        this.queues = queues;
    }

    @Override
    public void run() {
        long counter = 0;
        try {
            while (true) {
                String record = "{\"id\":" + counter + "}";
                for (BlockingQueue<String> q : queues) {
                    q.put(record);
                }
                counter++;
            }
        } catch (InterruptedException ignored) {}
    }
}

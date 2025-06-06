import java.util.concurrent.Semaphore;

public final class CPUBound {
    private CPUBound() {}

    public static void run(int seconds) {
        long end = System.currentTimeMillis() + seconds * 1000L;
        long dummy = 0;

        while (System.currentTimeMillis() < end) {
            for (int i = 0; i < 10_000; i++) {
                dummy += (long) (Math.sin(dummy + i) + Math.cos(dummy + i));
            }
        }

    }

    public static void run(Semaphore signal) {
        long dummy = 0;

        try {
            while (!signal.tryAcquire()) {
                for (int i = 0; i < 10_000; i++) {
                    dummy += (long) (Math.sin(dummy + i) + Math.cos(dummy + i));
                }
            }
        } catch (Exception e) {
            System.err.printf("%s%n", e.getMessage());
        }
    }
}

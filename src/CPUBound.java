import java.util.concurrent.Semaphore;

public final class CPUBound {
    private CPUBound() {}

    public static void run(int seconds, Fan fan) {
        long startTime = System.currentTimeMillis();
        long dummy = 0;

        fan.counter = seconds * 1000;

        while (true) {
            long now = System.currentTimeMillis();
            int elapsedMillis = (int) (now - startTime);

            int remaining = seconds * 1000 - elapsedMillis;
            fan.counter = Math.max(remaining, 0);

            if (remaining <= 0) break;

            for (int i = 0; i < 10_000; i++) {
                dummy += (long) (Math.sin(dummy + i) + Math.cos(dummy + i));
            }
        }
    }

    public static void run(int seconds, Demonstrator demo) {
        long startTime = System.currentTimeMillis();
        long dummy = 0;

        demo.counter = seconds * 1000;

        while (true) {
            long now = System.currentTimeMillis();
            int elapsedMillis = (int) (now - startTime);

            int remaining = seconds * 1000 - elapsedMillis;
            demo.counter = Math.max(remaining, 0);

            if (remaining <= 0) break;

            for (int i = 0; i < 10_000; i++) {
                dummy += (long) (Math.sin(dummy + i) + Math.cos(dummy + i));
            }
        }
    }


    public static void run(Semaphore signal, Fan fan) {
        long startTime = System.currentTimeMillis();
        long dummy = 0;

        final int totalDurationMs = fan.counter;

        try {
            while (true) {
                long now = System.currentTimeMillis();
                int elapsedMillis = (int) (now - startTime);
                int remaining = totalDurationMs - elapsedMillis;

                fan.counter = Math.max(remaining , 0);

                if (signal.tryAcquire()) {
                    break;
                }

                for (int i = 0; i < 10_000; i++) {
                    dummy += (long) (Math.sin(dummy + i) + Math.cos(dummy + i));
                }
            }
        } catch (Exception e) {
            System.err.printf("%s%n", e.getMessage());
        }
    }
}

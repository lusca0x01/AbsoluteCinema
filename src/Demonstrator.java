public class Demonstrator implements Runnable {
    private final Cinema cinema;

    public Demonstrator(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (cinema.waiting.get() < cinema.capacity) {
                    System.out.println("[Demonstrador] Cinema esperando pela audiência...");
                    continue;
                }

                System.out.println("[Demonstrador] Filme começou!");

                for (int i = 0; i < cinema.capacity; i++) {
                    cinema.canWatch.release(); // Libera o filme pras threads fãs
                }

                cinema.mutex.acquire(); // Entra na zona crítica para operar a variável waiting
                cinema.waiting.addAndGet(-cinema.capacity); // Remove a galera da fila de espera
                cinema.mutex.release(); // Sai da zona crítica

                CPUBound.run(cinema.movieTimeSeconds); // Começa o CPU bound com o tempo da sessão

                for (int i = 0; i < cinema.capacity; i++) {
                    cinema.endOfMovie.release();  // Sinaliza o fim do filme pras threads fãs
                }

                System.out.println("[Demonstrador] filme acabou.");
            } catch (InterruptedException e) {
                System.err.println("Demonstrador interrompido");
                return;
            }
        }
    }
}

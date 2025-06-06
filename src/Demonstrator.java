public class Demonstrator implements Runnable {
    private final Cinema cinema;

    public Demonstrator(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("[Demonstrador] Cinema esperando pela audiência...");

                cinema.waitForStart.acquire(cinema.capacity);

                for (int i = 0; i < cinema.capacity; i++) {
                    cinema.moveToChair.release(); // Libera o filme pras threads fãs
                }

                cinema.allSeated.acquire();
                cinema.seatedCount.set(0);

                System.out.println("[Demonstrador] Filme começou!");

                for (int i = 0; i < cinema.capacity; i++) {
                    cinema.startMovie.release();
                }

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

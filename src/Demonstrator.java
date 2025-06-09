import java.awt.*;
public class Demonstrator implements Runnable {
    private final Cinema cinema;

    public int counter = 0;


    public Demonstrator(Cinema cinema) {
        this.cinema = cinema;
    }

    public void desenhar(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        int textoX = 750;
        int textoY = 200;
        String exib = String.valueOf(counter / 1000);
        g.drawString(exib, textoX, textoY);
    }

    @Override
    public void run() {
        while (true) {
            try {
                counter = cinema.movieTimeSeconds * 1000;
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

                CPUBound.run(cinema.movieTimeSeconds, this); // Começa o CPU bound com o tempo da sessão
                
                for (int i = 0; i < cinema.capacity; i++) {
                    cinema.endOfMovie.release();  // Sinaliza o fim do filme pras threads fãs
                }
                counter = cinema.movieTimeSeconds * 1000;
                System.out.println("[Demonstrador] filme acabou.");
            } catch (InterruptedException e) {
                System.err.println("Demonstrador interrompido");
                return;
            }
        }
    }
}

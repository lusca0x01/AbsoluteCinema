import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Cinema {
    public final int capacity;
    public final int movieTimeSeconds;
    private PainelCinema painelCinema;

    public final Semaphore waitForStart = new Semaphore(0);
    public final Semaphore moveToChair = new Semaphore(0); // Semáforo para sinalizar a ida para a cadeira
    public final Semaphore startMovie = new Semaphore(0); // Semafóro para alertar o início do filme
    public final Semaphore allSeated = new Semaphore(0); // Semáforo para sinalizar o demonstrador para começar o filme
    public final Semaphore endOfMovie = new Semaphore(0); // Semáforo para alertar o fim do filme
    public final AtomicInteger seatedCount  = new AtomicInteger(0); // Contador de fãs sentados

    public Cinema(int capacity, int movieTimeSeconds) {
        this.capacity = capacity;
        this.movieTimeSeconds = movieTimeSeconds;
    }

    public void setPainelCinema(PainelCinema painelCinema) {
        this.painelCinema = painelCinema;
    }

    public PainelCinema getPainelCinema() {
        return painelCinema;
    }

}

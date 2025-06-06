import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Cinema {
    public final int capacity;
    public final int movieTimeSeconds;
    private PainelCinema painelCinema;
   
    public final Semaphore mutex = new Semaphore(1); // Mutex para a zona crítica da var waiting
    public final Semaphore canWatch = new Semaphore(0); // Semafóro para alertar o início do filme
    public final Semaphore endOfMovie = new Semaphore(0); // Semáforo para alertar o fim do filme
    public final AtomicInteger waiting = new AtomicInteger(0); // Inteiro que representa a fila de espera

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

public class Fan implements Runnable {
    private final String id;
    private final int tl;
    private final Cinema cinema;

    public Fan(String id, int tl, Cinema cinema) {
        this.id = id;
        this.tl = tl;
        this.cinema = cinema;
    }

    @Override
    public void run() {
        while (true) {
            try {
                cinema.mutex.acquire();  // Entra na zona crítica para operar a variável waiting
                cinema.waiting.incrementAndGet(); // Adiciona um novo fã a lista de espera
                cinema.mutex.release();  // Sai da zona crítica

                System.out.printf("[Fã %s] Esperando a próxima sessão...%n", id);
                cinema.canWatch.acquire();  // Espera a thread demonstrador liberar o filme

                System.out.printf("[Fã %s] Assistindo o filme...%n", id);
                CPUBound.run(cinema.endOfMovie); // Roda o método CPU bound até o filme terminar (sinalizado via semáforo)

                System.out.printf("[Fan %s] Indo comer (%ds)...%n", id, tl);
                CPUBound.run(tl); // Thread fã vai comer
            } catch (InterruptedException e) {
                System.err.printf("Fã %s interrompido %n", id);
                return;
            }
        }
    }
}

import java.awt.*;

public class Fan implements Runnable {
    private final String id;
    private final int tl;
    private final Cinema cinema;
    private PainelCinema painelCinema;

    private int x, y; // posição atual do fã
    private final String pathImagem;
    private Image imagem;

    public Fan(String id, int tl, Cinema cinema) {
        this.id = id;
        this.tl = tl;
        this.cinema = cinema;
        this.pathImagem = "../data/AG1.png"; //ajeitar esse path 
        this.imagem = Toolkit.getDefaultToolkit().getImage(pathImagem);

        this.x = 100;
        this.y = 400;
    }

    public void desenhar(Graphics g) {
        g.drawImage(imagem, x, y, 64, 64, null);
    }

    public void setPosicao(int x, int y) {
        this.x = x;
        this.y = y;
        if (painelCinema != null) {
            //painelCinema.atualizarPosicaoFan(id, getPosicaoNome(x));
            painelCinema.repaint();
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                cinema.mutex.acquire();
                cinema.waiting.incrementAndGet();
                cinema.mutex.release();

                setPosicaoPorEstado("esperando");

                System.out.printf("[Fã %s] Esperando a próxima sessão...%n", id);
                cinema.canWatch.acquire();

                setPosicaoPorEstado("assistindo");
                System.out.printf("[Fã %s] Assistindo o filme...%n", id);
                CPUBound.run(cinema.endOfMovie);

                setPosicaoPorEstado("comendo");
                System.out.printf("[Fan %s] Indo comer (%ds)...%n", id, tl);
                CPUBound.run(tl);
            } catch (InterruptedException e) {
                System.err.printf("Fã %s interrompido %n", id);
                return;
            }
        }
    }

    private void setPosicaoPorEstado(String estado) {
        if (painelCinema == null) return;

        int indice = painelCinema.getIndiceFan(this);

        switch (estado) {
            case "esperando":
                int baseX = 300; // ponto mais à direita da fila
                int espacamentoX = 70;
                int x = baseX - indice * espacamentoX;
                setPosicao(x, 400);
                break;

            case "assistindo":
                Point pos = painelCinema.getPosicaoCadeira(indice);
                setPosicao(pos.x, pos.y);
                break;

            case "comendo":
                int y = 100 + indice * 70;
                setPosicao(1000, y);
                break;
        }
    }


    public void setPainelCinema(PainelCinema painelCinema) {
        this.painelCinema = painelCinema;
    }

}

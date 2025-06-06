import java.awt.*;
import javax.swing.*;

public class Fan implements Runnable {
    private final String id;
    private final int tl;
    private final Cinema cinema;
    private PainelCinema painelCinema;

    private int x, y; // posição atual do fã
    private final String pathImagem;
    private final Image imagem;

    public Fan(String id, int tl, Cinema cinema) {
        this.id = id;
        this.tl = tl;
        this.cinema = cinema;
        this.pathImagem = "/data/AG1.png";

        // Carrega a imagem a partir do classpath corretamente
        this.imagem = new ImageIcon(getClass().getResource(pathImagem)).getImage();

        this.x = 100;
        this.y = 400;
    }

    public void desenhar(Graphics g) {
        g.drawImage(imagem, x, y, 64, 64, null);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));

        FontMetrics fm = g.getFontMetrics();
        int textoLargura = fm.stringWidth(id);
        int textoX = x + (64 - textoLargura) / 2;
        int textoY = y - 5;

        g.drawString(id, textoX, textoY);
    }

    public void setPosicao(int x, int y) {
        this.x = x;
        this.y = y;
        if (painelCinema != null) {
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
                cinema.moveToChair.acquire();
                setPosicaoPorEstado("assistindo");

                System.out.printf("[Fã %s] Sentado...%n", id);

                if (cinema.seatedCount.incrementAndGet() == cinema.capacity) {
                    cinema.allSeated.release(); // Libera o demonstrador ao atingir a capacidade da sessão
                }

                cinema.startMovie.acquire();

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
                int yi = 400;
                if (indice > 4) {
                    yi = 500;
                    x += 350;
                }
                setPosicao(x, yi);
                break;

            case "assistindo":
                //Point pos = painelCinema.getPosicaoCadeira(indice);
                //setPosicao(pos.x, pos.y);
                try {
                    moverParaCadeira();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case "comendo":
                int y = 410 + ((indice % 3 ) * 70);
                int xi = 1030 + ((indice % 4) * 70);
                //if (indice > 4) {
                //    y -= 350;
                //    xi = 1100;
                //}
                setPosicao(xi, y);
                break;
        }
    }

    private void moverParaCadeira() throws InterruptedException {
    if (painelCinema == null) return;

    int indice = painelCinema.getIndiceFan(this);
    Point destino = painelCinema.getPosicaoCadeira(indice);

    while (x < 500) {
        x += 5;
        setPosicao(x, y);
        Thread.sleep(10);
    }

    while (y < destino.y) {
        y += 5;
        setPosicao(x, y);
        Thread.sleep(10);
    }
    while (y > destino.y) { 
        y -= 5;
        setPosicao(x, y);
        Thread.sleep(10);
    }

    while (x < destino.x) {
        x += 5;
        setPosicao(x, y);
        Thread.sleep(10);
    }
    while (x > destino.x) {
        x -= 5;
        setPosicao(x, y);
        Thread.sleep(10);
    }

    setPosicao(destino.x, destino.y);
}


    public void setPainelCinema(PainelCinema painelCinema) {
        this.painelCinema = painelCinema;
    }
}

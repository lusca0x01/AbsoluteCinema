import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class PainelCinema extends JPanel {

    private final Image background;
    private final List<Fan> fans;

    private final Map<Integer, Point> posicoesCadeiras = new HashMap<>();

    public PainelCinema() {
        // Carrega a imagem a partir do classpath (pasta src/data)
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/data/BACKGROUND.png")));
        this.background = icon.getImage();

        this.fans = new CopyOnWriteArrayList<>();

        posicoesCadeiras.put(0, new Point(550, 520));
        posicoesCadeiras.put(1, new Point(620, 520));
        posicoesCadeiras.put(2, new Point(700, 520));
        posicoesCadeiras.put(3, new Point(790, 520));
        posicoesCadeiras.put(4, new Point(870, 520));
        posicoesCadeiras.put(5, new Point(500, 620));
        posicoesCadeiras.put(6, new Point(600, 620));
        posicoesCadeiras.put(7, new Point(690, 620));
        posicoesCadeiras.put(8, new Point(780, 620));
        posicoesCadeiras.put(9, new Point(880, 620));

        // Timer para repintar
        Timer timer = new Timer(100, e -> repaint());
        timer.start();
    }

    public Point getPosicaoCadeira(int index) {
        return posicoesCadeiras.getOrDefault(index, new Point(500, 400));
    }

    public void adicionarFan(Fan fan) {
        fans.add(fan);
    }

    public int getIndiceFan(Fan fan) {
        return fans.indexOf(fan);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha o fundo redimensionado para o tamanho do painel
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        // Desenha os fãs com base em suas posições atuais
        for (Fan fan : fans) {
            fan.desenhar(g);
        }
    }
}

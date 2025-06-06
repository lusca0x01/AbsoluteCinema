import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static Cinema cinema;
    private static PainelCinema painelCinema;

    public static void main(String[] args) {
        int capacity = Integer.parseInt(JOptionPane.showInputDialog("Capacidade do cinema:"));
        int showTime = Integer.parseInt(JOptionPane.showInputDialog("Duracao do filme (em segundos):"));

        cinema = new Cinema(capacity, showTime);
        new Thread(new Demonstrator(cinema), "Demonstrator").start();

        SwingUtilities.invokeLater(Main::criarJanela);
    }

    public static void criarJanela() {
        JFrame frame = new JFrame("Cinema Threads");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);
        frame.setLayout(new BorderLayout());

        // Painel da imagem central com slots para os fas
        painelCinema = new PainelCinema();
        frame.add(painelCinema, BorderLayout.CENTER);

        // Painel de formulario
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelFormulario.setPreferredSize(new Dimension(250, 0));

        JLabel idLabel = new JLabel("Nome do fa:");
        JTextField idField = new JTextField(1);

        JLabel tempoLabel = new JLabel("Tempo lanche (s):");
        JTextField tempoField = new JTextField(1);

        JButton criarFanButton = new JButton("Criar Fa");

        JTextArea logArea = new JTextArea(10, 20);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        painelFormulario.add(idLabel);
        painelFormulario.add(idField);
        painelFormulario.add(Box.createRigidArea(new Dimension(0, 1)));
        painelFormulario.add(tempoLabel);
        painelFormulario.add(tempoField);
        painelFormulario.add(Box.createRigidArea(new Dimension(0, 1)));
        painelFormulario.add(criarFanButton);
        painelFormulario.add(Box.createRigidArea(new Dimension(0, 20)));
        painelFormulario.add(new JLabel("Log de Criacao:"));
        painelFormulario.add(scrollPane);

        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempoField.setAlignmentX(Component.LEFT_ALIGNMENT);
        criarFanButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        frame.add(painelFormulario, BorderLayout.EAST);

        // Ação do botão
        criarFanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                String tempoStr = tempoField.getText().trim();

                if (id.isEmpty() || tempoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Preencha todos os campos.");
                    return;
                }

                try {
                    int tl = Integer.parseInt(tempoStr);
                    Fan fan = new Fan(id, tl, cinema);
                    fan.setPainelCinema(painelCinema);
                    new Thread(fan, "Fan-" + id).start();
                    painelCinema.adicionarFan(fan);
                    logArea.append("Fa " + id + " criado (TL=" + tl + "s)\n");
                    idField.setText("");
                    tempoField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Tempo invalido (use numero inteiro).");
                }
            }
        });

        frame.setVisible(true);
    }
}

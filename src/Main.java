import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Capacidade do cinema: ");
        int capacity = sc.nextInt();

        System.out.print("Duração do filme: ");
        int showTime = sc.nextInt();

        Cinema cinema = new Cinema(capacity, showTime);

        new Thread(new Demonstrator(cinema), "Demonstrator").start();

        while (true) {
            System.out.print("Criar um novo fã? ");
            String opt = sc.next();
            if (opt.equalsIgnoreCase("s")) {
                System.out.print("ID: ");
                String id = sc.next();
                System.out.print("Tempo de lanche (Tl) em segundos: ");
                int lunchTime = sc.nextInt();
                new Thread(new Fan(id, lunchTime, cinema), "Fan-" + id).start();
            }
        }
    }
}

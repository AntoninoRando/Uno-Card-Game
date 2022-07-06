package controller;

import java.util.Scanner;

public class HumanController extends Controller {
    Scanner sc = new Scanner(System.in);

    @Override
    public void run() {
        setName("Thread of " + source.getNickname());

        while (sc.hasNext()) {
            if (sc.hasNextInt())
                inputListener.accept(sc.nextInt(), source);
            else
                inputListener.accept(sc.next(), source);
        }
    }

    @Override
    public void interrupt() {
        // TODO non so se sia questo il metodo per chiudere il thread
        super.interrupt();
        sc.close();
    }
}
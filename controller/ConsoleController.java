package controller;

import java.util.Scanner;

public class ConsoleController extends Controller {
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
        sc.close();
        super.interrupt();
    }

    @Override
    public void setupPlayer() {
        // TODO Auto-generated method stub
        
    }
}
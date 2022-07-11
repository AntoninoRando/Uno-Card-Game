package controller;

import java.util.Scanner;

public class ControllerConsole extends Controller {
    Scanner sc = new Scanner(System.in);

    @Override
    public void run() {
        setName(source.getNickname() + "'s controller Thread");

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
    public void setupControls() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(String eventType, Object data) {
        // TODO Auto-generated method stub
        
    }
}
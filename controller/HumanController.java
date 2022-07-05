package controller;

import java.util.Scanner;

import model.Loop;

public class HumanController extends Controller {
    Scanner sc = new Scanner(System.in);

    @Override
    public void run() {
        try {
            while (sc.hasNext()) {
                if (sc.hasNextInt())
                    Loop.getInstance().events.notify("choiceMade" + source.getID(), source.getHand().get(sc.nextInt()));
                else
                    Loop.getInstance().events.notify("choiceMade" + source.getID(), sc.next());
            }
        } catch (IllegalStateException e) {
            // Scanner closed
            return;
        }
    }

    @Override
    public void on() {
        // TO-DO! Make it start a new thread.
        try {
            while (sc.hasNext()) {
                if (sc.hasNextInt())
                    inputListener.validate(sc.nextInt(), source);
                else
                    inputListener.validate(sc.next(), source);
            }
        } catch (IllegalStateException e) {
            // Scanner closed
            return;
        }
    }

    @Override
    public void off() {
        sc.close();
    }
}
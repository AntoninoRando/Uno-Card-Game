package controller;

import java.util.Scanner;

public class HumanController extends Controller {
    Scanner sc = new Scanner(System.in);

    @Override
    public void on() {
        // TO-DO! Make it start a new thread.
        try {
            while (sc.hasNext()) {
                int choice = sc.nextInt();
                inputListener.validate(choice, source);
            }
        } catch (IllegalStateException e) {
            // When the scanner is closed
            return;
        }
    }

    @Override
    public void off() {
        sc.close();
    }
}
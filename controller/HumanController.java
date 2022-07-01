package controller;

import java.util.Scanner;

public class HumanController extends Controller {
    Scanner sc = new Scanner(System.in);

    @Override
    public void on() {
        while (sc.hasNext()) {
            int choice = sc.nextInt();
            inputListener.validate(choice, source);
        }
    }
}
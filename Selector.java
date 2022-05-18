import java.util.List;
import java.util.Scanner;

public class Selector<T> {
    public T select(List<T> choiches) {
        Scanner choice = new Scanner(System.in);
        choice.close();
        return choiches.get(choice.nextInt());
    }

    public T select(T... choiches) {
        Scanner choice = new Scanner(System.in);
        choice.close();
        return choiches[choice.nextInt()];
    }
}

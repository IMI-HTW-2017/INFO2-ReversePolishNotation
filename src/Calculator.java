import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        System.out.println("Please enter an arithmetic problem to calculate: ");

        Scanner scanner = new Scanner(System.in);
        Postfix pfx = new Postfix();

        System.out.println(pfx.infixToPostfix(scanner.nextLine()));

        System.out.println(pfx.evaluate());
    }
}

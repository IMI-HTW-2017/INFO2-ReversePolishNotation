import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter an arithmetic problem to calculate: ");
        String expression = scanner.nextLine();
        System.out.println("Is the expression in hexadecimal or in decimal (true / false):");
        boolean hex = scanner.nextBoolean();

        Postfix pfx = new Postfix();
        System.out.println(pfx.infixToPostfix(expression, hex));
        System.out.println(pfx.evaluate());
    }
}

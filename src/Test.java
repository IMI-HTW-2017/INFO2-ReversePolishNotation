public class Test {

    public static void main(String[] args) {

        System.out.println(new Postfix("10 2.5 + 3 *", false).evaluate());
        System.out.println(new Postfix("a 2 + 2 *", true).evaluate());
    }
}

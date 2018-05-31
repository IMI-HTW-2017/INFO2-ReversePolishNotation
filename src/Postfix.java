public class Postfix {

    private String postfixNotation;
    private Stack<Integer> operandStack;

    public Postfix(String postfixNotation) {
        this.postfixNotation = postfixNotation;
        operandStack = new LinkedListStack<>();
    }

    public Postfix() {
        this("");
    }

    public int evaluate() {
        String pfx = postfixNotation;

        while (pfx.length() > 0) {
            String next = pfx.substring(0, 1);

            if (next.equals(" ")) {
                pfx = pfx.substring(1, pfx.length());
                continue;
            }

            try {
                operandStack.push(Integer.parseInt(next));
            } catch (NumberFormatException e) {
                try {
                    int rhs = operandStack.pop();
                    int lhs = operandStack.pop();
                    operandStack.push(calculate(lhs, next, rhs));
                } catch (StackUnderflowException e1) {
                    throw new PostfixFormatException("Malformed Postfix Expression");
                }
            }
            pfx = pfx.substring(1, pfx.length());
        }

        int result = operandStack.pop();

        if (!operandStack.isEmpty())
            throw new PostfixFormatException("Malformed Postfix Expression");

        return result;
    }

    public String infixToPostfix(String infix) {
        StringBuilder sb = new StringBuilder();
        Stack<String> stack = new LinkedListStack<>();

        while (infix.length() > 0) {
            String next = infix.substring(0, 1);

            if (next.equals(" ")) {
                infix = infix.substring(1, infix.length());
                continue;
            }

            try {
                int digit = Integer.parseInt(next);
                sb.append(digit);
            } catch (NumberFormatException e) {
                if (next.equals("("))
                    stack.push("(");
                else if (next.equals(")")) {
                    while (!stack.top().equals("("))
                        sb.append(stack.pop());
                    stack.pop();
                } else if (isOperator(next)) {
                    while (!stack.isEmpty() && !(isLowerPrecedence(stack.top(), next) || (next.equals("^") && stack.top().equals("^"))))
                        sb.append(stack.pop());
                    stack.push(next);
                } else throw new InfixFormatException("Malformed Infix Expression");
            }

            infix = infix.substring(1, infix.length());
        }

        while (!stack.isEmpty())
            sb.append(stack.pop());

        postfixNotation = sb.toString();

        return postfixNotation;
    }

    private boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("^");
    }

    private boolean isLowerPrecedence(String top, String next) {
        //True if top is of lower precedence than next
        return getPrecedence(top) < getPrecedence(next);
    }

    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            case "(":
            case ")":
            default:
                return 0;
        }
    }

    private int calculate(int lhs, String operator, int rhs) {
        switch (operator) {
            case "+":
                return lhs + rhs;
            case "-":
                return lhs - rhs;
            case "*":
                return lhs * rhs;
            case "/":
                return lhs / rhs;
            case "^":
                return (int) Math.pow(lhs, rhs);
            default:
                throw new PostfixFormatException("Invalid operator: " + operator);
        }
    }
}

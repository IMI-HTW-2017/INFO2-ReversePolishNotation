public class Postfix {

    private String postfixNotation;

    public Postfix(String postfixNotation) {
        this.postfixNotation = postfixNotation;
    }

    public Postfix() {
        this("");
    }

    public double evaluate() {
        String pfx = postfixNotation;
        Stack<Double> operandStack = new LinkedListStack<>();

        StringBuilder number = new StringBuilder();

        while (pfx.length() > 0) {
            String next = pfx.substring(0, 1);

            if (next.equals(" ")) {
                if (number.length() != 0) {
                    operandStack.push(Double.parseDouble(number.toString()));
                    number.delete(0, number.length());
                }
            } else if (isNumber(next) || next.equals(".")) {
                number.append(next);
            } else {
                if (number.length() != 0) {
                    operandStack.push(Double.parseDouble(number.toString()));
                    number.delete(0, number.length());
                }
                
                try {
                    double rhs = operandStack.pop();
                    double lhs = operandStack.pop();
                    operandStack.push(calculate(lhs, next, rhs));
                } catch (StackUnderflowException e) {
                    throw new MalformedPostfixExpressionException();
                }
            }
            pfx = pfx.substring(1, pfx.length());
        }

        double result = operandStack.pop();

        if (!operandStack.isEmpty())
            throw new MalformedPostfixExpressionException();

        return result;
    }

    public String infixToPostfix(String infix) {
        StringBuilder sb = new StringBuilder();
        Stack<String> stack = new LinkedListStack<>();

        StringBuilder number = new StringBuilder();
        boolean previousWasNumber = false;
        boolean previousWasOperator = false;

        while (infix.length() > 0) {
            String next = infix.substring(0, 1);

            if (next.equals(" ")) {
                if (number.length() != 0) {
                    previousWasNumber = true;
                    sb.append(" ").append(number.toString());
                    number.delete(0, number.length());
                }
            } else if (isNumber(next) || next.equals(".")) {
                if (previousWasNumber)
                    throw new MalformedInfixExpressionException("There may not be two consecutive numbers");
                previousWasOperator = false;
                number.append(next);
            } else {
                if (number.length() != 0) {
                    sb.append(" ").append(number.toString());
                    number.delete(0, number.length());
                }
                previousWasNumber = false;

                if (next.equals("(")) {
                    previousWasOperator = true;
                    stack.push("(");
                } else if (next.equals(")")) {
                    if (previousWasOperator)
                        throw new MalformedInfixExpressionException("There may not be an operator followed by a closed parenthesis");
                    while (!stack.isEmpty() && !stack.top().equals("("))
                        sb.append(" ").append(stack.pop());
                    try {
                        stack.pop();
                    } catch (StackUnderflowException e) {
                        throw new MalformedInfixExpressionException("A closed parenthesis without a matching open parenthesis was found");
                    }
                } else if (isOperator(next)) {
                    if (previousWasOperator)
                        throw new MalformedInfixExpressionException("There may not be two consecutive operators or " +
                                "an open parenthesis followed by an operator");
                    previousWasOperator = true;
                    while (!stack.isEmpty() && !(isLowerPrecedence(stack.top(), next) || (next.equals("^") && stack.top().equals("^"))))
                        sb.append(" ").append(stack.pop());
                    stack.push(next);
                } else throw new MalformedInfixExpressionException();
            }

            infix = infix.substring(1, infix.length());
        }

        if (number.length() != 0)
            sb.append(" ").append(number.toString());

        while (!stack.isEmpty()) {
            if (stack.top().equals("("))
                throw new MalformedInfixExpressionException("An open parenthesis without a matching closed parenthesis was found");
            else
                sb.append(" ").append(stack.pop());
        }

        postfixNotation = sb.toString().trim();

        return postfixNotation;
    }

    private boolean isNumber(String str) {
        return str.matches("[0-9]");
    }

    private boolean isOperator(String str) {
        return str.matches("[+\\-*/^]");
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

    private double calculate(double lhs, String operator, double rhs) {
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
                return Math.pow(lhs, rhs);
            default:
                throw new MalformedPostfixExpressionException("Invalid operator: " + operator);
        }
    }
}

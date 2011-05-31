package org.jprotocol.framework.list;

import static org.jprotocol.util.Contract.neverGetHere;

 
public class LList extends Expr {
    private final String expr;

    public LList(String expr) {
        assert expr != null;
        final String expression = removeUnecessarySpaces(expr.trim().replaceAll("\t", " ").replaceAll("\n", " "));
        assert isComplete(expression);
        this.expr = expression.substring(1, expression.length() - 1);
    }

    private static String removeUnecessarySpaces(String expr) {
        final StringBuffer buf = new StringBuffer();
        buf.append(expr.charAt(0));
        boolean isStr = false;
        for (int i = 1; i < expr.length() - 1; i++) {
            char curr = expr.charAt(i);
            char prev = expr.charAt(i - 1);
            char next = expr.charAt(i + 1);
            if (curr == '"') {
                isStr = !isStr;
            }
            if (!isStr && ((curr == ' ' && (prev == ' ' || prev == '(')) || (curr == ' ' && next == ')'))) {
                //
            } else {
                buf.append(expr.charAt(i));
            }
        }
        buf.append(')');
        return buf.toString();
    }

    public static boolean isComplete(String expr) {
        return hasEnclosingParens(expr.trim());
    }
    public String format(String e) {
        return e.replace('\n', ' ').replace('\t', ' ');
    }
    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public Expr car() {
        if (expr.startsWith("(")) {
            return new LList(expr.substring(0, findMatchingParen(expr, 1) + 1));
        }
        if (expr.charAt(0) == '"') {
            return new SimpleExpr(expr.substring(0, expr.indexOf('"', 1) + 1));
        }
        if (expr.indexOf(' ') < 0) {
            return new SimpleExpr(expr);
        }
        return new SimpleExpr(expr.split(" ")[0]);
    }

    @Override
    public Expr cdr() {
        final int ix = car().toString().length();
        if (ix >= expr.length()) return new LList("()");
        String cdrExpr = expr.substring(ix + 1);
//        if (hasEnclosingParens(cdrExpr)) {
//            return new LList(cdrExpr);
//        }
        return new LList("(" + cdrExpr + ")");
    }
    

    private static boolean hasEnclosingParens(String e) {
        if (e.charAt(0) == '(') {
            return findMatchingParen(e, 1) == e.length() - 1;
        }
        return false;
    }

    @Override
    public Expr cadr() {
        return cdr().car();
    }

    private static int findMatchingParen(String expr, int start) {
        int level = 1;
        for (int i = start; i < expr.length(); i++) {
            if (expr.charAt(i) == ')') {
                level--;
            }
            if (level <= 0) return i;
            if (expr.charAt(i) == '(') {
                level++;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "(" + expr + ")";
    }

    @Override
    public boolean isEmpty() {
        return expr.isEmpty();
    }

    @Override
    public String getCleanStr() {
        neverGetHere();
        return null;
    }

}

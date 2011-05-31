package org.jprotocol.framework.list;


public class SimpleExpr extends Expr {
    private final String expr;

    SimpleExpr(String expr) {
//        if (expr.length() >= 2 && expr.charAt(0) == '\"' && expr.charAt(expr.length() - 1) == '\"') {
//            this.expr = expr.substring(1, expr.length() - 1);
//        } else {
            this.expr = expr;
//        }
    }
    @Override
    public Expr car() {
        return this;
    }

    private void neverGetHere() {
        throw new RuntimeException("Never get here!");
    }
    @Override
    public Expr cdr() {
        return create("");
    }

    @Override
    public boolean isList() {
        return false;
    }
    @Override
    public String toString() {
        return expr;
    }
    @Override
    public Expr cadr() {
        neverGetHere();
        return null;
    }
    @Override
    public boolean isEmpty() {
        return expr.isEmpty();
    }
    @Override
    public String getCleanStr() {
        if (expr.length() >= 2 && expr.charAt(0) == '\"' && expr.charAt(expr.length() - 1) == '\"') {
            return expr.substring(1, expr.length() - 1);
        }
        return  expr.toString();
    }

}

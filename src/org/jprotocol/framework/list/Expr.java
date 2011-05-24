package org.jprotocol.framework.list;


abstract public class Expr {
    abstract public boolean isList();
    abstract public Expr car();
    abstract public Expr cdr();
    abstract public Expr cadr();
    abstract public boolean isEmpty();
    
    public static Expr create(String expr) {
        if (expr.startsWith("(")) {
            return new LList(expr);
        }
        return new SimpleExpr(expr);
    }
    public Expr cons(Expr e2) {
        if (!e2.isList()) throw new RuntimeException("Second part of consing must be a lists");
        return create("(" + (toString() + " " + rest(e2)).trim() + ")");
    }
    
    abstract public String getCleanStr();

    private String rest(Expr e2) {
        if (e2.isEmpty()) return "";
        return (e2.car() + " " + rest(e2.cdr())).trim();
    }
    
    @Override 
    public boolean equals(Object o) {
        if (o instanceof Expr) {
            return toString().equals(o.toString());
        }
        return false;
    }
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}

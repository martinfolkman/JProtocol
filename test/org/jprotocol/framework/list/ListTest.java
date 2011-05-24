package org.jprotocol.framework.list;

import junit.framework.TestCase;


public class ListTest extends TestCase {
    public void testLList() {
        Expr e1 = new LList("(+ 1 2 3)");
        assertEquals("(+ 1 2 3)", e1.toString());
        assertEquals("+", e1.car().toString());
        assertEquals("(1 2 3)", e1.cdr().toString());

        Expr e2 = new LList("(1 (4 5) 2 3)");
        assertEquals("1", e2.car().toString());
        assertEquals("((4 5) 2 3)", e2.cdr().toString());
        assertEquals("(4 5)", e2.cadr().toString());
        
        assertEquals("(5)", new LList("(4 5)").cdr().toString());
        assertEquals("()", new LList("(5)").cdr().toString());
        
    }
    
    public void test() {
        assertEquals("(1 (+ 1 1))", Expr.create("(+ 1 (+ 1 1))").cdr());
        assertEquals("((+ 1 1))", Expr.create("(+ 1 (+ 1 1))").cdr().cdr());
        assertEquals("(+ 1 1)", Expr.create("(+ 1 (+ 1 1))").cdr().cdr().car());
        assertEquals("()", Expr.create("(+ 1 (+ 1 1))").cdr().cdr().cdr());
        
    }

    public void testCarCdrEtc() {
        assertEquals("+", Expr.create("(+ 1 2)").car());
        assertEquals("(1 2)", Expr.create("(+ 1 2)").cdr());
        assertEquals("+", Expr.create("(+ (+ 1 2) 2)").car());
        assertEquals("((+ 1 2) 2)", Expr.create("(+ (+ 1 2) 2)").cdr());
        assertEquals("(+ 1 2)", Expr.create("((+ 1 2) 2)").car());
        assertEquals("(2)", Expr.create("((+ 1 2) 2)").cdr());
    
        assertEquals("+", Expr.create("(+ 1 (* 2 2) (/ (/ 100 10) 2))").car());
        assertEquals("(1 (* 2 2) (/ (/ 100 10) 2))", Expr.create("(+ 1 (* 2 2) (/ (/ 100 10) 2))").cdr());
        assertEquals("1", Expr.create("(1 (* 2 2) (/ (/ 100 10) 2))").car());
        assertEquals("((* 2 2) (/ (/ 100 10) 2))", Expr.create("(1 (* 2 2) (/ (/ 100 10) 2))").cdr());
        assertEquals("(* 2 2)", Expr.create("((* 2 2) (/ (/ 100 10) 2))").car());
        assertEquals("((/ (/ 100 10) 2))", Expr.create("((* 2 2) (/ (/ 100 10) 2))").cdr());
        assertEquals("/", Expr.create("(/ (/ 100 10) 2)").car());
    }
    
    
    public void testCadr() {
        assertEquals("((x) (+ x 1) (env 1 2))", Expr.create("(closure (x) (+ x 1) (env 1 2))").cdr());
        assertEquals("(x)", Expr.create("(closure (x) (+ x 1) (env 1 2))").cadr());
    }
    public void testCdrCdr() {
        assertEquals("((+ x 1))", Expr.create("(closure (x) (+ x 1))").cdr().cdr());
    }

    public void testCompleteness() {
        assertFalse(LList.isComplete("(+ (+ 1 1) 2"));
        assertTrue(LList.isComplete("(+ (+ 1 1) 2)"));
    }
    
    
    public void testConsing() {
        assertEquals("(1)", Expr.create("1").cons(Expr.create("()")));
        assertEquals("(1 2 3)", Expr.create("1").cons(Expr.create("(2 3)")));
        assertEquals("((1) 2 3)", Expr.create("(1)").cons(Expr.create("(2 3)")));
        assertEquals("((a 1) b 2)", Expr.create("(a 1)").cons(Expr.create("(b 2)")));
        assertEquals("(def a 1)", Expr.create("((def a 1) (def b 2))").car());
        assertEquals("(def b 2)", Expr.create("((def a 1) (def b 2))").cdr().car());
    }

    public void testLambda() {
        assertEquals("(def f (lambda (a b c) (+ a b c)))", Expr.create("((def f (lambda (a b c) (+ a b c))))").car());
        assertEquals("(f (lambda (a b c) (+ a b c)))", Expr.create("((def f (lambda (a b c) (+ a b c))))").car().cdr());
        assertEquals("((lambda (a b c) (+ a b c)))", Expr.create("((def f (lambda (a b c) (+ a b c))))").car().cdr().cdr());
        assertEquals("lambda", Expr.create("((def f (lambda (a b c) (+ a b c))))").car().cdr().cdr().car().car());
    }
    
    public void testWithFormattedText() {
        assertEquals("(def f (lambda (a b c) (+ a b c)))", Expr.create("( def  f   (lambda  \t(a   b  c ) \n( +  a  b c)))"));
    }
    
    public void testString() {
        Expr expr = Expr.create("(+ \"hej på dig\" \"  anders  eliasson  \")");
        assertEquals("(+ \"hej på dig\" \"  anders  eliasson  \")", expr);
        assertEquals("+", expr.car());
        assertEquals("\"hej på dig\"", expr.cdr().car());
        assertEquals("\"  anders  eliasson  \"", expr.cdr().cdr().car());
    }
    
    private void assertEquals(String e1, Expr e2) {
        assertEquals(e1, e2.toString());
    }

}

package org.jprotocol.framework.test;

import org.jprotocol.framework.handler.QualifiedName;
import org.jprotocol.framework.list.Expr;

public class QualifiedRequest {
    public final Expr request;
    public final QualifiedName context;
    QualifiedRequest(Expr request, QualifiedName context) {
        this.request = request;
        this.context = context;
    }
    
    @Override
    public String toString() {
    	return context + " " + request;
    }
}


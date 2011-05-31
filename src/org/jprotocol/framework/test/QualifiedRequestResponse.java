package org.jprotocol.framework.test;

import org.jprotocol.framework.handler.QualifiedName;
import org.jprotocol.framework.list.Expr;

public class QualifiedRequestResponse extends QualifiedRequest {
    public final Expr response;
	public final boolean removeWhenMatched;
    QualifiedRequestResponse(Expr request, Expr response, boolean removeWhenMatched, QualifiedName context) {
    	super(request, context);
    	this.response = response;
    	this.removeWhenMatched = removeWhenMatched;
    }
    @Override
    public String toString() {
    	return context + " Req:" + request + ", Res:" + response + ", removeWhenMatched: " + removeWhenMatched;
    }

}

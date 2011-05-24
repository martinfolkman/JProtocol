package org.jprotocol.framework.test;

import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.list.Expr;

/**
 * String protocol parser using the Lisp list syntax
 * @author eliasa01
 * @see Expr
 */
public class StringProtocolUtil {
	/**
	 * Get the name of a protocol expression
	 * @param expr a complete protocol string
	 * @return the name
	 */
    public static String protocolNameOf(Expr expr) {
        return expr.car().getCleanStr();
    }
    
    public static String protocolNameOfRequestResponse(Expr requestResponseExpr) {
    	return protocolNameOf(requestOf(requestResponseExpr));
    }
    public static Expr requestOf(Expr requestResponseExpr) {
    	return requestResponseExpr.car();
    }
    public static Expr responseOf(Expr requestResponseExpr) {
    	return requestResponseExpr.cdr();
    }
    
    /**
     * Get the arguments of the protocol string
     * @param expr a complete protocol string
     * @return
     */
    public static Expr argsOf(Expr expr) {
        return expr.cdr();
    }


    /**
     * The first argument in an argument/value expression (head in lisp terms)
     * @param args arguments/values of a protocol
     * @return the name of the argument
     */
    public static String argOf(Expr args) {
        return args.car().car().getCleanStr();
    }
    
    /**
     * The next arguments in a argument list
     * @param args
     * @return
     */
    public static Expr nextArgs(Expr args) {
        return args.cdr();
    }
    
    /**
     * The first value of an argument/value expression
     * @param args arguments/values of a protocol
     * @return the value of the first argument
     */
    public static String valueOf(Expr args) {
        return args.car().cdr().car().getCleanStr();
    }
    
    
    /**
     * Set the values of arguments for a protocol 
     * @param args the argument/value expression
     * @param p the protocol, will be change
     * @return the resulting protocol
     * 
     */
    public static IProtocolMessage setArgs(Expr args, IProtocolMessage p) {
        if (args.isEmpty()) return p;
        p.setValue(argOf(args), valueOf(args));
        return setArgs(args.cdr(), p);
    }
    
    public static boolean isMatchArgs(Expr args, IProtocolMessage protocol) {
        if (args.isEmpty()) return true;
        return valueOf(args).equals(protocol.getValue(argOf(args))) && isMatchArgs(nextArgs(args), protocol);
    }

    
    public static String readableProtocol(Expr protocol) {
		StringBuilder msg = new StringBuilder();
		msg.append('"');
        msg.append(protocolNameOf(protocol));
		msg.append('"');
		msg.append(" with arguments:");
        msg.append(readableArgs(nextArgs(protocol)));
        return msg.toString();
    }

	private static String readableArgs(Expr args) {
		if (args.isEmpty()) return "";
		String nextArgs = readableArgs(nextArgs(args));
		String result = " \"" + argOf(args) + "\"=\"" + valueOf(args) + "\"";
		if (nextArgs.isEmpty()) {
			return result;
		}
		return result + "," + nextArgs;
	}

}

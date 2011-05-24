package org.jprotocol.util;


import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;





/**
 * <b>Contract</b> - Pragmatic support for Design by Contract<sup>TM</sup> or Contract Programming.
 * <p>
 * <strong>Background</strong><br> 
 * The central idea of DBC is a metaphor on how elements of a software system 
 * collaborate with each other, on the basis of mutual obligations and benefits. 
 * The metaphor comes from business life, where a "client" and a "supplier" 
 * agree on a "contract" which defines for example that:<ul>
 * <li>The supplier must provide a certain product (obligation) and is entitled to 
 *     expect that the client has paid its fee (benefit). 
 * <li>The client must pay the fee (obligation) and is entitled to get the product 
 *     (benefit). 
 * <li>Both parties must satisfy certain obligations, such as laws and regulations, 
 *     applying to all contracts. [WikiPeadia]
 * </ul>
 * <strong>Guidelines for defining contracts</strong><ul>
 * <li>Do NOT use commands when defining contract statements. Only queries are
 * allowed. That means, executing the contract  shall not change the (perceived)
 * state of that object.   The reason is  that contracts are part of the specification, not the semantics!
 * <li>Clients must have the possibility to access the queries used in require 
 * contracts. Do not use private fields/methods in contract statements.
 * <li>Try to define "atomic" contracts, that is avoid merging contract that could
 * be stated separately. This helps the error location process after a contract
 * violation, you do not have to guess in which part the violation occurred.
 * <li>Contracts and multi-threading requires special attention! E.g. do not put 
 * contracts on the supplier object if used by multiple threads. 
 * </ul>
 * <strong>Practical info</strong><br>
 * Contract programming is supported in multiple ways with this pragmatic encapsulation: 
 * <ul>
 * <li>Some are based on <code>boolean</code>s and boolean expressions. These methods
 *     are named isXyz, eg <code>isNotNull(...)</code> and <code>isInRange(...)</code>.
 *     Use implementation in SyntacticSugar.java. The corresponding implementation in
 *     this class is deprecated.</li>
 * <li>Other are based on the Hamcrest matchers.</li>
 * <li>The rest are based on the type <code>IContractCondition</code>, to be able
 *      to support automatic logging for standard cases. This means that you
 *      don't have to (but you still may) include description strings, and the 
 *      readability is improved. The naming for these are eg <code>notNull(...)</code> and
 *      <code>inRange(...)</code></li>
 * </ul> 
 * <strong>Info and examples:</strong><ul>
 * <li>Read the java-doc for info and simple examples.
 * <li>Look at the unit-tests in ContractTest.java.
 * <li>Hit "Ctrl-Shift-G" in Eclipse having the cursor on e.g the require() definition below.
 * <li>Se e.g. <a href="http://en.wikipedia.org/wiki/Design_by_contract">http://en.wikipedia.org/wiki/Design_by_contract</a> for further information.
 * <li>Read chapter 11 (Design by Contract - building reliable software) in "Object Oriented Software Construction", 2:nd ed. by Bertrand Meyer.
 * </ul>
 * <p>
 * @author Christopher Graae (graaec01)
 */
public class Contract
{    
    // --------------------------------------------
    // Contract programming interfaces.
    // --------------------------------------------
    
    /**
     * Used to define conditions when formulation a contract, that when violated
     * supports automatic logging.
     */
    public interface IContractCondition
    {
        public boolean condition(); 
        public String description();
    }

    /**
     * The error class that is thrown in case of contract violations
     */
    @SuppressWarnings("serial")
	public static final class ContractError extends AssertionError
    {
        public ContractError(final String msg)
        {
            super(msg);
        }
    }

        
    // --------------------------------------------
    // Contract Programming primitive definitions.
    // --------------------------------------------
    
    /**
     * Use this to define pre-conditions based on a boolean value that is 
     * required to be valid when entering a method. 
     * <p>
     * This call is delegated to get a predefined description during the 
     * logging of a require contract violation. Use that variant directly when 
     * possible, to get maximum support for good readability and logging.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require(isInRage(value, rangeMin, rangeMax));
     * require(isInRage(value, rangeMin, rangeMax), "Value not in range [", rangeMin, "-", "rangeMax]");
     * </pre>
     * @see #require(IContractCondition, Object...)
     */
    public static void require(boolean requireCondition, final Object... description)
    {
        require(equalsTrue(requireCondition), description);
    }

    /**
     * Used this to define pre-conditions that are based on a ContractCondition 
     * required to be valid when entering a method. The contract condition are
     * logged in case of a violation. If needed you may add extra logging in 
     * addition to the default.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require(inRange(value, rangeMin, rangeMax));
     * require(inRange(value, rangeMin, rangeMax), "Optional logging, eg. this=", this);
     * </pre>
     * @see #require(boolean, Object...)
     */
    public static void require(final IContractCondition contract, final Object... description)
    {
        if (SyntacticSugar.isNull(contract) || !contract.condition())
        {
            throwContractViolationException(REQUIRE_LABEL, contract, description);
        }
    }

    /**
     * Used this to define pre-conditions that are based on Matchers.
     * Objects are required to fulfill the Matcher when entering a method. 
     * The contract condition are logged in case of a violation. 
     * If needed you may add extra logging in addition to the default.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require(value, is(not(nullValue()));
     * require(value, is(not(nullValue(), "Must provide value");
     * </pre>
     * @see #require(boolean, Object...)
     */
    public static void require(final Object object, final Matcher<?> matcher, final Object... additionalValues)
    {
        if (SyntacticSugar.isNull(matcher) || !matcher.matches(object))
        {
            throwContractViolationException(REQUIRE_LABEL, object, matcher, additionalValues);
        }
    }
    
    /**
     * Use this to define post-conditions that is ensured to be valid. 
     * <p>
     * This call is delegated to get a predefined description during the logging 
     * of a ensure contract violation.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * ensure(isInRage(value, rangeMin, rangeMax));
     * ensure(isInRage(value, rangeMin, rangeMax), "Value not in range [", rangeMin, "-", "rangeMax]");
     * </pre>
     * @see #ensure(IContractCondition, Object...)
     */
    public static void ensure(boolean ensureCondition, final Object... description)
    {
        ensure(equalsTrue(ensureCondition), description);
    }
    
    /**
     * Used to define post-conditions, required to be valid at exit-time.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * ensure(inRange(value, rangeMin, rangeMax));
     * ensure(inRange(value, rangeMin, rangeMax), "Optional logging, eg. this=", this);
     * </pre>
     * @see #ensure(boolean, Object...)
     */
    public static void ensure(final IContractCondition contract, final Object... description)
    {
        if (SyntacticSugar.isNull(contract) || !contract.condition())
        {
            throwContractViolationException(ENSURE_LABEL, contract, description);
        }
    }

    /**
     * Used to define post-conditions, required to be valid at exit-time.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * ensure(value, is(notNullValue());
     * </pre>
     * @see #ensure(boolean, Object...)
     */
    public static void ensure(final Object object, final Matcher<?> matcher, final Object... additionalValues)
    {
        if (SyntacticSugar.isNull(matcher) || !matcher.matches(object))
        {
            throwContractViolationException(ENSURE_LABEL, object, matcher, additionalValues);
        }
    }
    
    /**
     * Used to define invariants for a class, required to be valid at the end 
     * of construction, and at enter and exit of a public method.
     * <p>
     * This call is delegated to get a predefined description during the logging 
     * of a violation. 
     * @see #invariant(IContractCondition, Object...) (includes an example)
     */
    public static void invariant(boolean invariantCondition, final Object... description)
    {
        invariant(equalsTrue(invariantCondition), description);
    }

    /**
     * Used to define invariants for a class, required to be valid at the end 
     * of construction, and during entering and exiting of all public methods.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * public class Data
     * {
     *    private static final int MIN = 1;
     *    private static final int MAX = 12;
     *    private int myData;
     * 
     *    public Data()
     *    {
     *       myData = 1;
     *  
     *       invariant(isValid());
     *    }
     * 
     *    public void setBigData(final int theData)
     *    {
     *       invariant(isValid());
     *       require( theData > 7, "Expected big data, i.e > 7");
     *    
     *       myData = theData
     *    
     *       invariant(isValid());
     *     }
     * 
     *     public boolean isValid()
     *     {
     *       return isInRange(myData, MAX, MIN);
     *     }
     * }</pre>
     * @see #invariant(boolean, Object...)
     */
    public static void invariant(final IContractCondition contract, final Object... description)
    {
        if (SyntacticSugar.isNull(contract) || !contract.condition())
        {
            throwContractViolationException(INVARIANT_LABEL, contract, description);
        }
    }
    
    public static void invariant(final Object object, final Matcher<?> matcher, final Object... additionalValues)
    {
        if (SyntacticSugar.isNull(matcher) || !matcher.matches(object))
        {
            throwContractViolationException(INVARIANT_LABEL, object, matcher, additionalValues);
        }
    }
    
    /**
     * Used to check a condition in the middle of a set of statements, in contrast 
     * to require, ensure and invariant contracts. The need for this construct may
     * indicate that a better solution would be to refactor using "extract method",
     * and use real contract statements in that method.
     * <p>
     * The call is delegated to get a predefined description during the logging 
     * of a violation.
     * <p>
     * <b>Example:</b> 
     * <pre>
     *    check( retValue < 0, "Query returned error condition.");</pre>
     * @see #check(IContractCondition, Object... )
     */
    public static void check(boolean checkCondition, final Object... description)
    {
        check(equalsTrue(checkCondition), description);
    }

    /**
     * Used to check a condition in the middle of a set of statements, in contrast 
     * to require, ensure and invariant contracts. The need for this construct may
     * indicate that a better solution would be to refactor using "extract method",
     * and use real contract statements in that method.
     * <pre>
     *    check( retValue < 0, "Query returned error condition."); </pre>
     * @see #check(boolean, Object... )
     */
    public static void check(final IContractCondition contract, final Object... description)
    {
        if (SyntacticSugar.isNull(contract) || !contract.condition())
        {
            throwContractViolationException(CHECK_LABEL, contract, description);
        }
    }

    public static void check(final Object object, final Matcher<?> matcher, final Object... additionalValues)
    {
        if (SyntacticSugar.isNull(matcher) || !matcher.matches(object))
        {
            throwContractViolationException(CHECK_LABEL, object, matcher, additionalValues);
        }
    }
    
    /**
     * This type of contract is used to guard illegal / non-expected conditions
     * and execution paths.
     * <p>
     * <b>Example #1:</b> 
     * <pre>
     * if (query1())
     * { ... }
     * else if(query2())
     * { ... }
     * else
     * {
     *    <b>neverGetHere("The queries query1 and query2 were supposed to be complementary!");</b> 
     * }</pre>
     * <p>
     * <b>Example #2:</b>
     * <pre>
     * switch (month) 
     * {
     *       case 1:  System.out.println("January"); break;
     *       case 2:  System.out.println("February"); break;
     *       case 3:  System.out.println("March"); break;
     *       case 4:  System.out.println("April"); break;
     *       case 5:  System.out.println("May"); break;
     *       case 6:  System.out.println("June"); break;
     *       case 7:  System.out.println("July"); break;
     *       case 8:  System.out.println("August"); break;
     *       case 9:  System.out.println("September"); break;
     *       case 10: System.out.println("October"); break;
     *       case 11: System.out.println("November"); break;
     *       case 12: System.out.println("December"); break;
     *       default: <b>neverGetHere("Invalid month.")</b>;break;
     * }</pre>
     * </tt>
     */
    public static void neverGetHere(final Object... description) 
    {
        final String contractDescription = "This code-path was never expected to be executed. ";
        throwContractViolationException(NEVERGETHERE_LABEL, contractDescription, description);
    }

    /**
     * This type of "contract" is used to mark a point that is under construction. Code with 
     * this contract is not supposed to be released. It is ment to be used instead
     * of a neverGetHere-contract, for the purpose of showing that a pice of code is not ready
     * to be tested - i.e, no need to create a failure report. 
     * <p>
     * Tip: Code-spots under construction
     * is easily found by a quick search for references to this method.
     * <p>
     * @param description - eg the reason and the developers name writing this "contract".
     */
    public static void underConstruction(final Object... description) 
    {
        final String contractDescription = "You have reached code that is under construction, and not yet ready to be tested. ";
        throwContractViolationException(UNDERCONSTRUCTION_LABEL, contractDescription, description);
    }
    
    
    // --------------------------------------------
    // Contract programming helper methods.
    // --------------------------------------------
    
    
    /**
     * When there is a need to verify a condition only if another conditions is true.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( implies(givenIsInitiated(), thenVerifyStatus()) );
     * </pre>
     * 
     * @Deprecated Use equivalent in SyntaticSugar.java
     */
    public static boolean implies(
    		final boolean impliesCondition, 
    		final boolean condition)
    {
        final boolean doNotEvaluateCondition = true;
    	return impliesCondition ? condition : doNotEvaluateCondition;
    }
    
    /**
     * When there is a need to verify that a reference is null.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * ensure( isNull(myDataToBeCleared) );
     * </pre>
     * @see #equalsNull()
     * 
     * @Deprecated Use equivalent in SyntaticSugar.java
     */
    public static <T> boolean isNull(final T ref)
    {
    	return (null == ref);
    }

    /**
     * When there is a need to verify that a reference is null. The basic
     * logging is handled for you, but you may supply additional things to log
     * in case of a contract violation.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * ensure( equalsNull(myDataToBeCleared) );
     * </pre>
     * @see #isNull()
    */
    public static <T> IContractCondition equalsNull(final T ref)
    {
        final IContractCondition condition;
        if (SyntacticSugar.isNull(ref)) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String anonymous = "<anonymous>";
            final String className = ref.getClass().getSimpleName();
            final boolean noName = className.isEmpty();
            final String description = "The reference points to an [" + (noName ? anonymous : className) + "] instance, but was expected to be a null reference.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }
    
    /**
     * When there is a need to verify that a reference isn't null.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( isNotNull(myData), "Expected myData to refere to an instance, not null." );
     * </pre>
     * @see #notNull()
     *
     * @Deprecated Use equivalent in SyntaticSugar.java
    */
    public static <T> boolean isNotNull(final T ref)
    {
    	return (null != ref);
    }
    
    /**
     * When there is a need to verify that a reference isn't null. The basic
     * logging is handled for you, but you may supply additional things to log
     * in case of a contract violation.     
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( notNull(myData) );
     * </pre>
     * @see #isNotNull()
     */
    public static <T> IContractCondition notNull(final T ref)
    {
        final IContractCondition condition;
        if (SyntacticSugar.isNotNull(ref)) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The reference is null, but was expected to refere to an instance.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }

    // ------------------------------------
    // Verifying that a value is non-negative.
    // ------------------------------------
    
//    /**
//     * When there is a need to test for whether a value is non-negative or not.
//     * <p>
//     * <b>Example:</b> 
//     * <pre>
//     * require( isNonNegative(myValue), "Expected myValue to be non-negative, not: ", myValue );
//     * </pre>
//     * @see #nonNegative(int)
//     * @see #nonNegative(double)
//     * @see #isNonNegative(double)
//     * 
//     * @Deprecated Use equivalent in SyntaticSugar.java
//     */
//    public static boolean isNonNegative(final int value)
//    {
//        return (0 <= value);
//    }

    /**
     * When there is a need to test for whether a a value is non-negative or not.
     * The basic logging is handled for you, but you may supply additional things 
     * to log in case of a contract violation.     
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( nonNegative(myValue) );
     * </pre>
     * @see #isNonNegative(int)
     * @see #nonNegative(double)
     * @see #isNonNegative(double)
     */
    public static IContractCondition nonNegative(final int value)
    {
        final IContractCondition condition;
        if (0 <= value)
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The value is " + value + ", but was expected to be non-negative.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }


    /**
     * When there is a need to test for whether a a value is non-negative or not.
     * The basic logging is handled for you, but you may supply additional things 
     * to log in case of a contract violation.     
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( nonNegative(myValue) );
     * </pre>
     * @see #nonNegative(int)
     * @see #isNonNegative(int)
     * @see #isNonNegative(double)
     */
    public static IContractCondition nonNegative(final double value)
    {
        final IContractCondition condition;
        if (0 <= value)
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The value is < 0.0, but was expected to be non-negative.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }

    
    // -------------------------------------------
    // Testing if a value is in a specific range.
    // -------------------------------------------
    
    /**
     * When there is a need to test for whether a value is in a range 
     * (<code>int</code>) or not.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( isInRange(myIntValue, 0, 100) );
     * </pre>
     * @see #inRange(int)
     * @see #isInRange(double)
     * @see #inRange(double)
     * 
     * @Deprecated Use equivalent in SyntaticSugar.java
     */
    public static boolean isInRange(final int value, final int rangeMin, final int rangeMax)
    {
        final boolean notBellow = (value >= rangeMin);
        final boolean notAbove  = (value <= rangeMax);
        return notBellow && notAbove;
    }

    /**
     * When there is a need to test for whether a value is in a range 
     * (<code>int</code>) or not.
     * <p>
     * The basic logging is handled for you, but you may supply additional things 
     * to log in case of a contract violation.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( inRange(myIntValue, 0, 100) );
     * </pre>
     * @see #isInRange(int)
     * @see #isInRange(double)
     * @see #inRange(double)
     */
    public static IContractCondition inRange(final int value, final int rangeMin, final int rangeMax)
    {
        final boolean notBellow = (value >= rangeMin);
        final boolean notAbove  = (value <= rangeMax);

        final IContractCondition condition;
        if (notBellow && notAbove)
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The value " + value + " is not within the range [" + rangeMin + ", " + rangeMax + "].";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }

    
    /**
     * When there is a need to test for whether a value is in a range 
     * (<code>double</code>) or not.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( isInRange(myIntValue, 0, 100) );
     * </pre>
     * @see #isInRange(int)
     * @see #inRange(int)
     * @see #inRange(double)
     * 
     * @Deprecated Use equivalent in SyntaticSugar.java
    */
    public static boolean isInRange(final double value, final double rangeMin, final double rangeMax)
    {
        final boolean notBellow = (value >= rangeMin);
        final boolean notAbove  = (value <= rangeMax);
        return notBellow && notAbove;
    }
            
    /**
     * When there is a need to test for whether a value is in a range 
     * (<code>double</code>) or not.
     * <p>
     * The basic logging is handled for you, but you may supply additional things 
     * to log in case of a contract violation.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( inRange(myDoubleValue, -1.5, 3.14) );
     * </pre>
     * @see #isInRange(int)
     * @see #inRange(int)
     * @see #isInRange(double)
     */
    public static IContractCondition inRange(final double value, final double rangeMin, final double rangeMax)
    {
        final boolean notBellow = (value >= rangeMin);
        final boolean notAbove  = (value <= rangeMax);

        final IContractCondition condition;
        if (notBellow && notAbove) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The value " + value + " is not within the range [" + rangeMin + ", " + rangeMax + "].";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }
    
    // ------------------------------------
    // Testing if a value is non-empty.
    // ------------------------------------
    
    /**
     * When there is a need to test a collection for non-emptyness...
     * <p>
     * <b>Example:</b> 
     * <p>
     * require( isNotEmpty(getMyCollection()), "Expected a non-empty collection." );
     * <p>
     * @see #notEmpty(Collection)
     * @see #isNotEmpty(String)
     * @see #notEmpty(String)
     * @see #isNotEmpty(Object[])
     * @see #notEmpty(Object[])
     * 
     * @Deprecated Use equivalent in SyntaticSugar.java
     */
    public static boolean isNotEmpty(final Collection<?> collection)
    {
        boolean isNotEmpty = false;
        
        if (SyntacticSugar.isNotNull(collection) && !collection.isEmpty())
        {
            isNotEmpty = true;
        }
        
        return isNotEmpty;
    }

    /**
     * When there is a need to test a collection for non-emptyness...
     * <p>
     * The basic logging is handled for you, but you may supply additional 
     * things to log in case of a contract violation.     
     * <p>
     * <b>Example:</b> 
     * <p>
     * require( notEmpty(getMyCollection()) );
     * <p>
     * @see #isNotEmpty(Collection)
     * @see #isNotEmpty(String)
     * @see #notEmpty(String)
     * @see #isNotEmpty(Object[])
     * @see #notEmpty(Object[])
     */
    public static IContractCondition notEmpty(final Collection<?> collection)
    {
        final IContractCondition condition;
        if (SyntacticSugar.isNotNull(collection) && !collection.isEmpty()) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The collection is " + ((null == collection) ? "null" : "empty") + ", but was expected to be non-empty.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }

    

    /**
     * When there is a need to test a string for non-emptyness. Null is also 
     * considered beeing a failure.
     * <p>
     * <b>Example:</b> 
     * <p>
     * require( isNotEmpty(getSessionId()), "Expected a non-empy session id." );
     * <p>
     * @see #isNotEmpty(Collection)
     * @see #notEmpty(Collection)
     * @see #notEmpty(String)
     * @see #isNotEmpty(Object[])
     * @see #notEmpty(Object[])
     * 
     * @Deprecated Use equivalent in SyntaticSugar.java
     */
    public static boolean isNotEmpty(final String s)
    {
        boolean isNotEmpty = false;
        
        if (SyntacticSugar.isNotNull(s) && !s.isEmpty())
        {
            isNotEmpty = true;
        }
        
        return isNotEmpty;
    }

    /**
     * When there is a need to test for non-emptyness...
     * <p>
     * The basic logging is handled for you, but you may supply additional 
     * things to log in case of a contract violation.
     * <p>
     * <b>Example:</b> 
     * <p>
     * require( notEmpty(getSessionId()) );
     * <p>
     * @see #isNotEmpty(Collection)
     * @see #notEmpty(Collection)
     * @see #isNotEmpty(String)
     * @see #isNotEmpty(Object[])
     * @see #notEmpty(Object[])
     */
    public static IContractCondition notEmpty(final String s)
    {
        final IContractCondition condition;
        if (SyntacticSugar.isNotNull(s) && !s.isEmpty()) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The string is " + ((null == s) ? "null" : "empty") + ", but was expected to be non-empty.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }
    
    /**
     * When there is a need to test a "farmer-array" for non-emptyness...
     * <p>
     * The basic logging is handled for you, but you may supply additional 
     * things to log in case of a contract violation.
     * <p>
     * <b>Example:</b> 
     * <p>
     * require( notEmpty(getFarmerArray()) );
     * <p>
     * @see #isNotEmpty(Collection)
     * @see #notEmpty(Collection)
     * @see #isNotEmpty(String)
     * @see #notEmpty(String)
     * @see #isNotEmpty(Object[])
     */
    public static <T> IContractCondition notEmpty(final T[] objArr)
    {
        final IContractCondition condition;
        if (SyntacticSugar.isNotNull(objArr) && (objArr.length > 0)) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final boolean isNull = (null == objArr);
            final String description = "The array is " + (isNull ? "null" : "empty") + ", but was expected to be non-empty.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }

    /**
     * When there is a need to test a "farmer-array" for non-emptyness...
     * <p>
     * The basic logging is handled for you, but you may supply additional 
     * things to log in case of a contract violation.
     * <p>
     * <b>Example:</b> 
     * <p>
     * require( notEmpty(getFarmerArray()) );
     * <p>
     * @see #isNotEmpty(Collection)
     * @see #notEmpty(Collection)
     * @see #isNotEmpty(String)
     * @see #notEmpty(String)
     * @see #isNotEmpty(Object[])
     */
    public static IContractCondition notEmpty(final double[] doubleArr)
    {
        final IContractCondition condition;
        if (isNotNull(doubleArr) && (doubleArr.length > 0)) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final boolean isNull = (null == doubleArr);
            final String description = "The array of doubles is " + (isNull ? "null" : "empty") + ", but was expected to be non-empty.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }


    // ------------------------------------
    // Testing if a value is true or false.
    // ------------------------------------    
    
    /**
     * When there is a need to test if a value is true. It is provided mostly 
     * for the purpose of converting a <code>boolean</code>-based contract to a 
     * <code>ContractCondition</code> based contract.
     * <p>
     * The basic logging is handled for you, but you may supply additional 
     * things to log in case of a contract violation.
     * <p>
     * @see #equalsFalse()
     */
    public static IContractCondition equalsTrue(final boolean value)
    {
        final IContractCondition condition;
        if (value) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The value is false, but was expected to be true.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }


    /**
     * When there is a need to test if a value is false.
     * <p>
     * The basic logging is handled for you, but you may supply additional 
     * things to log in case of a contract violation.
     * <p>
     * @see #equalsTrue(boolean)
     */
    public static IContractCondition equalsFalse(final boolean value)
    {
        final IContractCondition condition;
        if (!value) 
        {
            condition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The value is true, but was expected to be false.";
            condition = createContractCondition(FAILURE, description);
        }
        
        return condition;
    }

    
    /**
     * When there is a need to verify a given condition for all elements in a 
     * collection.
     * <p>
     * The basic logging is handled for you, but you may supply additional 
     * things to log in case of a contract violation.
     * <p>
     * <b>Example:</b> 
     * <pre>
     *    ensure( forAll(myErrorLogCollection, isNotNull()) );
     * </pre>
     * @see #isForAll()
     */
    public static IContractCondition forAll(final Collection<?> collection, final Comparable<?> forAllCondition)
    {
        final IContractCondition contractCondition;
        if (SyntacticSugar.isForAll(collection, forAllCondition)) 
        {
            contractCondition = getSuccessContractCondition();
        }
        else 
        {
            final String description = "The supplied condition was not true for all elements in the collection: ";
            final String collectionStr;
            if (SyntacticSugar.isNotNull(collection))
            {
                collectionStr = collection.toString();
            }
            else
            {
                collectionStr = "<null>";
            }
            
            contractCondition = createContractCondition(FAILURE, description + collectionStr);
        }
        
        return contractCondition;
    }

    
    
    // *************************************************************************
    // *************************************************************************
    // Public helper methods
    // *************************************************************************
    // *************************************************************************

 
    
    /**
     * Getter for a statically created success condition. Use this when you
     * want to define your own conditions.
     * 
     * @see #createContractCondition(boolean, string)
     */
    public static IContractCondition getSuccessContractCondition()
    {
        return SUCCESS_CONDITION;
    }
   
    /**
     * Factory method for creating your own contract conditions.
     * 
     * @see #getSuccessContractCondition() 
     */
    public static IContractCondition createContractCondition(
            final boolean condition, 
            final String description)
    {
        final class ContractCondition implements IContractCondition
        {
            final boolean myCondition;
            final String myDescription;
            
            ContractCondition(final boolean cond, final String descr)
            {
                myCondition = cond;
                myDescription = descr;
            }
            
            public boolean condition()
            {
                return myCondition;
            }

            public String description()
            {
                return myDescription + " ";
            }
        }
        
        return new ContractCondition(condition, description);    
    }

    
    // *************************************************************************
    // *************************************************************************
    // Private helper methods
    // *************************************************************************
    // *************************************************************************    

    
    private static void throwContractViolationException(
            final String label, 
            final IContractCondition contract, 
            final Object... description) throws Error
    {
        String contractDescription = "Contract condition is null.";
        if (SyntacticSugar.isNotNull(contract))
        {
            contractDescription = contract.description();
        }
        
        throwContractViolationException(label, contractDescription, description);
    }

    private static void throwContractViolationException(
            final String label, 
            final Object violatingObject,
            final Matcher<?> matcher, 
            final Object... additionalValues) throws Error
    {
        final Description description = new StringDescription(); 
        description.appendText("\nExpected: ");
        
        if (SyntacticSugar.isNull(matcher)) {
            description.appendText("<null>");
        } else {  
            matcher.describeTo(description);
        }
        description.appendText("\nGot: ");
        description.appendText(String.valueOf(violatingObject));
        throwContractViolationException(label, description.toString(), additionalValues);
    }

    // Used by contracts without conditions.
    private static void throwContractViolationException(
            final String contractLabel, 
            final String contract, 
            final Object... description) throws Error
    {
        final Error error = new ContractError(createInfo(contractLabel, contract, description));
        
        if (hasUsefulStackTrace(error))
        {
            error.setStackTrace( getTrimmedCallstack(error) );
        }
        
        throw error;
    }
    
    private static boolean hasUsefulStackTrace(final Error error)
    {
        final boolean hasStackTraceElements = SyntacticSugar.isNotNull(error) && (error.getStackTrace().length > 0);
        
        boolean hasNoNullElements = false;
        if (hasStackTraceElements)
        {
            hasNoNullElements = hasNoNullElements(error);
        }       
        
        return  hasStackTraceElements && hasNoNullElements;
    }

    private static boolean hasNoNullElements(final Error error)
    {
        boolean noNullElements = false;
        for (final StackTraceElement element : error.getStackTrace())
        {
            noNullElements = SyntacticSugar.isNotNull(element);
            if (! noNullElements)
            {
                break;
            }
        }

        return noNullElements;
    }

    private static StackTraceElement[] getTrimmedCallstack(final Error error)
    {
        final StackTraceElement[] originalStackTrace = error.getStackTrace();
        final int firstNonContractStackTracePosition = getFirstNonContractStackTracePosition(originalStackTrace);

        final int newLength = (originalStackTrace.length - firstNonContractStackTracePosition);
        final StackTraceElement[] filteredStackTrace = new StackTraceElement[newLength];

        // Transfer all non-contract related stack trace elements.
        for (int i = 0; i < newLength; ++i)
        {
            filteredStackTrace[i] = originalStackTrace[i + firstNonContractStackTracePosition];
        }
        
        return filteredStackTrace;
    }
       
    private static int getFirstNonContractStackTracePosition(
            final StackTraceElement[] stackTrace)
    {
        int elementNo = stackTrace.length - 1;
        while (elementNo >= 0)
        {
            if (foundContractCallStackLocation(stackTrace, elementNo))
            {
                // Ok, the previous is the stack element we want.
                ++elementNo;
                break;
            }
            
            // Ok, no luck, try the next stack element.
            --elementNo;
        }
                
        return elementNo;
    }

    private static boolean foundContractCallStackLocation(
            final StackTraceElement[] stackTrace, 
            final int elementNo)
    {
        final String contractClassName = Contract.class.getCanonicalName();
        final String stackElementClassName = stackTrace[elementNo].getClassName();
        return stackElementClassName.contentEquals(contractClassName);
    }
    
    private static String getThreadInfo()
    {
        return " Thread id: " + Thread.currentThread().getId() + "." +
               " Thread name: " + Thread.currentThread().getName() + ".";
    }
    
    private static String getSystemInfo()
    {
        final String osNameKey = "os.name";
        final String systemLabel = " System: "; 
        String systemInfo;

        try
        {
            systemInfo = systemLabel + System.getProperties().getProperty(osNameKey) + ".";
        }
        catch (final SecurityException se)
        {
            systemInfo = systemLabel + "<Security manager does not allow access to property keys!>";    
        }
        
        return systemInfo;
    }
    
    private static String descriptionInfo(
            final String contract, 
            final Object... description)
    {
        final String descriptionHeader = "\nDescription: ";   
        return  descriptionHeader + contract + SyntacticSugar.concatenate(description);
    }
    
    private static String violationInfo(final String contractLabel)
    {
        final String violationHeader = "\n\nViolation:   ";
        return violationHeader + contractLabel;
    }

    private static String callStackInfo()
    {
        final String callStackHeader = "\nCall stack: ";        
        return callStackHeader + "\n"; 
    }
    
    private static String generalInfo()
    {
        final String infoHeader = "\nInformation:";
        return infoHeader + getThreadInfo() + getSystemInfo();
    }  

    private static String createInfo(
            final String contractLabel, 
            final String contract, 
            final Object... description)
    {
        return violationInfo(contractLabel) + 
               descriptionInfo(contract, description) + 
               generalInfo() +
               callStackInfo();
    }
    

    // *************************************************************************
    // *************************************************************************
    // Private fields.
    // *************************************************************************
    // *************************************************************************         
    
    // Texts to be logged.
    private static final String CONTRACT_VIOLATION      = " contract violation. ";
    private static final String REQUIRE_LABEL           = "REQUIRE" + CONTRACT_VIOLATION;
    private static final String ENSURE_LABEL            = "ENSURE" + CONTRACT_VIOLATION;
    private static final String INVARIANT_LABEL         = "INVARIANT" + CONTRACT_VIOLATION;
    private static final String NEVERGETHERE_LABEL      = "NEVER GET HERE" + CONTRACT_VIOLATION;
    private static final String CHECK_LABEL             = "CHECK failed. ";
    private static final String UNDERCONSTRUCTION_LABEL = "UNDER CONSTRUCTION" + CONTRACT_VIOLATION;
    
    private static final boolean SUCCESS = true; 
    private static final boolean FAILURE = false;
    private static final String NO_DESCRIPTION = "";
    private static final IContractCondition SUCCESS_CONDITION = createContractCondition(SUCCESS, NO_DESCRIPTION);   
}

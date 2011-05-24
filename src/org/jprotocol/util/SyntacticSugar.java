/*
 * SyntacticSugar
 *
 * St. Jude Medical CRMD Proprietary
 * Copyright, Pacesetter, Inc., 2009.
 * Unpublished work. All rights reserved.
 */
package org.jprotocol.util;

import java.util.*;

/**
 * SyntacticSugar - a pragmatic way to improve readability. 
 * (See Matchers from the hamcrest library for more options.)
 * 
 */
public class SyntacticSugar {

    // **********************
    // *** OPERATOR LOGIC ***
    // **********************
    
    public static boolean not(final boolean condition) {
        return (! condition);
    }

    // ************
    // *** NULL ***
    // ************

    public static <T> boolean isNull(final T ref) {
        return (null == ref);
    }
    
    public static <T> boolean isNotNull(final T ref) {
        return (null != ref);
    }
    
    public static <T> boolean has(final T ref) {
        return (null != ref);
    }
    
    public static <T> boolean hasNot(final T ref) {
        return (null == ref);
    }
    
    // *******************
    // *** ARITHMETICS ***
    // *******************
    
    /**
     * When there is a need to test for whether a a value is non-negative or not.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( isNonNegative(myValue), "Expected myValue to be non-negative, not: ", myValue);
     * </pre>
     */
    public static boolean isNonNegative(final double value) {
        return (0.0 <= value);
    }
    
    public static boolean isNonNegative(final int value) {
        return (0 <= value);
    }
    
    
    /**
     * When there is a need to test for whether a value is in a range 
     * (<code>int</code>) or not.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( isInRange(myIntValue, 0, 100) );
     * </pre>
     */
    public static boolean isInRange(final int value, final int rangeMin, final int rangeMax) {
        final boolean notBellow = (value >= rangeMin);
        final boolean notAbove  = (value <= rangeMax);
        return notBellow && notAbove;
    }
  
    /**
     * When there is a need to test for whether a value is in a range 
     * (<code>double</code>) or not.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( isInRange(myIntValue, 0, 100) );
     * </pre>
    */
    public static boolean isInRange(final double value, final double rangeMin, final double rangeMax) {
        final boolean notBellow = (value >= rangeMin);
        final boolean notAbove  = (value <= rangeMax);
        return notBellow && notAbove;
    }
    
    
    // *************
    // *** EMPTY ***
    // *************
    
    /**
     * When there is a need to test a "farmer-array" for non-emptyness...
     */
    public static <T> boolean isNotEmpty(final T[] objArr) {
        boolean isNotEmpty = false;
        
        if (isNotNull(objArr) && (objArr.length > 0)) {
            isNotEmpty = true;
        }
        
        return isNotEmpty;
    }
    
    /**
     * When there is a need to test a "farmer-array" of doubles for non-emptyness...
     */
    public static boolean isNotEmpty(final double[] doubleArr) {
        boolean isNotEmpty = false;
        
        if (isNotNull(doubleArr) && (doubleArr.length > 0)) {
            isNotEmpty = true;
        }
        
        return isNotEmpty;
    }
    
    /**
     * When there is a need to test a collection for non-emptyness...
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        boolean isNotEmpty = false;
        
        if (isNotNull(collection) && !collection.isEmpty()) {
            isNotEmpty = true;
        }
        
        return isNotEmpty;
    }
    
    
    /**
     * When there is a need to test a string for non-emptyness. Null is also 
     */
    public static boolean isNotEmpty(final String s) {
        boolean isNotEmpty = false;
        
        if (isNotNull(s) && !s.isEmpty()) {
            isNotEmpty = true;
        }
        
        return isNotEmpty;
    }
    
    /**
     * Helper factory method to be used with e.g. <code>forAll(...)</code> in case you want to 
     * make sure a collection does contain only non-empty strings.
     * <p>
     * <b>Example:</b> 
     * <pre>
     *    ensure( forAll(myErrorLogCollection), <b>isNonEmptyString()</b> );
     * </pre>
     */    
    public static Comparable<String> isNonEmptyString()
    {
        final Comparable<String> condition = new Comparable<String>()
        {
            // Define the condition.
            public int compareTo(final String str)
            {
                boolean conditionResult = false;
                if (isNotNull(str))
                {
                    conditionResult = ! str.isEmpty();
                }
                
                return analyzeConditionResult(conditionResult);
            }
        };
        
        return condition;        
    }
    
    /**
     * Helper factory method to be used with <code>forAll(...)</code> in case you want to make
     * sure a collection does contain only empty strings.
     * <p> 
     * (Utilizes the fact that java.lang.String implements the Comparable ifc.)
     * <p>
     * <b>Example:</b> 
     * <pre>
     *    ensure( forAll(myClearedErrorLogCollection), <b>isEmptyString()</b> );
     * </pre>
     */    
    public static Comparable<String> isEmptyString() {
        final String conditionEmptyString = "";
        return conditionEmptyString;        
    }

    
    
    // *************
    // *** OTHER ***
    // *************
    
    /**
     * When there is a need to verify a condition only if another conditions is true.
     * <p>
     * <b>Example:</b> 
     * <pre>
     * require( implies(givenIsInitiated(), thenVerifyStatus()) );
     * </pre>
     */
    public static boolean implies(
            final boolean impliesCondition, 
            final boolean condition) {
        final boolean doNotEvaluateCondition = true;
        return impliesCondition ? condition : doNotEvaluateCondition;
    }
    
    
    /**
     * When there is a need to verify a given condition for all elements in a 
     * collection.
     * <p>
     * <b>Example:</b> 
     * <pre>
     *    if ( isForAll(myErrorLogCollection), isNotNull() ) ...;
     * </pre>
     * <p>
     */
    public static boolean isForAll(final Collection<?> collection, final Comparable<?> condition)
    {
        final boolean resultForAllElements;

        // A null condition and/or collection is considered as a false condition.
        if (gotNullParameters(collection, condition))
        {
            resultForAllElements = false;
        } 
        else
        {
            // An empty collection is considered to be satisfied with any condition.
            resultForAllElements = verifyConditionOnAllCollectionElements(collection, condition);
        }
        
        return resultForAllElements;
    }
    
    /**
     * Helper method that converts a boolean value to an int compatible with the
     * Comparable ifc.
     */
    public static int analyzeConditionResult(final boolean conditionResult) {
        return conditionResult ? CONDITION_IS_OK : CONDITION_IS_NOT_OK;
    }
    
    /**
    * Utility method for concatenation String ellipsis arguments.
    */
    public static String concatenate(final Object... descriptions) {
        // No descriptions (or if somebody explicitly sends in "null"...) 
        if (isNull(descriptions) || descriptions.length < 1) {
            return NO_DESCRIPTION;
        }
        // Only one description, no need to concatenate.
        else if ((descriptions.length == 1) && (isNotNull(descriptions[0]))) {
            return descriptions[0].toString();
        }
        // Need to concatenate multiple descriptions...
        else {
            final StringBuffer sb = new StringBuffer();
            final String space = " ";
            for (final Object o : descriptions) {
                sb.append(o);
                sb.append(space);
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Helper factory method to be used with e.g. <code>forAll(...)</code> in case you want to make
     * sure a collection does not contain null elements.
     * <p>
     * <b>Example:</b> 
     * <pre>
     *    if ( isForAll(myErrorLogCollection), <b>isNotNull()</b> ) ...;
     * </pre>
     */    
	@SuppressWarnings("rawtypes")
	public static Comparable isNotNull() {
        final Comparable condition = new Comparable() {
            public int compareTo(final Object element) {
                // Define condition to be true for all elements.            
                return analyzeConditionResult( isNotNull(element) );
            }
        };
        
        return condition;
    }    
    

    /**
     * Helper factory method to be used with <code>forAll(...)</code> in case you want to 
     * make sure a collection only contains strings, where all strings includes 
     * a specific substring.
     * <p>
     * <b>Example:</b> 
     * <pre>
     *    ensure( forAll(myErrorLogCollection), <b>containsSubString("Success")</b> );
     * </pre>
      */
	@SuppressWarnings("rawtypes")
	public static Comparable containsSubString(final String substr) {
        final Comparable<String> condition = new Comparable<String>() {
            // Define condition to be true for all elements.            
            public int compareTo(final String str) {
                return analyzeConditionResult(str.contains(substr));
            }
        };
        
        return condition;
    }
        
    public static void givenNothing() {
        nothing();
    }

    public static void expectNothing() {
        doNothing();
    }
    
    public static void nothing() {
        // This method is of course empty (on behalf of Guran)
    }
    
    public static void doNothing() {
        // This method is of course empty
    }
    
    public static void doNothing(final String reason) {
        doNothing();
    }
    
    public static boolean isEqual(final int compareToValue) {
        return 0 == compareToValue;
    }

    public static boolean isNotEqual(final int compareToValue) {
        return 0 != compareToValue;
    }

    public static boolean isEqualOrLess(final int compareToValue) {
        return 0 >= compareToValue;
    }

    public static boolean isEqualOrMore(final int compareToValue) {
        return 0 <= compareToValue;
    }

    public static boolean isMore(final int compareToValue) {
        return 0 < compareToValue;
    }

    public static boolean isLess(final int compareToValue) {
        return 0 > compareToValue;
    }

	@SuppressWarnings("unchecked")
	public static boolean verifyCondition(
            @SuppressWarnings("rawtypes") final Comparable condition,
            final Object element)
    {
        return isNotEqual(condition.compareTo(element));
    }
    
    public static boolean isEmpty(final String string)
    {
        return string == null || string.trim().isEmpty();
    }
    
    // *******************************************************************************
    // *** Private *******************************************************************
    // *******************************************************************************
    
    
    private static boolean gotNullParameters(
            final Collection<?> collection, 
            final Comparable<?> condition) {
        return isNull(condition) || isNull(collection);
    }
    
    //TODO: Update "condition" to use generics... 
	private static boolean verifyConditionOnAllCollectionElements(
            final Collection<?> collection, 
            @SuppressWarnings("rawtypes") final Comparable condition) {
        boolean result = true;
        for (final Object element : collection) {
            if (verifyCondition(condition, element)) {
                result = false;
                break;
            }
        }
        
        return result;
    }
    
    
    
    private static final String NO_DESCRIPTION = "";
    private static final int CONDITION_IS_OK = 0;
    private static final int CONDITION_IS_NOT_OK = -1;     
}

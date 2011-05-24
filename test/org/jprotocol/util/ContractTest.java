package org.jprotocol.util;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.ensure;
import static org.jprotocol.util.Contract.equalsFalse;
import static org.jprotocol.util.Contract.equalsNull;
import static org.jprotocol.util.Contract.equalsTrue;
import static org.jprotocol.util.Contract.forAll;
import static org.jprotocol.util.Contract.implies;
import static org.jprotocol.util.Contract.inRange;
import static org.jprotocol.util.Contract.invariant;
import static org.jprotocol.util.Contract.neverGetHere;
import static org.jprotocol.util.Contract.nonNegative;
import static org.jprotocol.util.Contract.notEmpty;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;
import static org.jprotocol.util.Contract.underConstruction;
import static org.jprotocol.util.SyntacticSugar.containsSubString;
import static org.jprotocol.util.SyntacticSugar.isEmptyString;
import static org.jprotocol.util.SyntacticSugar.isForAll;
import static org.jprotocol.util.SyntacticSugar.isNonEmptyString;
import static org.jprotocol.util.SyntacticSugar.isNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.hamcrest.Matcher;

import org.jprotocol.util.SyntacticSugar;
import org.jprotocol.util.Contract.ContractError;


public class ContractTest extends TestCase 
{
    // *********************************************************************
    // Note! This is the only place where actual contracts are recommended 
    // to be tested.
    // *********************************************************************
    
	public void testNeverGetHere()
	{
        // No argument
        try
        {
            neverGetHere();
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior.
        }
        
        // Null argument
        try
        {
            final String nullStr = null;
            neverGetHere(nullStr);
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior.
        }
    }

    public void testNeverGetHereWithOneArgument()
    {
		try
		{
			neverGetHere("Goes without saying!");
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
    }

    public void testNeverGetHereWithMultipleArguments()
    {
        try
        {
            neverGetHere("Goes", "without", "saying!");
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior.
        }
    }
    
    public void testNeverGetHereWithNullArrayArgument()
    {
        // One very special (and evil) type of reference...
        try
        {
            final Object[] objarr = null;
            neverGetHere(objarr);
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior.
        }
	}
	
	public void testUnderConstruction()
	{
		// One string argument
		try
		{
			// if "happyFlow"
			//    handleHappyFlow
			// else if "alternativFlow1"
			//    handleAlternativeFlow1"
			// else if "alternativFlow2"
   			      underConstruction("graaec01: To be handled before release of Unity 1.1.1.");
			// else if "exceptionalFlow1"
			//    handleExceptionalFlow1"			
			// ...
   			      
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}		
	}
	
    public void testRequireSuccessForPlainBoolean()
    {
        require(true);
        require(true, "impossible to violate.");
        require(true, "impossible", "to", "violate.");
    }
    
    public void testRequireSuccessForContractCondition()
    {
        require(equalsTrue(true));
        require(equalsTrue(true), "impossible to violate");
        require(equalsTrue(true), "impossible", "to", "violate");
    }

    @SuppressWarnings("unchecked")
    public void testRequireSuccessForMatchers()
    {
        final int value = 2;
        require(value, is(not(nullValue())));
        require(value, is(greaterThan(1)));
        require(value, is(allOf(greaterThan(1), lessThan(3))));
    }

    @SuppressWarnings("unchecked")
    public void testRequireFailureForMatchers()
    {
        try
        {
            require(5, is(allOf(greaterThan(1), lessThan(3))));
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior
        }
    }

    public void testRequireFailureForPlainBoolean()
	{
		// No string argument
		try
		{
			require(false);
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}

		// One string argument
		try
		{
			require(false, "Arg 1");
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
		
		// Many string arguments...
		try
		{
			final int a = 1;
			final double b = 2.2;
			final byte z = 126;
			require(equalsTrue(false), "Arg 1", b, "Arg 2", z, "Arg 3", a);
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
	}

	public void testEnsureFailure()
	{
		// No string argument
		try
		{
			ensure(false);
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
		
		// One string argument
		try
		{
			ensure(false, "Arg 1");
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}

		// Many string arguments...
		try
		{
			ensure(equalsTrue(false), "Arg 1", "Arg 2", "Arg 3", "Arg 4");
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
		
        // Corner case 1
        try
        {        
            final String str = null;
            ensure(null,str,str,str);
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior.
        }		

        // Corner case 2
        try
        {        
            final Matcher<?> mather = null;
            final Object[] arargs = null;
            ensure(null,mather,arargs);
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior.
        }       

        // Corner case 3
        try
        {        
            ensure(null, "Arg 1", "Arg 2", "Arg 3", "Arg 4");
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior.
        }       

//        // Corner case 4 - can't even get this to work.
//        try
//        {            
//            final Boolean heavyMisuse = null;
//            ensure(heavyMisuse, "Arg 1", "Arg 2", "Arg 3", "Arg 4");
//            fail();
//        }
//        catch (final ContractError expected)
//        {
//            // Expected behavior.
//        }       
	}
    
	public void testEnsureSuccessForPlainBoolean()
	{
	    ensure(true);
	    ensure(true, "impossible to violate.");
	    ensure(true, "impossible", "to", "violate.");
    }
    
    public void testEnsureSuccessForMatchers()
    {
        ensure(equalsTrue(true));
	}
    
    public void testEnsureFailureForMatchers()
    {
        try
        {
            ensure(5, is(nullValue()));
            fail();
        }
        catch (ContractError expected)
        {
            // Expected behavior
        }
    }
	
	public void testInvariantFailure()
	{
		// No string argument.
		try
		{
			invariant(false);
			fail();
		}
		catch (final ContractError expected)
		{
		    // Expected behavior.
		}
		
		// One string argument.
		try
		{
			invariant(false, "Arg 1");
			fail();
		}
		catch (final ContractError expected)
		{
		    // Expected behavior.
		}
		
		// Many string arguments.
		try
		{
			invariant(equalsTrue(false), "Arg 1", "Arg 2", "Arg 3", "Arg 4", "Arg 5");
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
	}

	public void testInvariantSuccessForPlainBoolean()
	{
	    invariant(true);
	    invariant(true, "impossible to violate.");
	    invariant(true, "impossible", "to", "violate.");
    }
    
    public void testInvariantSuccessForContractCondition()
    {
        invariant(equalsTrue(true));
	}
	
    public void testInvariantSuccessForMatchers()
    {
        invariant(5, is(not(nullValue())));
    }
    
    public void testInvariantFailureForMatchers()
    {
        try
        {
            invariant(5, is(nullValue()));
            fail();
        }
        catch (ContractError expected)
        {
            // Expected behavior
        }
    }
    
    public void testCheckFailure()
	{
		// No string argument.
		try
		{
			check(false);
			fail();
		}
		catch (final ContractError expected)
		{
		    // Expected behavior.
		}

		// One string argument.
		try
		{
			check(false, "Arg 1");
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
		
		// Many string argument.
		try
		{
			check(equalsTrue(false), "Testing ellipsis 1.", "Testing ellipsis 2.");
			fail();
		}
		catch (final ContractError expected)
		{
            // Expected behavior.
		}
	}
			
	public void testCheckSuccessForPlainBoolean()
	{
	    check(true);
	    check(true, "impossible to violate.");
	    check(true, "impossible", "to", "violate.");
    }
    
    public void testCheckSuccessForContractConditions()
    {
        check(equalsTrue(true));
    }
    
    public void testCheckSuccessForMatchers()
    {
        check(5, is(notNullValue()));
    }
	
    public void testCheckFailureForMatchers()
    {
        try
        {
            check(0, not(lessThan(4)));
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behavior
        }
    }
    
    public void testEqualsTrueAndFalse()
    {
        // Can't get this to work.
        // final Boolean b = null;
        // equalsTrue(b);
    }
    
	public void testImplies()
	{
        boolean given = true;
        boolean then = true;
		assertTrue("Implies failed.", implies(given, then));
        
        given = false;
        then = false;
		assertTrue("Implies failed.", implies(given, then));

        given = false;
        then = true;
        assertTrue("Implies failed.", implies(given, then));
        
        given = true;
        then = false;
        assertFalse("Implies failed.", implies(given, then));
	}
    
	public void testIsNullAndIsNotNull()
	{
		final Object ref1 = null;
        assertTrue( equalsNull(ref1).condition() );
        assertTrue( equalsNull(null).condition() );

        assertFalse( isNotNull(ref1) );
        assertFalse( notNull(ref1).condition() );
        assertFalse( notNull(null).condition() );
	
		final String ref2 = new String();
        assertFalse( equalsNull(ref2).condition() );

        assertTrue( isNotNull(ref2) );
        assertTrue( notNull(ref2).condition() );    
    }
        
    public void testIsNonNegative()
    {
        int value = 0;
        assertTrue(nonNegative(value).condition());
        
        value = -1;
        assertFalse(nonNegative(value).condition());

        value = 1;
        assertTrue(nonNegative(value).condition());
        
        double doubleValue = 1.1;
        assertTrue(nonNegative(doubleValue).condition());
        
        doubleValue = -1.1;
        assertFalse(nonNegative(doubleValue).condition());
    
        doubleValue = -0.00001;
        assertFalse(nonNegative(doubleValue).condition());

        doubleValue = 0.1;
        assertTrue(nonNegative(doubleValue).condition());

        doubleValue = 0.0;
        assertTrue(nonNegative(doubleValue).condition());
    }
    
    public void testIsInRange()
    {
        final int rangeMin = -2;
        final int rangeMax = 5;
        
        int value = -2;
        assertTrue(inRange(value, rangeMin, rangeMax).condition());

        value = -3;
        assertFalse(inRange(value, rangeMin, rangeMax).condition());

        value = 5;
        assertTrue(inRange(value, rangeMin, rangeMax).condition());

        value = 22;
        assertFalse(inRange(value, rangeMin, rangeMax).condition());

        value = 0;
        assertTrue(inRange(value, rangeMin, rangeMax).condition());
        
        
        final double doubleRangeMin = -2.9;
        final double doubleRangeMax = 5.3;
        
        double doubleValue = -2.2;
        assertTrue(inRange(doubleValue, doubleRangeMin, doubleRangeMax).condition());

        doubleValue = -3.1;
        assertFalse(inRange(doubleValue, doubleRangeMin, doubleRangeMax).condition());

        doubleValue = 5.3;
        assertTrue(inRange(doubleValue, doubleRangeMin, doubleRangeMax).condition());

        doubleValue = 22.4;
        assertFalse(inRange(doubleValue, doubleRangeMin, doubleRangeMax).condition());

        doubleValue = 0.0;
        assertTrue(inRange(doubleValue, doubleRangeMin, doubleRangeMax).condition());
    }
    
    public void testIsNotEmpty()
    {
        // String
        String nonEmptyString = null;
        assertFalse(notEmpty(nonEmptyString).condition());
        assertFalse(notEmpty((String)null).condition());        
        
        nonEmptyString = "DbC";
        assertTrue(notEmpty(nonEmptyString).condition());

        final String emptyString = "";
        assertFalse(notEmpty(emptyString).condition());
        
        // Collection
        final Collection<?> colx = null;
        assertFalse(notEmpty(colx).condition());

        Collection<Integer> col = null;
        assertFalse(notEmpty(col).condition());
               
        col = new ArrayList<Integer>();
        assertFalse(notEmpty(col).condition());
        
        col.add(new Integer(1));
        assertTrue(notEmpty(col).condition());
          
        // Farmers-array aka RedNeck-array (please consider using a collection instead...)
        final Object[] objArr = {new Object(), new Object()}; 
        assertTrue(notEmpty(objArr).condition());

        final Object[] objArr2 = {}; 
        assertFalse(notEmpty(objArr2).condition());

        final Object[] objArr3 = null; 
        assertFalse(notEmpty(objArr3).condition());
       
        final Integer[] objArr4 = {};         
        assertFalse(notEmpty(objArr4).condition());
        
        // Array of primtives - double
        final double[] doubleArr = {1.2, -0.000001, 65476544.1}; 
        assertTrue(notEmpty(doubleArr).condition());        
    }    

    
    public void testForAllCornerCases()
    {       
        // A null collection is considered to be a failure.
        final Collection<?> nullCollection = null;
        assertFalse( isForAll(nullCollection, isNonEmptyString()) );        
        assertFalse( forAll(nullCollection, isNonEmptyString()).condition() );        
     
        // A null-condition is considered to be impossible to hold for any element.
        final Comparable<?> nullCondition = null;
        final Collection<String> nonNullCollection = new ArrayList<String>();
        nonNullCollection.add("DbC");
        assertFalse( isForAll(nonNullCollection, nullCondition) );        
        assertFalse( forAll(nonNullCollection, nullCondition).condition() ) ;        
        
        // Two failures does not cancel each other out...
        assertFalse( isForAll(nullCollection, nullCondition) );                
        assertFalse( forAll(nullCollection, nullCondition).condition() );                
    }
    
    public void testForAllSubString()
    {
        final String DbC = "DbC";
        final Collection<String> collection = new ArrayList<String>();
        collection.add("AAA " + DbC + " BBBB CCCC.");
        collection.add("GGG TTT"+ DbC);
        assertTrue( isForAll(collection, containsSubString(DbC)) );                
        assertTrue( forAll(collection, containsSubString(DbC)).condition() );                

        collection.add("Contract Programming is Design By Contract.");
        assertFalse( isForAll(collection, containsSubString(DbC)) );                
        assertFalse( forAll(collection, containsSubString(DbC)).condition() );                
    }

    public void testForAllWithYourOwnSpecificConditions()
    {
        final Collection<String> collection = new ArrayList<String>();
        collection.add("PASSED");
        collection.add("PASSED");
        assertTrue( isForAll(collection, isEqualToPASSED()) );                
        assertTrue( forAll(collection, isEqualToPASSED()).condition() ); 
        
        assertTrue( isForAll(collection, hasNoERROR()) );
        assertTrue( forAll(collection, hasNoERROR()).condition() );

        collection.add("ERROR #37");
        assertFalse( isForAll(collection, isEqualToPASSED()) );                
        assertFalse( forAll(collection, isEqualToPASSED()).condition() ); 
        
        assertFalse( isForAll(collection, hasNoERROR()) );
        assertFalse( forAll(collection, hasNoERROR()).condition() );
    }
    
    public void testForAllIsNotNullAndIsEmptyString() {
        final Collection<String> collection = new ArrayList<String>();
        collection.add("");
        collection.add("");  
        assertTrue( forAll(collection, isEmptyString()).condition() );
        
        collection.add("1");     
        assertFalse( forAll(collection, isEmptyString()).condition() );                    
        assertTrue( forAll(collection, isNotNull()).condition() );                
        
        collection.add(null);           
        assertFalse( forAll(collection, isNotNull()).condition() );              
        
        final Collection<String> collection2 = new ArrayList<String>();
        collection2.add("Bertrand");
        collection2.add("Meyer");
        assertTrue( forAll(collection2, isNonEmptyString()).condition() );
        
        collection2.add("");
        assertFalse( forAll(collection2, isNonEmptyString()).condition());
    }
    
    // Helper factory method for creating your own condition to be valid for all 
    // elements in a collection. All elements must be equal to "PASSED".
    private Comparable<String> isEqualToPASSED()
    {
        final String errorCondition = "PASSED";
        return errorCondition;
    }

    // Helper factory method for creating your own condition to be valid for all 
    // elements in a collection. No element may contain "ERROR".
    private Comparable<String> hasNoERROR()
    {
        final Comparable<String> condition = new Comparable<String>()
        {
            // Define condition to be true for all elements.            
            public int compareTo(final String str)
            {
                return SyntacticSugar.analyzeConditionResult(!str.contains("ERROR"));
            }
        };
        
        return condition;
    }

    public void testBooleanValuesToContractConditionMappers()
    {
        // Success scenarios
        require(equalsTrue(true));
        require(equalsFalse(false));

        // Failure scenarios
        try
        {
            require(equalsTrue(false));
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behaviour.
        }        
 
        try
        {
            require(equalsFalse(true));
            fail();
        }
        catch (final ContractError expected)
        {
            // Expected behaviour.
        }
    }
    
}

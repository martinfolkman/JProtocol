package org.jprotocol.util;

import static org.jprotocol.util.SyntacticSugar.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

public class SyntacticSugarTest 
{
    @Test
    public void nullSugar() {
        final Object existingObject = new Object();
        final Object nonexistingObject = null;
        
        assertTrue(has(existingObject));
        assertFalse(has(nonexistingObject));
        
        assertTrue(hasNot(nonexistingObject));        
        assertFalse(hasNot(existingObject));

        assertFalse(isNull(existingObject));
        assertTrue(isNull(nonexistingObject));
        
        assertTrue(isNotNull(existingObject));
        assertFalse(isNotNull(nonexistingObject));
    }
    
    @Test
    public void operatorSugar() {
        final boolean happy = true;
        final boolean sad = false;
        
        assertTrue(not(sad));
        assertFalse(not(happy));
    }
    
    
    @Test
    public void shouldDoNothingWhenExecuted() {       
        // Given
        nothing();
        
        // Then expect
        nothing();
        
        // When
        nothing();
    }

    @Test
    public void emptySugar() {
        // Farmers-array aka RedNeck-array (please consider using a collection instead...)
        final Object[] objArr = {new Object(), new Object()}; 
        assertTrue(isNotEmpty(objArr));

        final Object[] objArr2 = {}; 
        assertFalse(isNotEmpty(objArr2));

        final Object[] objArr3 = null; 
        assertFalse(isNotEmpty(objArr3));

        final Integer[] objArr4 = {}; 
        assertFalse(isNotEmpty(objArr4));        

        // Array of primtives - double
        final double[] doubleArr = {1.2, -0.000001, 65476544.1}; 
        assertTrue(isNotEmpty(doubleArr));
    }  
 
    public void testForAllIsNotNullAndIsEmptyString() {
        final Collection<String> collection = new ArrayList<String>();
        collection.add("");
        collection.add("");  
        assertTrue( isForAll(collection, isEmptyString()) );
        
        collection.add("1");
        assertFalse( isForAll(collection, isEmptyString()) );               
        assertTrue( isForAll(collection, isNotNull()) );                              
        
        collection.add(null);
        assertFalse( isForAll(collection, isNotNull()) );                           
        
        final Collection<String> collection2 = new ArrayList<String>();
        collection2.add("Bertrand");
        collection2.add("Meyer");
        assertTrue( isForAll(collection2, isNonEmptyString()) );
        
        collection2.add("");
        assertFalse( isForAll(collection2, isNonEmptyString()) );
    }
    

    @Test
    public void negativeSugar() {
        double doubleValue = 1.1;

        assertTrue(isNonNegative(doubleValue));

        doubleValue = -1.1;
        assertFalse(isNonNegative(doubleValue));

        doubleValue = -0.00001;
        assertFalse(isNonNegative(doubleValue));

        doubleValue = 0.1;
        assertTrue(isNonNegative(doubleValue));

        doubleValue = 0.0;
        assertTrue(isNonNegative(doubleValue));
    }    
    
    public void testIsNonNegative() {
        int value = 0;
        assertTrue(isNonNegative(value));
        
        value = -1;
        assertFalse(isNonNegative(value));

        value = 1;
        assertTrue(isNonNegative(value));
        
        double doubleValue = 1.1;
        assertTrue(isNonNegative(doubleValue));
        
        doubleValue = -1.1;
        assertFalse(isNonNegative(doubleValue));
    
        doubleValue = -0.00001;
        assertFalse(isNonNegative(doubleValue));

        doubleValue = 0.1;
        assertTrue(isNonNegative(doubleValue));

        doubleValue = 0.0;
        assertTrue(isNonNegative(doubleValue));
    }
    
    public void testIsInRange() {
        final int rangeMin = -2;
        final int rangeMax = 5;
        
        int value = -2;
        assertTrue(isInRange(value, rangeMin, rangeMax));

        value = -3;
        assertFalse(isInRange(value, rangeMin, rangeMax));

        value = 5;
        assertTrue(isInRange(value, rangeMin, rangeMax));

        value = 22;
        assertFalse(isInRange(value, rangeMin, rangeMax));

        value = 0;
        assertTrue(isInRange(value, rangeMin, rangeMax));
        
        
        final double doubleRangeMin = -2.9;
        final double doubleRangeMax = 5.3;
        
        double doubleValue = -2.2;
        assertTrue(isInRange(doubleValue, doubleRangeMin, doubleRangeMax));

        doubleValue = -3.1;
        assertFalse(isInRange(doubleValue, doubleRangeMin, doubleRangeMax));

        doubleValue = 5.3;
        assertTrue(isInRange(doubleValue, doubleRangeMin, doubleRangeMax));

        doubleValue = 22.4;
        assertFalse(isInRange(doubleValue, doubleRangeMin, doubleRangeMax));

        doubleValue = 0.0;
        assertTrue(isInRange(doubleValue, doubleRangeMin, doubleRangeMax));
    }
    
    public void testIsNotEmpty() {
        // String
        String nonEmptyString = null;
        assertFalse(isNotEmpty(nonEmptyString));
        assertFalse(isNotEmpty((String)null));
        
        nonEmptyString = "DbC";
        assertTrue(isNotEmpty(nonEmptyString));

        final String emptyString = "";
        assertFalse(isNotEmpty(emptyString));
        
        // Collection
        final Collection<?> colx = null;
        assertFalse(isNotEmpty(colx));

        Collection<Integer> col = null;
        assertFalse(isNotEmpty(col));
               
        col = new ArrayList<Integer>();
        assertFalse(isNotEmpty(col));
        
        col.add(new Integer(1));
        assertTrue(isNotEmpty(col));
          
        // Farmers-array aka RedNeck-array (please consider using a collection instead...)
        final Object[] objArr = {new Object(), new Object()}; 
        assertTrue(isNotEmpty(objArr));

        final Object[] objArr2 = {}; 
        assertFalse(isNotEmpty(objArr2));

        final Object[] objArr3 = null; 
        assertFalse(isNotEmpty(objArr3));
       
        final Integer[] objArr4 = {};         
        assertFalse(isNotEmpty(objArr4));
        
        // Array of primtives - double
        final double[] doubleArr = {1.2, -0.000001, 65476544.1}; 
        assertTrue(isNotEmpty(doubleArr));        
    }    

}
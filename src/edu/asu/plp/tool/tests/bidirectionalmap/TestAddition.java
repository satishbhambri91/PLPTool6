package edu.asu.plp.tool.tests.bidirectionalmap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import edu.asu.plp.tool.backend.BiDirectionalOneToManyMap;
import edu.asu.plp.tool.backend.OrderedBiDirectionalOneToManyHashMap;

public class TestAddition
{
	private BiDirectionalOneToManyMap<String, String> map;
	
	@Before
	public void setUp()
	{
		map = new OrderedBiDirectionalOneToManyHashMap<>();
	}
	
	@After
	public void tearDown()
	{
		map = null;
	}
	
	/*
	 * Put Tests
	 */
	@Test
	public void testNonNullKeyToNullValue()
	{
		
	}
	
	@Test
	public void testNullKeyToNonNullValue()
	{
		
	}
	
	@Test
	public void testInsertOnePair()
	{
		
	}
	
	@Test
	public void testInsertSingleKeyMultiValues()
	{
		
	}
	
	@Test
	public void testUniqueKeysSameValue()
	{
		
	}
	
	@Test
	public void testRetrieveSingleKeyMultiValues()
	{
		
	}
	
	/*
	 * Size Tests
	 */
	@Test
	public void testSingleKeyMultiValuesKeySize()
	{
		
	}
	
	@Test
	public void testSingleKeyMultiValuesValuesSize()
	{
		
	}
	
	@Test
	public void testSingleKeySingleValuesValuesSize()
	{
		
	}
	
	@Test
	public void testSingleKeyMultiValuesMapSize()
	{
		
	}
	
	@Test
	public void testSingleKeySingleValuesKeySize()
	{
		
	}
	
	/*
	 * Contains Tests
	 */
	
	/*
	 * Get Tests
	 */
	
	/*
	 * Key/Value Set Tests
	 */
	
}

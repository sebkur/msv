/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.tranquilo.datatype;

/**
 * "positiveInteger" type.
 * 
 * See http://www.w3.org/TR/xmlschema-2/#positiveInteger for the spec
 */
public class PositiveIntegerType extends IntegerType
{
	public static final PositiveIntegerType theInstance = new PositiveIntegerType();
	private PositiveIntegerType() { super("positiveInteger"); }
	
	public Object convertValue( String lexicalValue, ValidationContextProvider context )
	{
		Object o = super.convertToValue(lexicalValue,context);
		if(o==null)		return null;
		
		final IntegerValueType v = (IntegerValueType)o;
		if( !v.isPositive() )	return null;
		return v;
	}
}

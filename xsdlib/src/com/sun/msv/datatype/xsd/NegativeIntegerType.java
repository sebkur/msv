/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.msv.datatype.xsd;

import org.relaxng.datatype.ValidationContext;

/**
 * "negativeInteger" type.
 * 
 * type of the value object is {@link IntegerValueType}.
 * See http://www.w3.org/TR/xmlschema-2/#negativeInteger for the spec
 * 
 * v.isNegative is certainly faster than compareTo(ZERO).
 * This the sole reason why this class exists at all.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public class NegativeIntegerType extends IntegerType {
	public static final NegativeIntegerType theInstance = new NegativeIntegerType();
	private NegativeIntegerType() { super("negativeInteger"); }
	
	final public XSDatatype getBaseType() {
		return NonPositiveIntegerType.theInstance;
	}
	
	public Object convertToValue( String lexicalValue, ValidationContext context ) {
		Object o = super.convertToValue(lexicalValue,context);
		if(o==null)		return null;
		
		final IntegerValueType v = (IntegerValueType)o;
		if( !v.isNegative() )	return null;
		return v;
	}
}

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

import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;
import com.sun.msv.datatype.SerializationContext;

/**
 * "final" component.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public final class FinalComponent extends XSDatatypeImpl {
	/** immediate base type, which may be a concrete type or DataTypeWithFacet */
	public final XSDatatypeImpl baseType;
	final public XSDatatype getBaseType() { return baseType; }
	
	protected final int finalValue;
	
	public FinalComponent( XSDatatypeImpl baseType, int finalValue ) {
		this( baseType.getName(), baseType, finalValue );
	}
	
	public FinalComponent( String newTypeName, XSDatatypeImpl baseType, int finalValue ) {
		super( newTypeName, baseType.whiteSpace );
		this.baseType = baseType;
		this.finalValue = finalValue;
	}
	
	public boolean isFinal( int derivationType ) {
		if( (finalValue&derivationType) != 0 )	return true;
		return baseType.isFinal(derivationType);
	}
	
	public ConcreteType getConcreteType() {
		return baseType.getConcreteType();
	}
	
	public String displayName() {
		return baseType.displayName();
	}
	
	public int getVariety() {
		return baseType.getVariety();
	}
	
	public int isFacetApplicable( String facetName ) {
		return baseType.isFacetApplicable(facetName);
	}
	
	public boolean checkFormat( String content, ValidationContext context ) {
		return baseType.checkFormat(content,context);
	}
	
	public Object _createValue( String content, ValidationContext context ) {
		return baseType._createValue(content,context);
	}
	public Class getJavaObjectType() {
		return baseType.getJavaObjectType();
	}
	public Object _createJavaObject( String literal, ValidationContext context ) {
		return baseType._createJavaObject(literal,context);
	}
	public String serializeJavaObject( Object value, SerializationContext context ) {
		return baseType.serializeJavaObject(value,context);
	}
	
	public String convertToLexicalValue( Object value, SerializationContext context ) {
		return baseType.convertToLexicalValue(value,context);
	}
	
	public void _checkValid( String content, ValidationContext context ) throws DatatypeException {
		baseType._checkValid(content,context);
	}
	
}

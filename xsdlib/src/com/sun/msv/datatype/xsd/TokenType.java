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
 * "token" type.
 * 
 * See http://www.w3.org/TR/xmlschema-2/#token for the spec
 */
public class TokenType extends StringType
{
	public static final TokenType theInstance = new TokenType("token");
	protected TokenType( String typeName ) { super(typeName,WhiteSpaceProcessor.theCollapse); }
}

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

import com.sun.msv.datatype.SerializationContext;
import org.relaxng.datatype.ValidationContext;

/**
 * "hexBinary" type.
 * 
 * type of the value object is {@link BinaryValueType}.
 * See http://www.w3.org/TR/xmlschema-2/#hexBinary for the spec
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public class HexBinaryType extends BinaryBaseType {
	
	public static final HexBinaryType theInstance = new HexBinaryType();
	private HexBinaryType() { super("hexBinary"); }
	
	
// hex decoder
//====================================
	
	private static int hexToBin( char ch ) {
		if( '0'<=ch && ch<='9' )	return ch-'0';
		if( 'A'<=ch && ch<='F' )	return ch-'A'+10;
		if( 'a'<=ch && ch<='f' )	return ch-'a'+10;
		return -1;
	}

	public Object convertToValue( String lexicalValue, ValidationContext context ) {
		final int len = lexicalValue.length();

		// "111" is not a valid hex encoding.
		if( len%2 != 0 )	return null;

		byte[] out = new byte[len/2];

		for( int i=0; i<len; i+=2 ) {
			int h = hexToBin(lexicalValue.charAt(i  ));
			int l = hexToBin(lexicalValue.charAt(i+1));
			if( h==-1 || l==-1 )
				return null;	// illegal character

			out[i/2] = (byte)(h*16+l);
		}

		return new BinaryValueType(out);
	}

	protected boolean checkFormat( String lexicalValue, ValidationContext context ) {
		final int len = lexicalValue.length();

		// "111" is not a valid hex encoding.
		if( len%2 != 0 )	return false;

		for( int i=0; i<len; i++ )
			if( hexToBin(lexicalValue.charAt(i))==-1 )
				return false;

		return true;
	}

	public String serializeJavaObject( Object value, SerializationContext context ) {
		if(!(value instanceof byte[]))
			throw new IllegalArgumentException();

		byte[] data = (byte[])value;
		StringBuffer r = new StringBuffer(data.length*2);
		for( int i=0; i<data.length; i++ ) {
			r.append( encode(data[i]>>4) );
			r.append( encode(data[i]&0xF) );
		}
		return r.toString();
	}

	
	public String convertToLexicalValue( Object value, SerializationContext context ) {
		if(!(value instanceof BinaryValueType))
			throw new IllegalArgumentException();
		
		return serializeJavaObject( ((BinaryValueType)value).rawData, context );
	}
	
	public char encode( int ch ) {
		ch &= 0xF;
		if( ch<10 )		return (char)('0'+ch);
		else			return (char)('A'+(ch-10));
	}
}

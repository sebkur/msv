package com.sun.tranquilo.grammar.trex.typed;

import com.sun.tranquilo.grammar.Expression;
import com.sun.tranquilo.grammar.NameClass;
import com.sun.tranquilo.grammar.trex.ElementPattern;

/**
 * ElementPattern with type.
 * 
 * Proprietary extension by Tranquilo to support type-assignment in TREX.
 */
public class TypedElementPattern extends ElementPattern
{
	public final String label;
	
	public TypedElementPattern( NameClass nameClass, Expression contentModel, String label )
	{
		super(nameClass,contentModel);
		this.label = label;
	}
}
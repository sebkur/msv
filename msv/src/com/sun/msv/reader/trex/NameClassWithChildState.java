package com.sun.tranquilo.reader.trex;

import com.sun.tranquilo.grammar.NameClass;
import com.sun.tranquilo.grammar.AnyNameClass;
import com.sun.tranquilo.reader.State;
import com.sun.tranquilo.util.StartTagInfo;

public abstract class NameClassWithChildState extends NameClassState implements NameClassOwner
{
	/**
	 * name class object that is being created.
	 * See {@link castNameClass} and {@link annealNameClass} methods
	 * for how will a pattern be created.
	 */
	protected NameClass nameClass = null;

	/**
	 * receives a Pattern object that is contained in this element.
	 */
	public final void onEndChild( NameClass childNameClass )
	{
		nameClass = castNameClass( nameClass, childNameClass );
	}
	
	protected final NameClass makeNameClass()
	{
		if( nameClass==null )
		{
			reader.reportError( TREXGrammarReader.ERR_MISSING_CHILD_NAMECLASS );
			nameClass = AnyNameClass.theInstance;
			// recover by assuming some name class.
		}
		return annealNameClass(nameClass);
	}
	
	protected State createChildState( StartTagInfo tag )
	{
		return TREXGrammarReader.createNameClassChildState(tag);
	}

		
	/**
	 * combines half-made name class and newly found child name class into the name class.
	 * 
	 * <p>
	 * Say this container has three child name class n1,n2, and n3.
	 * Then, the name class of this container will be made by the following method
	 * invocations.
	 * 
	 * <pre>
	 *   annealNameClass( castNameClass( castNameClass( castNameClass(null,p1), p2), p3 ) )
	 * </pre>
	 */
	protected abstract NameClass castNameClass(
		NameClass halfCastedNameClass, NameClass newChildNameClass );
	
	/**
	 * performs final wrap-up and returns a fully created NameClass object
	 * that represents this element.
	 */
	protected NameClass annealNameClass( NameClass nameClass )
	{
		// default implementation does nothing.
		return nameClass;
	}
}
package com.sun.tranquilo.reader;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;
import com.sun.tranquilo.util.StartTagInfo;

/**
 * base interface of 'parsing state'.
 * 
 * parsing of XML representation of a grammar is done by
 * using various states.
 * 
 * <p>
 * Each State-derived class is responsible for a particular type of
 * declaration of the grammar. For example, SequenceState is responsible
 * for parsing &lt;sequence&gt; element of RELAX module.
 * 
 * <p>
 * State objects interact each other. There are two ways of interaction.
 * 
 * <ul>
 *  <li>from parent to child
 *  <li>from child to parent
 * </ul>
 * 
 * The first type of communication occurs only when a child state object is
 * created. The last type of communication occurs usually (but not limited to)
 * when a child state sees its end tag.
 * 
 * 
 * <p>
 * In this level of inheritance, contract is somewhat abstract.
 * 
 * <ol>
 *  <li>When a State object is created, its init method is called
 *		and various information is set. Particularly, start tag
 *		information (if any) and the parent state is set.
 *		This process should only be initiated by GrammarReader.
 * 
 *  <li>After that, startSelf method is called. Usually,
 *		this is the place to do something useful.
 * 
 *  <li>State object is registered as a ContentHandler, and
 *		therefore will receive SAX events from now on.
 * 
 *  <li>Derived classes are expected to do something useful
 *		by receiving SAX events.
 * 
 *  <li>When a State object finishes its own part, it should
 *		call GrammarReader.popState method. It will remove
 *		the current State object and registers the parent state 
 *		as a ContentHandler again.
 * </ol>
 * 
 * Of course some derived classes introduce more restricted
 * contract. See {@link SimpleState}.
 * 
 * <p>
 * this class also provides:
 * <ul>
 *  <li>access to the parent state
 *  <li>default implementations for all ContentHandler callbacks
 *		except startElement and endElement
 * </ul>
 */
public abstract class State implements ContentHandler
{
	/** parent state of this state.
	 * 
	 * In other words, the parent state is a state who is responsible
	 * for the parent element of the current element.
	 * 
	 * For states responsible for the document element, the parent state is
	 * a state who is responsible for the entire document.
	 * 
	 * For states responsible for the entire document, the parent state is
	 * always null.
	 */
	protected State parentState;
	
	/** reader object who is the owner of this object.
	 * 
	 * This information is avaiable after init method is called.
	 */
	protected GrammarReader reader;
				
	/** information of the start tag.
	 * 
	 * This information is avaiable after init method is called.
	 */
	protected StartTagInfo startTag;
	public StartTagInfo getStartTag() { return startTag; }
	
	/** Location of the start tag.
	 * 
	 * This information is avaiable after init method is called.
	 */
	protected Locator location;
	
	protected final void init( GrammarReader reader, State parentState, StartTagInfo startTag )
	{
		// use of the constructor, which is usually the preferable way,
		// is intentionally avoided for short hand.
		// if we use the constructor to do the initialization, every derived class
		// has to have a dumb constructor, which simply delegates all the parameter
		// to the super class.
		
		this.reader = reader;
		this.parentState = parentState;
		this.startTag = startTag;
		if( reader.locator!=null )	// locator could be null, in case of the root state.
		this.location = new LocatorImpl( reader.locator );
		startSelf();
	}
	
	/** performs a task that should be done before reading any child elements.
	 * 
	 * derived-class can safely read startTag and/or parentState values.
	 */
	protected void startSelf() {}
	

	public void characters(char[] buffer, int from, int len )
	{
		// both RELAX and TREX prohibits characters in their grammar.
		for( int i=from; i<len; i++ )
			switch(buffer[i])
			{
			case ' ': case '\t': case '\n': case '\r':
				break;
			default:
				reader.reportError( reader.ERR_CHARACTERS, new String(buffer,from,len).trim() );
				return;
			}
	}
		
	public void processingInstruction( String target, String data ) {}
	public void ignorableWhitespace(char[] buffer, int from, int len ) {}
	public void skippedEntity( String name ) {}
	public final void startDocument() {}
	
	public void setDocumentLocator( Locator loc )
	{
		reader.locator = loc;
	}
	
// namespace support
//----------------------------		
	public void startPrefixMapping(String prefix, String uri )
	{ reader.declareNamespacePrefix(prefix,uri); }
	public void endPrefixMapping(String prefix) {}
}
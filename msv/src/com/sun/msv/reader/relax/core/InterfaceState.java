/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.tranquilo.reader.relax.core;

import com.sun.tranquilo.util.StartTagInfo;
import com.sun.tranquilo.reader.State;
import com.sun.tranquilo.reader.SimpleState;
import com.sun.tranquilo.grammar.relax.RELAXModule;
import com.sun.tranquilo.grammar.Expression;
import com.sun.tranquilo.reader.ChildlessState;

/**
 * parses &lt;interface&gt; element and &lt;div&gt; in interface.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public class InterfaceState extends SimpleState
{
	protected State createChildState( StartTagInfo tag )
	{
		if(!tag.namespaceURI.equals(RELAXCoreReader.RELAXCoreNamespace))	return null;
		
		if(tag.localName.equals("div"))		return new InterfaceState();
		
		RELAXModule module = getReader().module;
		
		if(tag.localName.equals("export"))
		{
			final String label = tag.getAttribute("label");
			
			if(label!=null)
				module.elementRules.getOrCreate(label).exported = true;
			else
				reader.reportError(reader.ERR_MISSING_ATTRIBUTE,
								   "export", "label" );
				// recover by ignoring this export
			
			return new ChildlessState();
		}
		
		return null;
	}

	protected RELAXCoreReader getReader() { return (RELAXCoreReader)reader; }
}
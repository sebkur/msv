/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.msv.generator;

import junit.framework.TestSuite;
import java.util.StringTokenizer;

/**
 * batch test class that is called from ant as a part of the whole test.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public class GeneratorTest {
	public static TestSuite suite() {
		TestSuite s = new TestSuite();
		StringTokenizer tokens;
		
		// XSD
		tokens = new StringTokenizer( System.getProperty("XSDBatchTestDir"), ";" );
		while( tokens.hasMoreTokens() ) {
			GeneratorTester t = new GeneratorTester();
			t.init("xsd", tokens.nextToken(), ".xsd", BatchTester.genericLoader);
			s.addTest( t.suite() );
		}
		
		// RELAX
		tokens = new StringTokenizer( System.getProperty("RELAXBatchTestDir"), ";" );
		while( tokens.hasMoreTokens() ) {
			GeneratorTester t = new GeneratorTester();
			t.init("relax", tokens.nextToken(), ".rlx", BatchTester.genericLoader);
			s.addTest( t.suite() );
		}
		
		// TREX
		tokens = new StringTokenizer( System.getProperty("TREXBatchTestDir"), ";" );
		while( tokens.hasMoreTokens() ) {
			GeneratorTester t = new GeneratorTester();
			t.init("trex", tokens.nextToken(), ".trex", BatchTester.genericLoader);
			s.addTest( t.suite() );
		}
		
		// DTD
		tokens = new StringTokenizer( System.getProperty("DTDBatchTestDir"), ";" );
		while( tokens.hasMoreTokens() ) {
			GeneratorTester t = new GeneratorTester();
			t.init("dtd", tokens.nextToken(), ".dtd", BatchTester.dtdLoader );
			s.addTest( t.suite() );
		}
		
		return s;
	}
}

/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package batch;

import javax.xml.parsers.*;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.*;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.relaxng.testharness.model.*;
import junit.framework.*;
import com.sun.msv.verifier.*;
import com.sun.msv.reader.GrammarReaderController;
import com.sun.msv.grammar.Grammar;
import com.sun.msv.grammar.ExpressionPool;
import com.sun.resolver.tools.CatalogResolver;
import msv.*;
import batch.model.DirectoryTestReader;

/**
 * Test schemata/instances are expected to follow a naming convention.
 * 
 * <ol>
 *  <li>legal schema must have "*.rlx" or "*.trex"
 *  <li>invalid schema must have "*.e.rlx" or "*.e.trex"
 *  <li>valid test document must have "*.vNN.xml".
 *      these documents are validated against "*.rlx" or "*.trex".
 *  <li>invalid test document must have "*.nNN.xml".
 * </ol>
 * 
 * Files that follow this naming convention are all tested. If any unexpected
 * result is found, main method returns non-0 exit code.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public abstract class BatchTester {
	public SAXParserFactory factory =
		new org.apache.xerces.jaxp.SAXParserFactoryImpl();
//		new org.apache.crimson.jaxp.SAXParserFactoryImpl();

	public CatalogResolver resolver = new CatalogResolver();
	
	/** schema file extension ".rlx", ".trex", or ".dtd" */
	public String ext;
	
	public IValidatorEx validator;
	
	
	
	
	
	/**
	 * This method should print the usage information.
	 */
	protected abstract void usage();
	
	/**
	 * Creates JUnit test suite from the source test suite information.
	 */
	protected abstract TestSuite suite( RNGTestSuite src );
	
	/**
	 * Creates the test suite from the specified file/directory.
	 */
	public RNGTest parse( String target ) throws Exception {
		return parse(target,false);
	}
	
	/**
	 * Creates the test suite from the specified file/directory.
	 * 
	 * @param recursive
	 *		if the target is directory and this parameter is true, then
	 *		subdirectories are recursively parsed into a test suite.
	 */
	public RNGTest parse( String target, boolean recursive ) throws Exception {
		File src = new File(target);
		
		if(src.isDirectory())
			return DirectoryTestReader.parseDirectory(
				src, ext, recursive );
		else
			return DirectoryTestReader.parseSchema(src);
	}
	
	/**
	 * Initializes various fields by setting a target schema language.
	 */
	public void init( String target, boolean strict ) {
		
		if(strict)
			System.out.println("strict schema check will be done");
		
		if(strict && (
			target.equals("relax") ||
			target.equals("trex") ||
			target.equals("relax") ))
			System.out.println("*** strict option is not supported for the language "+target);
		
		if( target.equals("relax") )		_init( ".rlx", new GenericValidator() );
		else
		if( target.equals("trex") )		_init( ".trex", new GenericValidator() );
		else
		if( target.equals("rng") )		_init( ".rng", new IValidatorImplForRNG(strict) );
		else
		if( target.equals("xsd") )		_init( ".xsd", new IValidatorImplForXS(strict) );
		else
		if( target.equals("dtd") )		_init( ".dtd", new DTDValidator() );
		else
			throw new Error("unrecognized language type: "+target );
	}
		
	private void _init( String _ext, IValidatorEx _validator ) {
		this.ext = _ext;
		this.validator = _validator;
	}
	
	protected void onOption( String opt ) throws Exception {
		System.out.println("unrecognized option:"+opt);
		throw new Error();
	}
	
	public void run( String[] av ) throws Exception {
		
		String target = null;
		Vector instances = new Vector();
		boolean strict = false;
		boolean recursive = false;
		
		for( int i=0; i<av.length; i++ ) {
			if(av[i].charAt(0)=='-') {
				if(av[i].equals("-strict")) {
					strict = true;
					continue;
				}
				if(av[i].equals("-recursive")) {
					recursive = true;
					continue;
				}
				
				onOption(av[i]);
			} else {
				if(target==null)
					target = av[i];
				else
					instances.add(av[i]);
			}
		}

		if( instances.size()==0 ) {
			usage();
			return;
		}
		
		init(target,strict);
		
		// collect test cases
		RNGTestSuite s = new RNGTestSuite();
		for( int i=0; i<instances.size(); i++ )
			s.addTest(parse((String)instances.get(i),recursive));
		
		junit.textui.TestRunner.run( suite(s) );
	}

	/*
	public static void report( ValidityViolation vv ) {
		System.out.println(
			vv.getLineNumber()+":"+vv.getColumnNumber()+
			"  " + vv.getMessage());
	}
	*/
	
	
	/**
	 * Creates a JUnit test suite from a set of paths that are specified
	 * by a system property.
	 * 
	 * Mainly used to run an automated test from Ant.
	 */
	public TestSuite createFromProperty( String target, String propertyName ) throws Exception {
																								   
		init(target, System.getProperty("MSV_STRICT_CHECK")!=null);
		
		String property = System.getProperty(propertyName);
		if(property==null)
			return new TestSuite();	// return an empty test suite.
		
		StringTokenizer tokens = new StringTokenizer( property, ";" );

		// collect test cases
		RNGTestSuite s = new RNGTestSuite();
		while( tokens.hasMoreTokens() ) {
			String name = tokens.nextToken();
			if(name.charAt(name.length()-1)=='@')
				s.addTest(parse(name.substring(0,name.length()-1),true));
			else
				s.addTest(parse(name));
		}
		
		return suite(s);
	}
	
	/**
	 * Gets the name of the test case from a header, which can be possibly null.
	 */
	public String getName( RNGHeader header ) {
		String s=null;
		if(header!=null)	s = header.getName();
		if(s==null)			s = "";
		return s;
	}

}

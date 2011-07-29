package jsi3.lib.text;

import java.text.*;
import java.net.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import static javax.xml.xpath.XPathConstants.*;

import static jsi3.lib.console.Statics.*;


/**
 * This class is designed to be statically imported into your code.
 * I do try to do some internal checking on intermediate and returned values (and throw exceptions if appropriate).
 * But if you're a fucktard and pass nulls, negative indices etc then its your own fault if it explodes. Shit in, shit out.
 */
public class Statics
{
	static TransformerFactory tFactory;

	private static XPath xpath = XPathFactory.newInstance().newXPath();

	static String[] day_names;

	static String[] month_names;

	/** Holder of all illegal XML chars. **/

	private static byte[] ILLEGAL_XML_1_0_CHARS;


	private Statics(){};
	

	static
	{
		day_names = "Sunday Monday Tuesday Wednesday Thursday Friday Saturday".split( "\\s+" );

		month_names = "January Feburary March April May June July August September October November December".split( "\\s+" );

		final StringBuffer buff = new StringBuffer();

		for (char i = 0x0000; i < 0x0020; i++)
		{
			if (i != 0x0009 && i != 0x000A && i != 0x000D)
			{
				buff.append(i);
			}
		}

		ILLEGAL_XML_1_0_CHARS = buff.toString().getBytes();

		Arrays.sort( ILLEGAL_XML_1_0_CHARS );
	}


	/*
	 code to clean xml - I got this from  http://markmail.org/message/7ez6nb3i5e7get7l (by Nick Pellow)
	*/

	/** * Cleans a given String, so that it can be safely used in XML.
	* All Invalid characters, will be replaced with the given replace character.
	* Valid XML characters are described here:
	* {@link "http://www.w3c.org/TR/2000/REC-xml-20001006#dt-character"}
	*
	* @param pString the string to clean
	* @param pReplacement the char to use to replace the invalid characters
	* @return the string, cleaned for XML.
	*/
	public static String cleanStringForXml(String pString, char pReplacement)
	{
		final byte[] bytes = pString.getBytes();

		for (int i = 0; i < bytes.length; i++)
		{
			byte aByte = bytes[i];

			if (Arrays.binarySearch(ILLEGAL_XML_1_0_CHARS, aByte) >= 0)
			{
				bytes[i] = (byte) pReplacement;
			}
		}

		return new String(bytes);
	}


	/**
	* got this code from: http://www.javapractices.com/topic/TopicAction.do?Id=96
	*/
	public static String escapeStringForXml( String s )
	{
		final StringBuilder result = new StringBuilder();

		for( char character : s.toCharArray() )
		{
			if (character == '<')
			{
				result.append( "&lt;" );
			}
			else if (character == '>')
			{
				result.append( "&gt;" );
			}
			else if (character == '"')
			{
				result.append( "&quot;" );
			}
			else if (character == '\'')
			{
				result.append( "&apos;" );
			}
			else if (character == '&')
			{
				result.append( "&amp;" );
			}
			else
			{
				//the char is not a special one
				//add it to the result as is
				result.append(character);
			}
		}

		return result.toString();
	}


	/**
		returns length of the given string, and 0 if null
	*/
	public static int strlen( String s )
	{
		if( s == null ) return 0;

		return s.length();
	}


	/**
		returns true if the given string is null or contains no data (whitespace is not considered data in this method)
	*/
	public static boolean strnull( String s )
	{
		return s == null || s.trim().length() == 0;
	}


	/**
		returns true if the given string is null or contains no data (whitespace is not considered data in this method)
	*/
	public static boolean no_data( String s )
	{
		return s == null || s.trim().length() == 0;
	}


	public static String format_time( String fmt )
	{
		return format_time( new Date(), fmt );
	}


	public static String format_time( Date date, String fmt )
	{
		SimpleDateFormat sdfmt = new SimpleDateFormat( fmt );

		return sdfmt.format( date );

// 		Calendar c = new GregorianCalendar();
//
// 		c.setTime( date );
//
// 		int day_of_month = c.get( Calendar.DAY_OF_MONTH );
//
// 		int month = c.get( Calendar.MONTH ) + 1;
//
// 		int year = c.get( Calendar.YEAR );
//
// 		int hour12 = c.get( Calendar.HOUR_OF_DAY );
//
// 		int hour24 = c.get( Calendar.HOUR );
//
// 		int minute = c.get( Calendar.MINUTE );
//
// 		int second = c.get( Calendar.SECOND );
//
// 		String day_name = day_names[ c.get( Calendar.DAY_OF_WEEK_IN_MONTH ) ];
//
// 		String month_name = month_names[ c.get( Calendar.MONTH ) ];
//
// 		String am_pm = ( c.get( Calendar.AM_PM ) == Calendar.AM ? "AM" : "PM" );
//
// 		fmt = fmt.replaceAll( "%AP", am_pm );
//
// 		fmt = fmt.replaceAll( "%ap", am_pm.toLowerCase() );
//
// 		fmt = fmt.replaceAll( "%DN", day_name );
//
// 		fmt = fmt.replaceAll( "%MN", month_name );
//
// 		fmt = fmt.replaceAll( "%Y2", String.format( "%d", year ).substring( 2 ) );
//
// 		fmt = fmt.replaceAll( "%Y4", String.format( "%d", year ) );
//
// 		fmt = fmt.replaceAll( "%M", String.format( "%d", month ) );
//
// 		fmt = fmt.replaceAll( "%0M", String.format( "%02d", month ) );
//
// 		fmt = fmt.replaceAll( "%D", String.format( "%d", day_of_month ) );
//
// 		fmt = fmt.replaceAll( "%0D", String.format( "%02d", day_of_month ) );
//
// 		fmt = fmt.replaceAll( "%h12", String.format( "%d", hour12 ) );
//
// 		fmt = fmt.replaceAll( "%0h12", String.format( "%02d", hour12 ) );
//
// 		fmt = fmt.replaceAll( "%h24", String.format( "%d", hour24 ) );
//
// 		fmt = fmt.replaceAll( "%0h24", String.format( "%02d", hour24 ) );
//
// 		fmt = fmt.replaceAll( "%m", String.format( "%d", minute ) );
//
// 		fmt = fmt.replaceAll( "%0m", String.format( "%02d", minute ) );
//
// 		fmt = fmt.replaceAll( "%s", String.format( "%d", second ) );
//
// 		fmt = fmt.replaceAll( "%0s", String.format( "%02d", second ) );
//
// 		return fmt;
	}


	public static String left( String s, int i )
	{
		if( i <= 0 ) return "";

		if( i >= s.length() ) return s;

		return s.substring( 0, i );
	}

	public static String right( String s, int i )
	{
		if( i <= 0 ) return s;

		if( i >= s.length() ) return "";

		return s.substring( i );
	}

	/**
	return everything to the left of the first occurance of s2 in s or null if s2 doesn't occur in s
	*/
	public static String left_of_first( String s, String s2 )
	{
		return left( s, s.indexOf( s2 ) );
	}

	/**
	return everything to the left of the last occurance of s2 in s or null if s2 doesn't occur in s
	*/
	public static String left_of_last( String s, String s2 )
	{
		return left( s, s.lastIndexOf( s2 ) );
	}


// 	public static  boolean is_void( String s )
// 	{
// 		return s == null || s.trim().length() == 0;
// 	}


	public static String sprintf( String fmt, Object... args )
	{
		return String.format( fmt, args );
	}


	public static String fmt( String fmt, Object... args )
	{
		return String.format( fmt, args );
	}


	/**
	 * @throws NullPointerException if o is null
	 */
	public static String str( Object o )
	{
		return o.toString();
	}
	

	public static String reverse_line_order( String s )
	{
		String[] lines = s.split( "\n" );

		EString rv = new EString();

		for( int i=lines.length-1; i>=0; i-- )
		{
			rv.println( lines[ i ] );
		}

		return rv.toString();
	}


	public static String capitalise( String s )
	{
		char c = s.charAt( 0 );

		char C = Character.toUpperCase( c );

		return C +  s.substring( 1 );
	}



	public static HashMap<String, String> match( String data, String format )
	{
		//System.err.println( "data = " + data );

		//System.err.println( "format = " + format );

		if( data == null ) return null;

		if( format == null ) return null;

		String tokens_exp = "\\[.+?\\]";

		Pattern tkns = Pattern.compile( tokens_exp );

		if( ! format.matches( tokens_exp ) ) return null;

		Matcher tmatcher = tkns.matcher( format );

		ArrayList<String> vars = new ArrayList<String>();

		while( tmatcher.find() )
		{
			String var = tmatcher.group();

			var = var.substring( 1, var.length() - 1 );

			//System.err.printf( "var = %s\n", var );

			vars.add( var );
		}

		Pattern ptn = Pattern.compile( tmatcher.replaceAll( "(.+)" ) );

		//System.err.println( "ptn = " +  ptn.pattern() );

		Matcher matcher = ptn.matcher( data );

		ArrayList<String> vals = new ArrayList<String>();

		if( matcher.find() )
		{
			for( int i=1; i<=matcher.groupCount(); i++ )
			{
				String val = matcher.group( i );

				//System.err.printf( "val = %s\n", val );

				vals.add( val );
			}
		}


		if( vars.size() != vals.size() ) return null;

		HashMap<String, String> map = new HashMap<String, String>();

		for( int i=0; i<vars.size(); i++ )
		{
			map.put( vars.get( i ), vals.get( i ) );
		}

		//System.err.println( map );

		return map;
	}


	
	// --------------------------------------------------    HTML

	public static String html2text( String html ) throws IOException
	{
		Html2Text parser = new Html2Text();
		
		parser.parse( new StringReader( html ) );
		
		String text = parser.getText();
		
		cverbose.println( "html2text( %s ) = %s", html, text );
		
		return text;
	}
	
	public static String url2text( String url ) throws IOException
	{
		URLDecoder decoder = new URLDecoder();
		
		String text = decoder.decode( url, "UTF-8" );

		cverbose.println( "url2text( %s ) = %s", url, text );
		
		return text;
	}



	// --------------------------------------------------    HTML


	// --------------------------------------------------    XML


	/**
	 * Parses an XML file and returns a DOM document.
	*/
	public static Document parse_XML_file( String filename ) throws SAXException, ParserConfigurationException, IOException
	{
		return parse_XML_file( new File( filename ), false );
	}

	/**
	 * Parses an XML file and returns a DOM document.
	*/
	public static Document parse_XML_file( File file ) throws SAXException, ParserConfigurationException, IOException
	{
		return parse_XML_file( file, false );
	}

	/**
	 * Parses an XML file and returns a DOM document.
	 * If validating is true, the contents is validated against the DTD
	 * specified in the file.
	 * got this code from javaalminac
	*/
	public static Document parse_XML_file( String filename, boolean validating ) throws SAXException, ParserConfigurationException, IOException
	{
		return parse_XML_file( new File( filename ), validating );
	}


	/**
	 * Parses an XML file and returns a DOM document.
	 * If validating is true, the contents is validated against the DTD
	 * specified in the file.
	 * got this code from javaalminac
	*/
	public static Document parse_XML_file( File file, boolean validating ) throws SAXException, ParserConfigurationException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating( validating );

		Document doc = factory.newDocumentBuilder().parse( file );

		return doc;
	}


	/**
	 * parses the string containing an xml document
	 */
	public static Document parse_xml_data( String xml_data ) throws SAXException, ParserConfigurationException, IOException
	{
		return parse_xml_data( xml_data, false );
	}
	
	
	/**
	 * parses the string containing an xml document
	 * If validating is true, the contents is validated against the DTD
	 * specified in the file.
	 */
	public static Document parse_xml_data( String xml_data, boolean validating ) throws SAXException, ParserConfigurationException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating( validating );

		Document doc = factory.newDocumentBuilder().parse( new InputSource( new StringReader( xml_data ) ) );

		return doc;
	}


	/**
	 * does an xpath evaluation of the given expression on the current item (Node, Document)
	 */
	public static Node get_node( Object item, String expr ) throws XPathExpressionException
	{
		xpath.reset();

		Object o = xpath.evaluate( expr, item, NODE );

		if( o == null ) return null;

		return ( Node ) o;
	}


	public static String get_node_value( Object item, String expr ) throws XPathExpressionException
	{
		xpath.reset();

		Object o = xpath.evaluate( expr, item, STRING );

		if( o == null ) return null;

		return ( String ) o;
	}


	public static Node[] get_node_list( Object item, String expr ) throws XPathExpressionException
	{
		xpath.reset();

		Object o = xpath.evaluate( expr, item, NODESET );

		if( o == null ) return null;

		NodeList list = ( NodeList ) o;

		Node[] nodes = new Node[ list.getLength() ];

		for( int i=0; i<nodes.length; i++ )
		{
			nodes[ i ] = list.item( i );
		}

		return nodes;
	}


	public static String get_node_type( Node node )
	{
		short type = node.getNodeType();

		if( type == Node.ATTRIBUTE_NODE ) return "ATTRIBUTE_NODE";

		if( type == Node.CDATA_SECTION_NODE ) return "CDATA_SECTION_NODE";

		if( type == Node.COMMENT_NODE ) return "COMMENT_NODE";

		if( type == Node.DOCUMENT_FRAGMENT_NODE ) return "DOCUMENT_FRAGMENT_NODE";

		if( type == Node.DOCUMENT_NODE ) return "DOCUMENT_NODE";

		if( type == Node.DOCUMENT_TYPE_NODE ) return "DOCUMENT_TYPE_NODE";

		if( type == Node.ELEMENT_NODE ) return "ELEMENT_NODE";

		if( type == Node.ENTITY_NODE ) return "ENTITY_NODE";

		if( type == Node.ENTITY_REFERENCE_NODE ) return "ENTITY_REFERENCE_NODE";

		if( type == Node.NOTATION_NODE ) return "NOTATION_NODE";

		if( type == Node.PROCESSING_INSTRUCTION_NODE ) return "PROCESSING_INSTRUCTION_NODE";

		if( type == Node.TEXT_NODE ) return "TEXT_NODE";

		return null;
	}


	/**
	 * allows you to change the default transformer factory
	 * call this with null to go back to using the default java TransformerFactory
	 */
	public static void set_transformer_factory( TransformerFactory transformer_factory )
	{
		tFactory = transformer_factory; 
	}
	

	/**
	 * transforms the given xml string according to the given xsl string
	 * uses the default java TransformerFactory if set_transformer_factory has not been explicitly called yet
	 */
	public static String xslt( String xml, String xsl ) throws TransformerConfigurationException, TransformerException
	{
		//TransformerFactory tFactory = TransformerFactory.newInstance();

		//Transformer transformer = tFactory.newTransformer( new StreamSource( new ByteArrayInputStream( xsl.getBytes() ) ) );

		//if( tFactory == null ) tFactory = new net.sf.saxon.TransformerFactoryImpl();

		if( tFactory == null ) tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer( new StreamSource( new ByteArrayInputStream( xsl.getBytes() ) ) );

		StringWriter out = new StringWriter();

		transformer.transform( new StreamSource( new ByteArrayInputStream( xml.getBytes() ) ), new StreamResult( out ) );

		return out.toString();
	}


	/**
	 * transforms the given xml string according to the given xsl string
	 */
	public static String xslt( EString xml, String xsl ) throws TransformerConfigurationException, TransformerException
	{
		return xslt( xml.toString(), xsl );
	}

	// --------------------------------------------------    END XML



	/**
	 * returns s concatonated m times
	 */
	public static String mult( String s, int m )
	{
		EString es = new EString();

		for( int i=0; i<m; i++ )
		{
			es.print( s );
		}

		return es.toString();
	}
	
	
	/**
	 * splits the test string into lines (splits on the \n character)
	 * returns the subset of lines that match the search
	 * note it wraps the search term with .* at the beginning and end then does a regexp match
	*/
	public static String[] grep( String test, String search )
	{
		return grep( test.split( "\n" ), search );
	}
	
	
	/**
	 * returns the subset of lines that match the search
	 * note it wraps the search term with .* at the beginning and end then does a regexp match
	 */
	public static String[] grep( String[] test_lines, String search )
	{
		ArrayList<String> matching_lines = new ArrayList<String>();
		
		for( String line : test_lines )
		{
			cverbose.println( "line %s matcher %s match %b", line, search, ( line.indexOf( search ) != -1 ) );
			
			//if( line.indexOf( search ) != -1 ) matching_lines.add( line );
			
			if( line.matches( ".*" + search + ".*" ) ) matching_lines.add( line );
		}
		
		return list_to_array( matching_lines );
	}
	
	
	/**
	 * just a wrapper around list.toArray
	 */
	public static String[] list_to_array( List<String> list )
	{
		return list.toArray( new String[ list.size() ] );
	}
	
	
	/**
	 * Cuts the string s into tokens on the delimiter d and returns the token at index f (f starts at 0)
	 * returns null if f is positive and there is no token at index f
	 * eg cut( "rod", "j", 0 ) == "rod"
	 * eg cut( "rod", "j", 1 ) == null
	 * eg cut( "rod", "o", 0 ) == "r"
	 * eg cut( "rod", "o", 1 ) == "d"
	 * eg cut( "rod", "o", 2 ) == null
	 */
	public static String cut( String s, String d, int f )
	{
		String[] tokens = s.split( d );
		
		if( tokens.length >= f + 1 )
		{
			return tokens[ f ];
		}
		
		return null;
	}
}


// #!/usr/bin/env groovy
//
// //def str = "/usr/local/data/test.conf"
// //def test = "[path]/[name]\\.[ext]"
//
// //def str = "rtp://123/456/789"
// //def test = "[proto]:\\/\\/[num]\\/[num2]\\/[num3]"
//
// def str = args[ 0 ]
//
// def test = args[ 1 ]
//
//
// def tkns = /\[(.+?)\]/
//
// assert test ==~ tkns
//
// tmatcher = ( test =~ tkns )
//
// //for( int i=0; i<tmatcher.size(); i++ )
// //{
// //    println tmatcher[ i ]
// //}
//
// def ptn = tmatcher.replaceAll("(.+)");
//
// //test = test.replaceAll(  matcher[ 0 ][ 0 ], ".+" )
//
// //println( "ptn = " +  ptn )
//
// assert str ==~ ptn
//
// matcher = ( str =~ ptn )
//
// //println matcher[ 0 ]
//
// assert matcher[ 0 ].size() - 1 == tmatcher.size()
//
//
//
// map = [:]
//
//
// for( int i=0; i<tmatcher.size(); i++ )
// {
// //    println tmatcher[ i ][ 0 ]
// //    println matcher[ 0 ][ i + 1 ]
//     map[ tmatcher[ i ][ 0 ] ] = matcher[ 0 ][ i + 1 ]
// }
//
// if( args.length < 3 )
// {
// 	println map
// }
// else
// {
// 	println map[ "[" + args[ 2 ] + "]" ]
// }

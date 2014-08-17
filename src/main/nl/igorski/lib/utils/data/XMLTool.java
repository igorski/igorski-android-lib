/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 Igor Zinken - http://www.igorski.nl
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package nl.igorski.lib.utils.data;

import org.apache.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 27-06-12
 * Time: 20:34
 * To change this template use File | Settings | File Templates.
 *
 * Convenience tool to quickly parse XML content
 */
public final class XMLTool
{
    public static Document parseFromHTTPResponse( HttpResponse response )
    {
        try
        {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document dom       = db.parse( response.getEntity().getContent());

            return dom;
        }
        catch ( ParserConfigurationException e ) {}
        catch ( IOException e2 ) {}
        catch ( SAXException e3 ) {}

        return null;
    }

    /**
     * get the value of a specific tag in an Element
     *
     * @param element {Element}
     * @param tagName {String} name of the tag
     * @return {String} the tags value
     */
    public static String getTagValue( Element element, String tagName )
    {
        try
        {
            NodeList list      = element.getElementsByTagName( tagName );
            Element theElement = ( Element ) list.item( 0 );

            NodeList textList  = theElement.getChildNodes();
            return textList.item( 0 ).getNodeValue().trim();
        }
        catch ( Exception e ) {}

        return "";
    }

    /**
     * perform a full dump of a XML Document
     * for debugging purposes
     *
     * @param doc {Node}
     * @return {String}
     */
    public static String nodeToString( Node doc )
    {
        Transformer transformer = null;

        try
        {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        }
        catch ( TransformerConfigurationException e ) {}

        if ( transformer != null )
        {
            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source    = new DOMSource( doc );

            try
            {
                transformer.transform( source, result );
            }
            catch ( TransformerException e ) {}

            return result.getWriter().toString();
        }
        return null;
    }
}

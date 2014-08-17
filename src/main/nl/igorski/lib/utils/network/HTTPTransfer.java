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
package nl.igorski.lib.utils.network;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 13-07-12
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class HTTPTransfer
{
    private static DefaultHttpClient _httpClient;

    /* public */

    /**
     * quick wrapper to POST data to a server
     *
     * @param aURL    {String} URL of the server
     * @param aParams {List<NameValuePair>} optional list of parameters to send

     * @return {HttpResponse}
     */
    public static HttpResponse post( String aURL, List<NameValuePair> aParams )
    {
        HttpPost post = new HttpPost( urlEncode( aURL, null ));

        post.getParams().setBooleanParameter( "http.protocol.expect-continue", false );

        MultipartEntity entity = new MultipartEntity();

        for( NameValuePair pair : aParams )
        {
            try
            {
                entity.addPart( pair.getName(), new StringBodyNoHeaders( pair.getValue()));
            }
            catch ( UnsupportedEncodingException e )
            {
            }
        }
        post.setEntity( entity );

        HttpResponse out = null;

        try
        {
            out = getClient().execute( post );
        }
        catch( Exception e ) {}

        return out;
    }

    /* private */

    private static String urlEncode( String aURL, List<NameValuePair> aParams )
    {
        return aParams == null ? aURL : aURL + "?" + URLEncodedUtils.format( aParams, "UTF-8" );
    }

    private static DefaultHttpClient getClient()
    {
        if ( _httpClient == null )
        {
            HttpClient client = new DefaultHttpClient();

            _httpClient = new DefaultHttpClient
            (
                new ThreadSafeClientConnManager( client.getParams(), client.getConnectionManager().getSchemeRegistry()),
                client.getParams()
            );
        }
        return _httpClient;
    }

    private static class StringBodyNoHeaders extends StringBody
    {
        public StringBodyNoHeaders( String value ) throws UnsupportedEncodingException
        {
            super(value);
        }

        public String getMimeType()
        {
            return null;
        }

        public String getTransferEncoding()
        {
            return null;
        }
    }
}

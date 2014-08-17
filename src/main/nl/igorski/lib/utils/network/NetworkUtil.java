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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 26-06-12
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
public final class NetworkUtil
{
    /**
     * quick query to check if the device has
     * currently got Internet access
     * @param aContext {Context} current application context
     * @return {boolean}
     */
    public static boolean hasInternet( Context aContext )
    {
        ConnectivityManager cm =
            ( ConnectivityManager ) aContext.getSystemService( Context.CONNECTIVITY_SERVICE );

        return cm.getActiveNetworkInfo() != null &&
           cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * open a URL in the device's browser, note you can
     * only request this from a Activity
     *
     * @param aContext {Context} current application context
     * @param aURL {String} URL to open
     */
    public static void openURL( Context aContext, String aURL )
    {
        Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( aURL ));
        aContext.startActivity( browserIntent );
    }
}

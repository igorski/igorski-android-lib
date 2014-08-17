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
package nl.igorski.lib.utils.notifications;

import android.content.Context;
import android.widget.Toast;
import nl.igorski.lib.framework.Core;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 5/10/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 *
 * Alert shows quick feedback messages to the user, it is basically
 * a wrapper for the native "Toast" method and can be called from
 * inner threads outside of the main UI thread. Just think of it
 * as a JavaScript alert that dismisses itself ;)
 */
public final class Alert
{
    /**
     * show a quick feedback message
     * @param aContext {Context} the current context
     * @param aMessage {String} the message to show
     */
    public static void show( Context aContext, String aMessage )
    {
        doShow( aContext, aMessage );
    }

    /**
     * show a quick feedback message from a String resource
     * @param aContext {Context} the current contex
     * @param aMessage {int} res ID of the String message
     */
    public static void show( Context aContext, int aMessage )
    {
        doShow( aContext, aContext.getString( aMessage ));
    }

    /* private methods */

    private static void doShow( final Context aContext, final String aMessage )
    {
        Core.getActivity().runOnUiThread(
        new Runnable()
        {
            public void run()
            {
                Toast.makeText( aContext, aMessage, Toast.LENGTH_LONG ).show();
            }
        });
    }
}

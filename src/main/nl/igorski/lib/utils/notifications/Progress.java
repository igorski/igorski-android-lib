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

import android.app.ProgressDialog;
import android.content.Context;
import nl.igorski.lib.framework.Core;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 08-07-12
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 *
 * convenience class to create / show / update a Progress Dialog
 * to be used across threads / async tasks by invoking it on
 * the main UI thread
 */
public final class Progress
{
    private static ProgressDialog _pd;

    /* public methods */

    public static void initDialog( final Context aContext, final String aTitle, final String aMessage )
    {
        Core.getActivity().runOnUiThread(
        new Runnable()
        {
            public void run()
            {
                _pd = ProgressDialog.show( aContext, aTitle, aMessage, true, false );
            }
        });
    }

    public static void initDialog( Context aContext, int aTitle, int aMessage )
    {
        initDialog( aContext, aContext.getString( aTitle ), aContext.getString( aMessage ));
    }

    public static void updateDialogText( String aTitle, String aMessage )
    {
        if ( _pd != null )
        {
            final ProgressDialog pd = _pd;

            pd.setTitle  ( aTitle );
            pd.setMessage( aMessage );
        }
        else
        {
            initDialog( Core.getContext(), aTitle, aMessage );
        }
    }

    public static void updateDialogText( int aTitle, int aMessage )
    {
        final Context theContext = _pd.getContext();

        updateDialogText( theContext.getString( aTitle ), theContext.getString( aMessage ));
    }

    public static void dismiss()
    {
        if ( _pd != null )
            _pd.dismiss();
    }
}

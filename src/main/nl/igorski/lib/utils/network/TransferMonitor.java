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
import android.os.AsyncTask;
import nl.igorski.lib.utils.debugging.DebugTool;
import nl.igorski.lib.utils.network.interfaces.ITransfer;
import nl.igorski.lib.utils.notifications.Progress;
import org.apache.http.HttpResponse;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 03-07-12
 * Time: 19:43
 * To change this template use File | Settings | File Templates.
 */
public final class TransferMonitor
{
    /**
     * request a window stating a file transfer is in progress
     *
     * @param aContext  {Context} current context
     * @param aTransfer {ITransfer} ITransfer Object performing the request
     * @param aTitle    {String} title to show in the progress window
     * @param aBody     {String} body text to show in the progress window
     */
    public static void monitoredTransfer( Context aContext, final ITransfer aTransfer, String aTitle, String aBody )
    {
        new MonitorTask( aContext, aTransfer, aTitle, aBody ).execute();
    }

    public static void monitoredTransfer( Context aContext, ITransfer aTransfer, int aTitle, int aBody )
    {
        monitoredTransfer( aContext, aTransfer, aContext.getString( aTitle ), aContext.getString( aBody ));
    }

    /* private methods */

    private final static class MonitorTask extends AsyncTask<URL, Integer, Long>
    {
        private ITransfer _transfer;

        public MonitorTask( Context aContext, ITransfer transfer, String aTitle, String aBody )
        {
            _transfer = transfer;

            Progress.initDialog( aContext, aTitle, aBody );
        }

        protected Long doInBackground( URL... urls )
        {
            HttpResponse response = null;

            try
            {
                response = _transfer.doTransfer();
            }
            catch( Exception e )
            {
                DebugTool.log( "EXCEPTION DURING OCCURRED DURING TRANSFER => " + e.getLocalizedMessage());
            }

            Progress.dismiss();
            _transfer.returnHandler( response );

            return ( long ) 0;
        }

        protected void onProgressUpdate( Integer... progress )
        {
            DebugTool.log( "TRANSFER PROGRESS => " + progress[ 0 ]);
        }

        protected void onPostExecute( Long l )
        {
            DebugTool.log( "TRANSFER TASK COMPLETED" );
        }
    }
}

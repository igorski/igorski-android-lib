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
package nl.igorski.lib.utils.ui;

import android.content.res.Resources;
import android.widget.Button;
import android.widget.ImageButton;
import nl.igorski.lib.framework.Core;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 25-09-12
 * Time: 20:29
 * To change this template use File | Settings | File Templates.
 *
 * ButtonTool contains convenience functions to invoke operations
 * that differ across Android versions to maintain a single interface
 */
public final class ButtonTool
{
    /**
     * change the image source for a ImageButton
     *
     * @param aButton     {ImageButton} the ImageButton
     * @param aDrawableId {int} resource ID of the desired drawable resource
     */
    public static void setImageButtonImage( ImageButton aButton, int aDrawableId )
    {
        aButton.setImageDrawable( getResources().getDrawable( aDrawableId ));
    }

    /**
     * change the background for a Button
     *
     * @param aButton     {Button} the Button
     * @param aDrawableId {int} resource ID of the desired drawable resource
     */
    public static void setButtonBackground( Button aButton, int aDrawableId )
    {
        aButton.setCompoundDrawablesWithIntrinsicBounds( null, getResources().getDrawable( aDrawableId ), null, null );
    }

    /**
     * change the text value of a Button
     *
     * @param aButton   {Button} the button
     * @param aStringId {int} resource ID of the desired string resource
     */
    public static void setButtonText( Button aButton, int aStringId )
    {
        aButton.setText( aStringId );
    }

    /* private */

    private static Resources getResources()
    {
        return Core.getContext().getResources();
    }
}

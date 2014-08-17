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
package nl.igorski.lib.utils.fonts;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created with IntelliJ IDEA.
 * User: igorzinken
 * Date: 26-05-14
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
 */
public final class CanvasTextUtil
{
    /**
     * convenience method to calculate the y offset for aligning
     * text vertically in its containing Sprite
     *
     * @param {int} yPos y-coordinate of the Sprite / "container" holding the text
     * @param {int} spriteHeight height of the Sprite / "container" holding the text
     * @param {String} text to render
     * @param {Paint} textPaint the Paint used to render the text
     *
     * @return {int} the y coordinate for drawing the text
     */
    public static int alignTextVertically( int yPos, int spriteHeight, String text, Paint textPaint )
    {
        final Rect bounds = new Rect();
        textPaint.getTextBounds( text, 0, text.length(), bounds );

        return yPos + ( spriteHeight / 2 ) + ( bounds.height() / 2 );
    }
}

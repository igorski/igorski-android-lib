/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013-2014 Igor Zinken - http://www.igorski.nl
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
package nl.igorski.lib.utils.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created with IntelliJ IDEA.
 * User: igorzinken
 * Date: 13-08-14
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
public final class BitmapTool
{
    /**
     * create a smaller, cropped version of a given
     * Bitmap source at given crop Rectangle
     *
     * @param {Bitmap} source
     * @param {Rect} crop
     * @return Bitmap
     */
    public static Bitmap crop( Bitmap source, Rect crop )
    {
        Bitmap out = Bitmap.createBitmap( crop.width(), crop.height(), source.getConfig());

        final Canvas canvas = new Canvas( out );
        canvas.drawBitmap( source, -crop.left, -crop.top, null );
//        canvas.drawBitmap( source, crop, new Rect( 0, 0, crop.width(), crop.height()), null );

        return out;
    }
}

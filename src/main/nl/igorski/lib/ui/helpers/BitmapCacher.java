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
package nl.igorski.lib.ui.helpers;

import android.graphics.Point;
import nl.igorski.lib.ui.interfaces.ICacheableSprite;

/**
 * Created with IntelliJ IDEA.
 * User: igorzinken
 * Date: 16-08-14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 *
 * BitmapCacher can be used for AnimatedSprites whose draw method
 * uses custom graphics / Bitmap layering to render its contents.
 * Instead of redrawing the same value upon each render cycle, this
 * class can aid in creating a cached Bitmap during the first render
 * which can be used for subsequent renders to greatly free CPU resources
 */
public final class BitmapCacher
{
    /**
     * prior to caching a Bitmap, the sprite must be prepared
     * which basically restores its offset to a 0,0 coordinate
     * allowing the temporary canvas to occupy less memory as
     * a smaller area can be reserved
     *
     * @param {ICacheableSprite} sprite
     * @return {Point} holding the original sprite coordinates
     */
    public static Point prepare( ICacheableSprite sprite )
    {
        final Point out = new Point( sprite.getXPos(), sprite.getYPos() );

        sprite.setXPos( 0 );
        sprite.setYPos( 0 );

        return out;
    }

    /**
     * after the caching operation completed, this method must
     * be invoked to restore the given sprites original position
     *
     * @param {ICacheableSprite} sprite
     * @param {Point} originalCoords cached by the prepare method
     */
    public static void restore( ICacheableSprite sprite, Point originalCoords )
    {
        sprite.setXPos( originalCoords.x );
        sprite.setYPos( originalCoords.y );
    }
}

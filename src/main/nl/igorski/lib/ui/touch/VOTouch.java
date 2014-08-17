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
package nl.igorski.lib.ui.touch;

import nl.igorski.lib.ui.touch.interfaces.ITouch;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 12-08-12
 * Time: 18:43
 *
 * VOTouch is the base Value Object for a touch event, you
 * can extend this class to add custom properties for
 * custom use, this Object provides enough properties and
 * methods to be of value in the TouchManager
 */
public class VOTouch implements ITouch
{
    protected int pointer_id   = 0;
    protected int xPos         = 0;
    protected int yPos         = 0;

    public VOTouch( int aPointerId, int aXPos, int aYPos )
    {
        pointer_id = aPointerId;
        xPos       = aXPos;
        yPos       = aYPos;
    }

    /* public */

    public void setPointerId( int aPointerId )
    {
        pointer_id = aPointerId;
    }

    public int getPointerId()
    {
        return pointer_id;
    }

    public void updatePosition( int x, int y )
    {
        xPos = x;
        yPos = y;
    }

    public int getX()
    {
        return xPos;
    }

    public int getY()
    {
        return yPos;
    }
}

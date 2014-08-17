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

import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import nl.igorski.lib.ui.touch.interfaces.ITouch;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 12-08-12
 * Time: 18:45
 *
 * TouchMapper is a helper class to store references to pointers for managed multi-touch use
 * it differs from the CanvasTouchManager as it is instantiable and thus several TouchMappers
 * can mediate the same pointers for different Objects (which implement the ITouch interface
 * and can be any type of Object rather than just Sprites)
 */
public final class TouchMapper
{
    private ITouch[] _touches;

    public final int MAX_TOUCHES = CanvasTouchManager.MAXIMUM_POINTERS;

    public TouchMapper()
    {
        _touches = new ITouch[ MAX_TOUCHES ];
    }

    /* public methods */

    public boolean hasTouches()
    {
        int amount = 0;

        for ( int i = 0; i < MAX_TOUCHES; ++i )
        {
            if ( _touches[ i ] != null )
                ++amount;
        }
        return amount > 0;
    }

    public boolean hasTouch( int aPointer )
    {
        if ( aPointer < MAX_TOUCHES )
            return _touches[ aPointer ] != null;

        return false;
    }

    /**
     * return all touches present in the mapper
     * NOTE : this also contains empty slots, be sure
     * to check the array indexes for non-null values !!
     *
     * @return {ITouch[]}
     */
    public ITouch[] getAllTouches()
    {
        return _touches;
    }

    public int getAmountOfTouches()
    {
        int amt = 0;

        for ( int i = 0; i < MAX_TOUCHES; ++i )
        {
            if ( _touches[ i ] != null )
                ++amt;
        }
        return amt;
    }

    public void removeAllTouches()
    {
        for ( int i = 0, l = MAX_TOUCHES; i < l; ++i )
            _touches[ i ] = null;
    }

    /**
     * retrieve the VOTouch Object by the pointer ID
     * if it didn't exist, it is created
     *
     * @param aPointerId {int}
     * @param aX {int} pointer X position, used on creation
     * @param aY {int} pointer Y position, used on creation
     * @param aTouchClass {Class} optional ITouch implementation to instantiate when the touch
     *        didn't exist yet (this overrides the default creation of a igorski.lib...VOTouch Object)
     *
     * @return {ITouch}
     */
    public ITouch getTouchByPoint( int aPointerId, int aX, int aY, ITouch aTouchClass )
    {
        ITouch touch = getTouchByPoint( aPointerId );

        if ( touch == null && aPointerId < MAX_TOUCHES )
        {
            touch = aTouchClass;
            touch.setPointerId( aPointerId );
            touch.updatePosition( aX, aY );

            _touches[ aPointerId ] = touch;
        }
        return touch;
    }

    public ITouch getTouchByPoint( int aPointerId, int aX, int aY )
    {
        ITouch touch = getTouchByPoint( aPointerId );

        if ( touch == null && aPointerId < MAX_TOUCHES )
        {
            touch = new VOTouch( aPointerId, aX, aY );
            _touches[ aPointerId ] = touch;
        }
        return touch;
    }

    public ITouch getTouchByPoint( int aPointerId )
    {
        if ( aPointerId >= MAX_TOUCHES )
            return null;

        return _touches[ aPointerId ];
    }

    public boolean removeTouchByPoint( int aPointerId )
    {
        if ( hasTouch( aPointerId ))
        {
            _touches[ aPointerId ] = null;
            return true;
        }
        return false;
    }

    public boolean removeTouch( ITouch touch )
    {
        for ( int i = 0; i < MAX_TOUCHES; ++i )
        {
            if ( _touches[ i ] == touch )
            {
                _touches[ i ] = null;
                return true;
            }
        }
        return false;
    }

    public boolean setTouchForPoint( ITouch touch, int aPointerId )
    {
        if ( aPointerId < MAX_TOUCHES )
        {
            _touches[ aPointerId ] = touch;
        }
        return false;
    }

    /* static helper methods */

    /**
     * calculate the distance between two pointers
     * @param event {MotionEvent}
     * @return {float}
     */
    public static float spacing( MotionEvent event )
    {
        final float x = event.getX( 0 ) - event.getX( 1 );
        final float y = event.getY( 0 ) - event.getY( 1 );

        return FloatMath.sqrt( x * x + y * y );
    }

    /**
     * calculate the point in the middle of two pointers
     * @param point {PointF} to avoid garbage collections that can cause noticeable
     *        pauses in the application, we reuse an existing object to store the
     *        result rather than allocating and returning a new one each time.
     *
     * @param event {MotionEvent}
     */
    public static void midPoint( PointF point, MotionEvent event )
    {
        final float x = event.getX( 0 ) + event.getX( 1 );
        final float y = event.getY( 0 ) + event.getY( 1 );

        point.set( x / 2, y / 2 );
    }
}

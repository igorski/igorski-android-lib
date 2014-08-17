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
package nl.igorski.lib.ui.components;

import android.graphics.*;
import android.view.MotionEvent;
import nl.igorski.lib.interfaces.listeners.IChangeListener;
import nl.igorski.lib.ui.base.InteractiveSprite;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 11-10-12
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 *
 * A simple drawable view component that renders itself
 * onto a Canvas
 */
public class Slider extends InteractiveSprite
{
    /* constants */

    public static int SLIDER_HORIZONTAL = 0;
    public static int SLIDER_VERTICAL   = 1;

    /* class variables */

    private double _min;
    private double _max;
    private int _direction;
    private double _default;
    private boolean _enabled;
    protected boolean _roundedValues;
    private double _lastValue;

    protected Paint _trackPaint;
    protected Paint _handlePaint;
    protected Paint _barPaint;

    protected float handleX    = 0f;
    protected float handleY    = 0f;
    protected float orgHandleX = 0f;
    protected float orgHandleY = 0f;

    protected int _handleWidth  = 25;
    protected int _handleHeight = 25;

    protected RectF _handleRect;
    protected RectF _handleBarRect;

    /* event handlers */

    private IChangeListener _changeListener;

    public Slider( int aDirection, int aWidth, int aHeight, int aHandleWidth, int aHandleHeight, double aMinValue, double aMaxValue, double aDefaultValue, boolean aEnabled )
    {
        super( null, "SLIDER" + Math.random(), aWidth, aHeight, 30, 1 );

        _direction = aDirection;
        _min       = aMinValue;
        _max       = aMaxValue;
        _enabled   = aEnabled;

        if ( aDefaultValue == 0 )
            aDefaultValue = _min;

        _handleWidth   = aHandleWidth;
        _handleHeight  = aHandleHeight;
        _handleRect    = new RectF();
        _handleBarRect = new RectF();

        _default       = aDefaultValue;
        _lastValue     = aDefaultValue;
        _roundedValues = false;

        init();
    }

    /* public methods */

    // Allows the user to set an Listener and react to value changes

    public void setChangeListener( IChangeListener listener )
    {
        _changeListener = listener;
    }

    public void setTrackColor( int aColor )
    {
        _trackPaint.setColor( aColor );
    }

    public void setBarColor( int aColor )
    {
        _barPaint.setColor( aColor );
    }

    public void setHandleColor( int aColor )
    {
        _handlePaint.setColor( aColor );
    }

    public void setRoundedValues( boolean value )
    {
        _roundedValues = value;
    }

    public double getValue()
    {
        double pct       = 1.0;
        final double dev = _max - _min;

        if ( _direction == SLIDER_HORIZONTAL )
            pct = handleX / spriteWidth;

        else if ( _direction ==  SLIDER_VERTICAL )
            pct = handleY / spriteHeight;

        double theValue = ( dev * pct ) + _min;

        // invert value, highest y position is actually lowest value ;)
        if ( _direction == SLIDER_VERTICAL )
            theValue = Math.abs( theValue - _max );

        return _roundedValues ? Math.round( theValue ) : theValue;
    }

    public void setValue( double aValue )
    {
        final double pct = ( aValue - _min ) / ( _max - _min );

        if ( _direction == SLIDER_HORIZONTAL )
            handleX = ( float ) pct * spriteWidth;

        // invert value, highest y position is actually lowest value ;)
        else if ( _direction == SLIDER_VERTICAL )
            handleY = ( float ) ( spriteHeight - ( pct * spriteHeight ));

        updateValues( aValue );
    }

    public boolean getEnabled()
    {
        return _enabled;
    }

    public void setEnabled( boolean value )
    {
        _enabled = value;

        if ( value )
        {
            // set active paint
        }
        else
        {
            // set inactive paint
        }
    }

    @Override
    public void draw( Canvas canvas )
    {
        // track
        canvas.drawRect( coords, _trackPaint );

        // handle
        canvas.drawRect( _handleRect, _handlePaint );

        // bar on edge of handle
        canvas.drawRect( _handleBarRect, _barPaint );
    }

    public void destroy()
    {
        _changeListener = null;
    }

    /* event handlers */

    @Override
    public boolean handleTouchDown( Float x, Float y, MotionEvent ev )
    {
        boolean handled = super.handleTouchDown( x, y, ev );

        orgHandleX = x;
        orgHandleY = handleY;//y;

        updateValues( getValue());

        return handled;
    }

    @Override
    public boolean handleTouchUp( MotionEvent ev )
    {
        boolean handled = super.handleTouchUp( ev );

        // jump to position of touch was in range of "tap time"
        if ( System.currentTimeMillis() - _dragStartTime < 250 )
        {
            // can be null by TouchManager
            if ( ev != null )
            {
                if ( _direction == SLIDER_HORIZONTAL )
                {
                    handleX = ev.getX() - ( _handleWidth / 2 );

                    // snap to edges
                    if ( handleX < ( coords.left + _handleWidth ))
                        handleX = xPos;

                    else if ( ev.getX() > ( coords.right - _handleWidth ))
                        handleX = coords.right - ( _handleWidth / 2 );
                }
                else {
                    handleY = ev.getY() - _handleHeight;
                }
            }
            updateValues( getValue());
        }
        return handled;
    }

    @Override
    public void updatePosition( Float x, Float y, MotionEvent ev )
    {
        x = _dragStartX + x - ( _handleWidth / 2 );
        //y = _dragStartY + y;

        if (( _direction == SLIDER_VERTICAL && ( y >= -spriteHeight && y <= spriteHeight )) ||
            ( _direction == SLIDER_HORIZONTAL && ( x >= xPos && x <= ( xPos + spriteWidth ))))
        {
            // the minimum value is -_size and the maximum value is _size
            // note that a y position with the value of the positive _size is the MINIMUM value of the rotary
            // and the y position with the value of the negative _size is the MAXIMUM value of the rotary

            handleX = x;/*orgHandleX* + x */
            handleY = orgHandleY + y; //y;

            final int maxX = spriteWidth;

            if ( handleX < _handleWidth )
                handleX = 0;
            else if ( handleX > maxX )
                handleX = maxX;

            if ( handleY < 0 )
                handleY = 0;
            else if ( handleY > spriteHeight )
                handleY = spriteHeight;
        }
        updateValues( getValue());
    }

    /* protected methods */

    protected void init()
    {
        setValue  ( _default );
        setEnabled( _enabled );

        // prepare Paints
        _trackPaint = new Paint();
        _trackPaint.setColor( Color.WHITE );

        _barPaint = new Paint();
        _barPaint.setColor( Color.GRAY );

        _handlePaint = new Paint();
        _handlePaint.setColor( Color.RED );
    }

    protected void updateValues( double aValue )
    {
        if ( _enabled && _changeListener != null )
            _changeListener.handleChange( aValue );

        _lastValue = aValue;

        cacheCoordinates();
    }

    @Override
    protected void cacheCoordinates()
    {
        // matches entire horizontal strip (jumps to x), but for vertical only the current handle position
        coords.left   = xPos;
        coords.top    = yPos;
        coords.right  = xPos + spriteWidth;
        coords.bottom = yPos + spriteHeight;//( int ) ( yPos + handleY );// + _handleHeight );

        if ( _direction == SLIDER_HORIZONTAL )
        {
            _handleRect.left   = xPos;
            _handleRect.top    = yPos;
            _handleRect.right  = xPos + handleX;
            _handleRect.bottom = yPos + _handleHeight;

            _handleBarRect.left   = xPos + handleX - ( _handleWidth / 4 );
            _handleBarRect.top    = yPos;
            _handleBarRect.right  = xPos + handleX;
            _handleBarRect.bottom = yPos + _handleHeight;
        }
        else if ( _direction == SLIDER_VERTICAL )
        {
            _handleRect.left   = xPos;
            _handleRect.top    = yPos + handleY;
            _handleRect.right  = xPos + _handleWidth;
            _handleRect.bottom = yPos + spriteHeight;

            _handleBarRect.left   = xPos;
            _handleBarRect.top    = yPos + handleY;
            _handleBarRect.right  = xPos + _handleWidth;
            _handleBarRect.bottom =  yPos + handleY + ( spriteHeight / 25 );
        }
    }
}

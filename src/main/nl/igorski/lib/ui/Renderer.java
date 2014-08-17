/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2009-2014 Igor Zinken - http://www.igorski.nl
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
package nl.igorski.lib.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.SurfaceHolder;
import nl.igorski.lib.ui.interfaces.IAnimatedSprite;
import nl.igorski.lib.ui.interfaces.IViewPort;
import nl.igorski.lib.utils.debugging.DebugTool;
import nl.igorski.lib.utils.threading.BaseThread;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: igorzinken
 * Date: 17-08-14
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 *
 * Renderer is the class that works as the render loop, updating
 * the onscreen Canvas with all the Sprites registered on the DisplayList
 */
public class Renderer extends BaseThread implements IViewPort
{
    protected Resources _resources;
    protected SurfaceHolder _container;

    /* IViewPort properties */

    public Point offset; // offset describes the x- and y- pan of the viewport
    public Rect bounds;  // bounds describe the total "world" area (exceeds SurfaceHolder size)

    protected boolean _isDragging = false;
    protected long    _dragStartTime;
    protected int     _dragStartX;
    protected int     _dragStartY;
    protected Vector<IAnimatedSprite> _sprites;

    /* benchmark related */

    protected boolean BENCHMARK = true;
    protected long    _lastRender;
    protected Paint   _textPaint;

    protected long  _frameSampleTime;
    protected int   _frameSamplesCollected;
    protected float _fps;
    protected int   _realFps;
    protected float _executeInterval;

    /**
     * Construct a Renderer for a given SurfaceHolder
     * the Renderer will act as a mediator between individual Sprites and
     * getting them on screen in a strict timed fashion
     *
     * this is a thread so invoke "start" to get it running !
     *
     * @param {SurfaceHolder} aContainer the Canvas this Renderer will draw on
     * @param {Context} aContext the application context
     * @param {int} aFrameRate desired framerate for rendering
     */
    public Renderer( SurfaceHolder aContainer, Context aContext, int aFrameRate )
    {
        _container  = aContainer;
        _resources  = aContext.getResources();
        _lastRender = System.currentTimeMillis() + 1000;

        setFrameRate( aFrameRate );

        offset = new Point( 0, 0 );

        // by default the "world" is pre-initialized to match the SurfaceHolder dimensions
        // this can be overridden using the "setBounds"-method

        bounds = new Rect ( 0, 0,
                            aContainer.getSurfaceFrame().width(),
                            aContainer.getSurfaceFrame().height() );

        if ( BENCHMARK )
        {
            _textPaint = new Paint();
            _frameSampleTime       = 0;
            _frameSamplesCollected = 0;
        }
    }

    /* public methods */

    /**
     * specifies the size of the "world" we are
     * rendering, these dimensions can exceed the
     * SurfaceHolder dimensions (the viewport panning
     * ensures that we can offset our "camera" within
     * these dimensions)
     *
     * all Sprites x- and y-coordinate are relative to
     * this bounds Object (consider a given Sprite is
     * positioned 1500 pixels from the left within a
     * "world" which has 3000 pixels bounds while the
     * viewport is 800 pixels wide and offset 100 pixels
     * to the right, making the Sprite invisible unless
     * the viewport moves another 100 pixels).
     *
     * @param {number} aWidth
     * @param {number} aHeight
     */
    public void setBounds( int aWidth, int aHeight )
    {
        bounds.right  = bounds.left + aWidth;
        bounds.bottom = bounds.top  + aHeight;
    }

    /**
     * sets the desired framerate to render with
     * this can be throttled in real time
     *
     * @param {number} aFrameRate
     */
    public void setFrameRate( int aFrameRate )
    {
        _fps             = aFrameRate;
        _executeInterval = 1000 / ( float ) aFrameRate;
    }

    /**
     * add a Sprite to the renderers display list so
     * it can be rendered onto the Canvas
     *
     * this method can be extended in your custom
     * Renderer for more logic (i.e. separation of
     * Sprite by Class into separate lists for
     * view stacking purposes, etc.)
     *
     * @param {IAnimatedSprite} aSprite
     * @return {boolean}
     */
    public boolean addSprite( IAnimatedSprite aSprite )
    {
        if ( _sprites.contains( aSprite ))
            return false;

        return _sprites.add( aSprite );
    }

    /**
     * @param {IAnimatedSprite} aSprite to remove
     * @return {boolean}
     */
    public boolean removeSprite( IAnimatedSprite aSprite )
    {
        if ( !_sprites.contains( aSprite ))
            return false;

        return _sprites.remove( aSprite );
    }

    /* public IViewPort methods */

    /**
     * @return {number} the viewport x-coordinate
     */
    public int getXPos()
    {
        return offset.x;
    }

    /**
     * set the x-coordinate of the viewport
     * i.e. "pan the camera" horizontally
     *
     * @param {number} aValue
     */
    public void setXPos( int aValue )
    {
        if ( -aValue < bounds.left )
            aValue = bounds.left;

        else if ( -aValue > bounds.right )
            aValue = bounds.right;

        offset.x = aValue;
    }

    /**
     * @return {number} the viewport y-coordinate
     */
    public int getYPos()
    {
        return offset.y;
    }

    /**
     * set the y-coordinate of the viewport
     * i.e. "pan the camera" vertically
     *
     * @param {number} aValue
     */
    public void setYPos( int aValue )
    {
        if ( -aValue < bounds.top )
            aValue = bounds.top;

        else if ( -aValue > bounds.bottom )
            aValue = bounds.bottom;

        offset.y = aValue;
    }

    /* public drag methods (to pan inside the viewport using a pointer) */

    /**
     * update the position of the viewport, this should be invoked
     * after a MotionEvent the viewport movement is restricted to remain
     * within the world bounds
     *
     * @param {number} x
     * @param {number} y
     */
    public void updatePosition( Float x, Float y )
    {
        /**
         * position is updated during drag move, we
         * calculate the new position relative to the
         * original position
         */
        final int theX = Math.round( _dragStartX + x );
        final int theY = Math.round( _dragStartY + y );

        // keep container within constraints
        if ( -theX > bounds.left && -theX < ( bounds.right - bounds.width() ))
            offset.x = theX;

        if ( -theY > bounds.top && -theY < ( bounds.bottom - bounds.height() ))
            offset.y = theY;
    }

    /**
     * start dragging the container
     * we cache the viewport offset at drag start
     * as well as the timestamp
     */
    public void startViewportDrag()
    {
        _dragStartX = offset.x;
        _dragStartY = offset.y;

        _dragStartTime = System.currentTimeMillis();

        _isDragging = true;
    }

    /**
     * stops the dragging of the container, if the
     * container has been moved only a slight amount and
     * the delta time between drag start and drag end is short
     * enough, we assume the user has tapped
     *
     * @return {boolean} whether the user has tapped the viewport
     */
    public boolean stopViewportDrag()
    {
        _isDragging = false;

        final long now = System.currentTimeMillis();

        // return true if the user tapped on the container
        // unless we have moved the grid by at least 30 px

        if ( Math.abs( _dragStartX - offset.x ) >= 30 || Math.abs( _dragStartY - offset.y ) >= 30 )
            return false;

        // grid didn't move ( by much ) check if we were in time
        return (( now - _dragStartTime ) < 300 );
    }

    public boolean isDragging()
    {
        return _isDragging;
    }

    /* thread loop */

    @Override
    public void run()
    {
        Canvas c = null;
        //long now, delta;

        while ( _isRunning )
        {
            //now   = System.currentTimeMillis();
            //delta = now - _lastRender;

            // ideally, we'd only execute render processes at the allotted time slot
            // slot to keep it running at the requested frame rate but turns out this
            // check comes at the expense of dropped frames (?)

            //if ( delta > _executeInterval )
            //{
                try
                {
                    updateObjects( System.currentTimeMillis() /*now*/ );
                    c = _container.lockCanvas( null );
                    render( c );

                    if ( BENCHMARK )
                        doBenchmark( c );

                    c.save(); // prevents java.lang.IllegalStateException: Underflow in restore on Android 4.3
                    c.restore();

                    // get a new timestamp to keep the framerate in sync with the execution time
                    _lastRender = System.currentTimeMillis();
                }
                catch ( Exception e )
                {
                    // can occur when app loses focus...
                    DebugTool.log( "Renderer exception" );
                    DebugTool.logException( e );
                }
                finally
                {
                    // if an exception is thrown during the above, we don't
                    // leave the Surface in an inconsistent state

                    if ( c != null )
                    {
                        try {
                            _container.unlockCanvasAndPost( c );
                        }
                        catch ( IllegalArgumentException e ) {}
                    }
                }
            //}

            /*
            // we could do a sleep, but that would assume all actual rendering takes 0 ms to complete ;)
            try {
                Thread.sleep( _executeInterval );
            }
            catch( InterruptedException ex ) {}
            */

            synchronized ( _pauseLock )
            {
                while ( _paused )
                {
                    try
                    {
                        _pauseLock.wait();
                    }
                    catch ( InterruptedException ex ) {}
                }
            }
        }
    }

    /* protected methods */

    /**
     * invoke on all Sprites in the display list
     * just before rendering their contents
     *
     * @param {long} aTimestamp currentTime the current timestamp
     *              can be used by Sprites for update logic
     */
    protected void updateObjects( long aTimestamp )
    {
        final Vector<IAnimatedSprite> sprites = getSprites();

        // loop through the sprites and invoke update

        synchronized ( sprites )
        {
            for ( IAnimatedSprite a : sprites)
                a.update( aTimestamp );
        }
    }

    /**
     * the actual rendering where the Canvas is
     * cleared and all registered Sprites draw
     * themselves onto the Canvas
     *
     * @param {Canvas} canvas
     */
    protected void render( Canvas canvas )
    {
        if ( canvas == null )
            return;

        // draw background ( clears previous Canvas contents by overwriting all pixels )
        canvas.drawColor( Color.BLACK );

        final Vector<IAnimatedSprite> sprites = getSprites();

        // loop through the sprites and let them render themselves onto the Canvas

        synchronized ( sprites )
        {
            for ( IAnimatedSprite sprite : sprites )
                sprite.draw( canvas );
        }
    }

    /**
     * retrieve all Sprites registered for rendering
     * this can be left as is or extended to return
     * the result of multiple display lists in case
     * we want to stack them in separate containers
     * the Sprites that come later in the list, are
     * rendered with a higher z-index (i.e. overlap
     * those that appear sooner)
     *
     * @return {Vector<IAnimatedSprite>}
     */
    protected Vector<IAnimatedSprite> getSprites()
    {
        return _sprites;
    }

    protected void doBenchmark( Canvas canvas )
    {
        final long now = System.currentTimeMillis();

        // still preparing ?
        if ( _lastRender > now )
            return;

        if ( _lastRender != 0 )
        {
            final long elapsedTime = now - _lastRender;
            _frameSampleTime     += elapsedTime;

            if ( ++_frameSamplesCollected == 10 )
            {
                _realFps               = ( int ) ( 10000 / _frameSampleTime );
                _frameSampleTime       = 0;
                _frameSamplesCollected = 0;
            }
        }
        drawText( canvas, "fps:" + _realFps, bounds.width() - 85, bounds.height() - 40 );
    }

    /**
     * convenience method to draw text (for debugging purposes)
     * onto the Canvas
     *
     * @param {Canvas} canvas
     * @param {String} text
     * @param {int} x
     * @param {int} y
     */
    protected void drawText( Canvas canvas, String text, int x, int y )
    {
        canvas.drawText( text, x, y, _textPaint );
    }
}

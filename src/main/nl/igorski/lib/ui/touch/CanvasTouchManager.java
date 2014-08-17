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
package nl.igorski.lib.ui.touch;

import android.graphics.Rect;
import android.view.MotionEvent;
import nl.igorski.lib.ui.interfaces.IInteractiveSprite;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 13-10-12
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 *
 * CanvasTouchManager is a static class which can be used to store (and maintain)
 * references to active pointers and UI elements linked to the pointers
 * this class thus allows you to use multiple pointers to create individual
 * InteractiveSprites that, on their own, would have no multi touch support
 *
 * in other words : InteractiveSprites are registered to a pointer, each time
 * the pointer moves, the InteractiveSprites position update and and on/off
 * handlers are invoked, as if working with a single pointer
 */
public final class CanvasTouchManager
{
    public static boolean draggingObject    = false;
    public static IInteractiveSprite dragObject;

    public static int MAXIMUM_POINTERS              = 10;
    private static IInteractiveSprite[] _UIElements = new IInteractiveSprite[ MAXIMUM_POINTERS ];

    /* public methods */

    /**
     * invoke when a MotionEvent.ACTION_MOVE is fired, this
     * will invoke the update methods of all UI elements linked
     * to the currently active pointers
     *
     * @param aEvent {MotionEvent}
     *
     * @return {boolean} whether any of the pointers has handled one of the registered UI elements
     */
    public static boolean handlePointerMove( final MotionEvent aEvent )
    {
        boolean wasHandled = false;

        for ( int i = 0, l = aEvent.getPointerCount(); i < l; ++i )
        {
            final int id = aEvent.getPointerId( i );

            // note the first pointer is omitted as we're looking for additional touches
            if ( id > 0 )
            {
                if ( hasTouch( id ))
                {
                    // as opposed to a single (ACTION_DOWN and ACTION_UP)-triggered pointer, we now
                    // need to take the offset position of the UI element into account
                    final IInteractiveSprite theElement = _UIElements[ id ];
                    theElement.updatePosition( aEvent.getX( i ) - theElement.getXPos(),
                                               aEvent.getY( i ) - theElement.getYPos(), aEvent );

                    wasHandled = true;
                }
            }
        }
        return wasHandled;
    }

    /**
     * invoke when a MotionEvent.ACTION_POINTER_UP is fired, this
     * will evaluate the remaining pointers with the registered
     * pointers and break the links with the UI elements
     *
     * note that the element and pointer index will shift if
     * a pointer between the start and end of the total pointer amount
     * has been released (assume mappings for pointers [ 0, 1, 2 ] exist,
     * and pid 1 is triggering this event > mappings would result in
     * [ 0, null, 2 ] which would be fine, if what WAS pid 2 wouldn't
     * now have the index pid 1 (thus losing the reference to the original
     * element that was mapped at index 2, which we thus need to shift
     * down to the "free slot" cleared by pid 1's pointer up event
     *
     * @param aEvent {MotionEvent}
     *
     * @return {boolean} whether the pointer has handled one of the registered UI elements
     */
    public static boolean handlePointerUp( final MotionEvent aEvent )
    {
        final int pointerId = aEvent.getPointerId( aEvent.getActionIndex() );

        if ( !hasTouch( pointerId ))
            return false;

        removeTouch( pointerId, aEvent );

        final int remaining = aEvent.getPointerCount() - 1;

        // last pointer must be mapping to draggingObject as it
        // is now mediated by the first pointer (which is not handled by this manager)
        if ( pointerId == 0 && remaining == 1 )
        {
            if ( draggingObject )
            {
                for ( final IInteractiveSprite ui : _UIElements )
                {
                    if ( ui != null ) {
                        dragObject = ui;
                        break;
                    }
                }
            }
            removeAllTouches();
            return true;
        }
        return true;
    }

    /**
     * invoke when a MotionEvent.ACTION_POINTER_DOWN is fired, this will
     * perform a hit test on a given list of UI elements and link them to a pointer
     * if their coordinates overlap
     *
     * @param aEvent      {MotionEvent} the current motion event
     * @param aUIElements {Vector.<IInteractionAnimation>} list of UI elements
     *
     * @return {boolean} whether any of the pointers has handled one of the registered UI elements
     */
    public static boolean handlePointerDown( final MotionEvent aEvent, final Vector<IInteractiveSprite> aUIElements )
    {
        boolean wasHandled = false;

        // we can use multiple pointers for controlling multiple UI elements at once
        for ( int i = 0, l = aEvent.getPointerCount(); i < l; ++i )
        {
            final int id = aEvent.getPointerId( i );

            if ( id > 0 )
            {
                if ( !hasTouch( id ))
                {
                    for ( final IInteractiveSprite a : aUIElements )
                    {
                        // hit test
                        final Rect coords = a.getCoords();
                        final float x = aEvent.getX( i );
                        final float y = aEvent.getY( i );

                        if ( a.isInteractive())
                        {
                            if ( x >= coords.left && x <= coords.right &&
                                 y >= coords.top  && y <= coords.bottom )
                            {
                                // as opposed to a single (ACTION_DOWN and ACTION_UP)-triggered pointer, we now
                                // need to take the offset position of the UI element into account
                                a.handleTouchDown( x - a.getXPos(), y - a.getYPos(), aEvent );
                                setTouch( id, a );

                                wasHandled = true;
                            }
                        }
                    }
                }
            }
        }
        return wasHandled;
    }

    /**
     * query whether a specific pointer is linked
     * to a UI element Object
     *
     * @param aPointerId {int} pointer ID
     * @return {boolean}
     */
    public static boolean hasTouch( int aPointerId )
    {
        return _UIElements[ aPointerId ] != null;
    }

    /* private methods */

    /**
     * store a reference to a UI element
     * @param aPointerId   {int} the pointer ID
     * @param aUIElement {IInteractiveAnimation} the element to link to
     */
    private static void setTouch( int aPointerId, IInteractiveSprite aUIElement )
    {
        _UIElements[ aPointerId ] = aUIElement;
    }

    /**
     * break the reference to a UI element Object
     *
     * @param aPointerId {int} pointer ID
     * @param aEvent {MotionEvent} passed for touch up invocation
     */
    private static void removeTouch( int aPointerId, MotionEvent aEvent )
    {
        if ( hasTouch( aPointerId ))
        {
            _UIElements[ aPointerId ].handleTouchUp( aEvent );
            _UIElements[ aPointerId ] = null;
        }
    }

    private static void removeAllTouches()
    {
        int i = _UIElements.length;

        while ( i-- > 0 )
            _UIElements[ i ] = null;
    }

    private static int getAmountOfMappedTouches()
    {
        int out = 0;

        for ( int i = 0, l = _UIElements.length; i < l; ++i )
        {
            if ( _UIElements[ i ] != null )
                ++out;
        }
        return out;
    }
}

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
package nl.igorski.lib.managers;

import nl.igorski.lib.interfaces.IDestroyable;
import nl.igorski.lib.interfaces.view.IAnimateInable;
import nl.igorski.lib.interfaces.view.IAnimateOutable;
import nl.igorski.lib.interfaces.view.ICanvasViewSwitcher;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 02-03-13
 * Time: 14:43
 * To change this template use File | Settings | File Templates
 *
 * a CanvasViewManager is a simple method to control the viewstack
 * where "main view components" (i.e. a "page") can be queued and
 * transitioned when switching between them
 */
public final class CanvasViewManager
{
    public Object currentView;
    public Object queuedView;

    public boolean isSwitching;
    private ICanvasViewSwitcher _viewSwitcher;

    public CanvasViewManager( ICanvasViewSwitcher aViewSwitcher )
    {
        _viewSwitcher = aViewSwitcher;
        isSwitching   = false;
    }

    /* public methods */

    /**
     * invoke the view switching mechanism by appending a new view if the current view
     * implements the IAnimateOutable interface its animateOut-method is invoked before
     * completing the switch by appending the new view
     *
     * @param aNewView {Object} view to append, if null the current view is removed
     *        (optionally by animating out first) and no replacement is added
     */
    public boolean requestSwitch( Object aNewView )
    {
        if ( isSwitching )
            return false;

        queuedView = aNewView;

        if ( queuedView != null )
            isSwitching = true;

        if ( removeCurrentView())
            completeSwitch();

        return true;
    }

    /**
     * pops old view and adds the queued view, if the previous view
     * implemented the IAnimateOutable interface, this method must
     * be invoked by the ICanvasViewSwitcher in the "onViewRemoved"-method
     */
    public void completeSwitch()
    {
        if ( currentView != null )
        {
            if ( currentView instanceof IDestroyable )
                (( IDestroyable ) currentView ).destroy();
        }

        if ( queuedView != null )
        {
            currentView = queuedView;

            if ( currentView instanceof IAnimateInable )
            {
                (( IAnimateInable ) currentView ).animateIn( _viewSwitcher );
            }
        }
        else {
            currentView = null;
        }
        queuedView  = null;
        isSwitching = false;
    }

    /**
     * removes the current view, if it implements the IAnimateOutable interface
     * its animateOut method is invoked (and will in turn invoke "completeSwitch"
     * via the ICanvasViewSwitcher). If it implements the IDestroyable interface
     * it is destroyed
     *
     * @return {boolean} by default true to indicate the "completeSwitch"-method must
     *          be invoked by the CanvasViewManager, returns false in case the "completeSwitch"-
     *          method will be invoked via the ICanvasViewSwitcher after the current views
     *          animateOut sequenced has completed
     */
    public boolean removeCurrentView()
    {
        if ( currentView != null )
        {
            /*
            if ( currentView instanceof IAnimateOutable )
            {
                (( IAnimateOutable ) currentView ).animateOut( _viewSwitcher );
                return false;
            }
            */
            // NO! still buggy (fast switching leaves old views in place..)
            if ( currentView instanceof IAnimateOutable )
            {
                _viewSwitcher.onViewRemoved(( IAnimateOutable ) currentView );
                return false;
            }
        }
        return true;
    }

    public void removeCurrentView( Object aView )
    {
        if ( currentView == aView )
            removeCurrentView();
    }

    /**
     * removes all current and queued views
     * in case of application reset
     */
    public void reset()
    {
        if ( currentView != null )
        {
            if ( currentView instanceof IDestroyable )
                (( IDestroyable ) currentView ).destroy();

            currentView = null;
        }

        if ( queuedView != null )
        {
            if ( queuedView instanceof IDestroyable )
                (( IDestroyable ) queuedView ).destroy();

            queuedView = null;
        }
        isSwitching = false;
    }
}

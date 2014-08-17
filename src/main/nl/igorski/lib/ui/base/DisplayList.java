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
package nl.igorski.lib.ui.base;

import nl.igorski.lib.ui.interfaces.IDrawableContainer;
import nl.igorski.lib.ui.interfaces.IInteractiveSprite;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 22-08-12
 * Time: 21:08
 * To change this template use File | Settings | File Templates.
 *
 * A convenience class that contains a stack of IInteractiveSprites
 * and can move their offsets in bulk
 */
public class DisplayList
{
    protected int _offsetX;
    protected int _offsetY;
    protected Vector<IInteractiveSprite> _displayList = new Vector<IInteractiveSprite>();

    /* public methods */

    public Vector<IInteractiveSprite> getDisplayList()
    {
        return _displayList;
    }

    /**
     * when moving the "parent" container this is a convenience method
     * to move all child items on the DisplayList accordingly
     *
     * @param aDeltaX {int} the difference between the containers previous
     *                      X- and the current X-position
     * @param aDeltaY {int} the difference between the containers previous
     *                      Y- and the current Y-position
     */
    public void updateDisplayListBounds( int aDeltaX, int aDeltaY )
    {
        for ( final IInteractiveSprite displayObject : _displayList )
        {
            displayObject.setXPos( displayObject.getXPos() + aDeltaX );
            displayObject.setYPos( displayObject.getYPos() + aDeltaY );
        }
    }

    /* protected */

    protected void addToDisplayList( IInteractiveSprite aDisplayObject )
    {
        _displayList.add( aDisplayObject );
    }

    protected void addToDisplayList( IDrawableContainer aContainer )
    {
        for ( final IInteractiveSprite UIElement : aContainer.getDrawables())
            addToDisplayList( UIElement );
    }

    protected void removeFromDisplayList( IInteractiveSprite aDisplayObect )
    {
        _displayList.remove( aDisplayObect );
    }

    protected void clearDisplayList()
    {
        _displayList.clear();
    }
}

/**
 * The MIT License (MIT)
 *
 * PureMVC - Copyright © 2006-2012 Futurescale, Inc.
 * All rights reserved.
 *
 * Lightweight Android adaption by :
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
package nl.igorski.lib.framework.controller;

import nl.igorski.lib.framework.Core;
import nl.igorski.lib.framework.interfaces.ICommand;
import nl.igorski.lib.framework.interfaces.ICommandHandler;
import nl.igorski.lib.framework.interfaces.INotification;
import nl.igorski.lib.framework.interfaces.IProxy;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 03-07-12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 *
 * A base class for a command
 */
public class BaseSimpleCommand implements ICommand
{
    private ICommandHandler _completeHandler;

    public BaseSimpleCommand()
    {

    }

    /* public methods */

    public void execute( INotification aNotification )
    {
        /**
         * add command logic here, don't forget to
         * invoke super() for automatic calling of the complete handler
         */
        commandComplete();
    }

    public void setCompleteHandler( ICommandHandler completeHandler )
    {
        _completeHandler = completeHandler;
    }

    /* protected methods */

    protected void commandComplete()
    {
        if ( _completeHandler != null )
            _completeHandler.handleComplete( this );

        Core.unregisterCommand( this );
    }

    /**
     * quick getter for retrieving a proxy registered
     * in the framework Core
     *
     * @param aProxyName {String}
     * @return {IProxy}
     */
    protected IProxy getProxy( String aProxyName )
    {
        return Core.retrieveProxy( aProxyName );
    }
}

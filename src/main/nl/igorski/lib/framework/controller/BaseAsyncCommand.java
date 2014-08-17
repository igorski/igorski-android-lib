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
import nl.igorski.lib.framework.interfaces.IAsyncCommand;
import nl.igorski.lib.framework.interfaces.IAsyncCommandHandler;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 03-07-12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 *
 * A base class for asynchronous commands (requires manual invocation
 * of cancellation or completion when the asynchronous methods have finished)
 */
public class BaseAsyncCommand extends BaseSimpleCommand implements IAsyncCommand
{
    private IAsyncCommandHandler _cancelHandler;

    public BaseAsyncCommand( )
    {
        super();
    }

    /* public methods */

    public void execute()
    {
        throw new Error( "method 'execute' not overridden in ICommand implementation" );
    }

    public void setCancelHandler( IAsyncCommandHandler cancelHandler )
    {
        _cancelHandler = cancelHandler;
    }

    /* protected methods */

    protected void commandCancel()
    {
        if ( _cancelHandler != null )
            _cancelHandler.handleCancel( this );

        Core.unregisterCommand( this );
    }
}

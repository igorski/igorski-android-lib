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
import nl.igorski.lib.framework.interfaces.*;
import nl.igorski.lib.utils.debugging.DebugTool;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 03-07-12
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 *
 * A base class to register multiple asynchronous commands that are
 * executed in order, once the previous command has completed it's
 * asynchronous execution (by invoking commandComplete)
 */
public class BaseAsyncMacroCommand implements IMacroCommand, IAsyncCommandHandler
{
    private Vector<ICommand> _subCommands;
    private INotification _note;

    public BaseAsyncMacroCommand()
    {
        initializeMacroCommand();
    }

    /* public methods */

    public void initializeMacroCommand()
    {
        // add subcommands here in your subclass
        //addSubCommand( ... );
    }

     // do not override
    public void execute( INotification aNotification )
    {
        _note = aNotification; // hold reference to the Notification

        nextCommand();         // process the command queue
    }

    public void setCompleteHandler( ICommandHandler completeHandler )
    {
        // unused in a macro chain... as "chainComplete" is invoked
    }

    /* event handlers */

    /* listen for individual sub command completes and cancels */

    public void handleComplete( ICommand commandRef )
    {
        nextCommand();
    }

    // you may override the cancel handler for specific uses, don't forget to invoke super!
    // TODO: do we want to map a cancel command to a cancelled subcommand ?
    public void handleCancel( IAsyncCommand commandRef )
    {
        // cancel the remaining chain
        chainCancelled();
    }

    /* protected methods */

    protected void addSubCommand( ICommand commandRef )
    {
        if ( _subCommands == null )
            _subCommands = new Vector<ICommand>();

        _subCommands.add( commandRef );

        if ( commandRef instanceof IAsyncCommand )
        {
            (( IAsyncCommand ) commandRef ).setCancelHandler( this );
        }
        commandRef.setCompleteHandler( this );
    }

    /* private methods */

    private void nextCommand()
    {
        // no (more) subcommands in the queue ? we're finished
        if ( _subCommands == null || _subCommands.size() == 0 )
        {
            chainComplete();
        }
        else
        {
            // get the next command in line
            ICommand commandClass = _subCommands.get( 0 );
            _subCommands.remove( commandClass );

            commandClass.execute( _note );
        }
    }

    private void chainComplete()
    {
        DebugTool.log( "CHAIN COMPLETED" );
        Core.unregisterCommand( this );
    }

    /**
     * as a MacroCommand can have multiple sub commands
     * it is better to specify cancel handlers in each
     * subcommand for more precise error reporting, as
     * such a cancel of the chain occurs when commandCancel
     * has been invoked on a subcommand, it merely unregisters
     * this macro command from the Core
     */
    private void chainCancelled()
    {
        DebugTool.log( "CHAIN CANCELLED" );
        Core.unregisterCommand( this );
    }
}

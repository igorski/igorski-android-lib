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
package nl.igorski.lib.framework.interfaces;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 03-07-12
 * Time: 20:12
 * To change this template use File | Settings | File Templates.
 */
public interface ICommand
{
    /**
     * commands are fired by the Core invoking the execute method
     *
     * @param {INotification} notification
     */
    public void execute( INotification aNotification );

    /**
     * when commands are part of a chain (MacroCommand), a
     * complete handler is passed to inform the MacroCommand
     * the next command in the queue can be executed
     *
     * @param handler {ICommandHandler}
     */
    public void setCompleteHandler( ICommandHandler handler );
}

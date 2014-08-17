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
package nl.igorski.lib.utils.storage.database.definitions;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 4/18/12
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public final class DBColumn
{
    /**
     * DBColumn describes the properties of a single
     * table column, used as part of a DBColumn[] Array
     * this can be used by the DataBase class to map
     * the contents of a table
     */
    public String COLUMN_NAME;
    public String TYPE;
    public String PROPERTY;

    /**
     * @param aColumnName  {String} name of the column
     * @param aColumnType  {String} required, the column type ( "INTEGER", "TEXT", etc. )
     * @param aProperty    {String} optional property such as "PRIMARY KEY" or "INDEX"
     *                     can also be used to chain properties and restrictions such
     *                     as : "INDEX NOT NULL" as long as it is separated by a space
     *                     see SQL definition
     */
    public DBColumn( String aColumnName, String aColumnType, String aProperty )
    {
        COLUMN_NAME = aColumnName;
        TYPE        = aColumnType;
        PROPERTY    = aProperty;
    }

    public DBColumn( String aColumnName, String aColumnType )
    {
        COLUMN_NAME = aColumnName;
        TYPE        = aColumnType;
    }
}

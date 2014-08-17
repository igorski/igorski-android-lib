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
package nl.igorski.lib.utils.storage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import nl.igorski.lib.utils.storage.database.definitions.DBColumn;
import nl.igorski.lib.utils.storage.database.interfaces.IEntity;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 4/18/12
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 *
 * DataBase provides a convenience API for managing a SQLite table
 * using a simplified ORM system
 */
public class DataBase extends SQLiteOpenHelper
{
    protected Context _context;
    protected SQLiteDatabase _db;
    protected String _table;

    private String _lastException;

    /**
     * open an instance of a DataBase Object, the extension of
     * this class should be a model
     *
     * @param context          {Context}
     * @param aTable           {String} name of the table the model operates on
     * @param aDataBaseName    {String} name of the database to connect
     * @param aDataBaseVersion {int} version of the database
     */
    public DataBase( Context context, String aTable, String aDataBaseName, int aDataBaseVersion )
    {
        super( context, aDataBaseName, null, aDataBaseVersion );

        _context = context;
        _table   = aTable;

        try
        {
            getWritableDatabase(); // invokes the onCreate method
        }
        catch( SQLiteException e )
        {
            Log.d( "SYNTH", "Exception thrown attempting to access writeableDatabase" );
        }
    }

    /* public methods */

    /**
     * count the amount of available entities available
     * in the database
     * @return {int}
     */
    public int count()
    {
        String sql = "SELECT * FROM " + this._table;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery( sql, null ).getCount();
    }

    public boolean createTable( String tableName, DBColumn[] columns )
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (";

        for ( int i = 0, l = columns.length; i < l; ++i )
        {
            final DBColumn col = columns[ i ];

            sql += createSQLFromDBColumn( col, false );

            if ( i < ( l - 1 ))
                sql += ", ";
        }
        sql += ");";

        return query( sql );
    }

    // TODO: updateTable

    public boolean dropTable( String tableName )
    {
        return query( "DROP TABLE IF EXISTS " + tableName );
    }

    /**
     * when a transaction returned false, the last
     * error has been caught and formatted as text
     * @return {String}
     */
    public String getLastError()
    {
        return _lastException;
    }

    public Context getContext()
    {
        return _context;
    }

    /* inherited */

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        _db = db;
        /* in your subclass' extensions it is likely
           that you execute a createTable query here */
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
       // Logs that the database is being upgraded
//       Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//               + newVersion + ", which will destroy all old data");

       // TODO: migrate or else we lose all old data!

//       // Kills the table and existing data
//       db.execSQL("DROP TABLE IF EXISTS notes");
//
//       // Recreates the database with a new version
//       onCreate( db );
    }

    /* protected methods */

    /**
     * to be called by the methods in the subclass, formatting
     * the models Entity into a ContentValues object
     *
     * @param values {ContentValues}
     * @return {int} the insert ID
     */
    protected int insertRow( ContentValues values )
    {
        SQLiteDatabase db;

        try {
            db = this.getWritableDatabase();
        }
        catch ( SQLiteException e )
        {
            return -1;
        }

        long id = db.insert( this._table, null, values );
        db.close();

        return ( int ) id;
    }

    /**
     * to be called by the methods in the subclass, to be
     * used to update a single row / entity in the table
     * this assumes a primary key named "id" is present as a
     * column in the table !
     *
     * @param id {int} id of the row in the database
     * @param values {ContentValues} the row data with updated values
     * @return {boolean} success
     */
    protected boolean updateRow( int id, ContentValues values )
    {
        SQLiteDatabase db;

        try {
            db = this.getWritableDatabase();
        }
        catch ( SQLiteException e )
        {
            return false;
        }

        int success = db.update( this._table, values, "id = ?", new String[] { String.valueOf( id ) });
        db.close();

        return ( success > 0 );
    }

    /**
     * to be called by the methods in the subclass, to be
     * used to delete a single row / entity in the table
     * this assumes a primary key named "id" is present as a
     * column in the table !
     *
     * @param id {int} id of the row in the database
     * @return {boolean} success
     */
    protected boolean deleteRow( int id )
    {
        SQLiteDatabase db;

        try {
            db = this.getWritableDatabase();
        }
        catch ( SQLiteException e )
        {
            return false;
        }

        int success = db.delete( this._table, "id = ?", new String[] { String.valueOf( id ) });
        db.close();

        return ( success > 0 );
    }

    /**
     * same as "deleteRow", only now a different column
     * other than the primary key can be specified for removal
     * @param aColumnName {String} column name to select on
     * @param aValue      {String} the columns value
     * @return {Cursor} a Database Cursor instance containing the result
     */
    protected boolean deleteByColumn( String aColumnName, String aValue )
    {
        SQLiteDatabase db;

        try {
            db = this.getWritableDatabase();
        }
        catch ( SQLiteException e )
        {
            return false;
        }

        int success = db.delete( this._table, aColumnName + " = ?", new String[] { aValue });
        db.close();

        return ( success > 0 );
    }

    /**
     * to be used by the subclass, this selects a single item
     * by its unique id, this assumes a primary key named "id" is present
     * as a column in the table!
     *
     * @param id {int} id of the row in the database
     * @return {Cursor}  a Database Cursor instance containing the result
     */
    protected Cursor getRowById( int id )
    {
        SQLiteDatabase db;

        try {
            db = this.getWritableDatabase();
        }
        catch ( SQLiteException e )
        {
            return null;
        }

        Cursor cursor = db.query( this._table, new String[] { "*" }, "id = ?",
                                  new String[] { String.valueOf( id ) }, null, null, null, null );

        if ( cursor != null && cursor.getCount() > 0 )
            return cursor;
        else
            return null;
        /**
         * the returned Cursor can be cast to the model entity
         * by using the overriden "parseEntity"-method, for example:
         *
         * if ( cursor != null )
         *   cursor.moveToFirst();
         *
         * return ( {ENTITY_CLASS} ) parseEntity( cursor );
         */
    }

    /**
     * same as "getRowById", only now a different column
     * other than the primary key can be specified for retrieval
     *
     * @param aColumnName {String} column name to select on
     * @param aValue      {String} the columns value
     * @return {Cursor} a Database Cursor instance containing the result
     */
    protected Cursor getRowByColumn( String aColumnName, String aValue )
    {
        SQLiteDatabase db;

        try {
            db = this.getWritableDatabase();
        }
        catch ( SQLiteException e )
        {
            return null;
        }

        Cursor cursor = db.query( this._table, new String[] { "*" }, aColumnName + " = ?",
                                  new String[] { aValue }, null, null, null, null );

        if ( cursor != null && cursor.getCount() > 0 )
            return cursor;
        else
            return null;
    }

    /**
     * to be used by the subclass, this selects all items available
     * in the database, with pagination and conditions
     *
     * @param pageNo     {int} the current page number
     * @param amount     {int} the amount of items to be displayed on each page
     * @param conditions {String} optional conditions constructing a WHERE clause
     *                   for instance : "WHERE visible = 1 AND created < 1335010787"
     * @return {Cursor}  a Database Cursor instance containing the result
     */
    protected Cursor selectAll( int pageNo, int amount, String conditions )
    {
        SQLiteDatabase db;

        try {
            db = this.getWritableDatabase();
        }
        catch ( SQLiteException e )
        {
            return null;
        }

        String sql = "SELECT * FROM " + this._table + " ";
        sql      += conditions + " ";
        sql      += "LIMIT " + ( pageNo * amount ) + ", " + amount;

        return db.rawQuery( sql, null );

        /**
         * the returned cursor can be looped in your subclass
         * casting each result to the model entity by using
         * the overridden "parseEntity"-method, for example:
         * if ( cursor.moveToFirst())
         * {
         *     do
         *     {
         *         songList.add( ( VOSong ) parseEntity( cursor ));
         *     }
         *     while ( cursor.moveToNext());
         * }
         */
    }

    protected IEntity parseEntity( Cursor cursor )
    {
        class tmp implements IEntity
        {
            public String getType()
            {
                return "this is an example";
            }
            // each extension of this database Object
            // should be a model with their own Entity
            // results from getById, getList will all
            // parse their objects using this overridden method
        }
        return new tmp();
    }

    /* private methods */

    /**
     * executes a database transaction
     * @param  sql {String} the SQL statement to execute
     * @return {boolean} whether transaction completed successfully
     */
    private boolean query( String sql )
    {
        try
        {
            _db.execSQL( sql );
        }
        catch ( SQLException e )
        {
            formatException( e );
            return false;
        }
        return true;
    }

    /**
     * adds a column to a table
     * @param tableName         {String} name of the table to operate on
     * @param columnDescription {DBColumn} definition of the column to add
     *
     * @return {String}
     */
    private boolean addField( String tableName, DBColumn columnDescription )
    {
        String sql = "ALTER TABLE " + tableName + " ADD " + this.createSQLFromDBColumn( columnDescription, true );
        return query( sql );
    }

    /**
     * build the column definition for add / alter queries
     * @param columnDescription {DBColumn} a column as defined in a fields Array from the Tables class
     * @param isUpdate          {boolean} whether we're creating a update statement to an existing table
     *
     * @return {String}
     */
    private String createSQLFromDBColumn( DBColumn columnDescription, boolean isUpdate )
    {
        String sql = "";
        sql += columnDescription.COLUMN_NAME + " " + columnDescription.TYPE;

        if ( columnDescription.PROPERTY != null )
        {
            /**
             * if we're updating an existing table and the column
             * has a NOT NULL definition, we must add a default value
             * to be applied on the existing rows in the table
             */
            if ( columnDescription.PROPERTY.contains( "NOT NULL" ) && isUpdate )
            {
                sql += " default '' " + columnDescription.PROPERTY;
            }
            else {
                sql += " " + columnDescription.PROPERTY;
            }
        }
        return sql;
    }

    /**
     * whenever the execute statement catches an
     * error, it is passed through this function
     * @param e {SQLException}
     * @return {String}
     */
    private void formatException( SQLException e )
    {
        // TODO: generate IDs for queries and
        // store these in a stack ?

        _lastException = e.getMessage();
    }
}

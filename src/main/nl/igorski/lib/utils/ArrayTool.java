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
package nl.igorski.lib.utils;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 4/16/12
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */
public final class ArrayTool
{
    /* reverse array order */

    public static int[] reverse( int[] data )
    {
        for ( int left = 0, right = data.length - 1; left < right; left++, right-- )
        {
            // swap the values at the left and right indices
            int temp      = data[ left ];
            data[ left ]  = data[ right ];
            data[ right ] = temp;
        }
        return data;
    }

    public static double[] reverse( double[] data )
    {
        for ( int left = 0, right = data.length - 1; left < right; left++, right-- )
        {
            // swap the values at the left and right indices
            double temp   = data[ left ];
            data[ left ]  = data[ right ];
            data[ right ] = temp;
        }
        return data;
    }

    public static String[] reverse( String[] data )
    {
        for ( int left = 0, right = data.length - 1; left < right; left++, right-- )
        {
            // swap the values at the left and right indices
            String temp   = data[ left ];
            data[ left ]  = data[ right ];
            data[ right ] = temp;
        }
        return data;
    }

    /* push an item */

    public static int[] push( int[] array, int pushValue )
    {
        int[] longer = new int[ array.length + 1 ];
        System.arraycopy( array, 0, longer, 0, array.length );
        longer[ array.length ] = pushValue;

        return longer;
    }

    /* push an item only if it's value didn't exist in the Array */

    public static int[] pushUnique( int[] array, int pushValue )
    {
        for ( final int i : array )
        {
            if ( i == pushValue )
                return array;
        }
        int[] longer = new int[ array.length + 1 ];
        System.arraycopy( array, 0, longer, 0, array.length );
        longer[ array.length ] = pushValue;

        return longer;
    }

    /**
     * @param array {int[]} the array to work on
     * @param key   {int} the value of the array key
     * @return {int[]} array without the given key
     */
    public static int[] splice( int[] array, int key )
    {
        int[] shorter = new int[ array.length - 1 ];

        for ( int i = 0, l = array.length, w = 0; i < l; ++i )
        {
            if ( array[ i ] != key )
            {
                shorter[ w ] = array[ i ];
                ++w;
            }
        }
        return shorter;
    }

    public static long average( long[] array )
    {
        long result = 0;

         for ( final long i : array )
           result += i;

        return result / array.length;
    }

    /**
     * removes an item by reference from an Array
     *
     * @param {Object[]} array
     * @param {Object} item
     * @return {Object[]} the original Array without given item
     */
    public static Object[] removeItem( Object[] array, Object item )
    {
        final int itemIndex = getIndexOf( array, item );

        if ( itemIndex == -1 )
            return array;

        Object[] out = new Object[ array.length - 1 ];

        for ( int i = 0, l = array.length, w = 0; i < l; ++i )
        {
            if ( array[ i ] == item )
            {
                if ( array[ i ] != item )
                {
                    out[ w ] = array[ i ];
                    ++w;
                }
            }
        }
        return out;
    }

    public static boolean hasItem( Object[] array, Object item )
    {
        int i = array.length;

        while ( i-- > 0 )
        {
            if ( array[ i ] == item )
                return true;
        }
        return false;
    }

    /**
     * get the index of given item in given array
     * if the item isn't present in the Array -1 is returned
     *
     * @param {Object[]} array
     * @param {Object} item
     * @return {int}
     */
    public static int getIndexOf( Object[] array, Object item )
    {
        int i = array.length;

        while ( i-- > 0 )
        {
            if ( array[ i ] == item )
                return i;
        }
        return -1;
    }
}

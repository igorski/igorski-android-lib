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
 * Date: 24-09-12
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public final class StringUtil
{
    private static int uid = 0;

    public static String addLeadingZero( String aString, int aAmountOfZeros )
    {
        final int stringLength = aString.length();

        if ( stringLength < aAmountOfZeros )
        {
            for ( int i = 0, l = aAmountOfZeros - stringLength; i < l; ++i ) {
                aString = "0" + aString;
            }
        }
        return aString;
    }

    public static int[] parseNumericString( String aString )
    {
        int[] out = new int[]{};
        int i = -1;

        String[] strings = aString.split( "," );

        for ( final String entry : strings )
            out[ ++i ] = TypeConverter.stringToInt( entry );

        return out;
    }

    /**
     * generate a unique String identifier
     * @return {String}
     */
    public static String generateUnique()
    {
        return "UID" + ( ++uid );
    }
}

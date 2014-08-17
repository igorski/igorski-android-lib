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
package nl.igorski.lib.utils.math;

import nl.igorski.lib.utils.TypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User  igorzinken
 * Date  4/12/12
 * Time  1 01 PM
 * To change this template use File | Settings | File Templates.
 *
 * convenience methods for mathematic functions
 */
public final class MathTool
{
    /**
     * @method rand
     *
     * @param low  {int} lowest desired value
     * @param high {int} highest desired value
     *
     * @return {number} a random number within a given range
     */
    public static int rand( int low, int high )
    {
        return TypeConverter.longToInt( Math.round( Math.random() * ( high - low )) + low );
    }

    /**
     * @method scale
     * scales a value against a scale different to the one "value" stems from
     * i.e. a UI slider with a value 0 - 100 ( percent ) to match against a
     * scale with a maximum value of 255
     *
     * @param value           {double} value to get scaled to
     * @param maxValue        {double} the maximum value we are likely to expect for param value
     * @param maxCompareValue {double} the maximum value in the scale we're matching against
     *
     * @return double
     */
    public static double scale( double value, double maxValue, double maxCompareValue )
    {
        double ratio = maxCompareValue / maxValue;
        return value * ratio;
    }

    /**
     * @method deg2rad
     * translates a value in degrees to radians
     *
     * @param deg {double} value in degrees
     * @eturn {double}
     */
    public static double deg2rad( double deg )
    {
        return deg / ( 180 / Math.PI );
    }

    /**
     * @method rad2deg
     * translates a value in radians to degrees
     *
     * @param rad {double} value in radians
     * @return {double}
     */
    public static double rad2deg( double rad )
    {
        return rad / ( Math.PI / 180 );
    }

    /**
     *  faster alternatives to base Math methods
     *  abs and max are faster as there is no NaN handling
     */

    // provide a faster abs, no NaN handling
    public final static double abs( final double x )
    {
        return x < 0 ? -x : x;
    }

    // provide a faster max, no NaN handling
    public final static double max( final double a, final double b )
    {
        return a > b ? a : b;
    }
}

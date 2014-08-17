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
package nl.igorski.lib.utils.ui;

import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created with IntelliJ IDEA.
 * User: igorzinken
 * Date: 15-05-14
 * Time: 23:35
 * To change this template use File | Settings | File Templates.
 *
 * ViewTool contains convenience functions to invoke operations
 * that differ across Android versions to maintain a single interface
 */
public final class ViewTool
{
    public static void setAlpha( View v, float alpha )
    {
        if ( Build.VERSION.SDK_INT < 11 )
        {
            final AlphaAnimation animation = new AlphaAnimation( alpha, alpha );
            animation.setDuration ( 0 );
            animation.setFillAfter( true );
            v.startAnimation( animation );
        }
        else {
            v.setAlpha( alpha );
        }
    }
}

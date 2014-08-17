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
package nl.igorski.lib.utils.resources;

import android.content.res.Resources;
import nl.igorski.lib.utils.storage.FileSystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 06-01-13
 * Time: 23:32
 * To change this template use File | Settings | File Templates.
 *
 * ResourceManager is a convenience class to copy packaged resources
 * from the application onto the filesystem (for instance for manipulation
 * of their contents)
 */
public final class ResourceManager
{
    public static boolean copyResource( Resources aResources, int aResourceId, String aOutputFilePath )
    {
        InputStream in = aResources.openRawResource( aResourceId );
        FileOutputStream out;

        if ( !FileSystem.exists( aOutputFilePath ))
            FileSystem.open( aOutputFilePath );

        try
        {
            out = new FileOutputStream( aOutputFilePath );
        }
        catch ( FileNotFoundException e )
        {
            return false;
        }
        byte[] buff = new byte[ 1024 ];
        int read = 0;

        try
        {
           while (( read = in.read( buff )) > 0 )
           {
              out.write(buff, 0, read);
           }
        }
        catch ( IOException e )
        {
            return false;
        }
        finally
        {
            try
            {
                in.close();
                out.close();
            }
            catch ( IOException e ) {}
        }
        return true;
    }
}

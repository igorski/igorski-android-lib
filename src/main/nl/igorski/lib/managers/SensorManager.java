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
package nl.igorski.lib.managers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import nl.igorski.lib.interfaces.listeners.ISensorChangeListener;
import nl.igorski.lib.utils.debugging.DebugTool;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 4/27/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SensorManager implements SensorEventListener
{
    private android.hardware.SensorManager sensorManager;

    private double ax, ay, az;

    private ISensorChangeListener _listener;

    public SensorManager( Context aContext )
    {
        sensorManager = ( android.hardware.SensorManager) aContext.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener( this, sensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER ),
                                        android.hardware.SensorManager.SENSOR_DELAY_NORMAL );
    }

    /* public */

    public void addListener( ISensorChangeListener listener )
    {
        _listener = listener;
    }

    public void onAccuracyChanged( Sensor arg0, int arg1 )
    {

    }

    public void onSensorChanged( SensorEvent event )
    {
        if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
        {
            ax = event.values[ 0 ];
            ay = event.values[ 1 ];
            az = event.values[ 2 ];

            DebugTool.log( "x" + ax + " y " + ay + "z " + az );
        }
    }
}

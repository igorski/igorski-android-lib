package nl.igorski.lib.interfaces.listeners;

import android.content.Intent;

/**
 * Created by IntelliJ IDEA.
 * User: igorzinken
 * Date: 22-04-12
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public interface IActivityResultListener
{
    public abstract void handleActivityResult( int requestCode, int resultCode, Intent data );

    public abstract int getRequestCodeInterest();   // should return the id of the request code the listener is interested in
}

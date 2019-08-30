package wholesalefactory.co.model;

import android.app.Application;
import wholesalefactory.co.pref.Globals;

public class wholesaleApplication extends Application {

    private static wholesaleApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Globals.init(this);

    }
    public static synchronized wholesaleApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
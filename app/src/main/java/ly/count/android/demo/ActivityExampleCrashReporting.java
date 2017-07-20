package ly.count.android.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import junit.framework.Assert;

import ly.count.android.sdk.Countly;

public class ActivityExampleCrashReporting extends Activity {
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_crash_reporting);
        Countly.onCreate(this);

    }

    public void onClickCrashReporting01(View v) {
        Countly.sharedInstance().addCrashLog("Unrecognized selector crash");
    }

    public void onClickCrashReporting02(View v) {
        Countly.sharedInstance().addCrashLog("Out of bounds crash");
        int[] data = new int[]{};
        data[0] = 9;
    }

    public void onClickCrashReporting03(View v) {
        Countly.sharedInstance().addCrashLog("Null pointer crash");
        Countly.sharedInstance().crashTest(3);
    }

    public void onClickCrashReporting04(View v) {
        Countly.sharedInstance().addCrashLog("Invalid Geometry crash");
    }

    public void onClickCrashReporting05(View v) {
        Countly.sharedInstance().addCrashLog("Assert fail crash");
        Assert.assertEquals(1, 0);
    }

    public void onClickCrashReporting06(View v) {
        Countly.sharedInstance().addCrashLog("Kill process crash");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void onClickCrashReporting07(View v) {
        Countly.sharedInstance().addCrashLog("Custom crash log crash");
        Countly.sharedInstance().addCrashLog("Adding some custom crash log");
        Countly.sharedInstance().crashTest(2);
    }

    public void onClickCrashReporting08(View v) {
        Countly.sharedInstance().addCrashLog("Recording handled exception 1");
        Countly.sharedInstance().logException(new Exception("A logged exception"));
        Countly.sharedInstance().addCrashLog("Recording handled exception 3");
    }

    public void onClickCrashReporting09(View v) throws Exception {
        Countly.sharedInstance().addCrashLog("Unhandled exception info");
        throw new Exception("A unhandled uxception");
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Countly.sharedInstance().onStart(this);
    }

    @Override
    public void onStop()
    {
        Countly.sharedInstance().onStop();
        super.onStop();
    }
}

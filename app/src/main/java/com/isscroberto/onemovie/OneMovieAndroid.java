package com.isscroberto.onemovie;

import android.app.Application;

import com.github.stkent.amplify.feedback.DefaultEmailFeedbackCollector;
import com.github.stkent.amplify.feedback.GooglePlayStoreFeedbackCollector;
import com.github.stkent.amplify.tracking.Amplify;

/**
 * Created by roberto.orozco on 18/11/2017.
 */

public class OneMovieAndroid extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Feedback.
        Amplify.initSharedInstance(this)
                .setPositiveFeedbackCollectors(new GooglePlayStoreFeedbackCollector())
                .setCriticalFeedbackCollectors(new DefaultEmailFeedbackCollector(getString(R.string.my_email)))
                .applyAllDefaultRules();
        //.setAlwaysShow(BuildConfig.DEBUG);

        // Force a crash
        //throw new RuntimeException("Test Crash");
    }

}

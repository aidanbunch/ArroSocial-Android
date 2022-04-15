package com.aidanbunch.arrosocial.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TransitionAdapter;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aidanbunch.arrosocial.R;
import com.aidanbunch.arrosocial.utils.Constants;
import com.aidanbunch.arrosocial.utils.SharedPrefs;
import com.aidanbunch.arrosocial.view.welcome.WelcomeViewActivity;
import com.aidanbunch.arrosocial.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    SplashViewModel splashVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashVM = new ViewModelProvider(this).get(SplashViewModel.class);

        SharedPrefs.instance(this.getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        hideToolBar();

        MotionLayout splashLayout = (MotionLayout) findViewById(R.id.splashLayoutParent);

        splashLayout.setTransitionListener(new TransitionAdapter() {
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
            }
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {

                int colorFrom = getResources().getColor(Constants.AppColors.purple);
                int colorTo = getResources().getColor(Constants.AppColors.off_white);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(400);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        motionLayout.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
                if (SharedPrefs.instance().fetchValueString(Constants.FSUserData.username) == null) {
                    startActivity(new Intent(SplashActivity.this, WelcomeViewActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    Log.d("IMPORTANT", SharedPrefs.instance().fetchValueString(Constants.FSUserData.username));
                    startActivity(new Intent(SplashActivity.this, CentralActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
            }
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
            }
        });
    }

    public void hideToolBar(){

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)

    }
}
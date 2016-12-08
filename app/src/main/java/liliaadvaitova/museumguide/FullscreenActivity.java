package liliaadvaitova.museumguide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.net.*;
import java.util.*;
import com.illposed.osc.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private Button ButtonMap; // four buttons from general screen
    private Button ButtonSlides1;
    private Button ButtonSlides2;
    private Button ButtonGuideMode;

    /* These two variables hold the IP address and port number.
       You should change them to the appropriate address and port.
    */
    String IP = "12.34.56.78";
    int Port = 12345;
    OSCPortOut oscPortOut; // This is used to send messages


    private //void SendOSCMessage(/*String IP, int Port, OSCPortOut oscPortOut, */String message){
        /* These two variables hold the IP address and port number.
        * You should change them to the appropriate address and port.
        */
        /*String IP = "12.34.56.78";
        int Port = 12345;
        OSCPortOut oscPortOut; // This is used to send messages
        */
        Thread oscThread = new Thread() {
            @Override
            public void run() {

                try {
                    // Connect to some IP address and port
                    oscPortOut = new OSCPortOut(InetAddress.getByName(IP), Port);
                } catch(UnknownHostException e) {
                    // Error handling when your IP isn't found
                    return;
                } catch(Exception e) {
                    // Error handling for any other errors
                    return;
                }

                if (oscPortOut != null) {
                    // Creating the message
                    Object[] thingsToSend = new Object[3];
                    thingsToSend[0] = "Hello World";
                    thingsToSend[1] = 12345;
                    thingsToSend[2] = 1.2345;
                    OSCMessage message = new OSCMessage(IP, Arrays.asList(thingsToSend));
                    // OSCMessage message = new OSCMessage(IP, thingsToSend);
                    ArrayList<Object> moreThingsToSend = new ArrayList<Object>();
                    moreThingsToSend.add("Hello World2");
                    moreThingsToSend.add(123456);
                    moreThingsToSend.add(12.345);

                    OSCMessage message2 = new OSCMessage(IP, moreThingsToSend);
                    //OSCMessage message2 = new OSCMessage(myIP, moreThingsToSend.toArray());

                    try {
                        // Send the messages
                        oscPortOut.send(message);
                        oscPortOut.send(message2);

                        // Pause for half a second
                        //sleep(500);
                    } catch (Exception e) {
                        // Error handling for some error
                    }
                }
            }

        };
    //}



    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        ButtonMap = (Button) findViewById(R.id.button);
        ButtonSlides1 = (Button) findViewById(R.id.button2);
        ButtonSlides2 = (Button) findViewById(R.id.button3);
        ButtonGuideMode = (Button) findViewById(R.id.button4);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        ButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //oscThread.start();
                Intent myInt = new Intent(FullscreenActivity.this, FullscreenActivityMap.class);
                //myInt.putExtra("key", value);
                FullscreenActivity.this.startActivity(myInt);
            }
        });

        ButtonSlides1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //oscThread.start();
                Intent myInt = new Intent(FullscreenActivity.this, FullscreenActivitySlides1.class);
                //myInt.putExtra("key", value);
                FullscreenActivity.this.startActivity(myInt);
            }
        });

        ButtonSlides2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //oscThread.start();
                Intent myInt = new Intent(FullscreenActivity.this, FullscreenActivitySlides2.class);
                //myInt.putExtra("key", value);
                FullscreenActivity.this.startActivity(myInt);
            }
        });

        ButtonGuideMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //oscThread.start();
                Intent myInt = new Intent(FullscreenActivity.this, FullscreenActivityGuideMode.class);
                //myInt.putExtra("key", value);
                FullscreenActivity.this.startActivity(myInt);
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}

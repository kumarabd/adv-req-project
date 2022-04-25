package com.devcometvisionapp;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextToSpeech tts;

    private TextView textView;

    ConstraintLayout constraintLayout;
    SwipeListener swipeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request Permission from User
//        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        // Assign Variable
        constraintLayout = findViewById(R.id.constraint_layout);
        textView = findViewById(R.id.system_text);

        // Initialize swipe listener
        swipeListener = new SwipeListener(constraintLayout);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported");
                    } else {

                        tts.speak("Welcome to Comet Vision App. " +
                                "Swipe Right to Start Navigation. " +
                                "Swipe Left for Settings Menu. " +
                                "Swipe Up for Emergency. " +
                                "Swipe down to Exit.",
                                TextToSpeech.QUEUE_FLUSH, null, "WELCOME");
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String s) {

                            }

                            @Override
                            public void onDone(String s) {
                                if (s.contains("WELCOME")){
//                                    System.out.println("Running startListening...");
//                                    speechRecognizer.startListening(intentRecognizer);
//                                    speechRecognizer.stopListening();
                                }
                            }

                            @Override
                            public void onError(String s) {

                            }
                        });
                    }
                } else {
                    Log.e("TTS", "onInit: Initialization failed" );
                }
            }
        });

        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> userInput = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string = "";

                if (userInput != null) {
                    string = userInput.get(0);
                    System.out.println(string);
                    textView.setText(string);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

//        System.out.println("Running startListening...");
//        speechRecognizer.startListening(intentRecognizer);
//        speechRecognizer.stopListening();
    }


    private class SwipeListener implements View.OnTouchListener {
        // Initialize Variable
        GestureDetector gestureDetector;

        // Create Constructor
        SwipeListener(View view) {
            // Initialize threshold value
            int threshold = 100;
            int velocity_threshold = 100;

            // Initialize simple gesture listener
            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener(){
                        @Override
                        public boolean onDown(MotionEvent e) {
                            // Pass true value
                            return true;
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            // Get x and y difference
                            float xDiff = e2.getX() - e1.getX();
                            float yDiff = e2.getY() - e1.getY();
                            try {
                                // Check condition
                                if (Math.abs(xDiff) > Math.abs(yDiff)){
                                    // When x is greater than y
                                    if (Math.abs(xDiff) > threshold &&
                                    Math.abs(velocityX) > velocity_threshold) {
                                        // When x diff is greater than threshold
                                        // When x velocity is greater than velocity threshold
                                        if (xDiff > 0) {
                                            // When swipe right
                                            Intent i = new Intent(MainActivity.this, StartNavigation.class);
                                            startActivity(i);
                                        } else {
                                            // When swipe left
                                            Intent i = new Intent(MainActivity.this, Settings.class);
                                            startActivity(i);
                                        }
                                        return true;
                                    }
                                } else {
                                    // When y is greater than x
                                    if (Math.abs(yDiff) > threshold &&
                                    Math.abs(velocityY) > velocity_threshold) {
                                        // When y diff is greater than threshold
                                        // When y velocity is greater than velocity threshold
                                        if (yDiff < 0) {
                                            // When swipe up
                                            Intent i = new Intent(MainActivity.this, Emergency.class);
                                            startActivity(i);
                                        } else {
                                            finish();
                                        }
                                        // Ignoring Swipe Down case
                                        return true;
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };

            // Initialize gesture detector
            gestureDetector = new GestureDetector(listener);

            // Set listener on view
            view.setOnTouchListener(this);
        }



        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // Return gesture event
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }
}
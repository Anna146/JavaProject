package com.example.asus.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Radius extends Activity {
        Intent answerIntent = new Intent();
        public final static String dist = "10";
        private EditText radText;

        @Override
        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);

            Button confirmButton = (Button) findViewById(R.id.edit_button);
            radText = (EditText) findViewById(R.id.textViewRad);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    answerIntent.putExtra(dist, Integer.parseInt(radText.getText().toString()));
                    setResult(2, answerIntent);
                    finish();
                }

            });
        }


        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        protected void onPause() {
            super.onPause();
        }

        @Override
        protected void onResume() {
            super.onResume();
        }
    }

package com.example.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        // sets the title of the current activity (aka screen)
        getSupportActionBar().setTitle("Edit item");

        // obtains the text from the MainActivity to set as the default value
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // called when the user clicks the button to update their item
        // update values using Intents again
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // create intent with the results
                Intent intent = new Intent();

                // pass data that results from editing, using same value as before
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // finish the activity, close current screen, go back to main screen
                setResult(RESULT_OK, intent); // RESULT_OK - predefined in Android
                finish(); // closes the current Activity and sends user back to Main
            }
        });
    }
}
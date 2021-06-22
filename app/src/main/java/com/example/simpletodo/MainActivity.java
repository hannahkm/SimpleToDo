package com.example.simpletodo;

import android.content.Intent;
import org.apache.commons.io.FileUtils;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;
    // ^^ val doesn't specifically matter, since we only have one Intent

    // kind of like initializing global variables?
    // side note: have to import these variables (above!)
    List<String> items; //a list of String objects
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pull items from the display by ID
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems(); // initialize empty list

        // we create an adapter and ViewHolder to render the items in the app
        // we make a class for the ItemAdapter in the same folder as this MainActivity file
        // start by creating a listener for the long click on an item
        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position){
                // delete item from model
                items.remove(position);

                // notify the listener of the position and tell the user that the item was removed
                itemAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        // also a listener for a single tap on an item
        ItemAdapter.OnClickListener onClickListener = new ItemAdapter.OnClickListener() {
            @Override
            // we use Intents to create new activities
            public void onItemClicked(int position){
                // sanity check: log whenever we click on an item
                Log.d("MainActivity", "Single click at a position at " + position);

                // create new activity
                // we indicate that from MainActivity, we want to go to the EditActivity class
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                // pass the data being edited (both the position and the text at that position)
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                // display the new, edited activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemAdapter = new ItemAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemAdapter); // add the adapter to the recycler view
        rvItems.setLayoutManager(new LinearLayoutManager(this)); // add layout manager - adds things vertically

        // listens for any clicks on the add button (when the user wants to add an item to the list)
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // gets the value that was typed into the textbox (by the user)
                String todoItem = etItem.getText().toString();
                // Add item to the model - add to the list we created
                items.add(todoItem);
                // Notify adapter that an item is inserted
                itemAdapter.notifyItemInserted(items.size()-1);
                // reset the text in the user's textbox to be empty so they can type again
                etItem.setText("");
                // let the user know that the item was added - use a Toast command
                // gives a small pop up dialogue on the page that disappears after a SHORT period of time
                Toast.makeText(getApplicationContext(), "Item was added to your list", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    // we handle editing the items in the list
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        // check if things are valid
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract origin position from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the item at the correct position with new text
            items.set(position, itemText);
            // notify the adapter
            itemAdapter.notifyItemChanged(position);
            // persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else{
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    // functions to help the list persist even after closing the app
    // store list in a file and read from it when opening the app

    // obtains the file that we saved our list to
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // reads each line of the data.txt file to load in the items
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // saves items by writing to the txt file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}
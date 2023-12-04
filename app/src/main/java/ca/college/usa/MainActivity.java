package ca.college.usa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Full Name: Imarni ODONGO & Amaal OMER
 *
 * Student ID: 041071424
 *
 * Course: CST3104
 *
 * Term:  Fall 2023
 *
 * Assignment: Team Project
 *
 * Date : Submitted, December 3rd 2023
 */

public class MainActivity extends AppCompatActivity {

    /* START SharedPrefenced variables for the first step*/
    private static final String PREF_KEY= "game_results";
    private static final String PREF_HIGHEST_SCORE_KEY ="high_score";
    private static final String PRE_LOWEST_SCORE_KEY="lowest_score";
    /* END SharedPrefenced variables for the first step
     *
     * START seclared Variables for step 1
     */
    private TextView latestResultTextView, highestResultTextView , lowestResultTextView;
    private Button playButton;
    private ListView listView;
    /* END of declared variables for step 1
     *  START of step 3: declaring variable from the state class*/
    private ImageView stateFlagImageView;
    private TextView stateCapitalTextView;
    private ListView stateListView;
    private List<State> mStatesList;
    private TextView counterTextView;

    private int counter = 0;

    private static final String sFileName = "usa.json";

    // ListView <---> adapter <---> data
    private ArrayAdapter<State> mAdapter;
    private View relativeLayout;

    private boolean correctAnswerFound; // for  STEP 3 - STEP 1.7


    // Data Source for the Adapter

    /*Review the bottom line of code is part of the starter or did you add it ?>*/
    private ArrayList<State> mmStatesListList; // is this the same as the 'private List<State> mStatesList;'

    private  boolean gameActive;

    /*The onCreate method is meant to be the standardization of the project*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Step 3: findViewById */
        stateCapitalTextView = findViewById(R.id.stateCapitalTextView);
        stateFlagImageView = findViewById(R.id.stateFlagImageView);
        stateListView = findViewById(R.id.stateListView);
        relativeLayout = findViewById(R.id.activity_game);

        /*END - step 3*/

        // Load the data needed for the adapter
        mmStatesListList = State.readData(this, sFileName);

        /*Step 3: the following code is meant to show the random state*/
        displayRandomState();

        /*The on-clicker listener button*/
        stateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkAnswer(mmStatesListList.get(position));
            }
        });

        /*Step 3: Step 1*/
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mmStatesListList);
        stateListView.setAdapter(mAdapter);

        /*Imported code from starter project*/

        /*START initialising the data*/

        latestResultTextView = findViewById(R.id.latestResultTextView);
        highestResultTextView = findViewById(R.id.highestResultTextView);
        lowestResultTextView = findViewById(R.id.lowestResultTextView);
        playButton = findViewById(R.id.playButton);
        listView = findViewById(R.id.listView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        /*END initialising the data
         *
         * Toolbar Method implementation
         */
        setSupportActionBar(toolbar);

        /*START retrieving the gameresults*/
        List<Integer> gameResults = getStoredGameResults(); // to review

        /*START The condition that will run and show the results with the date and score
         * This will review if the 'gameResult' is empty and then return the results from the list, with an initialised value first*/

        String latestResultText = " "; // Initialize with an empty string

        if (!gameResults.isEmpty()) {
            int latestResult = gameResults.get(gameResults.size() - 1);
            String currentDate = getCurrentDate();
            latestResultText = "Latest Result: " + currentDate + " - Score: " + latestResult;
        }

        latestResultTextView.setText(latestResultText);

        /*START Displaying the results*/
        int highestResult = getHighestResult(gameResults);
        highestResultTextView.setText("Highest Result: " + highestResult);

        int lowestResult = getLowestResult(gameResults);
        lowestResultTextView.setText("Lowest Result: " + lowestResult);

        /*START the onClickListener the play button that will interact with the user's inputs*/

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*This will open the GameActivityClass*/
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }

    /*STEP 3: STEP 1.8 AKA LAST STEP , the play again button*/
    public void resetGame(){
        gameActive = true;
        correctAnswerFound = false; // this part will bring the correct... flag
        relativeLayout.setBackgroundColor(Color.WHITE); // this will make the bg white
        counterTextView.setText("Counter: 0");
        displayRandomState();
    }

    /* STEP 3: STEP1.8 Creating the Overflow Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    /* STEP 3: STEP1.8 receive menu selection notifications*/
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_play_again:
                resetGame(); // restart method
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    /*REVIEW THIS CODE BELOW, a tongue twister*/
    private void checkAnswer(State selectedState) {
        if (gameActive && !correctAnswerFound ) { // checks if the game is active using the boolean datatype
            State correctState = mmStatesListList.get(0); // defining the correctState that will compare with the selectedState

            if (selectedState.equals(correctState)) {
                correctAnswerFound = true;
                handleCorrectAnswer(selectedState);
            } else{
                handleIncorrectAnswer();
            }
        }
    }

    private void handleCorrectAnswer(State selectedState) {
        State correctState = mmStatesListList.get(0);
        /* The correct guess from the user part */
        relativeLayout.setBackgroundColor(Color.GREEN); // sets background to green
        counterTextView.setText("Counter: " + counter); // show the right value of the counter

        /*This part will update the dat in the adapter and modify the adapter and the stateCapitalTextView */
        mmStatesListList.remove(correctState);
        mAdapter.notifyDataSetChanged();

        /*Changed into the textView due to the setText method not being avaible with a ListView xml datatype*/
        stateCapitalTextView.setText("State: " + correctState.getName()); // show the name

    }

    private void handleIncorrectAnswer() {
        /* The incorrect guessing condition */
        relativeLayout.setBackgroundColor(Color.RED);
        counter++; // the increment to keep track of the number of incorrect guesses, meaning it will continue to increase
        counterTextView.setText("Counter: " + counter);
    }

    /*Step 3: the random mStatesList that will be displaye0, no value will be returned so a 'void' datatype*/
    private void displayRandomState() {

        /*The if-condition this will get the random state, flag & capital */
        if(mStatesList != null && !mStatesList.isEmpty()){ // this checks if not null and not empty

            /*the condition if the first statement is true*/
            Random random = new Random();
            State randomState = mStatesList.get(random.nextInt(mStatesList.size())); // random State

            /*The random mStatesList info*/
            randomState.flagInImageView(stateFlagImageView);
            stateCapitalTextView.setText(randomState.getCapital());
        }
    }

    /* START The important methods
     *  Declared array variable
     */
    List<Integer> results = new ArrayList<>();
    private int getHighestResult(List<Integer> results) { // takes the results parameter
        return results.isEmpty( ) ? 0: Collections.max(results); // ? being an condition if-else, otherwise return the max result
    }

    private int getLowestResult(List<Integer> results) { // takes the results parameter
        return results.isEmpty( ) ? 0: Collections.min(results); // ? being an condition if-else, otherwise return the min result
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd: mm:ss");
        return dateFormat.format(new Date());
    }

    private List<Integer> getStoredGameResults() {

        /*Using the SharedPreferences to retrieve the results*/

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE); // private mode
        String resultString = preferences.getString(PREF_KEY, ""); // retrives a string
        String [] resultsArray = resultString.split(",");  // splits tring into an arrary

        List<Integer> resultsList = new ArrayList<>(); // initialises to stored the parsed (a string rep of an int #) integers
        /*FOR condition*/

        for(String result : resultsArray ){ // execute the statement 1
            try{resultsList.add(Integer.parseInt(result));} // code to try and parse the string to an integer & add to the list
            catch (NumberFormatException e){ // handles the errors
                e.printStackTrace();
            }
        }
        return resultsList; // return the line 134
    }

    /*START Show Dialog for the details about the games shows up when clicking 'info'*/
    public void showInfoDialog(){
        /* this is instatiating the builder */
        AlertDialog.Builder builder= new AlertDialog.Builder(this);

        /* the builder needs a title, body and action button */
        builder.setTitle("App Info"); // title
        builder.setMessage(" The game shows participants random photos of mStatesList and capitals in the United mStatesList, prompting them to choose the correct state from a list. It keeps track of scores and dates, and upon each application opening, it displays the user's most recent, lowest, and highest scores." +
                "The authors of this app are Imarni Odongo & Amaal Omer. "); // body
        builder.setPositiveButton("Ok", null); // the button
        showInfoDialog(); // the displayer
    }
}
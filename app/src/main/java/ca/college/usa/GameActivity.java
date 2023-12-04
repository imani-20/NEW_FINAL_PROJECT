package ca.college.usa;
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    private ImageView stateFlagImageView;
    private TextView stateCapitalTextView;
    private ListView stateListView;

    private Map<String, State> stateInfoMap; // Map to associate states with State objects
    private String[] allStates = {
            "Alabama", "Alaska", "Arizona", "Arkansas", "California",
            "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
            "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
            "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
            "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
            "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio",
            "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
            "South Dakota", "Tennessee", "Texas", "Utah", "Vermont",
            "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"
    };
    private int maxBound = allStates.length;


    private State correctState; // The state to be guessed

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

            stateFlagImageView = findViewById(R.id.stateFlagImageView);
            stateCapitalTextView = findViewById(R.id.stateCapitalTextView);
            stateListView = findViewById(R.id.stateListView);

            // Initialize state information
            stateInfoMap = new HashMap<>();
            stateInfoMap.put("State1", new State("Name1", "Code1", "Capital1", 100, "Union1", "WikiUrl1"));
            stateInfoMap.put("State2", new State("Name2", "Code2", "Capital2", 200, "Union2", "WikiUrl2"));
            stateInfoMap.put("State3", new State("Name3", "Code3", "Capital3", 300, "Union3", "WikiUrl3"));
            // Add more states as needed

            // Set up the ListView with the list of all states
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStates);
            stateListView.setAdapter(adapter);

            // Set a click listener for the ListView items
            stateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Handle item click, e.g., check if the selected state is correct
                    String selectedState = allStates[position];
                    checkGuess(selectedState);
                }
            });

            // Start a new round of the game
            startNewRound();
            /*START Show Dialog for the details about the games shows up when clicking 'info'*/
            showInfoDialog();
        }

        private void startNewRound() {
            // Choose a random state for the user to guess
            correctState = chooseRandom();
            // Update the UI with the picture and capital of the selected state
            updateUI(correctState);
        }

        private void updateUI(State state) {
            // Update the UI with the provided state information
            if (state != null) {
                stateCapitalTextView.setText("Capital of " + state.getName() + ": " + state.getCapital());
                state.flagInImageView(stateFlagImageView);
            }
        }

        private void checkGuess(String selectedState) {
            // Check if the selected state is correct
            if (selectedState.equals(correctState.getName())) {
                // The guess is correct
                showFeedbackDialog("Correct! You guessed the state.");
            } else {
                // The guess is incorrect
                showFeedbackDialog("Incorrect. Try again!");
            }

            // Start a new round after each guess
            startNewRound();
        }

        private void showFeedbackDialog(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, dismiss the dialog
                            dialog.dismiss();
                        }
                    });
            // Create and show the AlertDialog
            builder.create().show();
        }

        private State chooseRandom() {
            Random rand = new Random();
            return stateInfoMap.get(allStates[rand.nextInt(maxBound)]);
        }

    private void showInfoDialog() {
            /* this is instantiating the builder */
            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(this);

            /* the builder needs a title, body and action button */
            builder.setTitle("App Info"); // title
            builder.setMessage(" The game shows participants random photos of mStatesList and capitals in the United mStatesList, prompting them to choose the correct state from a list. It keeps track of scores and dates, and upon each application opening, it displays the user's most recent, lowest, and highest scores." +
                    "The authors of this app are Imarni Odongo & Amaal Omer. "); // body
            builder.setPositiveButton("Ok", null); // the button
            showInfoDialog(); // the display
        }
    }



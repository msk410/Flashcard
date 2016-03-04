package com.mikekim.poweruser.flashcard;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity implements MainButtonsFragment.FragInterface {
    LinearLayout bottomContainer;

    @Override
    public void menu(View v) {

        switch (v.getId()) {
            case R.id.flashcardModeButton:
                bottomContainer.removeAllViews();
                showListView("FlashcardMode");
                break;
            case R.id.quizModeButton:
                bottomContainer.removeAllViews();
                showListView("QuizMode");
                break;
            case R.id.editCardButton:
                bottomContainer.removeAllViews();
                showListView("edit");
                break;
            case R.id.makeCardButton:
                bottomContainer.removeAllViews();
                showMakeCardFragment();
                break;
        }
    }

    public void showEditCardFragment(String json, int position) {
        Bundle b = new Bundle();
        b.putString("mode", "edit");
        b.putString("json", json);
        b.putInt("position", position);
        CreateCardsFragment createCardsFragment = new CreateCardsFragment();
        createCardsFragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bottomContainer, createCardsFragment);
        ft.commit();
    }
    public void showQuizMode(String name) {
        QuizModeFragment quizModeFragment = new QuizModeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        b.putString("cardName", name);
        quizModeFragment.setArguments(b);
        ft.replace(R.id.bottomContainer, quizModeFragment);
        ft.commit();
    }
    public void showMakeCardFragment() {
        //set the bottom fragment to make card fragment
        Bundle b = new Bundle();
        b.putString("mode", "new");
        CreateCardsFragment createCardsFragment = new CreateCardsFragment();
        createCardsFragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bottomContainer, createCardsFragment);
        ft.commit();
    }
    public void showScoreScreen(List<String> cards) {
        Bundle b = new Bundle();
        ArrayList<String> guessCards = (ArrayList<String>)cards;

        b.putStringArrayList("guesses", guessCards);
        ScoreScreenFragment scoreScreenFragment = new ScoreScreenFragment();
        scoreScreenFragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bottomContainer, scoreScreenFragment);
        ft.commit();

    }

    public void showListView(String mode) {
        ListCardsFragment listCardsFragment = new ListCardsFragment();
        Bundle b = new Bundle();
        b.putString("mode", mode);
        listCardsFragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bottomContainer, listCardsFragment);
        ft.commit();
    }
    public void showFlashcardMode(String name) {
        FlashcardModeFragment flashcardModeFragment = new FlashcardModeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        b.putString("cardName", name);
        flashcardModeFragment.setArguments(b);
        ft.replace(R.id.bottomContainer, flashcardModeFragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        makeFile();
        //set the menu buttons fragment
        MainButtonsFragment mainButtonsFragment = new MainButtonsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.buttonContainer, mainButtonsFragment);
        ft.commit();
        bottomContainer = (LinearLayout)findViewById(R.id.bottomContainer);
    }
    public void clearBottomContainer() {
        bottomContainer.removeAllViews();
    }
    //makes a file named cards to hold all the card's jsons
    public void makeFile() {
        try {
            FileOutputStream fos = openFileOutput("cards", MODE_APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

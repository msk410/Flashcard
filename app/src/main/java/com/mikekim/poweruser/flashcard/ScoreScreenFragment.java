package com.mikekim.poweruser.flashcard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreScreenFragment extends Fragment {

    private static TextView answerGuessesText;
    ArrayList<String> answersAndGuesses = new ArrayList<>();
    public ScoreScreenFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answersAndGuesses = b.getStringArrayList("guesses");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_score_screen, container, false);
        answerGuessesText = (TextView)rootView.findViewById(R.id.answerGuessesText);
        answerGuessesText.setText("");
        answerGuessesText.clearComposingText();
        answerGuessesText.append("건너띈단어\n_________\n");
        for(int i = 0; i < answersAndGuesses.size(); i++) {
            answerGuessesText.append(answersAndGuesses.get(i) + "\n");
        }
        return rootView;
    }

}

package com.mikekim.poweruser.flashcard;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainButtonsFragment extends Fragment  {
    FragInterface fragInterface;
    Button flashcardModeButton, quizModeButton, makeCardButton, editCardButton;
    public interface FragInterface {
        public void menu(View v);
    }


    public MainButtonsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragInterface = (FragInterface)context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_buttons_fragment_layout, container, false);
        flashcardModeButton = (Button)rootView.findViewById(R.id.flashcardModeButton);
        quizModeButton = (Button)rootView.findViewById(R.id.quizModeButton);
        makeCardButton = (Button)rootView.findViewById(R.id.makeCardButton);
        editCardButton = (Button)rootView.findViewById(R.id.editCardButton);

        //set on click listeners
        flashcardModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.menu(v);
            }
        });
        quizModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.menu(v);
            }
        });
        makeCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.menu(v);
            }
        });
        editCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.menu(v);
            }
        });
        return rootView;
    }

}

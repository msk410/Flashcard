package com.mikekim.poweruser.flashcard;


import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizModeFragment extends Fragment {
    private TextView infoText;
    private TextView scoreText;
    private Button enterButton, skipButton, hintbutton, quitButton;
    private int score = 0;
    private int hints = 2;
    private EditText guessTextfield;
    private ImageView animalImage;
    private String[] animals = new String[8];
    private AssetManager assetManager;
    private ArrayList<String> animalList = new ArrayList<>();
    private String animalGuess;
    private TextView hintText;
    public ArrayList<String> gue = new ArrayList<>();
    private String cards;
    private TextView koreanWord;
    private String koWord;
    private String enWord;
    private RelativeLayout background;
    private Cards myCard;

    Gson gson = new Gson();
    private static ArrayList<String> wordList = new ArrayList<>();
    RelativeLayout.LayoutParams hideParams = new RelativeLayout.LayoutParams(0, 0);
    RelativeLayout.LayoutParams showParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        cards = b.getString("cardName");
        assetManager = getContext().getAssets();
        if(!cards.contains("견")) {
            myCard = gson.fromJson(cards, Cards.class);
            wordList = myCard.cards;
        }
    }
    public QuizModeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_quiz_mode, container, false);
        koreanWord = (TextView)rootView.findViewById(R.id.koreanWord);
        enterButton = (Button)rootView.findViewById(R.id.enterButton);
        skipButton = (Button)rootView.findViewById(R.id.skipButton);
        hintbutton = (Button)rootView.findViewById(R.id.hintButton);
        quitButton = (Button)rootView.findViewById(R.id.quitButton);
        infoText = (TextView)rootView.findViewById(R.id.infoText);
        background = (RelativeLayout)rootView.findViewById(R.id.background);
        guessTextfield = (EditText)rootView.findViewById(R.id.guessTextfield);
        animalImage = (ImageView)rootView.findViewById(R.id.animalImage);
        scoreText = (TextView) rootView.findViewById(R.id.scoreText2);
        hintText = (TextView) rootView. findViewById(R.id.hintText);
        hintText.setText("힌트: " + hints);

        guessTextfield.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onEnterClick();
                }
                return false;
            }
        });

        if(cards.equals("견본. Animals")) {
            fillAnimalList();
            newCard();
        }
        else
        {
            animalImage.setLayoutParams(hideParams);
            newCard();
        }
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnterClick();
            }
        });
        hintbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHintClick();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSkipClick();
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuitClick();
            }
        });
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackgroundClick();
            }
        });
        animalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick();
            }
        });
        return rootView;
    }

    public void goToScoreScreen() {

        ((MainScreen) getActivity()).showScoreScreen(gue);
    }
    public void onQuitClick() {
        goToScoreScreen();
    }

    //makes a new animal and animal image
    public void newCard() {
        guessTextfield.setText("");
        infoText.setText("");
        hints = 2;
        hintText.setText("힌트: " + hints);
        guessTextfield.requestFocus();
        if(cards.equals("견본. Animals")) {
            animalGuess = newAnimal();
            setAnimalImage(animalGuess);
        }
        else {
            newAnimal();
            koreanWord.setText(koWord);
        }

    }
    //sets animalImage to animal
    public void setAnimalImage(String animal) {
        try {
            // get input stream
            animal = "Animals/" + animal;

            InputStream ims = assetManager.open(animal);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            animalImage.setImageDrawable(d);
        }
        catch(IOException ex) {
            return;
        }
    }
    //get animals from assets
    public void fillAnimalList() {
        try {
            animals = assetManager.list("Animals");
            for(int i = 0; i < animals.length; i++){
                animalList.add(animals[i]);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String newAnimal() {
        if (cards.equals("견본. Animals")) {
            if (animalList.size() == 0) {
                goToScoreScreen();
                return null;
            } else {
                int randomIndex = new Random().nextInt(animalList.size());
                String randomAnimal = animalList.get(randomIndex);
                animalList.remove(animalList.get(randomIndex));
                return randomAnimal;
            }
        } else {
            if (wordList.size() == 0) {
                goToScoreScreen();
                return null;
            } else {
                int randomIndex = new Random().nextInt(wordList.size());
                String randomAnimal = wordList.get(randomIndex);
                wordList.remove(wordList.get(randomIndex));
                String[] temp = randomAnimal.split("/");
                koWord = temp[0].trim();
                enWord = temp[1].trim();
                return null;
            }
        }
    }
    public void onEnterClick() {
        if (cards.equals("견본. Animals")) {
            String guess = guessTextfield.getText().toString().trim() + ".jpg";
            if (guess.trim().equalsIgnoreCase(animalGuess)) {
                String[] aniName = animalGuess.split("\\.");
                score++;
                scoreText.setText("점수: " + score);
                infoText.setText("");
                guessTextfield.setText("");
                newCard();
            } else
                infoText.setText("다시 입력하세요");
        }
        else
        {
            String guess = guessTextfield.getText().toString().trim();
            if(guess.trim().equalsIgnoreCase(enWord)) {
                score++;
                scoreText.setText("점수: " + score);
                infoText.setText("");
                guessTextfield.setText("");
                newCard();
            }
            else
                infoText.setText("다시 입력하세요");
        }
    }
    public void onHintClick() {
        String guess;
        if(cards.equals("견본. Animals")) {
            guess = animalGuess;
        }
        else {
            guess = enWord;
        }
        if(hints == 2) {
            --hints;
            hintText.setText("힌트: " + hints);
            guessTextfield.setText(guess.substring(0, 1));
        }
        else if(hints == 1) {
            --hints;
            hintText.setText("힌트: " + hints);
            guessTextfield.setText(guess.substring(0, 2));
        }
        else
            infoText.setText("힌트 없음");
    }
    public void onSkipClick() {
        if(cards.equals("견본. Animals")) {
            String[] aniName = animalGuess.split("\\.");
            gue.add(aniName[0]);
            newCard();
        }
        else {
            gue.add(koWord + " / " + enWord);
            newCard();
        }
    }
    public void onBackgroundClick() {
        guessTextfield.clearFocus();
    }
    public void onImageClick() {
        guessTextfield.clearFocus();
    }

}

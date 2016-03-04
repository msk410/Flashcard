package com.mikekim.poweruser.flashcard;


import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlashcardModeFragment extends Fragment {
    private static ImageView imageButton;
    private static AssetManager assetManager;
    private static String[] animals = new String[8];
    private static TextView answer;
    private static String animal;
    private static ArrayList<String> animalList = new ArrayList<>();
    private static TextView cardWord;
    private static String englishWord;
    private static String koreanWord;
    RelativeLayout.LayoutParams hideParams = new RelativeLayout.LayoutParams(0, 0);
    RelativeLayout.LayoutParams showParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
    private static String extraInfo;
    private static Intent intent;
    private int clicks = 1;
    private static ArrayList<String> wordList = new ArrayList<>();
    Gson gson = new Gson();


    public FlashcardModeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        extraInfo = b.getString("cardName");

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_flashcard_mode, container, false);
        assetManager = getContext().getAssets();
        imageButton = (ImageView) rootView.findViewById(R.id.imageView);
        answer = (TextView)rootView.findViewById(R.id.imageName);
        cardWord = (TextView)rootView.findViewById(R.id.cardWord);
        if(!extraInfo.equals("견본. Animals")) {
            Cards myCard = gson.fromJson(extraInfo, Cards.class);
            wordList = myCard.cards;
        }


        cardWord.setLayoutParams(hideParams);
        answer.setLayoutParams(hideParams);
        if(extraInfo.equals("견본. Animals")) {
            try {
                animals = assetManager.list("Animals");
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String a : animals) {
                animalList.add(a);
            }
            animal = getRandomAnimal();
            setAnimalImage(animal);
        }
        else {
            randomWord();
            showWord();
        }
        cardWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardWordClick();
            }
        });
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerClick();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick();
            }
        });
        return rootView;
    }


    private void showWord() {
        cardWord.setText(koreanWord);
        cardWord.setLayoutParams(showParams);
    }

    public void onCardWordClick() {
        if(clicks == 1) {
            cardWord.append("\n\n" + englishWord);
            clicks = 2;
        }
        else {
            if(wordList.size() == 0)
            {
                ((MainScreen)getActivity()).clearBottomContainer();
            }
            else {
                clicks = 1;
                randomWord();
                showWord();
            }
        }
    }
    public void randomWord() {
        int randomIndex = new Random().nextInt(wordList.size());
        String tempWord = wordList.get(randomIndex);
        String[] temp;
        temp = tempWord.split("\\/");
        koreanWord = temp[0];
        englishWord = temp[1];
        wordList.remove(randomIndex);
    }

    public void setAnimalImage(String animal) {
        try {
            // get input stream
            animal = "Animals/" + animal;
            InputStream ims = assetManager.open(animal);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageButton.setImageDrawable(d);
        }
        catch(IOException ex) {
            return;
        }
    }

    public String getRandomAnimal() {
        //get a random animal or word
        int randomIndex = new Random().nextInt(animalList.size());
        String tempAnimal = animalList.get(randomIndex);
        animalList.remove(randomIndex);
        return tempAnimal;

    }
    public void showAnimalName() {
        String[] a = animal.split("\\.");
        answer.setText(a[0]);
    }
    public void onAnswerClick() {
        if((wordList.size() == 0 && !extraInfo.equals("견본. Animals")) || (animalList.size() == 0 && extraInfo.equals("견본. Animals"))) {
            ((MainScreen)getActivity()).clearBottomContainer();
        }
        else if(extraInfo.equals("견본. Animals")) {
            imageButton.setLayoutParams(showParams);
            answer.setLayoutParams(hideParams);
            animal = getRandomAnimal();
            setAnimalImage(animal);
        }
        else if(!extraInfo.equals("견본. Animals")) {
            cardWord.setLayoutParams(showParams);
            answer.setLayoutParams(hideParams);
            randomWord();
            showWord();
        }
        answer.setText("");
    }
    public void onImageClick() {
        imageButton.setLayoutParams(hideParams);
        answer.setLayoutParams(showParams);
        showAnimalName();
    }


}



package com.mikekim.poweruser.flashcard;

/*
    Fragment to create cards
*/


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CreateCardsFragment extends Fragment {
    ListView listView;
    ArrayList<String> cardsNameList = new ArrayList<>();
    ArrayList<Cards> cardsArrayList = new ArrayList<>();
    EditText englishText, koreanText, cardName;
    String koWord, enWord;
    Button addWordButton, saveButton;
    Cards myCard, jsonCard;
    Gson gson = new Gson();
    String mode = "";
    String json = "";
    TextView cardInfo;
    int position = 0;

    public CreateCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        mode = b.getString("mode");
        json = b.getString("json");
        position = b.getInt("position");
        jsonCard = gson.fromJson(json, Cards.class);
        try {
            InputStream inputStream = getActivity().openFileInput("cards");
            //read from files and get json string
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ((receiveString = bufferedReader.readLine()) != null) {
                    Cards tempCard = gson.fromJson(receiveString, Cards.class);
                    cardsArrayList.add(tempCard);
                }
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.create_cards_fragment_layout, container, false);
        listView = (ListView)rootView.findViewById(R.id.editListView);
        englishText = (EditText)rootView.findViewById(R.id.editEnglishText);
        koreanText = (EditText)rootView.findViewById(R.id.editKoreanText);
        addWordButton = (Button)rootView.findViewById(R.id.editAddWordButton);
        saveButton = (Button)rootView.findViewById(R.id.editSaveButton);
        cardName = (EditText)rootView.findViewById(R.id.editCardName);
        cardInfo = (TextView)rootView.findViewById(R.id.cardInfo);
        if(mode.equals("edit")) {
            cardName.setVisibility(View.INVISIBLE);
            //set the name to selected card name
            cardInfo.setText(jsonCard.name);
            //set the list to select card's cards and show in list view
            cardsNameList = jsonCard.cards;
            updateList();


        }
        //set on click listeners for korean edittext and english editext
        koreanText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)) {

                    koWord = koreanText.getText().toString().trim();
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (englishText.getText().toString().equals("")) {
                          //  koreanText.setText(koWord);
                        }
                        else {
                            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(koreanText.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
        englishText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)) {

                    enWord = englishText.getText().toString().trim();
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (koreanText.getText().toString().equals("")) {
                            englishText.setText(enWord);
                            koreanText.requestFocus();
                        } else {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(koreanText.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
        //set on click listeners for add word and save buttons
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddWordClick();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.edit_dialog_box_layout);
                dialog.show();
                final Button editButton = (Button) dialog.findViewById(R.id.editButton);
                Button deleteButton = (Button) dialog.findViewById(R.id.deleteButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //show the edit word layout dialog box
                        final Dialog editWordDialog = new Dialog(getActivity());
                        editWordDialog.setContentView(R.layout.edit_word_layout);
                        editWordDialog.show();
                        Button editWordSave = (Button) editWordDialog.findViewById(R.id.editWordDialogButton);
                        final EditText newKoreanWord = (EditText) editWordDialog.findViewById(R.id.newKoreanWord);
                        final EditText newEnglishWord = (EditText) editWordDialog.findViewById(R.id.newEnglishWord);
                        TextView editDialogCardName = (TextView) editWordDialog.findViewById(R.id.editDialogCardName);
                        editDialogCardName.setText(cardsNameList.get(position));
                        String[] editWord = cardsNameList.get(position).split("/");
                        newKoreanWord.setText(editWord[0].trim());
                        newEnglishWord.setText(editWord[1].trim());
                        editWordSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (newEnglishWord.getText().length() != 0 && newKoreanWord.getText().length() != 0) {
                                    cardsNameList.set(position, newKoreanWord.getText().toString().trim()
                                            + "/" + newEnglishWord.getText().toString().trim());
                                    updateList();
                                    editWordDialog.hide();
                                    dialog.hide();
                                } else {
                                    Toast.makeText(getActivity(), "한국어와 영어를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteWord(position);
                        dialog.hide();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
            }
        });
        return rootView;
    }
    public void deleteWord(int position) {
        cardsNameList.remove(position);
        updateList();
    }

    public void onAddWordClick() {
        if(englishText.getText().toString().equals("")
                || koreanText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "한국어와 영어를 모두 입력해주세요.", Toast.LENGTH_LONG).show();
        }
        else {
            String word = koreanText.getText().toString().trim() + "/" + englishText.getText().toString().trim();
            cardsNameList.add(word);
            updateList();
            koreanText.requestFocus();
            koWord = null;
            enWord = null;
            englishText.setText("");
            koreanText.setText("");
        }
    }
    public void onSaveButtonClick() {
        if(!mode.equals("edit")) {
            if (cardName.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "단어장의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                cardName.requestFocus();
            } else {
                myCard = new Cards(cardName.getText().toString().trim());
                myCard.cards = cardsNameList;
                writeToFile();
            }
        } else {
            jsonCard.cards = cardsNameList;
            writeToFile();
        }

    }
    public void writeToFile() {

        OutputStreamWriter out = null;
        if (!mode.equals("edit")) {
            try {
                out = new OutputStreamWriter(getContext().openFileOutput("cards", Context.MODE_APPEND));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (myCard.cards.size() != 0) {
                    String json = gson.toJson(myCard);
                    out.write(json);
                    out.write("\n");
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((MainScreen) getActivity()).clearBottomContainer();
        }
        else {
            try {
                out = new OutputStreamWriter(getContext().openFileOutput("cards", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                cardsArrayList.set(position, jsonCard);
                for(int i = 0; i < cardsArrayList.size(); i++) {
                    String json = gson.toJson(cardsArrayList.get(i));
                    out.write(json);
                    out.write("\n");
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((MainScreen) getActivity()).clearBottomContainer();
        }
    }




    private void updateList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cardsNameList);
        listView.setAdapter(adapter);
    }

    public void showSavedFiles() {
        ArrayList<String> jsonList = new ArrayList<>();
        try {
            InputStream inputStream = getActivity().openFileInput("cards");
            //read from files and get json string

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ((receiveString = bufferedReader.readLine()) != null) {
                    jsonList.add(receiveString.trim());
                }
                for (int i = 0; i < jsonList.size(); i++) {
                    Cards tempCards = gson.fromJson(jsonList.get(i), Cards.class);
                    cardsArrayList.add(tempCards);
                }
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

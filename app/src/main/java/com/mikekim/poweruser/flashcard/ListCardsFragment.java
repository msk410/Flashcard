package com.mikekim.poweruser.flashcard;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 Fragment that shows the list of cards
 */
public class ListCardsFragment extends Fragment {
    private ListView listView;
    private String mode = "";
    private ArrayList<String> cardsNameList;
    private ArrayList<Cards> cardsArrayList = new ArrayList<>();
    Gson gson = new Gson();


    public ListCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        mode = b.getString("mode");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_list_cards, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        cardsNameList = new ArrayList<String>();
        cardsNameList.add("견본. Animals");
        showSavedFiles();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, cardsNameList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempCardJSON = "";
/*                if (cardsArrayList.size() != 0) {
                    Cards tempCard = cardsArrayList.get(position - 1);
                    tempCardJSON = gson.toJson(tempCard);
                }*/
                //if user clicked quiz, then goes to quiz fragment after clicking a list item
                if (mode.trim().equals("QuizMode")) {
                    if (position == 0) {
                        ((MainScreen) getActivity()).showQuizMode(parent.getItemAtPosition(position).toString());
                    } else {
                        Cards tempCard = cardsArrayList.get(position - 1);
                        tempCardJSON = gson.toJson(tempCard);
                        ((MainScreen) getActivity()).showQuizMode(tempCardJSON);
                    }
                    //if user clicked flashcards, then goes to flashcard fragment after clicking a list item
                } else if (mode.trim().equals("FlashcardMode")) {
                    if (position == 0) {
                        ((MainScreen) getActivity()).showFlashcardMode(parent.getItemAtPosition(position).toString());
                    } else {
                        Cards tempCard = cardsArrayList.get(position - 1);
                        tempCardJSON = gson.toJson(tempCard);
                        ((MainScreen) getActivity()).showFlashcardMode(tempCardJSON);
                    }
                    //if user clicked edit, then goes to edit fragment after clicking a list item
                } else if (mode.trim().equals("edit")) {
                    if (position != 0) {
                        Cards tempCard = cardsArrayList.get(position - 1);
                        tempCardJSON = gson.toJson(tempCard);
                        ((MainScreen) getActivity()).showEditCardFragment(tempCardJSON, (position - 1));
                    }
                }
            }
        });
        //on a long click, open a dialog box with extra options (delete and edit)
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if(position != 0) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.edit_dialog_box_layout);
                    dialog.show();
                    Button editButton = (Button)dialog.findViewById(R.id.editButton);
                    Button deleteButton = (Button)dialog.findViewById(R.id.deleteButton);
                    Button cancelButton = (Button)dialog.findViewById(R.id.cancelButton);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Cards tempCard = cardsArrayList.get(position - 1);
                            String tempCardJSON = gson.toJson(tempCard);
                            dialog.hide();
                            ((MainScreen)getActivity()).showEditCardFragment(tempCardJSON, (position - 1));
                        }
                    });
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSelectedFile(position);
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
                return true;
            }
        });
        return rootView;
    }

    //adds cards to card list

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
                String arraySize = Integer.toString(jsonList.size());
                int aSize = Integer.valueOf(arraySize);
                for (int i = 0; i < aSize; i++) {
                    Cards tempCards = gson.fromJson(jsonList.get(i), Cards.class);
                    cardsArrayList.add(tempCards);
                    cardsNameList.add((i + 1) + ". " + tempCards.name);
                }
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteSelectedFile(int position) {
        int selectedPosition = position;
        cardsArrayList.remove(selectedPosition - 1);
        cardsNameList.remove(selectedPosition);
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(getActivity().openFileOutput("cards", Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            for(int i = 0; i < cardsArrayList.size(); i++) {
                String cardJSON = gson.toJson(cardsArrayList.get(i));
                out.write(cardJSON);
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        if(mode.equals("QuizMode")){
            ((MainScreen)getActivity()).showListView("QuizMode");
        }
        else if(mode.equals("FlashcardMode")) {
            ((MainScreen)getActivity()).showListView("FlashcardMode");
        }
        else if(mode.equals("edit")) {
            ((MainScreen)getActivity()).showListView("edit");
        }

    }

}

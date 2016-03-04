package com.mikekim.poweruser.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    private Button menuFlashcardsButton;
    private Button menuQuitButton;
    private Button menuQuizModeButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        menuFlashcardsButton = (Button)findViewById(R.id.menuFlashcardsButton);
        menuQuitButton = (Button)findViewById(R.id.menuQuitButton);
        menuQuizModeButton = (Button)findViewById(R.id.menuQuizModeButton);
    }
    public void onMenuFlashcardsClick(View v) {
        /*intent = new Intent(this, ListCards.class);
        intent.putExtra("mode", "FlashcardMode");
        startActivity(intent);*/
        intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

    public void onQuitClick(View v) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

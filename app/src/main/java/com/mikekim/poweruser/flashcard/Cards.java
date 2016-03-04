package com.mikekim.poweruser.flashcard;

import java.util.ArrayList;

/**
 * Created by POWERUSER on 1/29/2016.
 * Cards class - holds user made cards in array list
 */
public class Cards {
    String name;
    ArrayList<String> cards = new ArrayList<>();

    public Cards(String name) {
        this.name = name;
    }
    public void addCards(String data) {
        cards.add(data.trim());
    }
}

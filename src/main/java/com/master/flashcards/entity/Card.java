package com.master.flashcards.entity;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable {
    private int card_id;

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    private String theme;
    private String front;
    private String back;
    private String cardId;
    private int level;

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Card(String theme, String front, String back) {
        this.theme = theme;
        this.front = front;
        this.back = back;
        this.cardId = generateCardId();
    }

    public String getTheme() { return theme; }
    public String getFront() { return front; }
    public String getBack() { return back; }
    public String getCardId() { return cardId; }
    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

    private String generateCardId() {
        return Integer.toHexString((theme + front).hashCode());
    }
}


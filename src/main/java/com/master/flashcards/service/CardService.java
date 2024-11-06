package com.master.flashcards.service;

import com.master.flashcards.entity.Card;
import com.master.flashcards.entity.CardCollection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private final CardCollection cards = new CardCollection();

    public CardService() {
        cards.loadCollection();
    }

    public List<String> getThemes() {
        return cards.getThemes();
    }

    public void addTheme(String theme) {
        cards.addTheme(theme);
    }

    public void renameTheme(String oldTheme, String newTheme) {
        // Получаем карточки для старой темы
        List<Card> cardsList = cards.getCards(oldTheme);

        // Удаляем старую тему
        cards.delTheme(oldTheme);

        // Добавляем новую тему
        cards.addTheme(newTheme);

        // Перемещаем карточки в новую тему
        for (Card card : cardsList) {
            card.setTheme(newTheme); // Меняем тему у каждой карточки
            cards.addCard(card); // Добавляем карточки в новую тему
        }
    }

    public List<Card> getCards(String theme) {
        return cards.getCards(theme);
    }

    public void addCard(Card card) {
        cards.addCard(card);
    }

    public void deleteCard(String theme, String cardId) {
        cards.delCard(theme, cardId);
    }

    public void deleteTheme(String theme) {
        cards.delTheme(theme);
    }
}

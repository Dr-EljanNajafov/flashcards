package com.master.flashcards.entity;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CardCollection implements Serializable {
    private Map<String, Map<String, Card>> collection = new LinkedHashMap<>();

    public void addCard(Card card) {
        collection.computeIfAbsent(card.getTheme(), k -> new LinkedHashMap<>()).put(card.getCardId(), card);
        updateCollection();
    }

    public void addTheme(String theme) {
        collection.putIfAbsent(theme, new LinkedHashMap<>());
        updateCollection();
    }

    public void delCard(String theme, String cardId) {
        Map<String, Card> themeCards = collection.get(theme);
        if (themeCards != null) {
            themeCards.remove(cardId);
            updateCollection();
        }
    }

    public void delTheme(String theme) {
        collection.remove(theme);
        updateCollection();
    }

    public List<Card> getCards(String theme) {
        return new ArrayList<>(collection.getOrDefault(theme, Collections.emptyMap()).values());
    }

    public void updateCollection() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_collection.ser"))) {
            oos.writeObject(collection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getThemes() {
        return new ArrayList<>(collection.keySet());
    }

    @SuppressWarnings("unchecked")
    public void loadCollection() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data_collection.ser"))) {
            collection = (Map<String, Map<String, Card>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            collection = new LinkedHashMap<>();
        }
    }
}


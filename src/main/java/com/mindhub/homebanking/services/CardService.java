package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {

    public void saveCard(Card card);

    Card findByNumber(String number);
}

package com.bearya.robot.base.card;


public abstract class Card {
    private int oid;
    private CardType cardType;

    public Card(int oid, CardType cardType) {
        this.oid = oid;
        this.cardType = cardType;
    }
}

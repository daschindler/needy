package com.hagenberg.needy.Entity;

public class Ingredient {
    private String name;
    private int amount;
    private Unit amountUnit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Unit getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(Unit amountUnit) {
        this.amountUnit = amountUnit;
    }

    public Ingredient(String name, int amount, Unit amountUnit) {

        this.name = name;
        this.amount = amount;
        this.amountUnit = amountUnit;
    }
}


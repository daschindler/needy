package com.hagenberg.needy.Entity;

public class Ingredient {
    private String name;
    private double amount;
    private Unit amountUnit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
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

    public Ingredient(String name, double amount, Unit amountUnit) {
        this.name = name;
        this.amount = amount;
        this.amountUnit = amountUnit;
    }
}


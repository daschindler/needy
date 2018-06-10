package com.hagenberg.needy.Entity;

public class IngredientViewId {
    private int nameId;
    private int amountId;
    private int UnitId;

    public IngredientViewId(int nameId, int amountId, int unitId) {
        this.nameId = nameId;
        this.amountId = amountId;
        UnitId = unitId;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getAmountId() {
        return amountId;
    }

    public void setAmountId(int amountId) {
        this.amountId = amountId;
    }

    public int getUnitId() {
        return UnitId;
    }

    public void setUnitId(int unitId) {
        UnitId = unitId;
    }
}

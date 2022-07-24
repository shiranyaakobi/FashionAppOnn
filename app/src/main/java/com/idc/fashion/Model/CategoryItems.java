package com.idc.fashion.Model;

import java.util.ArrayList;

public class CategoryItems {
    public static final String CATEGORIES = "Categories";
    public static final String SHIRTS = "Shirts";
    public static final String PANTS = "Pants";
    public static final String DRESS = "Dresses";
    public static final String SKIRTS = "Skirts";

    private ArrayList<Item> shirts;
    private ArrayList<Item> pants;
    private ArrayList<Item> skirts;
    private ArrayList<Item> dresses;

    public CategoryItems(ArrayList<Item> shirts,
                         ArrayList<Item> pants,
                         ArrayList<Item> skirts,
                         ArrayList<Item> dresses) {
        this.pants = pants;
        this.shirts = shirts;
        this.skirts = skirts;
        this.dresses = dresses;
    }

    public CategoryItems() {}


    public void setAllByCategory(String category, ArrayList<Item> items) {
        switch(category) {
            case SHIRTS:
                setShirts(items);
                break;
            case PANTS:
                setPants(items);
                break;
            case SKIRTS:
                setSkirts(items);
                break;
            case DRESS:
                setDresses(items);
                break;
        }
    }

    private void setDresses(ArrayList<Item> dresses) {
        this.dresses = dresses;
    }

    private void setPants(ArrayList<Item> pants) {
        this.pants = pants;
    }

    private void setShirts(ArrayList<Item> shirts) {
        this.shirts = shirts;
    }

    private void setSkirts(ArrayList<Item> skirts) {
        this.skirts = skirts;
    }

    public ArrayList<Item> getDresses() {
        return dresses;
    }

    public ArrayList<Item> getPants() {
        return pants;
    }

    public ArrayList<Item> getShirts() {
        return shirts;
    }

    public ArrayList<Item> getSkirts() {
        return skirts;
    }



}

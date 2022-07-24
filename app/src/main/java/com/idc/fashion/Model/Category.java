package com.idc.fashion.Model;

import java.util.List;

public class Category {
    private String name;
    private List<Item> items;

    public Category(String name, List<Item> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        if(item != null) {
            items.add(item);
        }
    }

    public void deleteItem(int position) {
        if(items.get(position) != null) {
            items.remove(position);
        }
    }

}

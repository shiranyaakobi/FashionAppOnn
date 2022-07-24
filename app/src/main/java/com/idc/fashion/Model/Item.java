package com.idc.fashion.Model;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class Item implements Serializable {


    private String itemId;
    private String ownerId;
    private String name;
    private String brand;
    private String description;
    private String condition;
    private String category;
    private String size;
    private int price;
    private String imageAddress;

    public Item() {

    }

    public Item(String ownerId, String category, String name,String imageAddress, String brand, String description, String condition, String size, int price) {

        this.name = name;
        this.brand = brand;
        this.description = description;
        this.condition = condition;
        this.category = category;
        this.size = size;
        this.ownerId = ownerId;
        this.imageAddress = imageAddress;
        this.itemId = UUID.randomUUID().toString();
        this.price = price;
    }

    public Item(String itemId, String category,String imageAddress, String ownerId, String name, String brand, String description, String condition, String size, int price) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.condition = condition;
        this.imageAddress = imageAddress;
        this.size = size;
        this.ownerId = ownerId;
        this.category = category;
        this.itemId = itemId;
        this.price = price;
    }

    public static Item fromMap(Map<String, Object> map) {
        return new Item(
                (String) map.get("itemId"),
                (String) map.get("category"),
                (String) map.get("imageAddress"),
                (String) map.get("ownerId"),
                (String) map.get("name"),
                (String) map.get("brand"),
                (String) map.get("description"),
                (String) map.get("condition"),
                (String) map.get("size"),
                (Integer) map.get("price")
        );
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", condition='" + condition + '\'' +
                ", category='" + category + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                ", imageAddress='" + imageAddress + '\'' +
                '}';
    }
}

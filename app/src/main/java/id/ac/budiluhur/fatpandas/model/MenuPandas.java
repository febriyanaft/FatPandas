package id.ac.budiluhur.fatpandas.model;

import java.util.Locale;

public class MenuPandas {
    private final String name;
    private final String description;
    private final String allergen;
    private final int price;
    private final String category;
    private final int imageRes;

    public MenuPandas(String name, String description, String allergen, int price, String category, int imageRes) {
        this.name = name;
        this.description = description;
        this.allergen = allergen;
        this.price = price;
        this.category = category;
        this.imageRes = imageRes;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAllergen() { return allergen; }
    public int getPrice() { return price; }
    public String getCategory() { return category; }
    public int getImageRes() { return imageRes; }
    public String getFormattedPrice() {
        return "Rp" + String.format(Locale.GERMANY, "%,d", price);
    }
}
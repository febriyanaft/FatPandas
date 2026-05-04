package id.ac.budiluhur.fatpandas;

import id.ac.budiluhur.fatpandas.model.CartItem;
import id.ac.budiluhur.fatpandas.model.MenuPandas;
import id.ac.budiluhur.fatpandas.model.OrderItem;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems = new ArrayList<>();
    private final List<OrderItem> orderHistory = new ArrayList<>();
    private final List<MenuPandas> favoriteItems = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    // FAVORITES
    public void toggleFavorite(MenuPandas menu) {
        if (isFavorite(menu)) {
            favoriteItems.removeIf(m -> m.getName().equals(menu.getName()));
        } else {
            favoriteItems.add(menu);
        }
    }

    public boolean isFavorite(MenuPandas menu) {
        for (MenuPandas m : favoriteItems) {
            if (m.getName().equals(menu.getName())) return true;
        }
        return false;
    }

    public List<MenuPandas> getFavoriteItems() {
        return favoriteItems;
    }

    // CART
    public void addItem(CartItem newItem) {
        for (CartItem item : cartItems) {
            if (item.getName().equals(newItem.getName())) {
                item.setQty(item.getQty() + newItem.getQty());
                return;
            }
        }
        cartItems.add(newItem);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < cartItems.size()) cartItems.remove(index);
    }

    public List<CartItem> getCartItems() { return cartItems; }

    public int getCartCount() {
        int total = 0;
        for (CartItem i : cartItems) total += i.getQty();
        return total;
    }

    public int getCartTotal() {
        int total = 0;
        for (CartItem i : cartItems) total += i.getTotalPrice();
        return total;
    }

    public int getSelectedTotal() {
        int total = 0;
        boolean anySelected = false;
        for (CartItem i : cartItems) {
            if (i.isSelected()) {
                total += i.getTotalPrice();
                anySelected = true;
            }
        }
        return anySelected ? total : getCartTotal();
    }

    public int getSelectedCount() {
        int count = 0;
        boolean anySelected = false;
        for (CartItem i : cartItems) {
            if (i.isSelected()) {
                count += i.getQty();
                anySelected = true;
            }
        }
        return anySelected ? count : getCartCount();
    }

    public List<CartItem> getSelectedItems() {
        List<CartItem> selected = new ArrayList<>();
        for (CartItem i : cartItems) {
            if (i.isSelected()) selected.add(i);
        }
        return selected.isEmpty() ? new ArrayList<>(cartItems) : selected;
    }

    public void removeCheckedOutItems(List<CartItem> items) {
        for (CartItem checkedItem : items) {
            cartItems.removeIf(i -> i.getName().equals(checkedItem.getName()));
        }
    }

    public void addOrder(OrderItem order) { orderHistory.add(0, order); }
    public List<OrderItem> getOrderHistory() { return orderHistory; }
}

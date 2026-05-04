package id.ac.budiluhur.fatpandas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.budiluhur.fatpandas.model.MenuPandas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_all_menu);

        findViewById(R.id.btnBack).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        RecyclerView rv = findViewById(R.id.rvAllMenu);
        List<MenuPandas> allMenus = new ArrayList<>();
        
        // MINUMAN (6)
        allMenus.add(new MenuPandas("Matcha Latte",       "Perpaduan matcha premium dengan susu segar", "Susu, Kafein", 25000, "Minuman", R.drawable.matcha));
        allMenus.add(new MenuPandas("Chocolate Drink",    "Cokelat premium yang creamy dan nikmat", "Susu", 22000, "Minuman", R.drawable.choco));
        allMenus.add(new MenuPandas("Thai Tea Pandas",    "Teh Thailand autentik dengan susu pilihan", "Susu, Kafein", 18000, "Minuman", R.drawable.thaitea));
        allMenus.add(new MenuPandas("Latte Pandas",       "Kopi susu dengan foam yang lembut", "Susu, Kafein", 23000, "Minuman", R.drawable.latte));
        allMenus.add(new MenuPandas("Ice Drink Pandas",   "Minuman segar pelepas dahaga", "Tidak ada", 15000, "Minuman", R.drawable.ice));
        allMenus.add(new MenuPandas("Es Teh Pandas",      "Teh segar dengan gula aren khas Pandas", "Kafein", 12000, "Minuman", R.drawable.es_teh));

        // MAKANAN (7)
        allMenus.add(new MenuPandas("Nasi Goreng Pandas", "Nasi goreng khas dengan topping ayam crispy", "Telur, Gluten, Kedelai", 30000, "Makanan", R.drawable.nasgor));
        allMenus.add(new MenuPandas("Ricebowl Chicken",   "Nasi hangat dengan ayam saus spesial", "Gluten, Kedelai", 28000, "Makanan", R.drawable.ricebowl));
        allMenus.add(new MenuPandas("Ricebowl Beef",      "Nasi hangat dengan daging sapi pilihan", "Gluten, Kedelai", 35000, "Makanan", R.drawable.ricebowl2));
        allMenus.add(new MenuPandas("Mie Goreng Pandas",  "Mie goreng bumbu rempah autentik", "Gluten, Telur, Kedelai", 25000, "Makanan", R.drawable.miegoreng));
        allMenus.add(new MenuPandas("Mie Kuah Pandas",    "Mie kuah hangat dengan kaldu gurih", "Gluten, Telur", 25000, "Makanan", R.drawable.miekuah));
        allMenus.add(new MenuPandas("Mix Platter Pandas", "Aneka gorengan, kentang, dan saus spesial", "Gluten", 35000, "Makanan", R.drawable.fries));
        allMenus.add(new MenuPandas("Ayam Crispy Pandas", "Ayam crispy renyah dengan saus spesial Fat Pandas", "Gluten", 32000, "Makanan", R.drawable.ayam));

        // CEMILAN (5)
        allMenus.add(new MenuPandas("Rollcake Pandas",    "Rollcake cokelat lumer yang nikmat", "Susu, Telur, Gluten", 28000, "Cemilan", R.drawable.cake));
        allMenus.add(new MenuPandas("Cheesecake Pandas",  "Kue keju lembut dengan rasa premium", "Susu, Telur, Gluten", 30000, "Cemilan", R.drawable.ciskek));
        allMenus.add(new MenuPandas("Pisang Goreng",      "Pisang goreng manis dan renyah", "Gluten", 15000, "Cemilan", R.drawable.pisang));
        allMenus.add(new MenuPandas("Tiramisu Pandas",    "Dessert kopi khas Italia yang menggoda", "Susu, Telur, Kafein", 28000, "Cemilan", R.drawable.tiramisu));
        allMenus.add(new MenuPandas("Roti Bakar Pandas",  "Roti bakar dengan aneka topping manis", "Gluten, Susu", 20000, "Cemilan", R.drawable.rotibakar));

        Collections.sort(allMenus, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));

        MenuAdapter adapter = new MenuAdapter(this, allMenus);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        NavHelper.setup(this, "all_menu");
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.setup(this, "all_menu");
    }
}
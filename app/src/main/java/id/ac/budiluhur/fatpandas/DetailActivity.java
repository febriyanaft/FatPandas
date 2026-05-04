package id.ac.budiluhur.fatpandas;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import id.ac.budiluhur.fatpandas.model.MenuPandas;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private MenuPandas currentMenu;
    private ImageView btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        String name  = getIntent().getStringExtra("name");
        String desc  = getIntent().getStringExtra("desc");
        String allergen = getIntent().getStringExtra("allergen");
        int price    = getIntent().getIntExtra("price", 0);
        int imageRes = getIntent().getIntExtra("image", R.drawable.nasgor);
        String cat   = getIntent().getStringExtra("category");

        currentMenu = new MenuPandas(name, desc, allergen, price, cat, imageRes);

        ((ImageView) findViewById(R.id.imgDetailMenu)).setImageResource(imageRes);
        ((TextView)  findViewById(R.id.tvDetailName)).setText(name);

        String formattedPrice = String.format(Locale.GERMANY, "%,d", price);
        ((TextView)  findViewById(R.id.tvDetailPrice)).setText(getString(R.string.cart_total_format, formattedPrice));

        String fullDesc = (desc != null ? desc : "") + getString(R.string.detail_desc_suffix);
        ((TextView)  findViewById(R.id.tvDetailDesc)).setText(fullDesc);

        ((TextView) findViewById(R.id.tvDetailDescAllergen)).setText(allergen != null ? allergen : "Tidak ada informasi alergen");

        btnFavorite = findViewById(R.id.btnFavorite);
        updateFavoriteUI();

        btnFavorite.setOnClickListener(v -> {
            CartManager.getInstance().toggleFavorite(currentMenu);
            updateFavoriteUI();
        });

        findViewById(R.id.btnPesanSekarang).setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("price", price);
            intent.putExtra("image", imageRes);
            startActivity(intent);
        });
    }

    private void updateFavoriteUI() {
        if (CartManager.getInstance().isFavorite(currentMenu)) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            btnFavorite.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            btnFavorite.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.icon_tint)));
        }
    }
}

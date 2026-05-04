package id.ac.budiluhur.fatpandas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.budiluhur.fatpandas.model.MenuPandas;
import java.util.Collections;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_favorite);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        RecyclerView rv = findViewById(R.id.rvFavorite);
        TextView tvEmpty = findViewById(R.id.tvEmptyFavorite);

        List<MenuPandas> favorites = CartManager.getInstance().getFavoriteItems();

        // 🔥 SORT ALPHABETICALLY
        Collections.sort(favorites, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));

        if (favorites.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            MenuAdapter adapter = new MenuAdapter(this, favorites);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adapter);
        }

        NavHelper.setup(this, "profil");
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.setup(this, "profil");
    }
}

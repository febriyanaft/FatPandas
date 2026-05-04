package id.ac.budiluhur.fatpandas;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        updateThemeIcon(btnThemeToggle);
        if (btnThemeToggle != null) {
            btnThemeToggle.setOnClickListener(v -> toggleTheme());
        }

        LinearLayout menuPesanan = findViewById(R.id.menuPesananSaya);
        if (menuPesanan != null) {
            menuPesanan.setOnClickListener(v -> {
                startActivity(new Intent(this, OrderListActivity.class));
            });
        }

        LinearLayout menuFavorit = findViewById(R.id.menuFavorit);
        if (menuFavorit != null) {
            menuFavorit.setOnClickListener(v -> {
                startActivity(new Intent(this, FavoriteActivity.class));
            });
        }

        LinearLayout menuTentang = findViewById(R.id.menuTentang);
        if (menuTentang != null) {
            menuTentang.setOnClickListener(v -> showAboutApp());
        }

        findViewById(R.id.btnLogout).setOnClickListener(v -> handleLogout());

        NavHelper.setup(this, "profil");
    }

    private void handleLogout() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View imgAlert = dialogView.findViewById(R.id.imgDialogAlert);
        if (imgAlert != null) imgAlert.setVisibility(View.GONE);

        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Logout");
        ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText("Apakah Anda yakin ingin keluar dari aplikasi Fat Pandas?");

        Button btnBatal = dialogView.findViewById(R.id.btnBatal);
        btnBatal.setText("Batal");
        btnBatal.setOnClickListener(v -> dialog.dismiss());

        Button btnKeluar = dialogView.findViewById(R.id.btnYa);
        btnKeluar.setText("Logout");
        btnKeluar.setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity();
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void updateThemeIcon(ImageView iv) {
        if (iv == null) return;
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            iv.setImageResource(R.drawable.ic_sun);
        } else {
            iv.setImageResource(R.drawable.ic_moon);
        }
    }

    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void showFavorites() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((ImageView) dialogView.findViewById(R.id.imgDialogAlert)).setImageResource(R.drawable.ic_favorite_border);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Menu Favorit");
        ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText("Fitur ini sedang dalam pengembangan. Segera kamu bisa menyimpan menu pilihanmu di sini!");

        dialogView.findViewById(R.id.btnBatal).setVisibility(View.GONE);
        Button btnOk = dialogView.findViewById(R.id.btnYa);
        btnOk.setText("Oke");
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showAboutApp() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((ImageView) dialogView.findViewById(R.id.imgDialogAlert)).setImageResource(R.drawable.logo);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Tentang FatPandas");
        ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText(
                "Fat Pandas adalah aplikasi pemesanan makanan Nusantara modern.\n\n" +
                "Versi: 1.0.0\n" +
                "Developer: Tim Fat Pandas"
        );

        dialogView.findViewById(R.id.btnBatal).setVisibility(View.GONE);
        Button btnOk = dialogView.findViewById(R.id.btnYa);
        btnOk.setText("Tutup");
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.setup(this, "profil");
    }
}

package id.ac.budiluhur.fatpandas;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import id.ac.budiluhur.fatpandas.model.BannerItem;
import id.ac.budiluhur.fatpandas.model.MenuPandas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.graphics.Rect;

public class MainActivity extends AppCompatActivity {

    private MenuAdapter adapter;
    private final List<MenuPandas> allMenus = new ArrayList<>();
    private TextView btnAll, btnDrink, btnFood, btnDessert;
    private EditText etSearch;
    private String currentCategory = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        RecyclerView rvMenu = findViewById(R.id.rvMenu);
        btnAll     = findViewById(R.id.btnAll);
        btnDrink   = findViewById(R.id.btnDrink);
        btnFood    = findViewById(R.id.btnFood);
        btnDessert = findViewById(R.id.btnDessert);
        etSearch   = findViewById(R.id.etSearch);

        currentCategory = "all";
        setActive(btnAll);

        setupMenuData();

        setupBanner();
        adapter = new MenuAdapter(this, new ArrayList<>(allMenus));
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(adapter);

        applyFilter();

        ImageView btnMenuBurger = findViewById(R.id.btnMenuBurger);
        btnMenuBurger.setOnClickListener(this::showSideMenu);

        View btnNotification = findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(v -> showNotifications());

        btnAll.setOnClickListener(v -> {
            currentCategory = "all";
            etSearch.setText(""); 
            applyFilter();
            setActive(btnAll);
        });

        btnDrink.setOnClickListener(v -> {
            currentCategory = "Minuman";
            etSearch.setText("");
            applyFilter();
            setActive(btnDrink);
        });

        btnFood.setOnClickListener(v -> {
            currentCategory = "Makanan";
            etSearch.setText("");
            applyFilter();
            setActive(btnFood);
        });

        btnDessert.setOnClickListener(v -> {
            currentCategory = "Cemilan";
            etSearch.setText("");
            applyFilter();
            setActive(btnDessert);
        });

        TextView tvSeeAll = findViewById(R.id.tvSeeAll);
        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v -> {
                Intent intent = new Intent(this, AllMenuActivity.class);
                startActivity(intent);
            });
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        NavHelper.setup(this, "beranda");
    }

    private void setupBanner() {
        ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);
        LinearLayout dotIndicator = findViewById(R.id.dotIndicator);
        dotIndicator.removeAllViews();

        List<BannerItem> bannerItems = new ArrayList<>();
        bannerItems.add(new BannerItem("Good Food\nGood Mood", "Nikmati makanan lezat\nala Fat Pandas!", R.drawable.logo));
        bannerItems.add(new BannerItem("Promo Spesial\nDiskon 20%", "Hanya untuk menu\nNasi Goreng Pandas!", R.drawable.ndas));
        bannerItems.add(new BannerItem("Menu Baru\nCemilan Lezat", "Cobain sensasi baru\nCheesecake Pandas!", R.drawable.nays));

        BannerAdapter bannerAdapter = new BannerAdapter(bannerItems);
        bannerViewPager.setAdapter(bannerAdapter);

        // infinite loop
        int middle = (Integer.MAX_VALUE / 2) - ((Integer.MAX_VALUE / 2) % bannerItems.size());
        bannerViewPager.setCurrentItem(middle, false);

        // DOTS INDICATOR
        ImageView[] dots = new ImageView[bannerItems.size()];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_indicator);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotIndicator.addView(dots[i], params);
        }

        // Set initial dot
        if (dots.length > 0) dots[0].setSelected(true);

        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int realPos = position % bannerItems.size();
                for (int i = 0; i < dots.length; i++) {
                    dots[i].setSelected(i == realPos);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.setup(this, "beranda");
    }

    private void setupMenuData() {
        allMenus.clear();
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
    }

    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        List<MenuPandas> result = new ArrayList<>();

        for (MenuPandas m : allMenus) {
            boolean matchCategory =
                    currentCategory.equalsIgnoreCase("all") ||
                            m.getCategory().equalsIgnoreCase(currentCategory);
            boolean matchSearch =
                    query.isEmpty() ||
                            m.getName().toLowerCase().contains(query);
            if (matchCategory && matchSearch) {
                result.add(m);
            }
        }
        Collections.sort(result, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
        adapter.updateList(result);
    }

    private void showSideMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        String themeText = (currentNightMode == Configuration.UI_MODE_NIGHT_YES) ? "Mode Terang" : "Mode Gelap";

        popup.getMenu().add("Informasi Toko");
        popup.getMenu().add("Promo Spesial");
        popup.getMenu().add(themeText);
        popup.getMenu().add("Bantuan & Dukungan");
        popup.getMenu().add("Keluar");

        popup.setOnMenuItemClickListener(item -> {
            String title = Objects.toString(item.getTitle(), "");
            switch (title) {
                case "Keluar":
                    finishAffinity();
                    break;
                case "Mode Terang":
                case "Mode Gelap":
                    toggleTheme();
                    break;
                case "Informasi Toko":
                    showStoreInfo();
                    break;
                case "Promo Spesial":
                    showPromos();
                    break;
                case "Bantuan & Dukungan":
                    showHelp();
                    break;
            }
            return true;
        });
        popup.show();
    }

    private void showStoreInfo() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((ImageView) dialogView.findViewById(R.id.imgDialogAlert)).setImageResource(R.drawable.info);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Informasi Toko");
        ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText(
                "📍 Alamat: Jl. Raya Ciledug No. 99, Jakarta Selatan\n\n" +
                "⏰ Jam Operasional:\n" +
                "Senin - Jumat: 10:00 - 21:00\n" +
                "Sabtu - Minggu: 09:00 - 22:00"
        );

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

    private void showPromos() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((ImageView) dialogView.findViewById(R.id.imgDialogAlert)).setImageResource(R.drawable.ndas);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Promo Spesial 🎁");
        ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText(
                "🔥 Promo Bulan Ini:\n\n" +
                "1. Diskon 20% untuk semua Makanan!\n" +
                "2. Buy 1 Get 1 Free untuk Thai Tea.\n" +
                "3. Gratis Rollcake untuk transaksi > 150rb."
        );

        Button btnBatal = dialogView.findViewById(R.id.btnBatal);
        btnBatal.setText("Tutup");
        btnBatal.setOnClickListener(v -> dialog.dismiss());

        Button btnKlaim = dialogView.findViewById(R.id.btnYa);
        btnKlaim.setText("Klaim");

        btnKlaim.setOnClickListener(v -> {
            dialog.dismiss();

            Toast toast = Toast.makeText(this, "Promo berhasil diaktifkan!", Toast.LENGTH_SHORT);
            toast.setGravity(android.view.Gravity.CENTER, 0, 0);
            toast.show();
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showHelp() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View btnClose = dialogView.findViewById(R.id.btnCloseDialog);
        if (btnClose != null) {
            btnClose.setVisibility(View.VISIBLE);
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        ((ImageView) dialogView.findViewById(R.id.imgDialogAlert)).setImageResource(R.drawable.dukungan);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Bantuan & Dukungan");
        ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText(
                "Punya kendala atau pertanyaan?\n\n" +
                "💬 WhatsApp: 0812-3456-7890\n" +
                "📧 Email: support@fatpandas.com"
        );

        dialogView.findViewById(R.id.btnBatal).setVisibility(View.GONE);

        Button btnHubungi = dialogView.findViewById(R.id.btnYa);
        btnHubungi.setText("Hubungi Kami");

        btnHubungi.setOnClickListener(v -> {
            dialog.dismiss();

            Toast toast = Toast.makeText(this, "Membuka WhatsApp...", Toast.LENGTH_SHORT);
            toast.setGravity(android.view.Gravity.CENTER, 0, 0);
            toast.show();
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private void showNotifications() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((ImageView) dialogView.findViewById(R.id.imgDialogAlert)).setImageResource(R.drawable.notif);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Notifikasi \uD83D\uDC8C");
        ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText(
                "• Promo Nasi Goreng Pandas Diskon 20%!\n" +
                "• Cek menu baru: Cheesecake Pandas!"
        );

        dialogView.findViewById(R.id.btnBatal).setVisibility(View.GONE);
        Button btnBaca = dialogView.findViewById(R.id.btnYa);
        btnBaca.setText("Tandai Dibaca");
        btnBaca.setOnClickListener(v -> {
            dialog.dismiss();
            View btnNotif = findViewById(R.id.btnNotification);
            if (btnNotif instanceof FrameLayout) {
                FrameLayout container = (FrameLayout) btnNotif;
                for (int i = 0; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);
                    if (!(child instanceof ImageView)) child.setVisibility(View.GONE);
                }
            }
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void setActive(TextView active) {
        for (TextView b : new TextView[]{btnAll, btnDrink, btnFood, btnDessert}) {
            if (b == active) {
                b.setBackgroundResource(R.drawable.bg_chip_active);
                b.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            } else {
                b.setBackgroundResource(R.drawable.bg_chip_inactive);
                b.setTextColor(ContextCompat.getColor(this, R.color.green));
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
package id.ac.budiluhur.fatpandas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.budiluhur.fatpandas.model.CartItem;
import id.ac.budiluhur.fatpandas.model.OrderItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private List<CartItem> checkoutItems = new ArrayList<>();
    private TextView tvTotal;
    private RadioGroup rgOrderType, rgPayment;
    private SummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_order);

        String singleName = getIntent().getStringExtra("name");
        if (singleName != null) {
            int price = getIntent().getIntExtra("price", 0);
            int img   = getIntent().getIntExtra("image", R.drawable.nasgor);
            checkoutItems.add(new CartItem(singleName, price, 1, img));
        } else {
            // Use CartManager to get selected items
            checkoutItems = CartManager.getInstance().getSelectedItems();
        }

        if (checkoutItems.isEmpty()) {
            finish();
            return;
        }

        tvTotal     = findViewById(R.id.tvTotal);
        rgOrderType = findViewById(R.id.rgOrderType);
        rgPayment   = findViewById(R.id.rgPayment);

        RecyclerView rv = findViewById(R.id.rvOrderItems);
        adapter = new SummaryAdapter(checkoutItems, this::updateTotalPrice);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        updateTotalPrice();

        findViewById(R.id.btnBack).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        findViewById(R.id.btnLanjutKonfirmasi).setOnClickListener(v -> {
            EditText etNama  = findViewById(R.id.etNama);
            EditText etPhone = findViewById(R.id.etPhone);
            EditText etMeja  = findViewById(R.id.etMeja);
            EditText etNotes = findViewById(R.id.etCatatan);

            String inputNama = etNama.getText().toString().trim();
            String inputPhone = etPhone.getText().toString().trim();

            if (inputNama.isEmpty()) {
                etNama.setError("Nama Harus Di Isi");
                etNama.requestFocus();
                return;
            } else if (inputPhone.isEmpty()) {
                etPhone.setError("No. Telp Harus Di Isi");
                etPhone.requestFocus();
                return;
            }

            final String nameLabel = inputNama;
            final String notes = etNotes.getText().toString().trim();
            final String meja  = etMeja.getText().toString().trim().isEmpty() ? "-" : etMeja.getText().toString().trim();
            final String waktu = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date());
            
            int totalSum = 0;
            for (CartItem i : checkoutItems) totalSum += i.getTotalPrice();
            final int finalTotal = totalSum;

            int typeId = rgOrderType.getCheckedRadioButtonId();
            final String orderType = ((RadioButton) findViewById(typeId)).getText().toString();

            int payId = rgPayment.getCheckedRadioButtonId();
            final String paymentMethod = ((RadioButton) findViewById(payId)).getText().toString();

            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
            
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            View btnBatalCustom = dialogView.findViewById(R.id.btnBatal);
            View btnYaCustom    = dialogView.findViewById(R.id.btnYa);

            if (btnBatalCustom != null) {
                btnBatalCustom.setOnClickListener(v1 -> dialog.dismiss());
            }

            if (btnYaCustom != null) {
                btnYaCustom.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    
                    StringBuilder detailBuilder = new StringBuilder();
                    for (CartItem item : checkoutItems) {
                        detailBuilder.append("• ")
                                .append(item.getName())
                                .append(" (")
                                .append(item.getQty())
                                .append("x)\n");
                    }

                    String orderId = String.format(Locale.getDefault(), "#FP%03d",
                            (CartManager.getInstance().getOrderHistory().size() + 1));
                    
                    String histName = checkoutItems.size() > 1 ? checkoutItems.get(0).getName() + " +" + (checkoutItems.size()-1) : checkoutItems.get(0).getName();

                    OrderItem order = new OrderItem(
                            orderId,
                            histName,
                            totalItemsCount(),
                            finalTotal,
                            waktu,
                            meja,
                            orderType,
                            paymentMethod,
                            detailBuilder.toString(),
                            notes
                    );
                    CartManager.getInstance().addOrder(order);

                    if (getIntent().getStringExtra("name") == null) {
                        CartManager.getInstance().removeCheckedOutItems(new ArrayList<>(checkoutItems));
                    }

                    Intent intent = new Intent(this, SuccessActivity.class);
                    intent.putExtra("name", histName);
                    intent.putExtra("qty", totalItemsCount());
                    intent.putExtra("total", finalTotal);
                    intent.putExtra("meja", meja);
                    intent.putExtra("waktu", waktu);
                    intent.putExtra("type", orderType);
                    intent.putExtra("pay", paymentMethod);
                    startActivity(intent);
                    finish();
                });
            }

            dialog.show();

            // 🔥 FIX: Set width to 90% of screen to prevent "narrow/sempit" look
            if (dialog.getWindow() != null) {
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    private int totalItemsCount() {
        int count = 0;
        for (CartItem i : checkoutItems) count += i.getQty();
        return count;
    }

    private void updateTotalPrice() {
        int total = 0;
        for (CartItem i : checkoutItems) total += i.getTotalPrice();
        String formattedTotal = String.format(Locale.GERMANY, "%,d", total);
        tvTotal.setText(getString(R.string.cart_total_format, formattedTotal));
    }

    static class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.VH> {
        List<CartItem> items;
        Runnable onQtyChanged;

        SummaryAdapter(List<CartItem> items, Runnable onQtyChanged) {
            this.items = items;
            this.onQtyChanged = onQtyChanged;
        }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_order_summary, p, false));
        }

        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            CartItem i = items.get(pos);
            h.name.setText(i.getName());
            h.qty.setText(String.valueOf(i.getQty()));
            h.price.setText(i.getFormattedPrice());
            h.img.setImageResource(i.getImageRes());

            h.btnPlus.setOnClickListener(v -> {
                i.setQty(i.getQty() + 1);
                h.qty.setText(String.valueOf(i.getQty()));
                onQtyChanged.run();
            });

            h.btnMinus.setOnClickListener(v -> {
                if (i.getQty() > 1) {
                    i.setQty(i.getQty() - 1);
                    h.qty.setText(String.valueOf(i.getQty()));
                    onQtyChanged.run();
                }
            });
        }

        @Override public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView name, qty, price;
            ImageView img, btnPlus, btnMinus;
            VH(View v) {
                super(v);
                name = v.findViewById(R.id.tvSummaryName);
                qty = v.findViewById(R.id.tvSummaryQty);
                price = v.findViewById(R.id.tvSummaryPrice);
                img = v.findViewById(R.id.imgSummary);
                btnPlus = v.findViewById(R.id.btnSummaryPlus);
                btnMinus = v.findViewById(R.id.btnSummaryMinus);
            }
        }
    }
}

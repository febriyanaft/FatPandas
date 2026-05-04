package id.ac.budiluhur.fatpandas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.budiluhur.fatpandas.model.CartItem;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private TextView tvTotal, tvItemCount;
    private LinearLayout emptyState;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_cart);

        rvCart       = findViewById(R.id.rvCart);
        tvTotal      = findViewById(R.id.tvCartTotal);
        tvItemCount  = findViewById(R.id.tvCartItemCount);
        emptyState   = findViewById(R.id.emptyState);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        cartAdapter = new CartAdapter(CartManager.getInstance().getCartItems(), this::updateTotals);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        updateTotals();

        findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            List<CartItem> toCheckout = CartManager.getInstance().getSelectedItems();
            if (toCheckout.isEmpty()) {
                Toast t = Toast.makeText(this, R.string.toast_cart_empty, Toast.LENGTH_SHORT);
                t.setGravity(android.view.Gravity.CENTER, 0, 0);
                t.show();
            } else {
                Intent intent = new Intent(this, OrderActivity.class);
                // Passing multiple items is tricky with just extras, let's use a flag or temporary storage
                // For simplicity, we'll mark them in CartManager and let OrderActivity fetch them
                startActivity(intent);
            }
        });

        NavHelper.setup(this, "keranjang");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTotals();
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void updateTotals() {
        int total = CartManager.getInstance().getSelectedTotal();
        int count = CartManager.getInstance().getSelectedCount();
        String formattedTotal = String.format(Locale.GERMANY, "%,d", total);
        tvTotal.setText(getString(R.string.cart_total_format, formattedTotal));
        tvItemCount.setText(getString(R.string.cart_item_count_format, count));

        boolean empty = CartManager.getInstance().getCartItems().isEmpty();
        emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvCart.setVisibility(empty ? View.GONE : View.VISIBLE);

        NavHelper.updateBadge(this);
    }

    static class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
        List<CartItem> items;
        Runnable onChanged;

        CartAdapter(List<CartItem> items, Runnable onChanged) {
            this.items = items;
            this.onChanged = onChanged;
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            CartItem item = items.get(pos);
            h.img.setImageResource(item.getImageRes());
            h.tvName.setText(item.getName());
            h.tvPrice.setText(item.getFormattedPrice());
            h.tvQty.setText(String.valueOf(item.getQty()));
            
            h.cbSelect.setOnCheckedChangeListener(null);
            h.cbSelect.setChecked(item.isSelected());
            h.cbSelect.setOnCheckedChangeListener((v, isChecked) -> {
                item.setSelected(isChecked);
                onChanged.run();
            });

            h.btnPlus.setOnClickListener(v -> {
                item.setQty(item.getQty() + 1);
                notifyItemChanged(h.getBindingAdapterPosition());
                onChanged.run();
            });
            h.btnMinus.setOnClickListener(v -> {
                int currentPos = h.getBindingAdapterPosition();
                if (item.getQty() > 1) {
                    item.setQty(item.getQty() - 1);
                    notifyItemChanged(currentPos);
                    onChanged.run();
                } else {
                    showDeleteDialog(h.itemView.getContext(), currentPos);
                }
            });
            h.btnDelete.setOnClickListener(v -> showDeleteDialog(h.itemView.getContext(), h.getBindingAdapterPosition()));
        }

        private void showDeleteDialog(android.content.Context context, int position) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_order, null);
            AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
            if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            View imgAlert = dialogView.findViewById(R.id.imgDialogAlert);
            if (imgAlert != null) imgAlert.setVisibility(View.GONE);

            ((TextView) dialogView.findViewById(R.id.tvTitle)).setText(R.string.content_desc_delete);
            ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText(R.string.confirm_delete_msg);

            Button btnBatal = dialogView.findViewById(R.id.btnBatal);
            Button btnHapus = dialogView.findViewById(R.id.btnYa);

            btnBatal.setText(R.string.btn_cancel);
            btnBatal.setOnClickListener(v -> dialog.dismiss());

            btnHapus.setText(R.string.btn_confirm_hapus);
            btnHapus.setOnClickListener(v -> {
                CartManager.getInstance().removeItem(position);
                notifyItemRemoved(position);
                onChanged.run();
                dialog.dismiss();
            });

            dialog.show();
            if (dialog.getWindow() != null) {
                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
                dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }

        @Override public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            ImageView img, btnPlus, btnMinus, btnDelete;
            TextView tvName, tvPrice, tvQty;
            CheckBox cbSelect;
            VH(@NonNull View v) {
                super(v);
                cbSelect  = v.findViewById(R.id.cbSelect);
                img       = v.findViewById(R.id.imgCartItem);
                tvName    = v.findViewById(R.id.tvCartName);
                tvPrice   = v.findViewById(R.id.tvCartPrice);
                tvQty     = v.findViewById(R.id.tvCartQty);
                btnPlus   = v.findViewById(R.id.btnCartPlus);
                btnMinus  = v.findViewById(R.id.btnCartMinus);
                btnDelete = v.findViewById(R.id.btnCartDelete);
            }
        }
    }
}

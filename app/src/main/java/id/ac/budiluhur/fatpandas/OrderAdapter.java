package id.ac.budiluhur.fatpandas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.budiluhur.fatpandas.model.OrderItem;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderItem> orders;

    public OrderAdapter(Context context, List<OrderItem> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem o = orders.get(position);
        holder.tvId.setText(o.getOrderId());
        holder.tvName.setText(o.getMenuName());
        holder.tvQty.setText(o.getQty() + "x item");
        holder.tvTotal.setText(o.getFormattedTotal());
        holder.tvTime.setText(o.getTime());
        holder.tvStatus.setText(o.getStatus());

        holder.itemView.setOnClickListener(v -> {
            String detail = (o.getDetailItems() == null || o.getDetailItems().isEmpty())
                    ? o.getMenuName()
                    : o.getDetailItems();

            String message =
                    "ID Pesanan: " + o.getOrderId() + "\n\n" +
                            "Menu: " + o.getMenuName() + "\n" +
                            "Jumlah: " + o.getQty() + " item\n" +
                            "Total: " + o.getFormattedTotal() + "\n\n" +
                            "Detail Pesanan:\n" + detail;

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_order, null);
            AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
            if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            ((ImageView) dialogView.findViewById(R.id.imgDialogAlert)).setImageResource(R.drawable.logo);
            ((TextView) dialogView.findViewById(R.id.tvTitle)).setText("Detail Pesanan");
            ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setText(message);
            ((TextView) dialogView.findViewById(R.id.tvDialogMessage)).setGravity(android.view.Gravity.CENTER);

            dialogView.findViewById(R.id.btnBatal).setVisibility(View.GONE);
            Button btnOk = dialogView.findViewById(R.id.btnYa);
            btnOk.setText("Tutup");
            btnOk.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
            if (dialog.getWindow() != null) {
                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
                dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    @Override
    public int getItemCount() { return orders.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvQty, tvTotal, tvTime, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId     = itemView.findViewById(R.id.tvOrderId);
            tvName   = itemView.findViewById(R.id.tvOrderMenuName);
            tvQty    = itemView.findViewById(R.id.tvOrderQty);
            tvTotal  = itemView.findViewById(R.id.tvOrderTotal);
            tvTime   = itemView.findViewById(R.id.tvOrderTime);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}

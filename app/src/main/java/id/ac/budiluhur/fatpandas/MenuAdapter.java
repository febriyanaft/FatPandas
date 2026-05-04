package id.ac.budiluhur.fatpandas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.budiluhur.fatpandas.model.CartItem;
import id.ac.budiluhur.fatpandas.model.MenuPandas;
import java.util.List;
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final Context context;
    private List<MenuPandas> menuList;
    public MenuAdapter(Context context, List<MenuPandas> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    public void updateList(List<MenuPandas> newList) {
        this.menuList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuPandas menu = menuList.get(position);
        holder.tvName.setText(menu.getName());
        holder.tvDesc.setText(menu.getDescription());
        holder.tvPrice.setText(menu.getFormattedPrice());
        holder.imgMenu.setImageResource(menu.getImageRes());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", menu.getName());
            intent.putExtra("desc", menu.getDescription());
            intent.putExtra("allergen", menu.getAllergen());
            intent.putExtra("price", menu.getPrice());
            intent.putExtra("image", menu.getImageRes());
            context.startActivity(intent);
        });

        holder.btnAdd.setOnClickListener(v -> {
            CartManager.getInstance().addItem(
                    new CartItem(menu.getName(), menu.getPrice(), 1, menu.getImageRes())
            );
            Toast toast = Toast.makeText(
                    context,
                    context.getString(R.string.toast_menu_added_format, menu.getName()),
                    Toast.LENGTH_SHORT
            );
            toast.setGravity(android.view.Gravity.CENTER, 0, 0);
            toast.show();
            
            if (context instanceof Activity) {
                NavHelper.updateBadge((Activity) context);
            }
        });
    }

    @Override
    public int getItemCount() { return menuList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenu, btnAdd;
        TextView tvName, tvDesc, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            btnAdd  = itemView.findViewById(R.id.btnAdd);
            tvName  = itemView.findViewById(R.id.tvMenuName);
            tvDesc  = itemView.findViewById(R.id.tvMenuDesc);
            tvPrice = itemView.findViewById(R.id.tvMenuPrice);
        }
    }
}
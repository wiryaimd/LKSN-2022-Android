package com.wiryaimd.pc_bali.ui.main.fragment.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.wiryaimd.pc_bali.BaseApplication;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.model.CartModel;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    private List<CartModel> cartList;
    private BaseApplication baseApplication;

    private Listener listener;

    public interface Listener{
        void price(CartModel cartModel, int count);
    }

    public CartAdapter(List<CartModel> cartList, BaseApplication baseApplication, Listener listener) {
        this.cartList = cartList;
        this.baseApplication = baseApplication;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyHolder holder, int position) {
        CartModel cartModel = cartList.get(position);
        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(cartModel.getImg(), 0, cartModel.getImg().length));
        holder.name.setText(cartModel.getName());
        holder.price.setText(String.format(Locale.getDefault(), "%,d", cartModel.getPrice()));
        holder.num.setText(String.valueOf(cartModel.getCount()));

        final int[] count = {cartModel.getCount()};

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0] += 1;
                holder.num.setText(String.valueOf(count[0]));
                baseApplication.getSharedPreferences().edit().putInt(cartModel.getId() + ":=cart", count[0]).apply();

                listener.price(cartModel, count[0]);
            }
        });

        holder.btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count[0] == 1){
                    return;
                }
                count[0] -= 1;
                holder.num.setText(String.valueOf(count[0]));
                baseApplication.getSharedPreferences().edit().putInt(cartModel.getId() + ":=cart", count[0]).apply();

                listener.price(cartModel, count[0]);
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseApplication.getSharedPreferences().edit().remove(cartModel.getId() + ":=cart").apply();
                cartList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

                listener.price(cartModel, -1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView name, price, num;
        public MaterialButton btnMin, btnPlus, btnRemove;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.cart_item_img);
            name = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            num = itemView.findViewById(R.id.cart_item_count);
            btnMin = itemView.findViewById(R.id.cart_item_min);
            btnPlus = itemView.findViewById(R.id.cart_item_plus);
            btnRemove = itemView.findViewById(R.id.cart_item_remove);

        }
    }
}

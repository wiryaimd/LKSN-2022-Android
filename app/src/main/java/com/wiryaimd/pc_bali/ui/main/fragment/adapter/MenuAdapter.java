package com.wiryaimd.pc_bali.ui.main.fragment.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.model.MenuModel;
import com.wiryaimd.pc_bali.ui.main.fragment.DetailMenuFragment;

import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyHolder> {

    private List<MenuModel> menuList;
    private FragmentManager fragmentManager;

    public MenuAdapter(List<MenuModel> menuList, FragmentManager fragmentManager) {
        this.menuList = menuList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MenuAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MyHolder holder, int position) {
        MenuModel menuModel = menuList.get(position);
        String name = menuModel.getNama() + "\n" + String.format(Locale.getDefault(), "%,d", menuModel.getPrice());
        holder.name.setText(name);
        if (menuModel.getImg() != null){
            holder.img.setImageBitmap(BitmapFactory.decodeByteArray(menuModel.getImg(), 0, menuModel.getImg().length));
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.main_frame, new DetailMenuFragment(menuModel));
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public MaterialCardView card;
        public ImageView img;
        public TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.menuitem_card);
            img = itemView.findViewById(R.id.menuitem_img);
            name = itemView.findViewById(R.id.menuitem_name);

        }
    }
}

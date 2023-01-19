package com.wiryaimd.pc_bali.ui.main.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.wiryaimd.pc_bali.BaseApplication;
import com.wiryaimd.pc_bali.HttpUtils;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.dto.ResponseDto;
import com.wiryaimd.pc_bali.model.MenuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executors;

public class DetailMenuFragment extends Fragment {

    private MenuModel menuModel;

    private ImageView img;
    private TextView tvTitle, tvDesc, tvPrice, tvCount;
    private MaterialButton btnMin, btnPlus, btnCart;

    private BaseApplication baseApplication;

    private int count = 1;

    public DetailMenuFragment(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menuitem, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        img = view.findViewById(R.id.menudetail_img);
        tvTitle = view.findViewById(R.id.menudetail_title);
        tvDesc = view.findViewById(R.id.menudetail_desc);
        tvPrice = view.findViewById(R.id.menudetail_price);
        btnMin = view.findViewById(R.id.menudetail_min);
        btnPlus = view.findViewById(R.id.menudetail_plus);
        tvCount = view.findViewById(R.id.menudetail_count);
        btnCart = view.findViewById(R.id.menudetail_addtocart);

        baseApplication = (BaseApplication) requireActivity().getApplication();

        tvTitle.setText(menuModel.getNama());
        tvPrice.setText(String.format(Locale.getDefault(), "%,d", menuModel.getPrice()));
        img.setImageBitmap(BitmapFactory.decodeByteArray(menuModel.getImg(), 0, menuModel.getImg().length));

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ResponseDto responseDto = HttpUtils.get("api/menu/" + menuModel.getId(), null, false);
                if (responseDto.getCode() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(responseDto.getJson());
                        String desc = jsonObject.getString("description");
                        tvDesc.post(new Runnable() {
                            @Override
                            public void run() {
                                tvDesc.setText(desc);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    HttpUtils.showToast(requireActivity(), "Failed to fetch Description");
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count += 1;
                tvCount.setText(String.valueOf(count));
            }
        });

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 1){
                    return;
                }
                count -= 1;
                tvCount.setText(String.valueOf(count));
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> set = baseApplication.getSharedPreferences().getStringSet("cart", new HashSet<>());

                Set<String> modif = new HashSet<>(set);
                modif.add(menuModel.getId());
                baseApplication.getSharedPreferences().edit().putStringSet("cart", modif).apply();

                int sum = count + baseApplication.getSharedPreferences().getInt(menuModel.getId() + ":=cart", 0);
                baseApplication.getSharedPreferences().edit().putInt(menuModel.getId() + ":=cart", sum).apply();

                Toast.makeText(requireActivity(), "Menu added to cart", Toast.LENGTH_LONG).show();
            }
        });

    }
}

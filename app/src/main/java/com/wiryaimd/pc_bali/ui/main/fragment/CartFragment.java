package com.wiryaimd.pc_bali.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.wiryaimd.pc_bali.BaseApplication;
import com.wiryaimd.pc_bali.HttpUtils;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.dto.ResponseDto;
import com.wiryaimd.pc_bali.model.CartModel;
import com.wiryaimd.pc_bali.ui.main.fragment.adapter.CartAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executors;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private BaseApplication baseApplication;

    private MaterialButton btnOrder;
    private TextView tvTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        baseApplication = (BaseApplication) requireActivity().getApplication();

        recyclerView = view.findViewById(R.id.cart_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        tvTotal = view.findViewById(R.id.cart_price);
        btnOrder = view.findViewById(R.id.cart_btn_order);

        List<CartModel> cartList = new ArrayList<>();
        Set<String> set = baseApplication.getSharedPreferences().getStringSet("cart", new HashSet<>());
        Iterator<String> iterator = set.iterator();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (iterator.hasNext()){
                    String id = iterator.next();
                    int count = baseApplication.getSharedPreferences().getInt(id + ":=cart", -1);
                    if (count != -1) {
                        ResponseDto responseDto = HttpUtils.get("api/menu/" + id, null, false);
                        ResponseDto imgRes = HttpUtils.get("api/menu/" + id + "/photo", "", true);
                        try {
                            JSONObject jsonObject = new JSONObject(responseDto.getJson());
                            if (responseDto.getCode() == 200) {
                                cartList.add(new CartModel(id, jsonObject.getString("name"), jsonObject.getInt("price"), count, imgRes.getCode() == 200 ? imgRes.getData() : null));
                            } else {
                                HttpUtils.showToast(requireActivity(), "Failed to get some Menu");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                CartAdapter adapter = new CartAdapter(cartList, baseApplication, new CartAdapter.Listener() {
                                    @Override
                                    public void price(CartModel cartModel, int count) {
                                        long sum = 0;
                                        for (int i = 0; i < cartList.size(); i++) {
                                            if (cartList.get(i).equals(cartModel) && count == -1){
                                                continue;
                                            }

                                            if (cartList.get(i).equals(cartModel)){
                                                sum += ((long) cartModel.getPrice() * count);
                                            }else{
                                                sum += ((long) cartList.get(i).getPrice() * cartList.get(i).getCount());
                                            }
                                        }
                                        tvTotal.setText(String.format(Locale.getDefault(), "Rp. %,d", sum));
                                    }
                                });
                                recyclerView.setAdapter(adapter);

                                long sum = 0;
                                for (int i = 0; i < cartList.size(); i++) {
                                    sum += ((long) cartList.get(i).getPrice() * cartList.get(i).getCount());
                                }
                                tvTotal.setText(String.format(Locale.getDefault(), "Rp. %,d", sum));
                            }
                        });
                    }
                }
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

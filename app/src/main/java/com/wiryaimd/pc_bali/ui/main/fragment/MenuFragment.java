package com.wiryaimd.pc_bali.ui.main.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.wiryaimd.pc_bali.BaseApplication;
import com.wiryaimd.pc_bali.HttpUtils;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.dto.ResponseDto;
import com.wiryaimd.pc_bali.model.MenuModel;
import com.wiryaimd.pc_bali.ui.main.fragment.adapter.MenuAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    private TabLayout tableLayout;
    private BaseApplication baseApplication;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));

        tableLayout = view.findViewById(R.id.menu_tablayout);
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: tab click: " + tab.getPosition());
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        ResponseDto responseDto;
                        switch (tab.getPosition()){
                            case 0:
                                responseDto = HttpUtils.get("api/menu/category/ayam", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 1:
                                responseDto = HttpUtils.get("api/menu/category/camilan", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 2:
                                responseDto = HttpUtils.get("api/menu/category/dagingsapi", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 3:
                                responseDto = HttpUtils.get("api/menu/category/happymeal", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 4:
                                responseDto = HttpUtils.get("api/menu/category/ikan", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 5:
                                responseDto = HttpUtils.get("api/menu/category/makananpenutup", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 6:
                                responseDto = HttpUtils.get("api/menu/category/minuman", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 7:
                                responseDto = HttpUtils.get("api/menu/category/paketfamily", null, false);
                                setupMenuItem(responseDto);
                                break;
                            case 8:
                                responseDto = HttpUtils.get("api/menu/category/sarapanpagi", null, false);
                                setupMenuItem(responseDto);
                                break;

                        }
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ResponseDto responseDto = HttpUtils.get("api/menu/category/ayam", null, false);
                setupMenuItem(responseDto);
            }
        });
    }

    public void setupMenuItem(ResponseDto responseDto){
        if (responseDto.getCode() == 200){
            try {
                JSONArray jsonArray = new JSONArray(responseDto.getJson());
                List<MenuModel> menuList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String id = json.getString("id");

                    ResponseDto imgRes = HttpUtils.get("api/menu/" + id + "/photo", "", true);

                    MenuModel menuModel = new MenuModel(
                            id,
                            json.getString("name"),
                            json.getInt("price"),
                            imgRes.getCode() == 200 ? imgRes.getData() : null
                    );
                    menuList.add(menuModel);
                }

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        MenuAdapter adapter = new MenuAdapter(menuList, getParentFragmentManager());
                        recyclerView.setAdapter(adapter);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            HttpUtils.showToast(requireActivity(), "Failed to fetch Menu");
        }
    }
}

package com.wiryaimd.pc_bali;

import android.app.Application;
import android.content.SharedPreferences;

import com.wiryaimd.pc_bali.model.TableCodeModel;

public class BaseApplication extends Application {

    private SharedPreferences sharedPreferences;
    private TableCodeModel tableCodeModel;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("AppPCBali", MODE_PRIVATE);
    }

    public TableCodeModel getTableCodeModel() {
        return tableCodeModel;
    }

    public void setTableCodeModel(TableCodeModel tableCodeModel) {
        this.tableCodeModel = tableCodeModel;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}

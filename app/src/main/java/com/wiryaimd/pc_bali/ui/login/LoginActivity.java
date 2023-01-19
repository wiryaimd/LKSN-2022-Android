package com.wiryaimd.pc_bali.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.wiryaimd.pc_bali.BaseApplication;
import com.wiryaimd.pc_bali.HttpUtils;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.dto.ResponseDto;
import com.wiryaimd.pc_bali.model.TableCodeModel;
import com.wiryaimd.pc_bali.ui.main.MainActivity;
import com.wiryaimd.pc_bali.ui.staff.StaffAcitivty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText edtCode;
    private MaterialButton btnStaff, btnSubmit;

    private BaseApplication baseApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtCode = findViewById(R.id.login_edt_tablecode);
        btnSubmit = findViewById(R.id.login_btn_submit);
        btnStaff = findViewById(R.id.login_btn_staff);

        baseApplication = (BaseApplication) getApplication();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtCode.getText().toString();
                if (code.trim().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Table Code is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        ResponseDto responseDto = HttpUtils.post("api/table/" + code, null,  "");
                        if (responseDto.getCode() == 200){
                            JSONObject json = null;
                            try {
                                json = new JSONObject(responseDto.getJson());

                                baseApplication.setTableCodeModel(new TableCodeModel(
                                    json.getString("id"),
                                    json.getInt("number")
                                ));

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                HttpUtils.showToast(LoginActivity.this, "Table Code not found");
                            }
                        }else{
                            HttpUtils.showToast(LoginActivity.this, "Table Code not found");
                        }
                    }
                });
            }
        });

        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, StaffAcitivty.class));
            }
        });

    }
}

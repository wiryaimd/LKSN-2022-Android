package com.wiryaimd.pc_bali.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.wiryaimd.pc_bali.HttpUtils;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.dto.ResponseDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

public class StaffAcitivty extends AppCompatActivity {

    private EditText edtUser, edtPass;
    private MaterialButton btnBack, btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stafflogin);

        edtUser = findViewById(R.id.login_edt_username);
        edtPass = findViewById(R.id.login_edt_pass);
        btnBack = findViewById(R.id.login_btn_backto);
        btnLogin = findViewById(R.id.login_btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                if (email.trim().isEmpty() || pass.trim().isEmpty()){
                    Toast.makeText(StaffAcitivty.this, "Email or Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(StaffAcitivty.this, "Email format not valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("email", email);
                            jsonObject.put("password", pass);

                            ResponseDto responseDto = HttpUtils.post("api/auth", null, jsonObject.toString());
                            if (responseDto.getCode() == 200){
                                startActivity(new Intent(StaffAcitivty.this, TableActivity.class));
                                finish();
                            }else{
                                HttpUtils.showToast(StaffAcitivty.this, "Email or Password is invalid");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}

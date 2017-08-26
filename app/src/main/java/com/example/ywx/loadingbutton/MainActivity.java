package com.example.ywx.loadingbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ywx.viewlibrary.LoadingButton;

public class MainActivity extends AppCompatActivity {
    private LoadingButton loadingButton;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        loadingButton=(LoadingButton)findViewById(R.id.loadButton);
        loadingButton.setOnButtonClickListener(new LoadingButton.OnButtonClickListener() {
            @Override
            public void onButtonClick() {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingButton.reset();
            }
        });
    }
}

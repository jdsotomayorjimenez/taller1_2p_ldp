package com.example.taller_1_2p;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import android.app.Activity;

public class MainActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAccion = findViewById(R.id.btnAccion);
        btnAccion.setOnClickListener(view ->
                Toast.makeText(this, R.string.button_pressed_message, Toast.LENGTH_SHORT).show()
        );
    }
}

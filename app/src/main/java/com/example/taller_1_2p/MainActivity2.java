package com.example.taller_1_2p;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    private FeedReaderDbHelper dbHelper;
    private TableLayout tablaPersonas;
    private TableRow filaPersonaVacia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new FeedReaderDbHelper(this);
        tablaPersonas = findViewById(R.id.tablaPersonas);
        filaPersonaVacia = findViewById(R.id.filaPersonaVacia);

        Button btnVolverRegistro = findViewById(R.id.btnVolverRegistro);
        btnVolverRegistro.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        cargarPersonas();
    }

    private void cargarPersonas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO
        };
        String sortOrder = FeedReaderContract.FeedEntry._ID + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor.getCount() == 0) {
            filaPersonaVacia.setVisibility(View.VISIBLE);
        } else {
            filaPersonaVacia.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE)
                );
                String apellido = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO)
                );

                agregarFila(id, nombre, apellido);
            }
        }

        cursor.close();
        db.close();
    }

    private void agregarFila(String id, String nombre, String apellido) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        row.addView(crearCelda(id, 76));
        row.addView(crearCelda(nombre, 140));
        row.addView(crearCelda(apellido, 140));
        tablaPersonas.addView(row);
    }

    private TextView crearCelda(String texto, int anchoDp) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(dp(anchoDp), dp(56)));
        textView.setBackgroundResource(R.drawable.tabla_cell_background);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setText(texto);
        textView.setTextColor(0xFF665C6B);
        textView.setTextSize(15);
        return textView;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}

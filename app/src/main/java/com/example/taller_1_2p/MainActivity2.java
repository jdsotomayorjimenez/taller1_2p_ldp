package com.example.taller_1_2p;

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

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private TableLayout tblistado;
    private String[] cabecera = {"Id", "Nombre", "Apellido"};
    private DynamicTable creaTabla;
    private ArrayList<String[]> datos = new ArrayList<String[]>();
    private FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        dbHelper = new FeedReaderDbHelper(this);
        tblistado = findViewById(R.id.tablaPersonas);
        tblistado.removeAllViews();
        creaTabla = new DynamicTable(tblistado, getApplicationContext());
        creaTabla.setCabecera(cabecera);
        TraerDatos();

        creaTabla.setDatos(datos);
        creaTabla.crearCabecera();
        creaTabla.crearFilas();

        Button btnVolverRegistro = findViewById(R.id.btnVolverRegistro);
        btnVolverRegistro.setOnClickListener(this::Regresar);
    }

    private void TraerDatos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };
        String sortOrder = BaseColumns._ID + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            String[] fila = new String[3];
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
            fila[0] = itemId + "";
            fila[1] = nombre;
            fila[2] = apellido;
            datos.add(fila);
        }
        cursor.close();
        db.close();
    }

    public void Regresar(View vista) {
        finish();
    }
    
    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}

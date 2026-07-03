package com.example.taller_1_2p;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;
    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new FeedReaderDbHelper(this);

        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txtapellido = findViewById(R.id.txtapellido);

        Button btnGuardar = findViewById(R.id.btnguardar);
        Button btnActualizar = findViewById(R.id.btnactualizar);
        Button btnBuscar = findViewById(R.id.btnbuscar);
        Button btnEliminar = findViewById(R.id.btneliminar);
        Button btnListar = findViewById(R.id.btnlistar);

        btnGuardar.setOnClickListener(this::guardar);
        btnActualizar.setOnClickListener(this::actualizar);
        btnBuscar.setOnClickListener(this::buscar);
        btnEliminar.setOnClickListener(this::eliminar);
        btnListar.setOnClickListener(this::listar);
    }

    public void listar(View vista) {
        Intent listar = new Intent(this, MainActivity2.class);
        startActivity(listar);
    }

    public void guardar(View vista) {
        String nombre = txtnombre.getText().toString().trim();
        String apellido = txtapellido.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Ingrese nombre y apellido", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE, nombre);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO, apellido);

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        db.close();

        if (newRowId == -1) {
            Toast.makeText(this, "No se pudo guardar el registro", Toast.LENGTH_SHORT).show();
            return;
        }

        txtid.setText(String.valueOf(newRowId));
        Toast.makeText(this, "Registro guardado con ID: " + newRowId, Toast.LENGTH_SHORT).show();
    }

    public void buscar(View vista) {
        String id = txtid.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO
        };
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE)
            );
            String apellido = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO)
            );

            txtnombre.setText(nombre);
            txtapellido.setText(apellido);
            Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe una persona con ese ID", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    public void actualizar(View vista) {
        String id = txtid.getText().toString().trim();
        String nombre = txtnombre.getText().toString().trim();
        String apellido = txtapellido.getText().toString().trim();

        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Ingrese ID, nombre y apellido", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {id};
        int count = db.update(
                FeedReaderContract.FeedEntry.nameTable,
                values,
                selection,
                selectionArgs
        );
        Toast.makeText(this, "Registros actualizados: " + count, Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void eliminar(View vista) {
        String id = txtid.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {id};
        int deletedRows = db.delete(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                selection,
                selectionArgs
        );

        if (deletedRows > 0) {
            long registrosRestantes = DatabaseUtils.queryNumEntries(
                    db,
                    FeedReaderContract.FeedEntry.TABLE_NAME
            );
            if (registrosRestantes == 0) {
                db.execSQL(
                        "DELETE FROM sqlite_sequence WHERE name = ?",
                        new String[]{FeedReaderContract.FeedEntry.TABLE_NAME}
                );
            }
            txtid.setText("");
            txtnombre.setText("");
            txtapellido.setText("");
        }

        db.close();
        Toast.makeText(this, "Registros eliminados: " + deletedRows, Toast.LENGTH_SHORT).show();
    }
}

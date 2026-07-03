package com.example.taller_1_2p;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;

import java.util.ArrayList;

public class DynamicTable {
    private static final float[] PESOS_COLUMNAS = {0.7f, 1.3f, 1.3f};

    private TableLayout tabla;
    private Context contexto;
    private String[] cabecera;
    private ArrayList<String[]> datos;
    private TableRow fila;
    private TextView celda;

    public DynamicTable(TableLayout tabla, Context contexto) {
        this.tabla = tabla;
        this.contexto = contexto;
        this.tabla.setStretchAllColumns(true);
        this.tabla.setShrinkAllColumns(true);
    }

    public void setCabecera(String[] cabecera) {
        this.cabecera = cabecera;
    }

    public void setDatos(ArrayList<String[]> datos) {
        this.datos = datos;
    }

    private void nuevaFila() {
        fila = new TableRow(contexto);
        fila.setGravity(Gravity.CENTER);
    }

    private void nuevaCelda(boolean esCabecera) {
        celda = new TextView(contexto);
        celda.setGravity(Gravity.CENTER);
        celda.setPadding(dp(8), 0, dp(8), 0);
        celda.setSingleLine(false);
        celda.setTextSize(esCabecera ? 14 : 15);
        celda.setTextColor(esCabecera ? Color.WHITE : Color.parseColor("#665C6B"));
        celda.setTypeface(null, esCabecera ? Typeface.BOLD : Typeface.NORMAL);
        if (!esCabecera) {
            celda.setBackgroundResource(R.drawable.tabla_cell_background);
        }
    }

    public void crearCabecera() {
        nuevaFila();
        fila.setBackgroundResource(R.drawable.tabla_header_background);
        for (int i = 0; i < cabecera.length; i++) {
            nuevaCelda(true);
            String titulo = cabecera[i];
            celda.setText(titulo);
            fila.addView(celda, parametrosCelda(i, true));
        }
        tabla.addView(fila);
    }

    public void crearFilas() {
        if (datos == null || datos.isEmpty()) {
            crearFila(new String[]{"-", "Sin registros", "-"});
            return;
        }

        for (String[] datosfila:datos) {
            crearFila(datosfila);
        }
    }

    private void crearFila(String[] datosFila) {
        nuevaFila();
        for (int i = 0; i < cabecera.length; i++) {
            nuevaCelda(false);
            celda.setText(i < datosFila.length ? datosFila[i] : "");
            fila.addView(celda, parametrosCelda(i, false));
        }
        tabla.addView(fila);
    }

    public TableRow.LayoutParams parametrosCelda(int columna, boolean esCabecera) {
        TableRow.LayoutParams parametros = new TableRow.LayoutParams(
                0,
                dp(esCabecera ? 48 : 56),
                PESOS_COLUMNAS[Math.min(columna, PESOS_COLUMNAS.length - 1)]
        );

        return parametros;
    }

    private int dp(int value) {
        return (int) (value * contexto.getResources().getDisplayMetrics().density + 0.5f);
    }
}

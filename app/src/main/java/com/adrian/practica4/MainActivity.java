package com.adrian.practica4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.adrian.practica4.adapters.ItemsAdapter;
import com.adrian.practica4.database.DbHelper;
import com.adrian.practica4.model.Articulo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add;
    SearchView searchView;
    RecyclerView itemsList;
    AlertDialog dialog;
    DbHelper helper;
    //Data
    List<Articulo> articulos;
    //Adapter
    ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        events();
        loadData();
    }

    private void loadData() {
        searchView.setQuery("",false);
        articulos = Articulo.getList(helper.select(Articulo.getSelect(),new String[0]));
        adapter = new ItemsAdapter(this, articulos);
        setListenerItems();
        itemsList.setAdapter(adapter);
        itemsList.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setListenerItems() {
        adapter.setOnItemsEvents(new ItemsAdapter.OnItemsEvents() {
            @Override
            public void OnEditClick(Articulo articulo, int position) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.article_view, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Actualizar el articulo")
                        .setView(view);
                EditText codigo =view.findViewById(R.id.editCodigo), price = view.findViewById(R.id.editPrice), description = view.findViewById(R.id.editDescription);
                Button guardar = view.findViewById(R.id.btnSave), cancel =  view.findViewById(R.id.btnCancel);
                codigo.setText(String.valueOf(articulo.getCodigo()));
                price.setText(String.valueOf(articulo.getPrecio()));
                description.setText(articulo.getDescripcion());
                guardar.setOnClickListener(vv->{
                    Articulo temp = new Articulo(Integer.parseInt(codigo.getText().toString()),description.getText().toString(),Double.parseDouble(price.getText().toString()));
                    long id = helper.update(Articulo.NAME,temp.getContentValues(true),String.format("%s = %s",Articulo.CODIGO,temp.getCodigo()));
                    if(id > 0){
                        Toast.makeText(MainActivity.this,"Se actualizo el articulo",Toast.LENGTH_LONG).show();
                        loadData();
                        dialog.dismiss();
                    }
                    else
                        Toast.makeText(MainActivity.this,"No se actualizo el articulo",Toast.LENGTH_LONG).show();

                });
                cancel.setOnClickListener(vv->{
                    dialog.dismiss();
                });
                dialog = builder.show();
            }

            @Override
            public void OnRemoveClick(Articulo articulo, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Eliminar Articulo")
                        .setMessage(String.format("Desea eliminar el articulo \n %s $%s",articulo.getDescripcion(),articulo.getPrecio()))
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            int rows = helper.remove(Articulo.NAME,String.format("%s = %s",Articulo.CODIGO,articulo.getCodigo()));
                            if(rows < 1){
                                Toast.makeText(MainActivity.this,"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                                return;
                            }

                            Toast.makeText(MainActivity.this,"Se elimino el articulo",Toast.LENGTH_LONG).show();
                            loadData();

                        })
                        .setNegativeButton("Cancelar",(dialog,which)->{
                            Toast.makeText(MainActivity.this,"Se cancelo la operacion",Toast.LENGTH_LONG).show();
                        });
                builder.show();
            }
        });
    }


    private void events() {
        add.setOnClickListener(v->{
            View view = LayoutInflater.from(this).inflate(R.layout.article_view, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Agregar nuevo")
                    .setView(view);
            EditText codigo =view.findViewById(R.id.editCodigo), price = view.findViewById(R.id.editPrice), description = view.findViewById(R.id.editDescription);
            Button guardar = view.findViewById(R.id.btnSave), cancel =  view.findViewById(R.id.btnCancel);
            guardar.setOnClickListener(vv->{
                Articulo articulo = new Articulo(Integer.parseInt(codigo.getText().toString()),description.getText().toString(),Double.parseDouble(price.getText().toString()));
                long id = helper.insert(Articulo.NAME,articulo.getContentValues(true));
                if(id >= 0){
                    Toast.makeText(this,"Se registro el articulo",Toast.LENGTH_LONG).show();
                    loadData();
                    dialog.dismiss();
                }
                else
                    Toast.makeText(this,"No se registro el articulo",Toast.LENGTH_LONG).show();

            });
            cancel.setOnClickListener(vv->{
                dialog.dismiss();
            });
            dialog = builder.show();
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Articulo> list = new ArrayList<>();
                for (Articulo articulo :
                        articulos) {
                    if(String.valueOf(articulo.getCodigo()).contains(newText) || articulo.getDescripcion().contains(newText))
                        list.add(articulo);
                }
                adapter = new ItemsAdapter(MainActivity.this, list);
                setListenerItems();
                itemsList.setAdapter(adapter);
                return true;
            }
        });
    }

    private void init() {
        add = findViewById(R.id.addButton);
        searchView = findViewById(R.id.searchView);
        itemsList = findViewById(R.id.itemsList);

        helper = new DbHelper(this);
        articulos = new ArrayList<>();

    }
}
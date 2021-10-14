package com.adrian.practica4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrian.practica4.R;
import com.adrian.practica4.model.Articulo;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.Holder> {

    private List<Articulo> articulos;
    private Context context;
    private OnItemsEvents onItemsEvents;
    public ItemsAdapter(Context context, List<Articulo> articulos){
        this.context = context;
        this.articulos = articulos;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.items_card, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        h.edit.setOnClickListener(null);
        h.remove.setOnClickListener(null);
        h.codigo.setText(String.valueOf(articulos.get(position).getCodigo()));
        h.price.setText(String.format("$ %s", articulos.get(position).getPrecio()));
        h.description.setText(articulos.get(position).getDescripcion());

        h.edit.setOnClickListener(v->{
            if(onItemsEvents != null)
                onItemsEvents.OnEditClick(articulos.get(position),position);
        });
        h.remove.setOnClickListener(v->{
            if(onItemsEvents != null)
                onItemsEvents.OnRemoveClick(articulos.get(position),position);
        });
    }

    @Override
    public int getItemCount() {
        return articulos.size();
    }

    public void setOnItemsEvents(OnItemsEvents onItemsEvents){
        this.onItemsEvents = onItemsEvents;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView codigo, price, description;
        ImageButton edit, remove;
        public Holder(@NonNull View i) {
            super(i);
            codigo = i.findViewById(R.id.txtCodigo);
            price = i.findViewById(R.id.txtPrice);
            description = i.findViewById(R.id.txtDescription);
            edit = i.findViewById(R.id.btnEdit);
            remove = i.findViewById(R.id.btnRemove);
        }
    }

    public interface OnItemsEvents{
        void OnEditClick(Articulo articulo, int position);
        void OnRemoveClick(Articulo articulo, int position);
    }
}

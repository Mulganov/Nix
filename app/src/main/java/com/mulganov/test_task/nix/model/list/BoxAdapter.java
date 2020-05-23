package com.mulganov.test_task.nix.model.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mulganov.test_task.nix.Presenter;
import com.mulganov.test_task.nix.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Info> objects;
    Presenter presenter;

    public BoxAdapter(Context context, ArrayList<Info> elements, Presenter presenter) {
        this.presenter = presenter;
        ctx = context;
        objects = elements;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.adapter_main, parent, false);
        }

        Info p = getProduct(position);

        view.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
        view.findViewById(R.id.textView).setVisibility(View.VISIBLE);
        view.findViewById(R.id.checkBox).setOnClickListener(v -> presenter.onClick.adapter.Check( ((CheckBox)v).isChecked() , p));

        ((CheckBox)view.findViewById(R.id.checkBox)).setChecked(p.check);

        if (p.bitmap != null) {
            ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(p.bitmap);
            view.findViewById(R.id.textView).setVisibility(View.INVISIBLE);
        }
        else {
            ((TextView) view.findViewById(R.id.textView)).setText(p.text);
            view.findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
        }


        return view;
    }

    // товар по позиции
    Info getProduct(int position) {
        return ((Info) getItem(position));
    }

}
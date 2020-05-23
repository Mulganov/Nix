package com.mulganov.test_task.nix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.room.Room;

import com.mulganov.test_task.nix.model.db.DB;
import com.mulganov.test_task.nix.model.db.Element;
import com.mulganov.test_task.nix.model.list.Info;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Presenter {

    private MainActivity activity;

    public onClick onClick;

    public Add add;

    public Room room;

    public Presenter(MainActivity mainActivity){
        activity = mainActivity;
        onClick = new onClick(this);
        add = new Add(this);
        room = new Room(this);
        System.out.println("Presenter create");
    }

    public static class Room{
        private Presenter presenter;
        private final String nameBD = "Nix";

        private ArrayList<Info> infos = new ArrayList<>();

        public Room(Presenter presenter){
            this.presenter = presenter;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DB db = androidx.room.Room.databaseBuilder(presenter.activity,
                            DB.class, nameBD).build();

                    for (Element e: db.getElementDoa().getAllElement()){
                        infos.add(new Info(e));
                    }

                    presenter.activity.reloadList(infos);
                }
            }
            ).start();
        }

        public void add(Info info) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DB db = androidx.room.Room.databaseBuilder(presenter.activity,
                            DB.class, nameBD).build();

                    db.getElementDoa().insertAll(new Element(info));

                    infos.add(info);

                    presenter.activity.reloadList(infos);
                }
            }
            ).start();
        }

        public void save(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DB db = androidx.room.Room.databaseBuilder(presenter.activity,
                            DB.class, nameBD).build();

                    for (Element e: db.getElementDoa().getAllElement()){
                        db.getElementDoa().delete(e);
                    }

                    for (Info i: infos){
                        System.out.println(i.text);
                        db.getElementDoa().insertAll(new Element(i));
                    }
                }
            }
            ).start();
        }

    }

    public static class Add{
        private Presenter presenter;

        public Add(Presenter presenter){
            this.presenter = presenter;
        }

        public void onResult(File file, Bitmap bitmap) {
            presenter.onClick.popup.bitmap = bitmap;
            presenter.onClick.popup.file = file;
        }
    }

    public static class onClick{
        private Presenter presenter;
        private Popup popup = new Popup();

        public Adapter adapter;

        public onClick( Presenter presenter){
            this.presenter = presenter;
            adapter = new Adapter(presenter);
        }

        public void add(View view){

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    presenter.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_add, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it

            popupView.findViewById(R.id.pButton).setOnClickListener(v -> popup.button(popupView.findViewById(R.id.pText)));

            popupView.findViewById(R.id.pImage).setOnClickListener(popup::image);
            popupView.findViewById(R.id.pCamera).setOnClickListener(popup::camera);


            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken

            popupWindow.showAtLocation(presenter.activity.findViewById(R.id.main), Gravity.CENTER,0, 0);

            popup.popupWindow = popupWindow;
        }

        public void all(View view){
            ((CheckBox)presenter.activity.findViewById(R.id.checkBox_otmetka)).setChecked(false);
            check_otmetka(presenter.activity.findViewById(R.id.checkBox_otmetka));

            presenter.activity.reloadList(presenter.room.infos);
        }

        public void on(View view){
            ((CheckBox)presenter.activity.findViewById(R.id.checkBox_otmetka)).setChecked(false);
            check_otmetka(presenter.activity.findViewById(R.id.checkBox_otmetka));

            ArrayList<Info> a = new ArrayList<>();

            for (Info i: presenter.room.infos){
                if (i.kyplen)
                    a.add(i);
            }

            presenter.activity.reloadList(a);
        }

        public void off(View view){
            ((CheckBox)presenter.activity.findViewById(R.id.checkBox_otmetka)).setChecked(false);
            check_otmetka(presenter.activity.findViewById(R.id.checkBox_otmetka));
            
            ArrayList<Info> a = new ArrayList<>();

            for (Info i: presenter.room.infos){
                if (!i.kyplen)
                    a.add(i);
            }

            presenter.activity.reloadList(a);
        }

        public void button_otmetka_on(View view) {
            for (Info i: presenter.onClick.adapter.infos.keySet())
                i.kyplen = true;

            presenter.room.save();
        }

        public void check_otmetka(View view) {
            for (Info info: presenter.room.infos){
                info.check = ((CheckBox)view).isChecked();

                if (((CheckBox)view).isChecked())
                    presenter.onClick.adapter.infos.put(info, ((CheckBox)view).isChecked());
                else
                    presenter.onClick.adapter.infos.remove(info);
            }

            if (!((CheckBox)view).isChecked()){
                presenter.activity.findViewById(R.id.button_otmetka_on).setAlpha(0.3f);
                presenter.activity.findViewById(R.id.button_otmetka_on).setClickable(false);
            }
            else {
                presenter.activity.findViewById(R.id.button_otmetka_on).setAlpha(1f);
                presenter.activity.findViewById(R.id.button_otmetka_on).setClickable(true);
            }

            presenter.activity.reloadList(presenter.room.infos);
        }

        public static class Adapter{
            private Presenter presenter;
            public HashMap<Info, Boolean> infos = new HashMap<Info, Boolean>();

            public Adapter(Presenter presenter){
                this.presenter = presenter;
            }

            public void Check(boolean check, Info info){
                if (check)
                    infos.put(info, check);
                else
                    infos.remove(info);

                if (infos.size() == 0){
                    presenter.activity.findViewById(R.id.button_otmetka_on).setAlpha(0.3f);
                    presenter.activity.findViewById(R.id.button_otmetka_on).setClickable(false);

                    ((CheckBox)presenter.activity.findViewById(R.id.checkBox_otmetka)).setChecked(false);
                }
                else {
                    presenter.activity.findViewById(R.id.button_otmetka_on).setAlpha(1f);
                    presenter.activity.findViewById(R.id.button_otmetka_on).setClickable(true);
                }

            }

        }

        public class Popup{

            private PopupWindow popupWindow;
            private Bitmap bitmap;
            public File file;

            public void image(View view) {
                // Галерея
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            presenter.activity.startActivityForResult(intent, 1);
            }

            public void button(TextView view) {
                if (view.getText().length() == 0){
                    view.setError("Поле пустое");
                    return;
                }

                String text = view.getText() + "";

                Info info = new Info();
                info.text = text;
                info.bitmap = bitmap;
                info.kyplen = false;

                if (file != null){
                    info.file = file + "/" + text + ".png";

                    file.mkdirs();

                    // Save Bitmap
                    try (FileOutputStream out = new FileOutputStream(info.file)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("FIle: " + info.file + " is: "+ new File(info.file).isFile());
                }else{
                    info.file = null;
                }

                popupWindow.dismiss();

                presenter.room.add(info);
            }

            public void camera(View view) {
                // Камера
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            presenter.activity.startActivityForResult(cameraIntent, 0);

            }

        }
    }
}

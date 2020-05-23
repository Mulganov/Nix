package com.mulganov.test_task.nix;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.mulganov.test_task.nix.model.list.BoxAdapter;
import com.mulganov.test_task.nix.model.list.Info;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new Presenter(this);

        registerOnClicks();

    }

    public void reloadList(ArrayList<Info> infos) {
        BoxAdapter adapter = new BoxAdapter(this, infos, presenter);

        runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  ((ListView)findViewById(R.id.listContent)).setAdapter(adapter);
              }
          }
        );
    }

    private void registerOnClicks() {
        findViewById(R.id.button_add).setOnClickListener(presenter.onClick::add);

        findViewById(R.id.button_all).setOnClickListener(presenter.onClick::all);

        findViewById(R.id.button_on).setOnClickListener(presenter.onClick::on);

        findViewById(R.id.button_off).setOnClickListener(presenter.onClick::off);


        findViewById(R.id.checkBox_otmetka).setOnClickListener(presenter.onClick::check_otmetka);

        findViewById(R.id.button_otmetka_on).setOnClickListener(presenter.onClick::button_otmetka_on);

        findViewById(R.id.button_otmetka_on).setClickable(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Фотка сделана, извлекаем картинку
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");

            presenter.add.onResult(new File(getFilesDir() + "/save/img/"), thumbnailBitmap);
        }

        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null) {

            // Получаем URI изображения
            Uri imageUri = data.getData();

            if (imageUri != null) {
                try {

                    // Получаем InputStream, из которого будем декодировать Bitmap
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);

                    // Декодируем Bitmap
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    presenter.add.onResult(new File(getFilesDir() + "/save/img/"), bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        presenter.room.save();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        presenter.room.save();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        presenter.room.save();
//    }
}

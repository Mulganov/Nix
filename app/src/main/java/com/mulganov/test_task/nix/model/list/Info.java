package com.mulganov.test_task.nix.model.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mulganov.test_task.nix.model.db.Element;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Info {
    public String text;
    public Bitmap bitmap;
    public boolean kyplen;
    public String file;
    public boolean check;

    public Info(){}

    public Info(Element e) {
        text = e.getText();
        kyplen = e.isKyplen();
        file = e.getImg();

        if (file != null)
            try { // Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(file), null, o);
                // The new size we want to scale to
                int REQUIRED_SIZE = 70;
                // Find the correct scale value. It should be the power of 2.
                int scale = 1;
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                        o.outHeight / scale / 2 >= REQUIRED_SIZE
                ) {
                    scale *= 2;
                }
                // Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
            } catch (FileNotFoundException ex) {
            }

        System.out.println(file + " " + bitmap);
    }
}

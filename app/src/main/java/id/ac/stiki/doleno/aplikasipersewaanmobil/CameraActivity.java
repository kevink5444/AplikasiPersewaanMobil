package id.ac.stiki.doleno.aplikasipersewaanmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageview;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        this.imageview = (ImageView) this.findViewById(R.id.imageCamera);
        Button photobutton = (Button) this.findViewById(R.id.buttonCamera);
        photobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent, CAMERA_REQUEST);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);

            ImageView image = new ImageView(this);
            image.setImageBitmap(photo);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this).
                            setMessage("Apakah Ingin Menyimpan Foto?").
                            setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    imageview.setImageBitmap(photo);
                                    SaveImage(photo);
                                    dialog.dismiss();
                                }
                            }).
                            setView(image);

            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private void SaveImage(Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String nuFile = System.currentTimeMillis() + ".jpg";
        File files = new File(Environment.getExternalStorageDirectory() + File.separator + nuFile);

        try {
            files.createNewFile();
        } catch (IOException e) {
            Toast.makeText(CameraActivity.this,e.toString(), Toast.LENGTH_LONG).show();
        }
        if (files.exists()) {
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(files);
            } catch (FileNotFoundException e) {
                Toast.makeText(CameraActivity.this,e.toString(), Toast.LENGTH_LONG).show();
            }
            try {
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                Toast.makeText(CameraActivity.this,e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
package com.example.sapphire;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class ItamCodeDetail extends AppCompatActivity {

    TextView itemcodeTextView;
    TextView descriptionTexttView;
    ImageView imageView;
    Button btncapture;
    ServerConfigration sc = new ServerConfigration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itam_code_detail);
        imageView = findViewById(R.id.ItemCode_Image_Display);
        btncapture = findViewById(R.id.Capture_Imgae_btn);
        itemcodeTextView = (TextView) findViewById(R.id.textitem);
        itemcodeTextView.setText(getIntent().getStringExtra("ItemCode"));
        descriptionTexttView = (TextView) findViewById(R.id.textDescription);
        descriptionTexttView.setText(getIntent().getStringExtra("ItemName"));

        ///
        try {
            //Get Channel
            ChannelSftp channelSftp = (ChannelSftp) sc.getChannel();
            //Set Directory
            channelSftp.cd (sc.FileServerDirectory);
            InputStream imageStream = (InputStream) channelSftp.get(getIntent().getStringExtra("FileName") + ".jpg");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(imageStream);
            imageView.setImageBitmap(BitmapFactory.decodeStream(bufferedInputStream));
        } catch (SftpException e) {
            e.printStackTrace();
        }

        ///


        if (ContextCompat.checkSelfPermission(ItamCodeDetail.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ItamCodeDetail.this, new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }
        btncapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
            SaveMeAsFile(captureImage);
        }
    }

    public void SaveMeAsFile(Bitmap captureImage) {
        try {
            //Get Channel
            ChannelSftp channelSftp = (ChannelSftp) sc.getChannel();
            //Set Directory
            channelSftp.cd(sc.FileServerDirectory);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            captureImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            // Get the ByteArrayInputStream.
            ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
            channelSftp.put(bs, getIntent().getStringExtra("FileName") + ".jpg");

        } catch (SftpException e) {
            e.printStackTrace();
        }
    }


}

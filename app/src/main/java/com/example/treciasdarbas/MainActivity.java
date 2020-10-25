package com.example.treciasdarbas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageView;
    private CanvasView canvasView;
    private TextView textvieww;

    private Button autorius;
    private int defaultColor;

    Button btnColorPick;

    private final int REQ_CODE_SPEECH_OUTPUT = 143;
    private Button openMic;

    private TextView showVoiceText;
    private ToggleButton toogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        canvasView = findViewById(R.id.canvas);

        textvieww = findViewById(R.id.textView);
        textvieww.setText("");

        btnColorPick = findViewById(R.id.spalva);
        autorius = findViewById(R.id.autorius);
        btnColorPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColourPicker();
            }
        });

        openMic = findViewById(R.id.button);
        showVoiceText = findViewById(R.id.textView2);
        toogle = findViewById(R.id.trintukas);

        canvasView.clearCanvas();
        textvieww.setText("");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }


    Bitmap bitmap = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap)extras.get("data");
            imageView.setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
            //
            switch(requestCode){
                case REQ_CODE_SPEECH_OUTPUT: {
                    if(resultCode == RESULT_OK && null != data){
                        ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        //showVoiceText.setText(voiceInText.get(0));

                        String keyWord = voiceInText.get(0);
                        if(keyWord.equals("autorius")){
                            onAutorysteClick(textvieww);
                        }
                        else if(keyWord.equals("trintukas")) {
                            toogle.setChecked(!toogle.isChecked());
                            if(toogle.isChecked()) {
                                canvasView.setCanDraw(true);
                            } else {
                                canvasView.setCanDraw(false);
                            }
                        }
                    }
                    break;
                }

    }}

    public void onAutorysteClick(View v){
        if(textvieww.getText().toString() == "Egle Sabaliauskaite MKDf-16/3"){
            textvieww.setText("");
        }
        else textvieww.setText("Egle Sabaliauskaite MKDf-16/3");

    }
    public void onToggleClicked(View v){
        if(((ToggleButton) v).isChecked()) {
            canvasView.setCanDraw(true);
        } else {
            canvasView.setCanDraw(false);
        }
    }


    @Override
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnColorPick);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_colors, popupMenu.getMenu());
        //btnToOpenMic();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.white:
                        canvasView.setmPenColor(Color.WHITE);
                        canvasView.addPath();
                        return true;
                    case R.id.gray:
                        canvasView.setmPenColor(Color.GRAY);
                        canvasView.addPath();
                        return true;
                    case R.id.red:
                        canvasView.setmPenColor(Color.RED);
                        canvasView.addPath();
                        return true;
                    case R.id.black:
                        canvasView.setmPenColor(Color.BLACK);
                        canvasView.addPath();
                        return true;
                    default:
                        return true;
                }
            }
        });
        popupMenu.show();

        }
    public void btnToOpenMic(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "lt-LT");

        startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
    }
    private void openColourPicker () {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

                Toast.makeText(MainActivity.this, "Unavailable", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                defaultColor = color;
                canvasView.setmPenColor(color);

            }

        });

        ambilWarnaDialog.show();

    }
}


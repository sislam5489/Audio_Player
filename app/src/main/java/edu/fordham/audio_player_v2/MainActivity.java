package edu.fordham.audio_player_v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button pickButton;
    private Button playButton;
    private TextView filePathTextView;
    private Uri audioFile;

    SimpleExoPlayer player;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePathTextView = findViewById(R.id.file);
        playButton = findViewById(R.id.play);
        pickButton = findViewById(R.id.select);
        playButton.setClickable(false);

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //let the user select a file
                Intent pickIntent = new Intent();
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                pickIntent.setType("audio/*");
                //ley users get content
                startActivityForResult(pickIntent, 1);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //play the media fil0
                if(!isPlaying){
                    startPlaying();
                   // playButton.setText("Start Playing");
                }else{
                    stopPlaying();
                   // playButton.setText("Stop Playing");
                }
            }
        });
        playButton.setClickable(false);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    200
            );
    }

    void startPlaying() {
        player = new SimpleExoPlayer.Builder(this).build();
        MediaItem mediaItem = MediaItem.fromUri(audioFile);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(true);
        player.prepare();
        isPlaying = true;
        playButton.setText("Stop Playing");
    }

    void stopPlaying(){
        player.release();
        player = null;
        isPlaying = false;
        playButton.setText("Start Playing");
    }
    //were back from whatever activity happened and returned to main activity
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check if everything okay
        if(resultCode == RESULT_OK && requestCode == 1) {
            //return path to audio file
            if (data == null) throw new AssertionError();
            audioFile = data.getData();
            filePathTextView.setText(audioFile.getPath());
            playButton.setClickable(true);
        } else {
            filePathTextView.setText("No file selected");
            playButton.setClickable(false);
        }

    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted!",Toast.LENGTH_SHORT).show();
                playButton.setClickable(true);
                pickButton.setClickable(true);
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                playButton.setClickable(false);
                pickButton.setClickable(false);
            }
        }
    }
*/
}
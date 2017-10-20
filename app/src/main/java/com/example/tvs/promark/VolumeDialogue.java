package com.example.tvs.promark;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class VolumeDialogue extends Dialog implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar volumeSeekbar;
    private int volume, defaultVol;
    private TextView volumePercentText;
    private Button cancelVolume, setVolume;
    private Profile.Action action;
    private VolumeHandler volumeHandler;

    public VolumeDialogue(Context context, Profile.Action action, VolumeHandler volumeHandler) {
        super(context);
        this.action = action;
        this.volume = action.ringingVolume;
        this.defaultVol = action.ringingVolume;
        this.volumeHandler = volumeHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volume_dialogue);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);

        setVolume = (Button) findViewById(R.id.setVolume);
        cancelVolume = (Button) findViewById(R.id.cancelVolume);
        volumePercentText = (TextView) findViewById(R.id.volumePercentText);
        volumeSeekbar = (SeekBar) findViewById(R.id.volumeSeekbar);

        setVolume.setOnClickListener(this);
        cancelVolume.setOnClickListener(this);
        volumePercentText.setText(action.ringingVolume + " ");
        volumeSeekbar.setProgress(action.ringingVolume);
        volumeSeekbar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        volumePercentText.setText(progress + " ");
        volume = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.setVolume:
                volumeHandler.volumeSet(volume);
                dismiss();
                break;
            case R.id.cancelVolume:
                volumeHandler.volumeSet(defaultVol);
                dismiss();
                break;
        }
    }

    public interface VolumeHandler {
        void volumeSet(int volume);
    }
}
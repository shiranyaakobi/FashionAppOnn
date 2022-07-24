package com.idc.fashion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PickImageDialog extends DialogFragment {
    Button cameraBtn, galleryBtn;

    IImagePicker iImagePicker;

    public PickImageDialog(IImagePicker iImagePicker) {
        this.iImagePicker = iImagePicker;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pick_image, null, false);
        cameraBtn = view.findViewById(R.id.cameraBtn);
        galleryBtn = view.findViewById(R.id.galleryBtn);
        cameraBtn.setOnClickListener(v -> {
            iImagePicker.openCamera();
            dismiss();
        });
        galleryBtn.setOnClickListener(v -> {
            iImagePicker.openGallery();
            dismiss();
        });
        return new AlertDialog.Builder(getContext()).setTitle("FashionApp")
                .setView(view).create();
    }
}

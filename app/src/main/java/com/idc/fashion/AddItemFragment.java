package com.idc.fashion;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.idc.fashion.Firebase.Database;
import com.idc.fashion.Model.Category;
import com.idc.fashion.Model.Condition;
import com.idc.fashion.Model.Item;
import com.idc.fashion.Model.Size;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemFragment extends Fragment implements IImagePicker, AdapterView.OnItemSelectedListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_CHOOSE_PHOTO = 2;
    private String category;
    private ImageView itemAvatar;
    private EditText itemName, itemBrand, itemDesc, itemPrice;
    private RadioGroup radioGroup;
    private Button addButton;
    private Uri photoUri;
    String itemSize;

    String[] size = {"XXL", "XL", "L", "M", "S", "XS"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);


        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
        itemAvatar = view.findViewById(R.id.item_avatar);
        itemName = view.findViewById(R.id.item_name_editText);
        itemBrand = view.findViewById(R.id.item_brand_editText);
        itemDesc = view.findViewById(R.id.item_desc_editText);
        itemPrice = view.findViewById(R.id.item_price_editText);
        Spinner spin = view.findViewById(R.id.size_spinner);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, size);
        aa.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(this);
        itemAvatar.setOnClickListener(v -> {
            PickImageDialog dialog = new PickImageDialog(AddItemFragment.this);
            dialog.show(getChildFragmentManager(), "pick_image");
        });

        radioGroup = view.findViewById(R.id.item_condition_group);

        addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> addItemToCategory());

        return view;
    }

    private void addItemToCategory() {
        String name = itemName.getText().toString();
        String brand = itemBrand.getText().toString();
        String desc = itemDesc.getText().toString();
        int price = 0;
        if (!itemPrice.getText().toString().isEmpty()) {
            price = Integer.parseInt(itemPrice.getText().toString());
        }
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = getActivity().findViewById(selectedId);
        String condition = Condition.BAD;

        if (radioButton != null) {
            String choice = radioButton.getText().toString();
            switch (choice) {
                case "Excellent":
                    condition = Condition.EXCELLENT;
                    break;
                case "Good":
                    condition = Condition.GOOD;
                    break;
                case "Bad":
                default:
                    condition = Condition.BAD;
                    break;
            }
        }
        if (photoUri == null) {
            Toast.makeText(getContext(), "You must pick a photo or capture one first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!name.isEmpty() && !brand.isEmpty() && price != 0 && condition != null && itemSize != null) {
            Item itemToAdd = new Item(FirebaseAuth.getInstance().getUid(), category, name, null, brand, desc, condition, itemSize, price);
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle("FashionApp");
            dialog.setMessage("Adding item...");
            dialog.show();
            Database.ItemsManager.saveItem(itemToAdd, photoUri, s -> {
                dialog.dismiss();
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            }, e -> {
                dialog.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        } else {

            Toast.makeText(getContext(), "Need to fill all fields", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                itemAvatar.setImageBitmap(photo);
                photoUri = getImageUri(photo);
            } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
                photoUri = data.getData();
                itemAvatar.setImageURI(photoUri);
            }
        }
    }


    @Override
    public void openGallery() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, REQUEST_CHOOSE_PHOTO);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void openCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, REQUEST_TAKE_PHOTO);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (size[position]) {
            case "XXL":
                itemSize = Size.XXL;
                break;
            case "XL":
                itemSize = Size.XL;
                break;
            case "L":
                itemSize = Size.L;
                break;
            case "M":
                itemSize = Size.M;
                break;
            case "S":
                itemSize = Size.S;
                break;
            case "XS":
                itemSize = Size.XS;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}

package com.idc.fashion;

import static android.view.View.GONE;

import static com.idc.fashion.Firebase.Database.USERS_PATH;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.idc.fashion.Firebase.Database;
import com.idc.fashion.Model.Item;
import com.squareup.picasso.Picasso;

public class ShowItemFragment extends Fragment {

    Item item;
    Button contactButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_item, container, false);
        if (getArguments() != null) {
            String itemString = getArguments().getString("item");
            Gson gson = new Gson();
            item = gson.fromJson(itemString, Item.class);
            ImageView avatar = view.findViewById(R.id.item_pic);
            contactButton = view.findViewById(R.id.contactSeller);
            TextView name = view.findViewById(R.id.item_name);
            TextView brand = view.findViewById(R.id.item_brand);
            TextView desc = view.findViewById(R.id.item_desc);
            TextView price = view.findViewById(R.id.item_price);
            TextView condition = view.findViewById(R.id.item_condition);
            TextView size = view.findViewById(R.id.item_size);
            ImageView delete_button = view.findViewById(R.id.btn_delete);

            if (item != null) {
                Picasso.get().load(SecurityHelper.Decrypt(item.getImageAddress())).into(avatar);
                name.setText(item.getName());
                brand.setText(item.getBrand());
                desc.setText(item.getDescription());
                price.setText(item.getPrice() + "₪");
                size.setText(item.getSize());
                condition.setText(condition.getText() + item.getCondition());

                if (!item.getOwnerId().equals(FirebaseAuth.getInstance().getUid())) {
                    delete_button.setVisibility(GONE);
                }

            }
            contactButton.setOnClickListener(v -> {
                Intent email = new Intent(Intent.ACTION_SEND);
                FirebaseFirestore.getInstance()
                        .collection(USERS_PATH)
                        .document(item.getOwnerId())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String ownerEmail = documentSnapshot.getString("email");
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{FirebaseAuth.getInstance().getCurrentUser().getEmail(), ownerEmail});
                            email.putExtra(Intent.EXTRA_SUBJECT, "Fashion App - " + item.getCategory() + " " + item.getName());
                            email.putExtra(Intent.EXTRA_TEXT, "Hey i am interested in buying " + item.getName() + " !");
                            //need this to prompts email client only
                            email.setType("message/rfc822");
                            startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        });
            });

            delete_button.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("FashionApp")
                        .setMessage("Are you sure you would like to delete " + item.getName() + " ?")
                        .setPositiveButton("Yes", (x, y) ->
                                Database.ItemsManager.removeItem(item, s -> {
                                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(this).popBackStack();
                                }, e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show())).setNegativeButton("No", null)
                        .show();
            });
        }
        return view;
    }
}

package com.idc.fashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    private EditText nameEdit, emailEdit, descEdit;
    private Button sendButton, resetButton;

    public ContactFragment() {
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        nameEdit = view.findViewById(R.id.name_editText);
        emailEdit = view.findViewById(R.id.email_editText);
        descEdit = view.findViewById(R.id.desc_editText);

        sendButton = view.findViewById(R.id.send_button);
        resetButton = view.findViewById(R.id.reset_button);
        sendButton.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();
            String email = emailEdit.getText().toString();
            String desc = descEdit.getText().toString();
            if(!name.isEmpty() && !email.isEmpty() && !desc.isEmpty()) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, "Email from: " + name);
                i.putExtra(Intent.EXTRA_TEXT, desc);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetButton.setOnClickListener(v -> {
            nameEdit.setText("");
            emailEdit.setText("");
            descEdit.setText("");
        });

        return view;
    }

}

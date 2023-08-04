package com.silentswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.silentswitch.databinding.ActivityAddContactBinding;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends AppCompatActivity implements ContactAdapter.ContactInterface {
    private ActivityAddContactBinding binding;
    private ViewModel viewModel;
    private ContactAdapter adapter;
    private  BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ContactAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        viewModel.getContact().observe(this, new Observer<List<ContactModel>>() {
            @Override
            public void onChanged(List<ContactModel> contactModels) {
                if (contactModels != null){
                    if (contactModels.size() == 0){
                        binding.emptyContact.setVisibility(View.VISIBLE);
                    }else {
                        binding.emptyContact.setVisibility(View.GONE);
                    }
                    adapter.submitList(contactModels);
                }
            }
        });
        showBottomSheetDialog();

        binding.addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
    }
    private void showBottomSheetDialog() {

       bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_contact);


        EditText name = bottomSheetDialog.findViewById(R.id.name);
        EditText number = bottomSheetDialog.findViewById(R.id.number);

        Button button = bottomSheetDialog.findViewById(R.id.save_contact);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().trim().isEmpty() && !number.getText().toString().trim().isEmpty()) {
                    bottomSheetDialog.dismiss();
                    ContactModel contactModel = new ContactModel(0, name.getText().toString().trim(), number.getText().toString().trim());

                    insertContact(contactModel);
                }else {
                    Toast.makeText(AddContactActivity.this, "Fill the Empty flied", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void insertContact(ContactModel contactModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SilentRoomDatabase.getInstance(AddContactActivity.this)
                        .switchDao()
                        .insertContact(contactModel);
            }
        }).start();
    }
    @Override
    public void Onclick(ContactModel contactModel) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SilentRoomDatabase.getInstance(AddContactActivity.this)
                        .switchDao()
                        .deleteContact(contactModel);
            }
        }).start();
    }
}
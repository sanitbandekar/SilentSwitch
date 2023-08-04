package com.silentswitch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.silentswitch.databinding.ItemContactBinding;

public class ContactAdapter extends ListAdapter<ContactModel, ContactAdapter.ViewHolder> {
    private ContactInterface contactInterface;
    public ContactAdapter(ContactInterface contactInterface) {
        super(ContactModel.itemCallback);
        this.contactInterface = contactInterface;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemContactBinding binding = ItemContactBinding.inflate(layoutInflater,parent,false);
        binding.setContactInterface(contactInterface);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        ContactModel contactModel = getItem(position);
        holder.itemContactBinding.setContact(contactModel);
        holder.itemContactBinding.executePendingBindings();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemContactBinding itemContactBinding;
        public ViewHolder(ItemContactBinding itemView) {
            super(itemView.getRoot());
            itemContactBinding = itemView;
        }
    }
    public interface ContactInterface {
        void Onclick(ContactModel contactModel);
    }
}

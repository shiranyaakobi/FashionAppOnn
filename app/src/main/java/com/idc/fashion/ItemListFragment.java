package com.idc.fashion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.idc.fashion.Adapters.ItemListAdapter;
import com.idc.fashion.Firebase.Database;
import com.idc.fashion.Firebase.OnDataFetched;
import com.idc.fashion.Model.Category;
import com.idc.fashion.Model.CategoryItems;
import com.idc.fashion.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment implements ItemListAdapter.RecyclerViewClickListener {

    private RecyclerView recyclerView;
    private ItemListAdapter adapter;
    private String category;
    FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        recyclerView = view.findViewById(R.id.item_list_layout);
        if (getArguments() != null) {
            category = getArguments().getString("category");
            floatingActionButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("category", category);
                NavHostFragment.findNavController(ItemListFragment.this)
                        .navigate(R.id.action_itemListFragment_to_addItemFragment, bundle);
            });

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            gridLayoutManager.setSpanCount(2);
            recyclerView.setLayoutManager(gridLayoutManager);

            Database.ItemsManager.listenForAllItemsByCategory(getViewLifecycleOwner(), category, data -> {
                adapter = new ItemListAdapter(data, this);
                recyclerView.setAdapter(adapter);
            });
            recyclerView = view.findViewById(R.id.item_list_layout);
        }
        return view;
    }

    @Override
    public void itemClicked(Item item) {
        Bundle b = new Bundle();
        Gson g = new Gson();
        b.putString("item", g.toJson(item));
        NavHostFragment.findNavController(this).navigate(R.id.action_itemListFragment_to_showItemFragment,b);
    }


}

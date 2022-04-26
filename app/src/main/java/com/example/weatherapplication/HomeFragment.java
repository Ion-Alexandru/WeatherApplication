package com.example.weatherapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecyclerViewInterface{

    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private ArrayList<City> cityArrayList;

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        buildRecyclerView();

        searchView = view.findViewById(R.id.search_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                filter(s);

                return true;
            }

        });

        return view;
    }

    private void filter(String s) {
        ArrayList<City> filteredList = new ArrayList<>();

        for(City item : cityArrayList){
            if(item.getCityCountry().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(item);
            } else if(item.getCityName().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(item);
            }
        }
        cityAdapter.filterList(filteredList);
    }

    private void buildRecyclerView() {

        cityArrayList = new ArrayList<>();

        cityAdapter = new CityAdapter(this, cityArrayList, getContext());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(cityAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), cityArrayList.get(position).getCityCountry() + ", " + cityArrayList.get(position).getCityName() + " selected!", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("cityData", cityArrayList.get(position).getCityCountry());

        getParentFragmentManager().setFragmentResult("cityData", bundle);
    }
}
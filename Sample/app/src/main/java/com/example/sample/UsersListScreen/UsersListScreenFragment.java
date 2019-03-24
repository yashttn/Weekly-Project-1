package com.example.sample.UsersListScreen;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.sample.Models.UsersModel;
import com.example.sample.R;

import java.util.ArrayList;
import java.util.List;

public class UsersListScreenFragment extends Fragment {

    RecyclerView usersListRV;
    UsersListAdapter usersListAdapter;
    List<UsersModel> usersData;
    Bundle bundle;
    LinearLayoutManager linearLayoutManager;
    boolean isScrolling = false;
    IDataChangeListener dataChangeListener;

    public void setDataChangeListener(IDataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        usersData = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list_screen, container, false);
        usersListRV = view.findViewById(R.id.users_list_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        usersListRV.setLayoutManager(linearLayoutManager);
        usersListAdapter = new UsersListAdapter(usersData);
        usersListRV.setAdapter(usersListAdapter);
        usersListRV.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        bundle = getArguments();
        if (bundle != null) {
            List<UsersModel> modelList = bundle.getParcelableArrayList("users_list");
            usersData.addAll(modelList);
        }
        usersListAdapter.notifyDataSetChanged();

        usersListRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItems = linearLayoutManager.getItemCount();
                int visibleItems = linearLayoutManager.getChildCount();
                int scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && visibleItems + scrolledOutItems == totalItems) {
                    Toast.makeText(getActivity(), "Loading ...", Toast.LENGTH_LONG).show();
                    isScrolling = false;
                    dataChangeListener.getMoreData(totalItems);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_screen, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu_add_user:
                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_menu_update_user:
                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_menu_delete_user:
                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_menu_logout_user:
                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

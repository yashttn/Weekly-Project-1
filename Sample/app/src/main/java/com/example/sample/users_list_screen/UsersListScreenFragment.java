package com.example.sample.users_list_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.sample.models.UsersModel;
import com.example.sample.R;

import java.util.ArrayList;
import java.util.List;

public class UsersListScreenFragment extends Fragment {

    RecyclerView usersListRV;
    UsersListAdapter usersListAdapter;
    List<UsersModel> usersData;
    LinearLayoutManager linearLayoutManager;
    IDataChangeListener dataChangeListener;
    boolean isScrolling = false;

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

        buildUsersList(view);
        return view;
    }

    private void buildUsersList(View view){
        usersListRV = view.findViewById(R.id.users_list_rv);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        usersListRV.setLayoutManager(linearLayoutManager);
        usersListAdapter = new UsersListAdapter();
        usersListAdapter.setUsersModelList(usersData);
        usersListRV.setAdapter(usersListAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                    dataChangeListener.getMoreData();
                }
            }
        });
    }

    public void setUsersData(List<UsersModel> usersModelList) {
        this.usersData = usersModelList;
        usersListAdapter.setUsersModelList(usersModelList);
        usersListAdapter.notifyDataSetChanged();
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
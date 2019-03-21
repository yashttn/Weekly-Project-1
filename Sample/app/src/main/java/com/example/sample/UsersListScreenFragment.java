package com.example.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class UsersListScreenFragment extends Fragment {

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_users_list_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_menu_update_user:
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_menu_delete_user:
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_menu_logout_user:
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

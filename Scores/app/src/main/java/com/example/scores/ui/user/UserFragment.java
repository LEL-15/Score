package com.example.scores.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scores.UserActivity;
import com.example.scores.R;

public class UserFragment extends Fragment {
    String TAG = "test";
    private UserViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        Log.d(TAG, "onCreateView: In gallery fragment");

        Context hold = getContext();
        Intent intent = new Intent(hold, UserActivity.class);
        startActivity(intent);
        return root;
    }
}
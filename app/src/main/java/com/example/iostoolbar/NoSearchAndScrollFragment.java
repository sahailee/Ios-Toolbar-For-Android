package com.example.iostoolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NoSearchAndScrollFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_frag, container, false);

        Button frag1 = view.findViewById(R.id.frag1);
        Button frag2 = view.findViewById(R.id.frag2);

        frag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionFragment(new SearchAndScrollFragment());
            }
        });

        frag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionFragment(new NoSearchAndScrollFragment());
            }
        });

        return view;
    }

    private void transitionFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, fragment).commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        ExampleIosToolbarActivity.myToolbar.disableSearchBar();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

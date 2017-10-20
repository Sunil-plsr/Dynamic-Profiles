package com.example.tvs.promark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ProfileList extends Fragment implements ProfileRVAdapter.ProfileListHandler {

    private static String NEW_PROFILE_FRAGMENT = "new_profile_fragment";
    private ImageView addNewProfile;
    private ArrayList<Profile> allProfiles;
    private RecyclerView profilesRV;
    private static final Profile NEW_PROFILE = new Profile("Default", 0,
            new Profile.Trigger(0, 0, null, null, null, null, 0, 100), new Profile.Action(0, 0, 0, 4));

    public ProfileList() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addNewProfile = (ImageView) getActivity().findViewById(R.id.addNewProfile);
        profilesRV = (RecyclerView) getActivity().findViewById(R.id.profilesRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        profilesRV.setLayoutManager(linearLayoutManager);
        notifyDataSetChanged();

        addNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageProfile(NEW_PROFILE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_list, container, false);
    }

    @Override
    public void notifyDataSetChanged() {
        allProfiles = MainActivity.databaseManager.getAllProfiles();
        ProfileRVAdapter profileRVAdapter = new ProfileRVAdapter(getActivity(), R.layout.profiles_list, allProfiles, this);
        profilesRV.setAdapter(profileRVAdapter);
    }

    @Override
    public void viewDetailsOfProfile(Profile profile) {
        manageProfile(profile);
    }

    public void manageProfile(Profile profile) {
        NewProfile newProfile = new NewProfile();
        newProfile.profile(profile);
        getFragmentManager().beginTransaction()
                .replace(R.id.activity_main, newProfile, NEW_PROFILE_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }
}

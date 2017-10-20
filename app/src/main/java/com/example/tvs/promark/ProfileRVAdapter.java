package com.example.tvs.promark;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileRVAdapter extends RecyclerView.Adapter<ProfileRVAdapter.ViewHolder> {

    Context context;
    int resource;
    ArrayList<Profile> objects;
    private ProfileListHandler profileListHandler;

    public ProfileRVAdapter(Context context, int resource, ArrayList<Profile> objects, ProfileListHandler profileListHandler) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.profileListHandler = profileListHandler;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView profileTitle;
        LinearLayout container;
        ImageView deleteButton;
        Switch active;

        public ViewHolder(View itemView) {
            super(itemView);

            profileTitle = (TextView) itemView.findViewById(R.id.profileTitle);
            container = (LinearLayout) itemView.findViewById(R.id.profileListItem);
            deleteButton = (ImageView) itemView.findViewById(R.id.deleteProfile);
            active = (Switch) itemView.findViewById(R.id.activeSwitch);

            profileTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileListHandler.viewDetailsOfProfile(objects.get(getAdapterPosition()));
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.databaseManager.deleteProfile(objects.get(getAdapterPosition()).id);
                    profileListHandler.notifyDataSetChanged();
                }
            });

            active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    long id = objects.get(getAdapterPosition()).id;
                    if(!isChecked) MainActivity.databaseManager.deactivateProfile(id);
                    else {
                        MainActivity.databaseManager.activateProfile(id);
                        Scheduler.activateProfile(context);
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View child = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(child);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile profile = objects.get(position);
        if(profile.active == 1) {
            holder.active.setChecked(true);
            holder.container.setBackgroundResource(R.drawable.active_profile);
        } else {
            holder.active.setChecked(false);
            holder.container.setBackgroundResource(R.drawable.round_border);
        }
        holder.profileTitle.setText(profile.name);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    interface ProfileListHandler {
        void notifyDataSetChanged();
        void viewDetailsOfProfile(Profile profile);
    }
}

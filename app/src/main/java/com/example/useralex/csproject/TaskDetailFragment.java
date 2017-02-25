package com.example.useralex.csproject;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.useralex.csproject.ListActivity;
import com.example.useralex.csproject.R;
import com.example.useralex.csproject.TaskDetailActivity;
import com.example.useralex.csproject.dummy.DummyContent;

/**
 * A fragment representing a single Task detail screen.
 * This fragment is either contained in a {@link ListActivity}
 * in two-pane mode (on tablets) or a {@link TaskDetailActivity}
 * on handsets.
 */

// TODO: I don't really understand this file because I don't have a tablet and don't care yet.
    // It has is used in taskDetailActivity.
public class TaskDetailFragment extends Fragment {

    private TaskDataModel.TaskItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("Title")) {
            // In a real-world scenario, use a Loader
            // to load content from a content provider.
            // mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            mItem = new TaskDataModel.TaskItem(
                    getArguments().getString("Title"),
                    getArguments().getString("Description")
            );

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && mItem != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.old_city_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.city_detail)).setText(mItem.getDescription());
        }

        return rootView;
    }
}

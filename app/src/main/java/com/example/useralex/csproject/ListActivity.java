package com.example.useralex.csproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useralex.csproject.TaskDataModel.TaskItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * An activity representing a list of tasks. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TaskDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ListActivity extends AppCompatActivity {

    private String TAG = "ListActivity";
    private String LIST_FILE = "tasklist";
    private int ADD_TASK_ITEM_RC = 1;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private TabLayout mTabs;
    private FloatingActionButton mFAB;
    private TaskDataModel mDataModel;
    private List<TaskItem> mTaskList;
    private View mRecyclerView;
    private SimpleItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mDataModel = new TaskDataModel();

        // Try loading LIST_FILE into mDataModel.
        try {
            FileInputStream fis = openFileInput(LIST_FILE);

            // Read LIST_FILE into str and close fis when done.
            String str = "";
            int byteIn;
            while((byteIn = fis.read()) != -1) {
                str += ((char) byteIn);
            }
            fis.close();

            // Create JSONArray with str and convert to ArrayList<TaskItem> for mDataModel
            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject taskItem = jsonArray.getJSONObject(i);
                mDataModel.addItem(
                        new TaskItem(
                                taskItem.getString("Title"),
                                taskItem.getString("Description"),
                                taskItem.getInt("Status")
                        )
                );
            }
        } catch (IOException|JSONException e) {
            Toast.makeText(this, "Error loading list", Toast.LENGTH_SHORT);
        }

        // Couldn't get this to be not null when trying to save the ListArray<TaskItem> between
        // activities.
        if (savedInstanceState == null) {
            mTaskList = mDataModel.getAll();
        }


        mRecyclerView = findViewById(R.id.recycler_task);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);

        // Setup tabs and onclicks
        mTabs = (TabLayout) findViewById(R.id.tabs);
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { updateList(); }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Setup FAB and onclick
        mFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If Tablet maybe make it a dialog or something? Don't know don't care yet.
                if (mTwoPane) {

                } else {
                    Intent intent = new Intent(getApplicationContext(), AddTaskItemActivity.class);
                    startActivityForResult(intent, ADD_TASK_ITEM_RC);
                }

                updateList();
            }
        });

        // Initially update the recycler view.
        updateList();
    }

    // Save the users list to a private file in JSON
    @Override
    protected void onPause() {
        super.onPause();

        FileOutputStream fos;
        OutputStreamWriter osw = null;

        try {
            fos = this.openFileOutput(LIST_FILE, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            JSONArray array = new JSONArray();
            final List<TaskItem> list = mDataModel.getAll();

            for (TaskItem t : list) {
                array.put(t.toJSON());
            }

            osw.write(array.toString());
            osw.flush();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving list", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Called when activity launched with startActivityForResult completes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which activity it is.
        if (requestCode == ADD_TASK_ITEM_RC) {
            if (resultCode == RESULT_OK) {
                mDataModel.addItem(
                        new TaskItem(
                                data.getStringExtra("Title"),
                                data.getStringExtra("Description"),
                                data.getDoubleExtra("Latitude", 0),
                                data.getDoubleExtra("Longitude", 0)
                        )
                );
                updateList();
            }
        }
    }

    // Again.. Couldnt really figure this out. but the file save method seems to work alright.
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //Log.d(TAG, "onSaveInstanceState!");
        //outState.putInt("test", 10);
        super.onSaveInstanceState(outState);
    }

    // Used to filter the list in the adapter based on tab position.
    protected void updateList() {
        if (mTabs == null) return;
        switch (mTabs.getSelectedTabPosition()) {
            case 0:
                mAdapter.swapList(mDataModel.getIncomplete());
                break;
            case 1:
                mAdapter.swapList(mDataModel.getCompleted());
                break;
            case 2:
                mAdapter.swapList(mDataModel.getAll());
                break;
            default:
                throw new Error("Default should never happen");
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        mAdapter = new SimpleItemRecyclerViewAdapter(mTaskList);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), lm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<TaskItem> mItems;

        public SimpleItemRecyclerViewAdapter(List<TaskItem> items) {
            mItems = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mItems.get(position);
            holder.mTitleView.setText(mItems.get(position).getTitle());
            holder.mDescriptionView.setText(mItems.get(position).getDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) { // TODO: create fragment for larger screens
                        /*
                        Bundle arguments = new Bundle();
                        arguments.putString(CourseDetailFragment.ARG_ITEM_ID, holder.mItem.desc);
                        CourseDetailFragment fragment = new CourseDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.course_detail_container, fragment)
                                .commit();
                        */
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, TaskDetailActivity.class);
                        intent.putExtra("Description", holder.mItem.getDescription());
                        intent.putExtra("Title", holder.mItem.getTitle());
                        intent.putExtra("Status", holder.mItem.getStatus());
                        intent.putExtra("Latitude", holder.mItem.getLatitude());
                        intent.putExtra("Longitude", holder.mItem.getLongitude());
                        context.startActivity(intent);
                    }
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getApplicationContext(), "Long click!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                    // Add to @string constants later
                    builder.setMessage("What would you like to do?");

                    // Toggle complete or not
                    if (holder.mItem.getStatus() != TaskDataModel.COMPLETE) {
                        builder.setPositiveButton("Set Complete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                holder.mItem.setComplete();
                                updateList();
                            }
                        });
                    } else {
                        builder.setPositiveButton("Set Incomplete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.mItem.setIncomplete();
                                updateList();
                            }
                        });
                    }

                    // Ability to delete item from the list
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDataModel.removeItem(holder.mItem);
                            updateList();
                        }
                    });

                    // Create the AlertDialog object and show it
                    builder.create().show();

                    // Return true if you don't need further processing. For example, if we return
                    // false, the views onClick will fire along with the onLongClick.
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            // This method was giving me errors sometimes so I added a null check and it seems to be
            // fine now.
            if (mItems == null) return 0;
            return mItems.size();
        }

        public void swapList(List<TaskItem> items) {
            mItems = items;
            this.notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mDescriptionView;
            public TaskItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.task_title);
                mDescriptionView = (TextView) view.findViewById(R.id.task_description);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mDescriptionView.getText() + "'";
            }
        }
    }
}

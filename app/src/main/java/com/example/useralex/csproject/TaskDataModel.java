package com.example.useralex.csproject;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class handles the list of tasks to be displayed in ListActivity. It also has an inner class
 * defining TaskItem objects.
 */

public class TaskDataModel {
    public static final int COMPLETABLE = 0;
    public static final int INCOMPLETE = 1;
    public static final int COMPLETE = 2;
    public static final int EXPIRED = 3;

    private List<TaskItem> tasks;

    public TaskDataModel() {
        this.tasks = new ArrayList<TaskItem>();
    }

    // Getters
    public List<TaskItem> getAll() { return this.tasks; }

    public List<TaskItem> getIncomplete() {
        Iterator<TaskItem> iterator = this.tasks.iterator();
        ArrayList<TaskItem> incomplete = new ArrayList<TaskItem>();
        while(iterator.hasNext()) {
            TaskItem item = iterator.next();
            if (item.status == INCOMPLETE)
                incomplete.add(item);
        }
        return incomplete;
    }

    public List<TaskItem> getCompleted() {
        Iterator<TaskItem> iterator = this.tasks.iterator();
        ArrayList<TaskItem> complete = new ArrayList<TaskItem>();
        while(iterator.hasNext()) {
            TaskItem item = iterator.next();
            if (item.status == COMPLETE)
                complete.add(item);
        }
        return complete;
    }

    public List<TaskItem> getCompletable() {
        Iterator<TaskItem> iterator = this.tasks.iterator();
        ArrayList<TaskItem> completable = new ArrayList<TaskItem>();
        while(iterator.hasNext()) {
            TaskItem item = iterator.next();
            if (item.status == COMPLETABLE)
                completable.add(item);
        }
        return completable;
    }

    public List<TaskItem> getExpired() {
        Iterator<TaskItem> iterator = this.tasks.iterator();
        ArrayList<TaskItem> expired = new ArrayList<TaskItem>();
        while(iterator.hasNext()) {
            TaskItem item = iterator.next();
            if (item.status == EXPIRED)
                expired.add(item);
        }
        return expired;
    }

    // Setters
    public void addItem(TaskItem item) {
        this.tasks.add(item);
    }
    public void removeItem(TaskItem item) {
        this.tasks.remove(item);
    }
    public int updateItem(TaskItem oldItem, TaskItem newItem) {
        // Originally I had made this to toggle the TaskItems from Complete <-> Incomplete in the
        // DataModel but just calling TaskItem.setComplete() or Incomplete() seems to update the
        // DataModel fine.
        // Returns index if found and replaced. Else returns -1.

        Iterator it = tasks.iterator();
        int i = 0;

        while(it.hasNext()) {
            TaskItem listItem = (TaskItem) it.next();
            if (listItem.equals(oldItem)) {
                tasks.add(i, newItem);
                return i;
            }

            i++;
        }

        return -1;
    }

    // TaskItem Object Class
    public static class TaskItem {
        private String title;
        private String description;
        private int status;
        private double latitude;
        private double longitude;

        public TaskItem(String title, String description, double latitude, double longitude) {
            this.title = title;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
            this.status = INCOMPLETE;
        }

        public TaskItem(String title, String description, int status) {
            this.title = title;
            this.description = description;
            this.status = status;
        }

        // Getters
        public String getTitle() { return this.title; }
        public String getDescription() { return this.description; }
        public int getStatus() { return this.status; }
        public double getLatitude(){return this.latitude;}
        public double getLongitude() { return  this.longitude; }
        public JSONObject toJSON() {
            JSONObject obj = new JSONObject();

            try {
                obj.put("Title", this.title);
                obj.put("Description", this.description);
                obj.put("Status", this.status);
                obj.put("Latitude", this.latitude);
                obj.put("Longitude", this.longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return obj;
        }

        // Setters
        public void setTitle(String title) { this.title = title; }
        public void setDescription(String description) { this.description = description; }
        public void setIncomplete() { this.status = INCOMPLETE; }
        public void setComplete() { this.status = COMPLETE; }
        public void setCompleteable() { this.status = COMPLETABLE; }
        public void setExpired() { this.status = EXPIRED; }
    }
}

package net.astechdesign.diningsolutions.app;

import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.repositories.TaskRepo;
import net.astechdesign.diningsolutions.tasks.TaskListActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static TaskListActivity mActivity;
    private static TaskRepo repo;
    private static List<Task> mTaskList = new ArrayList<>();

    public static void setActivity(TaskListActivity activity) {
        mActivity = activity;
        repo = TaskRepo.get(mActivity);
    }

    public static List<Task> getTasks() {
        mTaskList.clear();
        mTaskList.addAll(repo.get());
        return mTaskList;
    }

    public static void save(Task task) {
        repo.addOrUpdate(task);
        updateView();
    }

    public static void delete(Task task) {
        repo.delete(task);
        updateView();
    }

    public static void updateView() {
        mTaskList.clear();
        mTaskList.addAll(getTasks());
        mActivity.updateView();
    }
}
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private static final Path STORE = Paths.get("tasks.json");

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(STORE)) {
            return new ArrayList<>();
        }
        try {
            String content = Files.readString(STORE);
            String[] taskList = content.replace("[", "")
                    .replace("]", "")
                    .split("},");

            for (String task : taskList) {
                if (!task.endsWith("}")) {
                    task = task + "}";
                    tasks.add(Task.fromJson(task));
                } else {
                    tasks.add(Task.fromJson(task));
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            return new ArrayList<>();
        }
        return tasks;
    }

    public void listTasks(List<Task> tasks, TaskStatus status) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        tasks.stream()
                .filter(task -> status == null || task.getStatus() == status)
                .forEach(System.out::println);
    }

    public void saveTask(List<Task> tasks) {
        try {
            String joiner = tasks.stream().map(Task::toJson).collect(Collectors.joining(","));
            String joinedString = "[" + joiner + "]";
            Files.writeString(STORE, joinedString);
        } catch (IOException e) {
            System.err.println("Error saving task: " + e.getMessage());
        }
    }

    public Task createTask(List<Task> current, String description, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        int maxId = current.stream().mapToInt(Task::getId).max().orElse(0);
        Task task = new Task(maxId + 1, description, status, createdAt, updatedAt);
        current.add(task);
        return task;
    }

    public boolean updateDescription(List<Task> current, int id, String newDescription) {
        return current.stream().filter(task -> task.getId() == id)
                .findFirst()
                .map(task -> {
                    task.setDescription(newDescription);
                    task.setUpdatedAt(LocalDateTime.now());
                    return true;
                })
                .orElse(false);
    }

    public boolean deleteTasks(List<Task> current, int id) {
        return current.removeIf(task -> task.getId() == id);
    }

    public boolean markInProgress(List<Task> current, int id) {
        current.stream().filter(task -> task.getId() == id).forEach(Task::markInProgress);
        return true;
    }

    public boolean markDone(List<Task> current, int id) {
        current.stream().filter(task -> task.getId() == id).forEach(Task::markDone);
        return true;
    }
}

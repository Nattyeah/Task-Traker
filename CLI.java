import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CLI {

    private static final String TASK_NOT_FOUND = "Task not found: ";

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        List<Task> tasks = taskManager.loadTasks();

        if (args.length < 1) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();

        try {
            switch (command) {
                case "add":
                    if (args.length != 2) {
                        System.out.println("Error: Missing task description.");
                        printUsage();
                        return;
                    }
                    String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    Task newTask = taskManager.createTask(tasks, description, TaskStatus.TODO, LocalDateTime.now(), LocalDateTime.now());
                    taskManager.saveTask(tasks);
                    System.out.println("Task added successfully: " + newTask);
                    break;

                case "list":
                    TaskStatus status = null;
                    if (args.length > 1) {
                        try {
                            status = TaskStatus.valueOf(args[1].toUpperCase().replace("-", "_"));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid status: " + args[1]);
                            System.out.println("Available statuses: " + Arrays.toString(TaskStatus.values()));
                            return;
                        }
                    }
                    taskManager.listTasks(tasks, status);
                    break;

                case "update":
                    if (args.length < 3) {
                        System.out.println("Error: Missing task id or new description.");
                        printUsage();
                        return;
                    }
                    int updatedId = Integer.parseInt(args[1]);
                    String newDescription = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                    if (taskManager.updateDescription(tasks, updatedId, newDescription)) {
                        taskManager.saveTask(tasks);
                        System.out.println("Task updated successfully: " + updatedId);
                    } else {
                        System.out.println(TASK_NOT_FOUND + updatedId);
                    }
                    break;

                case "mark-in-progress":
                    if (args.length != 2) {
                        System.out.println("Error: Missing task id or new status.");
                        printUsage();
                        return;
                    }
                    int idToMarkInProgress = Integer.parseInt(args[1]);
                    if (taskManager.markInProgress(tasks, idToMarkInProgress)) {
                        taskManager.saveTask(tasks);
                        System.out.println("Task status updated successfully: " + idToMarkInProgress);
                    } else {
                        System.out.println(TASK_NOT_FOUND + idToMarkInProgress);
                    }
                    break;

                case "mark-done":
                    if (args.length != 2) {
                        System.out.println("Error: Missing task id or new status.");
                        printUsage();
                        return;
                    }
                    int idToMarkDone = Integer.parseInt(args[1]);
                    if (taskManager.markDone(tasks, idToMarkDone)) {
                        taskManager.saveTask(tasks);
                        System.out.println("Task status updated successfully: " + idToMarkDone);
                    } else {
                        System.out.println(TASK_NOT_FOUND + idToMarkDone);
                    }
                    break;

                case "delete":
                    if (args.length != 2) {
                        System.out.println("Error: Missing task id.");
                        printUsage();
                        return;
                    }
                    int idToDelete = Integer.parseInt(args[1]);
                    if (taskManager.deleteTasks(tasks, idToDelete)) {
                        taskManager.saveTask(tasks);
                        System.out.println("Task deleted successfully: " + idToDelete);
                    } else {
                        System.out.println(TASK_NOT_FOUND + idToDelete);
                    }
                    break;

                default:
                    System.out.println("Error: Unknown command: " + command);
                    printUsage();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid arguments: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Task Tracker - Command Line Interface");
        System.out.println("Usage: java CLI <command> [arguments]");
        System.out.println("\nCommands:");
        System.out.println("  add <description>      Add a new task");
        System.out.println("  list                   List all tasks");
        System.out.println("  list <status>          List by status");
        System.out.println("  update <id> <desc>     Update a task's description");
        System.out.println("  mark-in-progress <id>  Update task status to IN_PROGRESS");
        System.out.println("  mark-done <id>         Update task status to DONE");
        System.out.println("  delete <id>            Delete a task");
    }
}

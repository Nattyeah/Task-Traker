import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Integer id;
    private String description;
    private TaskStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(Integer id, String description, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void markInProgress() {
    this.status = TaskStatus.IN_PROGRESS;
    this.updatedAt = LocalDateTime.now();
    }

    public void markDone() {
        this.status = TaskStatus.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    public String toJson() {
        return "{\"id\":" + id +
                ", \"description\":\"" + description.strip() +
                "\", \"status\":\"" + status +
                "\", \"createdAt\":\"" + createdAt.format(formatter) +
                "\", \"updatedAt\":\"" + updatedAt.format(formatter) + "\"}";
    }

    public static Task fromJson(String json) {
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] fields = json.split(",");

        int id = Integer.parseInt(fields[0].split(":")[1].strip());
        String description = fields[1].split(":")[1].strip();
        String status = fields[2].split(":")[1].strip();
        String createdAt = fields[3].split("[a-z]:")[1].strip();
        String updatedAt = fields[4].split("[a-z]:")[1].strip();

        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase().replace(" ", "_"));

        return new Task(id, description, taskStatus, LocalDateTime.parse(createdAt), LocalDateTime.parse(updatedAt));
    }

    @Override
    public String toString() {
        return String.format("[Task: %d | Description: %s | Status: %s | Created: %s | Updated: %s]",
                this.id,
                this.description,
                this.status,
                this.createdAt.toString(),
                this.updatedAt.toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

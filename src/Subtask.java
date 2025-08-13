public class Subtask extends Task {
    protected Epic epic;
    private boolean completed;

    public Subtask(String name, String description, long id, Epic epic) {
        super(name, description, id);
        this.epic = epic;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return  "Subtask" +"\n" +
                "Имя'" + name + '\'' + "," + "\n" +
                "Описание'" + description + '\'' + "," + "\n" +
                "Статус'" + getStatus() + '\'' + "," + "\n" +
                "id '" + id + '\'';
    }
}
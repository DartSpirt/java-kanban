import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Subtask> subtasks = new HashMap<>();
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, ArrayList<Subtask>> epicsSubtask = new HashMap<>();
    private final HashMap<Long, Long> subtasksEpic = new HashMap<>();

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    public HashMap<Long, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Long, Epic> getEpics() {
        return epics;
    }

    public HashMap<Long, ArrayList<Subtask>> getEpicVsSubtask() {
        return epicsSubtask;
    }

    public HashMap<Long, Long> getSubtaskVsEpic() {
        return subtasksEpic;
    }

    public Epic createNewEpic(String inputName, String inputDescription, long id) {
        Epic epic = new Epic(inputName, inputDescription, id);
        epics.put(id, epic);
        System.out.println("Задача (Epic, ID: " + id + ") добавлена");
        return epic;
    }

    public void createNewSubtask(String inputName, String inputDescription, long id, Epic epic) {
        Subtask subtask = new Subtask(inputName, inputDescription, id, epic);
        subtasks.put(id, subtask);
        ArrayList<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.add(subtask);
        epic.setSubtaskList(subtaskList);
        epicsSubtask.put(epic.getId(), subtaskList);
        subtasksEpic.put(id, epic.getId());
        System.out.println("Задача (Subtask, ID: " + id + ") добавлена");
        recalculateEpicStatus(epic); // Вызываем метод для пересчёта статуса эпика
    }

    public void createNewTask(String inputName, String inputDescription, long id) {
        Task task = new Task(inputName, inputDescription, id);
        tasks.put(id, task);
        System.out.println("Задача (Task, ID: " + id + ") добавлена");
    }

    public Task getTask(long inputId) {
        return tasks.get(inputId);
    }

    public Epic getEpic(long inputId) {
        return epics.get(inputId);
    }

    public Subtask getSubtask(long inputId) {
        return subtasks.get(inputId);
    }

    public void recalculateEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasksList = epic.getSubtaskList();
        boolean allCompleted = true;

        for (Subtask subtask : subtasksList) {
            if (!subtask.isCompleted()) {
                allCompleted = false;
                break;
            }
        }

        if (allCompleted) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }


    public void updateTask(long inputId, Task task) {
            tasks.put(inputId, task);
        }

    public void updateSubtask(long inputId, Subtask subtask) {
        if (subtasks.containsKey(inputId)) {
            Epic epic = epics.get(subtasksEpic.get(inputId));
            ArrayList<Subtask> subtasksList = epic.getSubtaskList();
            int index = subtasksList.indexOf(subtasks.get(inputId));
            if (index != -1) {
                subtasksList.set(index, subtask);
                epic.setSubtaskList(subtasksList);
                subtasks.put(inputId, subtask);
            }
            subtask.setCompleted(true);
            recalculateEpicStatus(epic); // Вызываем метод для пересчёта статуса эпика
        }
    }

    public void removeTask(long inputId) {
        if (!subtasks.containsKey(inputId)){
            throw new IllegalArgumentException("Задачи с таким id нет");
        } else {
            tasks.remove(inputId);
            System.out.println("Задача удалена");

        }
    }

    public void deleteEpic(long inputId) {
        if (!epics.containsKey(inputId)) {
            throw new IllegalArgumentException("Эпика с таким id нет");
        } else {
            epics.remove(inputId);
            ArrayList<Subtask> subtasksList = epicsSubtask.get(inputId);
            if (subtasksList != null) {
                subtasksList.clear();
            }
            System.out.println("Эпик удалён");
        }
    }

    public void deleteSubtask(long inputId) {
        if (!subtasks.containsKey(inputId)) {
            throw new IllegalArgumentException("Подзадачи с таким id нет");
        } else {
            Long epicId = subtasksEpic.get(inputId);
            subtasks.remove(inputId);
            Epic epic = epics.get(epicId);
            if (epic != null) {
                ArrayList<Subtask> subtasksList = epic.getSubtaskList();
                if (subtasksList != null) {
                    subtasksList.removeIf(subtask -> subtask.getId() == inputId);
                    epic.setSubtaskList(subtasksList);
                }
            }
            System.out.println("Подзадача удалена");
            assert epic != null;
            recalculateEpicStatus(epic); // Вызываем метод для пересчёта статуса эпика
        }
    }
    public void deleteAllTasks(HashMap<Long, Task> tasks,
                               HashMap<Long, Subtask> subtasks,
                               HashMap<Long, Epic> epics,
                               HashMap<Long, ArrayList<Subtask>> epicVsSubtask,
                               HashMap<Long, Long> subtaskVsEpic) {
        tasks.clear();
        subtasks.clear();
        epics.clear();
        epicVsSubtask.clear();
        subtaskVsEpic.clear();
    }
}
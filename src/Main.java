import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        String inputName;
        String inputDescription;
        String inputAnswer = null;
        String command;
        long inputId = 0;
        long id = 0;

        do {
            printMenu();
            command = scanner.next();
            scanner.nextLine();
            switch (command) {
                case "1":
                    System.out.println("Введите имя задачи");
                    inputName = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    inputDescription = scanner.nextLine();
                    id++;
                    System.out.println("Задача требует разделения?");
                    System.out.println("Да / нет");
                    inputAnswer = scanner.next();
                    scanner.nextLine();
                    if (inputAnswer.equals("Да")) {
                        Epic epic = manager.createNewEpic(inputName, inputDescription, id);
                        do {
                            System.out.println("Введите название Subtask");
                            inputName = scanner.nextLine();
                            if (inputName.equals("Стоп") && !epic.getSubtaskList().isEmpty()) {
                                break;
                            }
                            System.out.println("Введите описание Subtask");
                            inputDescription = scanner.nextLine();
                            id++;
                            manager.createNewSubtask(inputName, inputDescription, id, epic);
                            System.out.println("Для завершения введите: Стоп");
                        } while (true);
                    } else if (inputAnswer.equals("Нет")) {
                        manager.createNewTask(inputName, inputDescription, id);
                    }
                    break;
                case "2":
                case "3":
                    printTasks(command, inputId, manager);
                    break;
                case "4":
                    System.out.println("Введите id Epic для просмотра его подзадач");
                    inputId = scanner.nextLong();
                    if (manager.getEpics().containsKey(inputId)) {
                        printTasks(command, inputId, manager);
                    } else {
                        System.out.println("Epic с таким id нет");
                    }
                    break;
                case "5":
                    System.out.println("Введите id");
                    inputId = scanner.nextLong();
                    if ((manager.getTasks().containsKey(inputId) && manager.getEpics().containsKey(inputId))
                            || (manager.getTasks().containsKey(inputId) && manager.getSubtasks().containsKey(inputId))
                            || (manager.getEpics().containsKey(inputId) && manager.getSubtasks().containsKey(inputId))
                            || (manager.getTasks().containsKey(inputId) && manager.getSubtasks().containsKey(inputId)
                            && manager.getEpics().containsKey(inputId))) {
                        System.out.println("Введите task, subtask или epic");
                        inputAnswer = scanner.next();
                    }
                    if (manager.getTasks().containsKey(inputId) || Objects.equals(inputAnswer, "task")) {
                        Task task1 = manager.getTask(inputId);
                        System.out.println(task1);
                    } else if (manager.getEpics().containsKey(inputId) || Objects.equals(inputAnswer, "epic")) {
                        Epic epic1 = manager.getEpic(inputId);
                        System.out.println(epic1);
                    } else if (manager.getSubtasks().containsKey(inputId) || Objects.equals(inputAnswer, "subtask")) {
                        Subtask subtask1 = manager.getSubtask(inputId);
                        System.out.println(subtask1);
                        Long epicId = manager.getSubtaskVsEpic().get(inputId);
                        System.out.println(manager.getEpics().get(epicId));
                    } else {
                        System.out.println("Задач c таким id нет");
                    }
                    break;
                case "6":
                    System.out.println("Введите id задачи для изменения");
                    inputId = scanner.nextLong();
                    scanner.nextLine();//
                    if (manager.getTasks().containsKey(inputId) || manager.getSubtasks().containsKey(inputId)
                            || manager.getEpics().containsKey(inputId)) {
                        System.out.println("Хотите изменить только статус?");
                        System.out.println("Введите NEW, IN_PROGRESS или DONE для Task и Subtask");
                        String answer = scanner.nextLine();
                        if (answer.equals("NEW") || answer.equals("IN_PROGRESS") || answer.equals("DONE")) {
                            if (manager.getTasks().containsKey(inputId)) {
                                Task newTask = manager.getTasks().get(inputId);
                                newTask.setStatus(answer);
                                System.out.println("Статус изменён на " + answer);
                            } else if (manager.getSubtasks().containsKey(inputId)) {
                                Subtask newSubtask = manager.getSubtasks().get(inputId);
                                newSubtask.setStatus(answer);
                                System.out.println("Статус изменён на " + answer);
                                Long epicId = manager.getSubtaskVsEpic().get(inputId);
                                Epic epic = manager.getEpics().get(epicId);
                                epic.setStatus(answer);
                            } else {
                                System.out.println("Статус Epic изменится только с выполнением Subtask");
                            }
                        } else {
                            System.out.println("Введите имя новой задачи");
                            inputName = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            inputDescription = scanner.nextLine();
                            if (manager.getTasks().containsKey(inputId)) {
                                Task newTask = manager.getTasks().get(inputId);
                                newTask.setName(inputName);
                                newTask.setDescription(inputDescription);
                                manager.updateTask(inputId, newTask);
                            } else if (manager.getSubtasks().containsKey(inputId)) {
                                Subtask newSubtask = manager.getSubtasks().get(inputId);
                                newSubtask.setName(inputName);
                                newSubtask.setDescription(inputDescription);
                                manager.updateSubtask(inputId, newSubtask);
                            } else if (manager.getEpics().containsKey(inputId)) {
                                Epic newEpic = manager.getEpics().get(inputId);
                                newEpic.setName(inputName);
                                newEpic.setDescription(inputDescription);
                                manager.updateTask(inputId, newEpic);
                            }
                        }
                        break;
                    } else {
                        System.out.println("Список пуст");
                        break;
                    }
                case "7":
                    System.out.println("Введите id");
                    inputId = scanner.nextLong();
                    manager.removeTask(inputId);
                    break;
                case "8":
                    System.out.println("Введите id");
                    inputId = scanner.nextLong();
                    manager.deleteSubtask(inputId);
                    break;
                case "9":
                    System.out.println("Введите id");
                    inputId = scanner.nextLong();
                    manager.deleteEpic(inputId);
                    break;
                case "10":
                    manager.deleteAllTasks(manager.getTasks(), manager.getSubtasks(), manager.getEpics(),
                            manager.getEpicVsSubtask(), manager.getSubtaskVsEpic());
                    System.out.println("Задачи удалены");
                    break;
                case "0":
                    break;
            }
        } while (!command.equals("0"));
    }

    public static void printMenu() {
        System.out.println("""
                1. Добавить Task
                2. Получить список Task
                3. Получить список Epic
                4. Получить список Subtask по id эпика
                5. Получить Task по id
                6. Обновить Task по id
                7. Удалить Task по id
                8. Удалить Subtask по id
                9. Удалить Epic по id
                10. Удалить все задачи
                0. Выход""");
    }

    public static void printTasks(String command, Long inputId, TaskManager manager) throws NullPointerException {
        switch (command) {
            case "2":
                if (!manager.getTasks().isEmpty()) {
                    System.out.println(manager.getTasks().values());
                    break;
                } else {
                    System.out.println("Список пуст");
                    break;
                }
            case "3":
                if (!manager.getEpics().isEmpty()) {
                    System.out.println(manager.getEpics().values());
                    break;
                } else {
                    System.out.println("Список пуст");
                    break;
                }
            case "4":
                System.out.println(manager.getEpics().get(inputId));
                ArrayList<Subtask> subtaskId = manager.getEpicVsSubtask().get(inputId);
                for (Subtask sub : subtaskId) {
                    if (sub != null) {
                        System.out.println(sub);
                    }
                }
                break;
            default:
                System.out.println("___________________________________");
        }
    }
}
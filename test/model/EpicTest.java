package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Epic")
class EpicTest {

    Epic epic;
    Epic epicExpected;
    Epic epic1;

    @BeforeEach
    public void init() {
        epic = new Epic("эпик1", "описание1");
        epicExpected = new Epic("эпик2", "описание2");
        epic1 = new Epic("эпик1", "описание1");
    }

    @Test
    @DisplayName("Должны совпадать по id")
    void shouldEqualsOfId() {
        assertEquals(epicExpected, epic, "эпики не совпадают по id");
    }

    @Test
    @DisplayName("Должен совпадать со своей копией")
    void shouldEqualsWithCopy() {
        assertEqualsTask(epic1, epic, "эпики не совпадают по");
    }

    private void assertEqualsTask(Task expected, Task task, String message) {
        assertEquals(expected.getId(), task.getId(), message + " id");
        assertEquals(expected.getTaskName(), task.getTaskName(), message + " name");
        assertEquals(expected.getDescription(), task.getDescription(), message + " description");
    }
}
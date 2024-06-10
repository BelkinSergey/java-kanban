package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Managers")
class ManagersTest {


    @Test
    @DisplayName("утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры")
    public void shouldInitializedInstancesNotNull() {
        assertNotNull(Managers.getDefault(), "экземпляр класса не проинициализирован");
        assertNotNull(Managers.getDefaultHistory(), "экземпляр класса не проинициализирован");
        assertNotNull(Managers.getDefaultFileBackedTaskManager(), "экземпляр класса не проинициализирован");


    }

}


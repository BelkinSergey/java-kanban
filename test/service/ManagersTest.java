package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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


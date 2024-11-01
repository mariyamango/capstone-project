package com.example.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkTypeTest {
    @Test
    void createWorkType_shouldInitializeFieldsCorrectly() {
        // GIVEN
        String id = "1";
        String workTypeName = "Work 1";
        int mileageDuration = 1000;
        int timeDuration = 500;
        // WHEN
        WorkType workType = new WorkType(id, workTypeName, mileageDuration, timeDuration);
        // THEN
        assertEquals(id, workType.id());
        assertEquals(workTypeName, workType.workTypeName());
        assertEquals(mileageDuration, workType.mileageDuration());
        assertEquals(timeDuration, workType.timeDuration());
    }
}
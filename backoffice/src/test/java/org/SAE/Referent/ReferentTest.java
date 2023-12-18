package org.SAE.Referent;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.SAE.Referent.Referent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReferentTest {
    private SQL sqlMock;
    private Referent referent;

    @BeforeEach
    public void setup() {
        sqlMock = Mockito.mock(SQL.class);
        Mockito.when(sqlMock.select(Mockito.anyString())).thenReturn(null);
        Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(true);
        Main.sql = sqlMock;
        referent = new Referent(1, "John Doe", "1234567890", "john.doe@example.com");
    }

    @Test
    void shouldAddNewReferent() {
        assertEquals(1, Referent.referents.size());
        assertEquals("John Doe", Referent.referents.get(0).nom);
    }

    @Test
    void shouldUpdateReferent() {
        referent.nom = "Jane Doe";
        try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
            Referent.update(referent);
            mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
        }
        Mockito.verify(sqlMock).updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldDeleteReferent() {
        try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
            Referent.delete(referent);
            mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
        }
        Mockito.verify(sqlMock).deletePrepareStatement(Mockito.anyString(), Mockito.any());
        assertTrue(Referent.referents.isEmpty());
    }

    @Test
    void shouldCreateReferent() {
        try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
            Referent.create(referent);
            mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
        }
        Mockito.verify(sqlMock).createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldGetReferentFromDatabase() {
        Referent.getFromDatabase();
        Mockito.verify(sqlMock).select(Mockito.anyString());
    }
}

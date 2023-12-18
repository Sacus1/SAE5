package org.SAE.Depot;

import org.SAE.Depot.Depot;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class DepotTest {
	Depot depot;
	SQL sqlMock;

	@BeforeEach
	void setUp() {
		sqlMock = Mockito.mock(SQL.class);
		Main.sql = sqlMock;
		Mockito.when(sqlMock.select(Mockito.anyString())).thenReturn(null);
		Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(),
						Mockito.any())).thenReturn(true);
		depot = new Depot(1, 1, 1, "Test Depot", "1234567890", "Test Presentation", "Test Comment", "test@mail.com", "www.test.com", new File("test.jpg"));
	}

	@Test
	void createDepotSuccessfully() {
		Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(false);
		Depot.create(depot);
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Depot.create(depot);
			mockLogger.verify(() -> Logger.error(anyString()), times(0));
		}	}

	@Test
	void createDepotFailure() {
		Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);
		Depot.create(depot);
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Depot.create(depot);
			mockLogger.verify(() -> Logger.error(anyString()), times(1));
		}
	}

	@Test
	void updateDepotSuccessfully() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		Depot.update(depot);
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Depot.update(depot);
			mockLogger.verify(() -> Logger.error(anyString()), times(0));
		}
	}

	@Test
	void updateDepotFailure() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Depot.update(depot);
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Depot.update(depot);
			mockLogger.verify(() -> Logger.error(anyString()), times(1));
		}
	}

	@Test
	void deleteDepotSuccessfully() {
		Mockito.when(sqlMock.deletePrepareStatement(Mockito.anyString(), Mockito.any())).thenReturn(false);
		depot.delete();
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			depot.delete();
			mockLogger.verify(() -> Logger.error(anyString()), times(0));
		}
	}

	@Test
	void deleteDepotFailure() {
		Mockito.when(sqlMock.deletePrepareStatement(Mockito.anyString(), Mockito.any())).thenReturn(true);
		depot.delete();
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			depot.delete();
			mockLogger.verify(() -> Logger.error(anyString()), times(1));
		}
	}

	@Test
	void archiveDepotSuccessfully() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		depot.archive();
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			depot.archive();
			mockLogger.verify(() -> Logger.error(anyString()), times(0));
		}
	}

	@Test
	void archiveDepotFailure() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		depot.archive();
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			depot.archive();
			mockLogger.verify(() -> Logger.error(anyString()), times(1));
		}
	}
}

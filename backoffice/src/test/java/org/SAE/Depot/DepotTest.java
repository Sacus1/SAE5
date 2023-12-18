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
	Logger loggerMock;
	Main mainMock;

	@BeforeEach
	void setUp() {
		sqlMock = Mockito.mock(SQL.class);
		mainMock = Mockito.mock(Main.class);
		Main.sql = sqlMock;
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
		Mockito.verify(loggerMock).error("Can't create depot");
	}

	@Test
	void updateDepotSuccessfully() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		Depot.update(depot);
		Mockito.verify(loggerMock, Mockito.never()).error(Mockito.anyString());
	}

	@Test
	void updateDepotFailure() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Depot.update(depot);
		Mockito.verify(loggerMock).error("Failed to update depot");
	}

	@Test
	void deleteDepotSuccessfully() {
		Mockito.when(sqlMock.deletePrepareStatement(Mockito.anyString(), Mockito.any())).thenReturn(false);
		depot.delete();
		Mockito.verify(loggerMock, Mockito.never()).error(Mockito.anyString());
	}

	@Test
	void deleteDepotFailure() {
		Mockito.when(sqlMock.deletePrepareStatement(Mockito.anyString(), Mockito.any())).thenReturn(true);
		depot.delete();
		Mockito.verify(loggerMock).error("Can't delete depot");
	}

	@Test
	void archiveDepotSuccessfully() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		depot.archive();
		Mockito.verify(loggerMock, Mockito.never()).error(Mockito.anyString());
	}

	@Test
	void archiveDepotFailure() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		depot.archive();
		Mockito.verify(loggerMock).error("Can't archive depot");
	}
}

package org.SAE.Unite;

import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniteTest {

	private SQL sql;
	private ResultSet resultSet;
	private Unite unite;

	@BeforeEach
	public void setUp() throws Exception {
		sql = Mockito.mock(SQL.class);
		resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(sql.select(Mockito.anyString())).thenReturn(null);
		Mockito.when(sql.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(sql.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(),
						Mockito.any())).thenReturn(true);
		unite = new Unite(1, "Test");

		Main.sql = sql;
	}

	@Test
	void getFromDatabaseShouldClearUnites() throws Exception {
		when(sql.select("Unite")).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		Unite.getFromDatabase();

		assertTrue(Unite.unites.isEmpty());
	}

	@Test
	void getFromDatabaseShouldAddUnite() throws Exception {
		when(sql.select("Unite")).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getInt("idUnite")).thenReturn(1);
		when(resultSet.getString("nom")).thenReturn("Test");

		Unite.getFromDatabase();

		assertEquals(1, Unite.unites.size());
		assertEquals(unite.toString(), Unite.unites.get(0).toString());
	}

	@Test
	void updateShouldCallSqlUpdate() {
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Unite.update(unite);
			mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
		}

		verify(sql).updatePreparedStatement(eq("Unite"), any(String[].class), any(Object[].class), any(String[].class));
	}

	@Test
	void createShouldCallSqlCreate() {
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Unite.create(unite);
			mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
		}

			verify(sql).createPrepareStatement(eq("Unite"), any(String[].class), any(Object[].class));
	}

	@Test
	void deleteShouldCallSqlDeleteAndRemoveUnite() {
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Unite.delete(unite);
			mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
		}

			verify(sql).deletePrepareStatement(eq("Unite"), any(String[].class));
		assertFalse(Unite.unites.contains(unite));
	}
}

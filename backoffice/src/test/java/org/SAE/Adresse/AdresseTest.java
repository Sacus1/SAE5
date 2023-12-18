package org.SAE.Adresse;

import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdresseTest {
	private SQL sqlMock;
	private Adresse adresse;

	@BeforeEach
	public void setup() {
		sqlMock = Mockito.mock(SQL.class);
		Main.sql = sqlMock;
		Mockito.when(sqlMock.select(Mockito.anyString())).thenReturn(null);
		Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(),
						Mockito.any())).thenReturn(true);
		adresse = new Adresse("123 Main St", "Anytown", "12345");
	}

	@Test
	void createAddressAddsAddressToDatabase() {
		Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any()))
						.thenReturn(false);
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Adresse.create(adresse);
			mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
		}
		Mockito.verify(sqlMock).createPrepareStatement(Mockito.eq(Adresse.TABLE_NAME), Mockito.any(), Mockito.any());
	}

	@Test
	void updateAddressUpdatesAddressInDatabase() {
		Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any()))
						.thenReturn(true);
		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Adresse.update(adresse);
			mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
		}
		Mockito.verify(sqlMock).updatePreparedStatement(Mockito.eq(Adresse.TABLE_NAME), Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	void deleteAddressRemovesAddressFromDatabase() {
		Mockito.when(sqlMock.deletePrepareStatement(Mockito.anyString(), Mockito.any()))
						.thenReturn(false);

		try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
			Adresse.delete(adresse);
			mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
		}
		Mockito.verify(sqlMock).deletePrepareStatement(Mockito.eq(Adresse.TABLE_NAME), Mockito.any());
	}

	@Test
	void toStringReturnsCorrectFormat() {
		String expected = "123 Main St, Anytown";
		assertEquals(expected, adresse.toString());
	}
}

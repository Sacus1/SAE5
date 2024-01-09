package org.SAE.Produit;

import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProduitTest {
 private SQL sqlMock;
 private Produit produit;

 @BeforeEach
 public void setup() {
  sqlMock = Mockito.mock(SQL.class);
  Main.sql = sqlMock;
  Mockito.when(sqlMock.select(Mockito.anyString())).thenReturn(null);
  Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);
  Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(),
          Mockito.any())).thenReturn(true);
  produit = new Produit("product","desc",1,new File("path/to/image"));
 }

 @Test
 void createProductAddsProductToDatabase() {
  Mockito.when(sqlMock.createPrepareStatement(Mockito.anyString(), Mockito.any(), Mockito.any()))
      .thenReturn(false);
  try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
    Produit.create(produit);
    mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
    }
  Mockito.verify(sqlMock).createPrepareStatement(Mockito.eq(Produit.TABLE_NAME), Mockito.any(), Mockito.any());
 }

 @Test
 void updateProductUpdatesProductInDatabase() {
  Mockito.when(sqlMock.updatePreparedStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any()))
      .thenReturn(true);

  try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
     Produit.update(produit);
      mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
      }

   Mockito.verify(sqlMock).updatePreparedStatement(Mockito.eq(Produit.TABLE_NAME), Mockito.any(), Mockito.any(), Mockito.any());
 }

 @Test
 void deleteProductRemovesProductFromDatabase() {
  Mockito.when(sqlMock.deletePrepareStatement(Mockito.anyString(), Mockito.any()))
      .thenReturn(false);

  try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
    produit.delete();
      mockLogger.verify(() -> Logger.error(Mockito.anyString()), Mockito.times(0));
      }

   Mockito.verify(sqlMock).deletePrepareStatement(Mockito.eq(Produit.TABLE_NAME), Mockito.any());
 }

 @Test
 void getFromDatabaseRetrievesProducts() throws SQLException {
  ResultSet resultSetMock = Mockito.mock(ResultSet.class);
  Mockito.when(sqlMock.select(Mockito.anyString())).thenReturn(resultSetMock);
  Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
  Mockito.when(resultSetMock.getInt("idProduit")).thenReturn(1);
  Mockito.when(resultSetMock.getString("nom")).thenReturn("Product1");
  Mockito.when(resultSetMock.getString("imagePath")).thenReturn("path/to/image");
  Mockito.when(resultSetMock.getString("description")).thenReturn("Description1");
  Mockito.when(resultSetMock.getDouble("prix")).thenReturn(100.0);
  Mockito.when(resultSetMock.getInt("Unite_idUnite")).thenReturn(1);

  Produit.getFromDatabase();

  assertEquals(1, Produit.produits.size());
  assertEquals("Product1", Produit.produits.get(0).nom);
 }
}

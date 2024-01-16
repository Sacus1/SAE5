package org.SAE.Adhesion;

import org.SAE.Main.Base;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;

import java.util.ArrayList;
import java.util.List;

public class TypeAdhesion extends Base {
	public final int id;
	public String nom;
	public  int tarif;

	public static final List<TypeAdhesion> typeAdhesions = new ArrayList<>();
	public TypeAdhesion(int id, String nom, int tarif) {
		this.id = id;
		this.nom = nom;
		this.tarif = tarif;
		typeAdhesions.add(this);
	}
	public TypeAdhesion(String nom, int tarif) {
		this.id = Main.sql.getNextId("TypeAdhesion");
		this.nom = nom;
		this.tarif = tarif;
		typeAdhesions.add(this);
	}
	static TypeAdhesion getTypeAdhesionById(int typeAdhesionIdTypeAdhesion) {
		for (TypeAdhesion typeAdhesion : typeAdhesions) {
			if (typeAdhesion.id == typeAdhesionIdTypeAdhesion) {
				return typeAdhesion;
			}
		}
		return null;
	}

	@Override
	public void loadFromDatabase() {
		getFromDatabase();
	}

	public static void getFromDatabase() {
		typeAdhesions.clear();
		SQL sql = Main.sql;
		try {
			var res = sql.select("TypeAdhesion");
			if (res == null) return;
			while (res.next()) {
				int id = res.getInt("idTypeAdhesion");
				String nom = res.getString("nom");
				int tarif = res.getInt("tarif");
				new TypeAdhesion(id, nom, tarif);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return nom + " (" + tarif + "€)";
	}

	public static void create(TypeAdhesion typeAdhesion) {
		if (!Main.sql.createPrepareStatement("TypeAdhesion", new String[]{"nom", "tarif"},
						new Object[]{typeAdhesion.nom, typeAdhesion.tarif}))
			throw new RuntimeException("Failed to insert TypeAdhesion");
		getFromDatabase();
	}

	public void delete() {
		if (!Main.sql.deletePrepareStatement("TypeAdhesion", new String[]{"idTypeAdhesion = " + id}))
			throw new RuntimeException("Echec de la suppression");
		typeAdhesions.remove(this);
	}


}

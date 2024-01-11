package org.SAE.Depot;

import org.SAE.Main.Base;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.SAE.Main.Main.sql;

public class PeriodeNonLivrable extends Base {
	int id;
	public Date dateDebut;
	public Date dateFin;
	public Depot depot;

	public static List<PeriodeNonLivrable> periodesNonLivrables = new ArrayList<>();
	private static final String TABLE_NAME = "PeriodeNonLivrable";

	public PeriodeNonLivrable(int id, Date dateDebut, Date dateFin, Depot depot) {
		this.id = id;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.depot = depot;
	}

	public PeriodeNonLivrable(Date dateDebut, Date dateFin, Depot depot) {
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.depot = depot;
	}

	@Override
	public String toString() {
		return "Du " + dateDebut + " au " + dateFin;
	}

	public static void getFromDatabase() {
		periodesNonLivrables.clear();
		try {
			ResultSet res = sql.select(TABLE_NAME);
			while (res.next()) {
				int id = res.getInt("idPeriode");
				Date dateDebut = res.getDate("debut");
				Date dateFin = res.getDate("fin");
				int depotId = res.getInt("Depot_idDepot");
				Depot depot = Depot.getDepotById(depotId);
				periodesNonLivrables.add(new PeriodeNonLivrable(id, dateDebut, dateFin, depot));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadFromDatabase() {
		getFromDatabase();
	}

	@Override
	protected void delete() {
		sql.deletePrepareStatement(TABLE_NAME, new String[]{"idPeriode = " + id});
		periodesNonLivrables.remove(this);
	}

	static void create(PeriodeNonLivrable periodeNonLivrable) {
		sql.createPrepareStatement(TABLE_NAME, new String[]{"debut", "fin", "Depot_idDepot"},
						new String[]{periodeNonLivrable.dateDebut.toString(), periodeNonLivrable.dateFin.toString(), String.valueOf(periodeNonLivrable.depot.id)});
		getFromDatabase();
	}
}

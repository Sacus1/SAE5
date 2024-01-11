package org.SAE.Livraison;

public enum Etat {
	EN_ATTENTE("En attente"),
	EN_COURS("En cours"),
	LIVRE("Livré"),
	ANNULE("Annule");

		private String etat;

		Etat(String etat) {
				this.etat = etat;
		}

		public String getEtat() {
				return etat;
		}
}

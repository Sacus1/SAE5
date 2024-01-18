package org.SAE.Depot;

public enum JourSemaine {
    Lundi,
    Mardi,
    Mercredi,
    Jeudi,
    Vendredi,
    Samedi,
    Dimanche;


    static JourSemaine dayOfWeek(String string) {
	    return switch (string) {
		    case "MONDAY" -> Lundi;
		    case "TUESDAY" -> Mardi;
		    case "WEDNESDAY" -> Mercredi;
		    case "THURSDAY" -> Jeudi;
		    case "FRIDAY" -> Vendredi;
		    case "SATURDAY" -> Samedi;
		    case "SUNDAY" -> Dimanche;
		    default -> null;
	    };
    }
}

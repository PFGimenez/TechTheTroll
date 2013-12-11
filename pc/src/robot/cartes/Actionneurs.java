package robot.cartes;

import robot.serial.Serial;
import utils.Log;
import utils.Read_Ini;
import container.Service;


/**
 * Classe des actionneurs. Utilisée par robot pour bouger les actionneurs.
 * @author pf
 */
public class Actionneurs implements Service {

	// Dépendances
	private Read_Ini config;
	private Log log;
	private Serial serie;

	public Actionneurs(Service config, Service log, Service serie)
	{
		this.config = (Read_Ini)config;
		this.log = (Log)log;
		this.serie = (Serial)serie;
	}
	// un exemple de méthode
	public void bouger_bras(int angle)
	{
		// ABWABWA
	}
}
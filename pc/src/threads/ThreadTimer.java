package threads;

import container.Service;
import obstacles.ObstacleManager;
import exceptions.serial.SerialConnexionException;
import robot.cardsWrappers.LocomotionCardWrapper;
import robot.cardsWrappers.SensorsCardWrapper;
import utils.Config;
import utils.Log;
import utils.Sleep;

/**
 * Thread qui s'occupe de la gestion du temps: début du match, péremption des obstacles
 * C'est lui qui active les capteurs en début de match.
 * @author pf
 *
 */

public class ThreadTimer extends AbstractThread implements Service
{

	// Dépendance
	private Log log;
	private Config config;
	private ObstacleManager obstaclemanager;
	private SensorsCardWrapper capteur;
	private LocomotionCardWrapper deplacements;
	
	private long dureeMatch = 90000;
	private long dateFin;
	public static int obstacleRefreshInterval = 500; // temps en ms entre deux appels par le thread timer du rafraichissement des obstacles de la table
		
	public ThreadTimer(Log log, Config config, ObstacleManager obstaclemanager, SensorsCardWrapper capteur, LocomotionCardWrapper deplacements)
	{
		this.log = log;
		this.config = config;
		this.obstaclemanager = obstaclemanager;
		this.capteur = capteur;
		this.deplacements = deplacements;
		
		updateConfig();
		Thread.currentThread().setPriority(1);
	}

	@Override
	public void run()
	{
		log.debug("Lancement du thread timer", this);

		// les capteurs sont initialement éteints
		config.set("capteurs_on", false);
		capteur.updateConfig();	
		
		// Attente du démarrage du match
		while(!capteur.demarrage_match() && !matchDemarre)
		{
			if(stopThreads)
			{
				log.debug("Arrêt du thread timer avant le début du match", this);
				return;
			}
			Sleep.sleep(50);
		}

		Config.dateDebutMatch = System.currentTimeMillis();

		// Permet de signaler que le match a démarré
		matchDemarre = true;

		// On démarre les capteurs
		config.set("capteurs_on", true);
		capteur.updateConfig();

		log.debug("LE MATCH COMMENCE !", this);

		dateFin = dureeMatch + Config.dateDebutMatch;

		// Le match à démarré. On retire périodiquement les obstacles périmés
		while(System.currentTimeMillis() < dateFin)
		{
			if(stopThreads)
			{
				log.debug("Arrêt du thread timer demandé durant le match", this);
				return;
			}
			obstaclemanager.supprimerObstaclesPerimes();
			
			try
			{
				Thread.sleep(obstacleRefreshInterval);
			}
			catch(Exception e)
			{
				log.warning(e.toString(), this);
			}
		}

		onMatchEnded();
		
		log.debug("Fin du thread timer", this);
		
	}
	
	private void onMatchEnded()
	{
		log.debug("Fin du Match !", this);
		
		finMatch = true;

		// TODO: ici la funny action s'il y en a une
		
		// fin du match : désasser final
		try {
			deplacements.disableRotationnalFeedbackLoop();
			deplacements.disableTranslationnalFeedbackLoop();
		} catch (SerialConnexionException e) {
			e.printStackTrace();
		}
		deplacements.closeLocomotion();
	}
	
	public void updateConfig()
	{
		// facteur 1000 car temps_match est en secondes et duree_match en ms
		try {
			dureeMatch = 1000*Long.parseLong(config.get("temps_match"));
		}
		catch(Exception e)
		{
			log.warning(e, this);
		}
	}
	
}

package astar.arcmanager;

import astar.MemoryManager;
import astar.arc.Arc;
import astar.arc.PathfindingNodes;
import container.Service;
import robot.RobotChrono;
import robot.Speed;
import strategie.GameState;
import utils.Log;
import utils.Config;
import exceptions.ArcManagerException;
import exceptions.FinMatchException;
import exceptions.MemoryManagerException;

/**
 * Réalise les calculs entre PathfindingNodes pour l'AStar.
 * @author pf
 *
 */

public class PathfindingArcManager implements Service, ArcManager {

	private int iterator, id_node_iterator;
	private PathfindingNodes arrivee;
	protected Config config;
	protected Log log;
	private MemoryManager memorymanager;
	private GameState<RobotChrono> state_iterator;

	public PathfindingArcManager(Log log, Config config, MemoryManager memorymanager)
	{
		this.log = log;
		this.config = config;
		this.memorymanager = memorymanager;
		updateConfig();
	}
	
	/**
	 * Renvoie la distance entre deux points.
	 */
	@Override
	public int distanceTo(GameState<RobotChrono> state, Arc arc) throws FinMatchException
	{
		/*
		 * Il n'y a pas d'utilisation de hook.
		 * En effet, les obstacles temporaires sont vérifiés à chaque copie (=chaque segment)
		 * Et la disparition éléments de jeu n'influence pas la recherche de chemin
		 * Par contre, à l'"exécution" par robotchrono du chemin entre deux scripts, là ils seront exécutés.
		 * Rappel: même quand on fait un appel à RobotChrono sans hook, le hook de fin de match est exécuté
		 */
		int temps_debut = state.robot.getTempsDepuisDebutMatch();
		state.robot.va_au_point_pathfinding_no_hook((PathfindingNodes)arc);
		return state.robot.getTempsDepuisDebutMatch() - temps_debut;
	}

	/**
	 * Renvoie la distance à vol d'oiseau
	 */
	@Override
	public int heuristicCost(GameState<RobotChrono> state1)
	{
		// durée de rotation minimale
		int duree = (int)state1.robot.calculateDelta(state1.robot.getPositionPathfinding().getOrientationFinale(arrivee)) * Speed.BETWEEN_SCRIPTS.invertedRotationnalSpeed;
		// durée de translation minimale
		duree += (int)state1.robot.getPositionPathfinding().distanceTo(arrivee)*Speed.BETWEEN_SCRIPTS.invertedTranslationnalSpeed;
		return duree;
	}

	/**
	 * Le hash d'un PathfindingNodes est son ordinal dans l'enum des PathfindingNodes.
	 */
	@Override
	public int getHash(GameState<RobotChrono> state) throws ArcManagerException
	{
		return state.robot.getPositionPathfinding().ordinal();
	}

	/**
	 * Aucune différence parce qu'il n'y a pas de création.
	 */
	@Override
	public int getHashAndCreateIfNecessary(GameState<RobotChrono> state)
	{
		return state.robot.getPositionPathfinding().ordinal();
	}

    @SuppressWarnings("unchecked")
	@Override
    public PathfindingNodes next()
    {
    	return PathfindingNodes.values[iterator];
    }
    
    @Override
    public boolean hasNext()
    {
    	int max_value = PathfindingNodes.length, i;
    	for(i = iterator+1; i < max_value; i++)
    	{
    		if(i == id_node_iterator)
    			continue;
    		if(state_iterator.gridspace.isTraversable(PathfindingNodes.values[id_node_iterator], PathfindingNodes.values[i], state_iterator.robot.getTempsDepuisDebutMatch()))
    			break;
    	}
    	iterator = i;
    	return iterator != PathfindingNodes.length;
    }
    
    @Override
    public void reinitIterator(GameState<RobotChrono> gamestate)
    {
//    	log.debug("Réinit pathfinding iterator!", this);
    	id_node_iterator = gamestate.robot.getPositionPathfinding().ordinal();
    	iterator = -1;
    	state_iterator = gamestate;
    }

	@Override
	public void updateConfig()
	{}

	/**
	 * Utilisé avant process, afin de pouvoir utiliser ensuite isArrivee
	 * @param n
	 */
	public void chargePointArrivee(PathfindingNodes n)
	{
		arrivee = n;
	}

	@Override
	public boolean isArrive(int hash)
	{
		return hash == arrivee.ordinal();
	}
	
	/**
	 * Non utilisé car on ne reconstruit normalement jamais un chemin partiel avec la recherche de chemin
	 * Sinon, on prendrait simplement le point le plus proche (le "-" vient du fait que l'AStar cherche à minimiser cette note)
	 * @param h
	 * @return
	 */
	public int getNoteReconstruct(int h)
	{
		return -(int)PathfindingNodes.values[h].distanceTo(arrivee);
	}
	
	public void destroyGameState(GameState<RobotChrono> state) throws MemoryManagerException
	{
		memorymanager.destroyGameState(state, 0);
	}

	public GameState<RobotChrono> getNewGameState() throws FinMatchException
	{
		return memorymanager.getNewGameState(0);
	}
	
	public void empty()
	{
		memorymanager.empty(0);
	}

}

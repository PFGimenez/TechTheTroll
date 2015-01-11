package scripts.anticipables;

import java.util.ArrayList;

import astar.arc.PathfindingNodes;
import exceptions.FinMatchException;
import exceptions.ScriptHookException;
import exceptions.SerialConnexionException;
import exceptions.UnableToMoveException;
import hook.HookFactory;
import robot.RobotChrono;
import scripts.Script;
import strategie.GameState;
import utils.Config;
import utils.Log;

/**
 * N'est pas vraiment un script à proprement parler. C'est juste une attente.
 * Mis sous la forme d'un script afin d'être utilisé dans la stratégie
 * @author pf
 *
 */

public class ScriptAttente extends Script
{

	public ScriptAttente(HookFactory hookgenerator, Config config, Log log)
	{
		super(hookgenerator, config, log);
	}

	@Override
	public ArrayList<Integer> getVersions(GameState<RobotChrono> state) {
		ArrayList<Integer> version = new ArrayList<Integer>();
		PathfindingNodes entree_sortie = ((RobotChrono)state.robot).getPositionPathfinding();
		if(entree_sortie != null && state.canSleepUntilSomethingChange())
			version.add(entree_sortie.ordinal());
		return version;
	}

	@Override
	public PathfindingNodes point_entree(int id) {
		// ce qui signifie: on ne bouge pas
		return PathfindingNodes.values()[id];
	}

	@Override
	public PathfindingNodes point_sortie(int id) {
		// ce qui signifie: on ne bouge pas
		return PathfindingNodes.values()[id];
	}

	@Override
	protected void execute(int id_version, GameState<?> state)
			throws UnableToMoveException, SerialConnexionException,
			FinMatchException, ScriptHookException
	{
		/**
		 * On attend jusqu'à ce qu'un obstacle ait disparu.
		 */
		state.sleepUntilSomethingChange();
	}

	@Override
	protected void termine(GameState<?> state) throws SerialConnexionException,
			FinMatchException, ScriptHookException
	{
	}

}
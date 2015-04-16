package scripts.anticipables;

import java.util.ArrayList;

import permissions.ReadOnly;
import permissions.ReadWrite;
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
	public ArrayList<PathfindingNodes> getVersions(GameState<RobotChrono,ReadOnly> state)
	{
		ArrayList<PathfindingNodes> version = new ArrayList<PathfindingNodes>();
		PathfindingNodes entree_sortie = GameState.getPositionPathfinding(state);
		if(entree_sortie != null && state.canSleepUntilSomethingChange())
			version.add(entree_sortie);
		return version;
	}

	@Override
	public PathfindingNodes point_sortie(PathfindingNodes id) {
		// ce qui signifie: on ne bouge pas
		return id;
	}

	@Override
	protected void execute(PathfindingNodes id_version, GameState<?,ReadWrite> state)
			throws UnableToMoveException, SerialConnexionException,
			FinMatchException, ScriptHookException
	{
		/**
		 * On attend jusqu'à ce qu'un obstacle ait disparu.
		 */
		GameState.sleepUntilSomethingChange(state);
	}

	@Override
	protected void termine(GameState<?,ReadWrite> state) throws SerialConnexionException,
			FinMatchException, ScriptHookException
	{
	}

}

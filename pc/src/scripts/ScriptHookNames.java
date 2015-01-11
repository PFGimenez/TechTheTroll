package scripts;

/**
 * Les noms des scripts de hooks.
 * @author pf
 *
 */

public enum ScriptHookNames {
	FUNNY_ACTION(false),
	SCRIPT_PREND_PLOT(false);
	
	private boolean canIDoIt; // ce booléan dépend du robot!
	// si on a deux robots, ils ne pourront pas faire la même chose...
	
	ScriptHookNames(boolean canIDoIt)
	{
		this.canIDoIt = canIDoIt;
	}
	
	public boolean canIDoIt()
	{
		return canIDoIt;
	}

}
package tests;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import container.ServiceNames;
import pathfinding.CheminPathfinding;
import pathfinding.astarCourbe.AStarCourbe;
import pathfinding.astarCourbe.ArcCourbe;
import pathfinding.dstarlite.DStarLite;
import pathfinding.dstarlite.GridSpace;
import permissions.ReadOnly;
import robot.DirectionStrategy;
import robot.RobotReal;
import utils.Vec2;

/**
 * Tests unitaires de la recherche de chemin.
 * @author pf
 *
 */

public class JUnit_Pathfinding extends JUnit_Test {

	private DStarLite pathfinding;
	private AStarCourbe pathfindingCourbe;
	private CheminPathfinding chemin;
	private RobotReal robot;
	private GridSpace gridspace;
	
	@Before
    public void setUp() throws Exception {
        super.setUp();
        pathfinding = (DStarLite) container.getService(ServiceNames.D_STAR_LITE);
        pathfindingCourbe = (AStarCourbe) container.getService(ServiceNames.A_STAR_COURBE);
        chemin = (CheminPathfinding) container.getService(ServiceNames.CHEMIN_PATHFINDING);
        robot = (RobotReal) container.getService(ServiceNames.ROBOT_REAL);
        gridspace = (GridSpace) container.getService(ServiceNames.GRID_SPACE);
	}

	@Test
    public void test_chemin_dstarlite() throws Exception
    {
		gridspace.addObstacle(new Vec2<ReadOnly>(400, 1000), false);
		pathfinding.computeNewPath(new Vec2<ReadOnly>(-1000, 200), new Vec2<ReadOnly>(1200, 1500));
		ArrayList<Vec2<ReadOnly>> trajet = pathfinding.itineraireBrut();
		for(Vec2<ReadOnly> v : trajet)
		{
			log.debug(v);
		}
//		pathfinding.updatePath(new Vec2<ReadOnly>(0,600));
    }

	@Test
    public void test_chemin_thetastar() throws Exception
    {
		robot.setPositionOrientationCourbureDirection(new Vec2<ReadOnly>(-1000, 200), 0, 0, true);
		long avant = System.currentTimeMillis();
		for(int i = 0; i < 10000; i++)
		pathfindingCourbe.computeNewPath(new Vec2<ReadOnly>(1000, 400), true, DirectionStrategy.FASTEST);
		log.debug("Durée d'une recherche : "+(System.currentTimeMillis() - avant)/10000.+" ms");
    }

}

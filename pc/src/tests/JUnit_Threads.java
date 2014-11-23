package tests;

import org.junit.Assert;
import org.junit.Test;

import robot.RobotReal;
import robot.cards.Locomotion;
import smartMath.Vec2;
import table.Table;
import threads.ThreadTimer;

/**
 * Tests unitaires des threads
 * @author pf
 *
 */

public class JUnit_Threads extends JUnit_Test {

	@Test
	public void test_arret() throws Exception
	{
		Locomotion deplacements = (Locomotion)container.getService("Deplacements");
		deplacements.set_x(0);
		deplacements.set_y(1500);
		deplacements.set_orientation(0);
		deplacements.set_vitesse_translation(80);
		RobotReal robotvrai = (RobotReal) container.getService("RobotVrai");
		container.getService("threadPosition");
		container.startInstanciedThreads();
		Thread.sleep(100);
		Assert.assertTrue(robotvrai.getPosition().equals(new Vec2(0,1500)));
		container.stopAllThreads();
		deplacements.set_x(100);
		deplacements.set_y(1400);
		Thread.sleep(100);
		Assert.assertTrue(robotvrai.getPosition().equals(new Vec2(0,1500)));
	}

	@Test
	public void test_detection_obstacle() throws Exception
	{
		RobotReal robotvrai = (RobotReal) container.getService("RobotVrai");
		robotvrai.setPosition(new Vec2(0, 900));
		robotvrai.setOrientation(0);
		
		Table table = (Table) container.getService("Table");
		Assert.assertTrue(table.gestionobstacles.nb_obstacles() == 0);
		
		container.getService("threadCapteurs");
		container.startInstanciedThreads();
		Thread.sleep(300);
		Assert.assertTrue(table.gestionobstacles.nb_obstacles() >= 1);

	}
	
	@Test
	public void test_fin_match() throws Exception
	{
		config.set("temps_match", 3);
		container.getService("threadTimer");
		long t1 = System.currentTimeMillis();
		container.startAllThreads();
		while(!ThreadTimer.fin_match)
		{
			Thread.sleep(500);
			if(System.currentTimeMillis()-t1 >= 4000)
				break;
		}
		Assert.assertTrue(System.currentTimeMillis()-t1 < 4000);
	}
	
	@Test
	public void test_demarrage_match() throws Exception
	{
		container.getService("threadTimer");
		System.out.println("Veuillez mettre le jumper");
		Thread.sleep(2000);
		container.startInstanciedThreads();
		Thread.sleep(200);
		Assert.assertTrue(!ThreadTimer.match_demarre);
		System.out.println("Veuillez retirer le jumper");
		Thread.sleep(2000);
		Assert.assertTrue(ThreadTimer.match_demarre);
	}

	@Test
	public void test_serie() throws Exception
	{
		RobotReal robotvrai = (RobotReal) container.getService("RobotVrai");
		robotvrai.setPosition(new Vec2(1000, 1400));
		robotvrai.setOrientation((float)Math.PI);
		container.startAllThreads();
		Thread.sleep(200);
		robotvrai.avancer(1000);
	}

	@Test
	public void test_fin_thread_avant_match() throws Exception
	{
		container.startAllThreads();
		container.stopAllThreads();
	}

	
}
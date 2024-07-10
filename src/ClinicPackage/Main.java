package ClinicPackage;

import ClinicPackage.IO.FileIO;

public class Main 
{
	/**The instance of Main when running the game.
	 * Used to get anything that isn't static inside of Main.
	 */
	public Main Instance;
	
	final public static String GameName = "ClinicPuzzelGame"; //Shouldn't have spaces
	
	public void Initialisation()
	{
		FileIO.CheckFolderspace();
		Logging.Log("Initialised", Logging.LoggingType.Base);
		//Code runs here
		Instance = this;
	}
	
	public void Update()
	{
		//System.out.println("running");
		//System.out.println("Is initialised: " + Instance);
	}
}

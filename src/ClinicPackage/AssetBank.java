package ClinicPackage;

import java.awt.Image;

import Lion8cake.Texture2D;

/** There isn't anything wrong with getting the asset using Lion8cake's Texture2D as long as it isn't being repeated TONS and TONS of times.
 * AssetBank is here to store lots of reused assets so when drawing lots of textures they aren't being gotten from the source files over and over.
 */
public class AssetBank {
	public static void InitiliseTextures()
	{
		PlayerTest = Texture2D.Get("PlayerTest");
		
		TileTestFloor = Texture2D.Get("FloorTest"); //Replace with a Tile array that gets the tile based on ID
		TileTestWall = Texture2D.Get("WallTest");
	}
	
	public static Image PlayerTest;
	
	public static Image TileTestFloor;
	
	public static Image TileTestWall;
}

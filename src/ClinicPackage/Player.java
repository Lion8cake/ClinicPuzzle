package ClinicPackage;

import java.awt.Rectangle;

public class Player extends Entity {

	public static boolean kUp = false;
	
	public static boolean kDown = false;
	
	public static boolean kLeft = false;
	
	public static boolean kRight = false;
	
	public boolean[] isColliding = new boolean[4];
	
	public Player()
	{
		for (int col = 0; col < isColliding.length; col++)
		{
			isColliding[col] = false;
		}
		SetStaticDefaults();
	}
	
	public void SetStaticDefaults()
	{
		x = (int)(8.5 * 32);
		y = (int)(4.5 * 32);
		velocityY = 4;
		velocityX = 4;
		Width = 32;
		Height = 32;
	}
	
	public void UpdateSpeeds()
	{
		//Logging.Log("Key Up is pressed: " + Player.kUp, LoggingType.Base);
		
		//Collision
		int i = (hitbox.x + hitbox.width + 2) / 32;
		int i2 = (hitbox.x - 2) / 32;
		int j = (hitbox.y + hitbox.height + 2) / 32;
		int j2 = (hitbox.y - 2) / 32;
		for (int upDown = hitbox.y; upDown < hitbox.y + hitbox.height; upDown++)
		{
			Tile tileCol = Main.tile[i][upDown / 32];
			Tile tileCol2 = Main.tile[i2][upDown / 32];
			if (tileCol != null && Main.tileSolid[tileCol.Type])
				isColliding[3] = true;
			if (tileCol2 != null && Main.tileSolid[tileCol2.Type])
				isColliding[2] = true;
		}
		for (int leftRight = hitbox.x; leftRight < hitbox.x + hitbox.width; leftRight++)
		{
			Tile tileCol3 = Main.tile[leftRight / 32][j];
			Tile tileCol4 = Main.tile[leftRight / 32][j2];
			if (tileCol3 != null && Main.tileSolid[tileCol3.Type])
				isColliding[1] = true;
			if (tileCol4 != null && Main.tileSolid[tileCol4.Type])
				isColliding[0] = true;
		}
		
		//Movement
		if (Player.kUp && !isColliding[0])
		{
			y -= velocityY;
		}
		if (Player.kDown && !isColliding[1])
		{
			y += velocityY;
		}
		if (Player.kLeft && !isColliding[2])
		{
			x -= velocityX;
		}
		if (Player.kRight && !isColliding[3])
		{
			x += velocityX;
		}
	}
	
	public void Update()
	{
		hitbox = new Rectangle(x, y, Width, Height);
		UpdateSpeeds();
		for (int col = 0; col < isColliding.length; col++)
		{
			isColliding[col] = false;
		}
	}
}

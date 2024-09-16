package ClinicPackage;

import java.awt.Rectangle;

class PlrDirectionTypes {
	public final static int Down = 0;
	public final static int Left = 1;
	public final static int Up = 2;
	public final static int Right = 3;
}

public class Player extends Entity {

	public static boolean kUp = false;
	
	public static boolean kDown = false;
	
	public static boolean kLeft = false;
	
	public static boolean kRight = false;
	
	public static boolean kSpace = false;
	
	public static boolean kESC = false;
	
	public static boolean IsInteracting = false;
	
	public boolean[] isColliding = new boolean[4];
	
	public int dirFacing = 0;
	
	public int InteractionCooldown = 30;
	
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
		x = 0;
		y = 0;
		velocityY = 4;
		velocityX = 4;
		Width = 32;
		Height = 32;
		frameXCount = 4;
	}
	
	public void ResetEffects()
	{
		IsInteracting = false;
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
			Furniture furnCol = Main.furniture[i][upDown / 32];
			Furniture furnCol2 = Main.furniture[i2][upDown / 32];
			if (tileCol != null && (Main.tileSolid[tileCol.Type] || Main.furnitureSolid[furnCol.Type]))
				isColliding[3] = true;
			if (tileCol2 != null && (Main.tileSolid[tileCol2.Type] || Main.furnitureSolid[furnCol2.Type]))
				isColliding[2] = true;
		}
		for (int leftRight = hitbox.x; leftRight < hitbox.x + hitbox.width; leftRight++)
		{
			Tile tileCol3 = Main.tile[leftRight / 32][j];
			Tile tileCol4 = Main.tile[leftRight / 32][j2];
			Furniture furnCol3 = Main.furniture[leftRight / 32][j];
			Furniture furnCol4 = Main.furniture[leftRight / 32][j2];
			if (tileCol3 != null && (Main.tileSolid[tileCol3.Type] || Main.furnitureSolid[furnCol3.Type]))
				isColliding[1] = true;
			if (tileCol4 != null && (Main.tileSolid[tileCol4.Type] || Main.furnitureSolid[furnCol4.Type]))
				isColliding[0] = true;
		}
		
		//Movement
		if (Player.kUp)
		{
			if (!isColliding[0])
				y -= velocityY;
			dirFacing = PlrDirectionTypes.Up;
		}
		if (Player.kDown)
		{
			if (!isColliding[1])
				y += velocityY;
			dirFacing = PlrDirectionTypes.Down;
		}
		if (Player.kLeft)
		{
			if (!isColliding[2])
				x -= velocityX;
			dirFacing = PlrDirectionTypes.Left;
		}
		if (Player.kRight)
		{
			if (!isColliding[3])
				x += velocityX;
			dirFacing = PlrDirectionTypes.Right;
		}
	}
	
	public void Update()
	{
		ResetEffects();
		hitbox = new Rectangle(x, y, Width, Height);
		if (!Main.textBoxOpen)
		{
			UpdateSpeeds();
			if (kSpace)
			{
				IsInteracting = true;
			}
		}
		frameX = dirFacing;
		for (int col = 0; col < isColliding.length; col++)
		{
			isColliding[col] = false;
		}
		Interaction();
		if (InteractionCooldown > 0)
			InteractionCooldown--;
		if (InteractionCooldown <= 0)
			InteractionCooldown = 0;
	}
	
	public void Interaction()
	{
		if (IsInteracting && InteractionCooldown <= 0)
		{
			int x;
			int y;
			Furniture furn;
			if (dirFacing == PlrDirectionTypes.Down)
			{
				x = (hitbox.x + hitbox.width) / 32;
				y = (hitbox.y + (hitbox.height / 2)) / 32;
				furn = Main.furniture[x][y + 1];
			}
			else if (dirFacing == PlrDirectionTypes.Left)
			{
				y = hitbox.y / 32;
				x = (hitbox.x + (hitbox.width / 2)) / 32;
				furn = Main.furniture[x - 1][y];
			}
			else if (dirFacing == PlrDirectionTypes.Up)
			{
				x = hitbox.x / 32;
				y = (hitbox.y + (hitbox.height / 2)) / 32;
				furn = Main.furniture[x][y - 1];
			}
			else
			{
				y = (hitbox.y + hitbox.height) / 32;
				x = (hitbox.x + (hitbox.width / 2)) / 32;
				furn = Main.furniture[x + 1][y];
			}
			if (Main.furnitureInteractable[furn.Type])
			{
				Main.TileInteraction(this, furn.Type, x, y);
			}
		}
	}
}

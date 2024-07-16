package ClinicPackage;

import java.awt.Rectangle;

public class Entity {
	
	/**The X position of the entity.
	 */
	public int x;
	
	/**The Y position of the entity. 
	 */
	public int y;
	
	/**Velocity of the entity in the X position.
	 * Use VelocityY for the entity's Y velocity.
	 */
	public float velocityX;
	
	/**Velocity of the entity in the Y position.
	 * Use VelocityX for the entity's X velocity.
	 */
	public float velocityY;
	
	/**The Width of the Entity Hitbox.
	 */
	public int Width;
	
	/**The Height of the Entity Hitbox.
	 */
	public int Height;
	
	/**The collision rectangle of the Entity
	 */
	protected Rectangle hitbox;
}

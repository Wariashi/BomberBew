package de.wariashi.bomberbew.controller.tempii;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import de.wariashi.bomberbew.Textures;
import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.controller.ControllerOutput;
import de.wariashi.bomberbew.model.Direction;
import de.wariashi.bomberbew.model.Map;
import de.wariashi.bomberbew.model.Material;
import de.wariashi.bomberbew.model.projection.BombData;
import de.wariashi.bomberbew.model.projection.MapData;
import de.wariashi.bomberbew.model.projection.PlayerData;

public class Tempii implements Controller {
	
//build a little home for freedom of movement
private final int HOME = 0;
//build a path in the direction of the bigger dimension
private final int SCOUT = 1;
//build a path to the opponent
private final int HUNT = 2;
//try to kill the enemy
private final int FIGHT = 3;

private int phase = 0;

private int startPosition = 0;

private final int TOP_LEFT = 0;
private final int TOP_RIGHT = 1;
private final int BOTTOM_LEFT = 2;
private final int BOTTOM_RIGHT = 3;

private List<Direction> path = new ArrayList<Direction>();


	@Override
	public String getName() {
		return "Tempii";
	}

	@Override
	public BufferedImage getPlayerImage() {
		return Textures.getTempii();
	}

	@Override
	public ControllerOutput update(ControllerInput input) {
		MapData map = input.getMapData();
		PlayerData playerData = input.getPlayerData();
		List<PlayerData> enemyData = input.getEnemyData();
		ControllerOutput out = new ControllerOutput();
		int currentX = playerData.getTileX();
		int currentY = playerData.getTileY();
		
		
		if(playerData.getOffsetX() == 0 && playerData.getOffsetY() == 0) {
			//System.out.println("Step");
			//out.setDirection(Direction.NORTH_EAST);
			
			int[][] dangerZone = getExplosionTimers(map);

			//if there is no current plan, get one
			if(path.isEmpty()) {
				path = findEscapeRoute(currentX, currentY, currentX, currentY, dangerZone, map, null);
			}
			else {
				out.setDirection(path.get(path.size()-1));
				path.remove(path.size()-1);
				System.out.println("path");
				return out;
			}
			
			for(int i = 0; i<path.size(); i++) {
				System.out.print(path.get(i) + "-");
			}
			if(!path.isEmpty()) {
				System.out.println();
			}
			
			//player in dangerzone
			if(dangerZone[currentX][currentY] != 0) {
				Direction current = null;
				int danger = dangerZone[currentX][currentY];
				List<Direction> options = new ArrayList<Direction>();

				
				if(currentX-1 >= 0 && !map.getMaterial(currentX-1, currentY).isSolid()) {
					if(dangerZone[currentX-1][currentY] == 0) {
						options.add(Direction.WEST);
					}
					else if(dangerZone[currentX-1][currentY] > danger){
						danger = dangerZone[currentX-1][currentY];
						current = Direction.WEST;
					}					
				}
				
				if(currentX+1 < map.getWidth() && !map.getMaterial(currentX+1, currentY).isSolid()) {
					if(dangerZone[currentX+1][currentY] == 0) {
						options.add(Direction.EAST);
					}
					else if(dangerZone[currentX+1][currentY] > danger){
						danger = dangerZone[currentX+1][currentY];
						current = Direction.EAST;
					}					
				}
				
				if(currentY-1 >= 0 && !map.getMaterial(currentX, currentY-1).isSolid()) {
					if(dangerZone[currentX][currentY-1] == 0) {
						options.add(Direction.NORTH);
					}
					else if(dangerZone[currentX][currentY-1] > danger){
						danger = dangerZone[currentX][currentY-1];
						current = Direction.NORTH;
					}					
				}
				
				if(currentY+1 < map.getHeight() && !map.getMaterial(currentX, currentY+1).isSolid()) {
					if(dangerZone[currentX][currentY+1] == 0) {
						options.add(Direction.SOUTH);
					}
					else if(dangerZone[currentX][currentY+1] > danger){
						danger = dangerZone[currentX][currentY+1];
						current = Direction.SOUTH;
					}					
				}
				
				if(!options.isEmpty()) {
					int decision = (int)(Math.random()*options.size());
					out.setDirection(options.get(decision));
					System.out.println("decision" + "-" + options.size() + "-" + decision);
				}
				else if(current != null){
					out.setDirection(current);
				}
				
				//TODO moveBitch();
				
			}
			//not in immediate danger
			else {
				if(!path.isEmpty()) {
					//TODO check for good bomb location
					if(Math.random() < 0.8) {
						out.setDropBomb(true);
					}					
					out.setDirection(path.get(path.size()-1));
					path.remove(path.size()-1);
					System.out.println("path continue");
				}
				else {
					List<Direction> options = new ArrayList<Direction>();
					
					if(currentX-1 >= 0 && dangerZone[currentX-1][currentY] == 0 && !map.getMaterial(currentX-1, currentY).isSolid()) {
						options.add(Direction.WEST);
					}
					if(currentX+1 < map.getWidth() && dangerZone[currentX+1][currentY] == 0 && !map.getMaterial(currentX+1, currentY).isSolid()) {
						options.add(Direction.EAST);
					}
					if(currentY-1 >= 0 && dangerZone[currentX][currentY-1] == 0 && !map.getMaterial(currentX, currentY-1).isSolid()) {
						options.add(Direction.NORTH);
					}
					if(currentY+1 < map.getHeight() && dangerZone[currentX][currentY+1] == 0 && !map.getMaterial(currentX, currentY+1).isSolid()) {
						options.add(Direction.SOUTH);
					}
					if(!options.isEmpty()) {
						int decision = (int)(Math.random()*options.size());
						out.setDirection(options.get(decision));
						System.out.println("decision" + "-" + options.size() + "-" + decision);
					}					
				}				
			}
			
		}else {
			//System.out.println("Currently moving");
		}
		/*System.out.println("Tile: " + playerData.getTileX() + " - " + playerData.getTileY());
		System.out.println("IsIntersection: " + isIntersection(playerData.getTileX(), playerData.getTileY()));
		System.out.println("IsColumn: " + isColumn(playerData.getTileX(), playerData.getTileY()));
		System.out.println("IsRow: " + isRow(playerData.getTileX(), playerData.getTileY()));*/
		return out;
	}
	
	private boolean isIntersection(int TileX, int TileY) {
		return (TileX%2 == 0 && TileY%2 == 0);
	}
	
	private boolean isColumn(int TileX, int TileY) {
		return (TileX%2 == 0);
	}
	
	private boolean isRow(int TileX, int TileY) {
		return (TileY%2 == 0);
	}
	
	private int numAccessbleTiles(MapData map, int playerX, int playerY) {
		int tiles = 0;
		List accessibleTiles = new ArrayList();
		
		return 0;
	}
	
	private List<Integer> bombedTiles(MapData map){
		List<Integer> tiles = new ArrayList();
		
		return tiles;
	}
	
	private List<Direction> findEscapeRoute(int startX, int startY, int currentX, int currentY, int[][] dangerZone, MapData map, Direction last) {
		
		boolean check1 = false;
		boolean check2 = false;
		boolean check3 = false;
		boolean check4 = false;
		List<Direction> path = new ArrayList<Direction>();
		
		int tmpX;
		int tmpY;
		
		//check west		
		if(last != Direction.EAST) {
			tmpX = currentX-1;
			tmpY = currentY;
			if(tmpX < map.getWidth() && tmpX >= 0 && tmpY < map.getHeight() && tmpY >= 0) {
				if(dangerZone[tmpX][tmpY] == 0 && !map.getMaterial(tmpX, tmpY).isSolid()) {
					//path long enough?
					if(tmpX != startX && tmpY != startY) {
						path.add(Direction.WEST);
						return path;
					}else {
						path = findEscapeRoute(startX, startY, tmpX, tmpY, dangerZone, map, Direction.WEST);
						if(!path.isEmpty()) {
							path.add(Direction.WEST);
							return path;
						}						
					}
				}
			}
		}
		
		//check east
		if(last != Direction.WEST) {
			tmpX = currentX+1;
			tmpY = currentY;
			if(tmpX < map.getWidth() && tmpX >= 0 && tmpY < map.getHeight() && tmpY >= 0) {
				if(dangerZone[tmpX][tmpY] == 0 && !map.getMaterial(tmpX, tmpY).isSolid()) {
					//path long enough?
					if(tmpX != startX && tmpY != startY) {
						path.add(Direction.EAST);
						return path;
					}else {
						path = findEscapeRoute(startX, startY, tmpX, tmpY, dangerZone, map,Direction.EAST);
						if(!path.isEmpty()) {
							path.add(Direction.EAST);
							return path;
						}						
					}
				}
			}
		}
		
		
		//check north
		if(last != Direction.SOUTH) {
			tmpX = currentX;
			tmpY = currentY-1;
			if(tmpX < map.getWidth() && tmpX >= 0 && tmpY < map.getHeight() && tmpY >= 0) {
				if(dangerZone[tmpX][tmpY] == 0 && !map.getMaterial(tmpX, tmpY).isSolid()) {
					//path long enough?
					if(tmpX != startX && tmpY != startY) {
						path.add(Direction.NORTH);
						return path;
					}else {
						path = findEscapeRoute(startX, startY, tmpX, tmpY, dangerZone, map, Direction.NORTH);
						if(!path.isEmpty()) {
							path.add(Direction.NORTH);
							return path;
						}
						
					}
				}
			}
		}
		
		
		//check south
		if(last != Direction.NORTH) {
			tmpX = currentX;
			tmpY = currentY+1;
			if(tmpX < map.getWidth() && tmpX >= 0 && tmpY < map.getHeight() && tmpY >= 0) {
				if(dangerZone[tmpX][tmpY] == 0 && !map.getMaterial(tmpX, tmpY).isSolid()) {
					//path long enough?
					if(tmpX != startX && tmpY != startY) {
						path.add(Direction.SOUTH);
						return path;
					}else {
						path = findEscapeRoute(startX, startY, tmpX, tmpY, dangerZone, map, Direction.SOUTH);
						if(!path.isEmpty()) {
							path.add(Direction.SOUTH);
						return path;
						}						
					}
				}
			}
		}
		return path;
	}
	
	/**
	 * Returns a number indicating the state of the tiles in regards of bombs and explosions.
	 * Returns the number of ticks an {@link Material#EXPLOSION explosion} has remaining before
	 * it is removed from the {@link Map map} as a negative number, if there is an explosion. 
	 * Returns the number of remaining ticks until an upcoming explosion based on the currently placed bombs.
	 * A value of 0 indicates that there is no {@link Material#EXPLOSION explosion} 
	 * and there will be no explosion based of the bombs currently placed.
	 * If the given coordinates are outside of the map, this method will fail silently.
	 * 
	 * @param map the Mapdata of the Map to inspect
	 */
	private int[][] getExplosionTimers(MapData map){
		int[][] dangerZone = new int[map.getWidth()][map.getHeight()];
		
		for(int i=0; i<map.getWidth(); i++) {
			for(int j=0; j<map.getHeight(); j++) {
				int explosionTimer = map.getExplosionTimer(i, j);
				BombData bomb = map.getBomb(i, j);
				int bombTimer = 0;
				int range = 0;
				if(bomb != null) {
					bombTimer = bomb.getTimer();
					range = bomb.getRange();
				}
				
				//explosion found
				if(explosionTimer > 0) {
					dangerZone[i][j] = -explosionTimer;
				}
				//bomb found
				if(bombTimer > 0) {
					if(isIntersection(i, j)) {
						for(int k = -range; k<=range; k++) {
							//bigger danger detected
							if(i+k<map.getWidth() && i+k>=0) { //out of bounds detection
								if(dangerZone[i+k][j] == 0 || dangerZone[i+k][j] > bombTimer) {
									dangerZone[i+k][j] = bombTimer;
								}
							}
							
							if(j+k<map.getHeight() && j+k>=0) { //out of bounds detection
								if(dangerZone[i][j+k] == 0 || dangerZone[i][j+k] > bombTimer) {
									dangerZone[i][j+k] = bombTimer;
								}
							}
							
						}
					}
					else if(isRow(i, j)) {
						for(int k = -range; k<=range; k++) {
							//bigger danger detected
							if(i+k<map.getWidth() && i+k>=0) { //out of bounds detection
								if(dangerZone[i+k][j] == 0 || dangerZone[i+k][j] > bombTimer) {
									dangerZone[i+k][j] = bombTimer;
								}
							}
							
						}
					}
					else {
						for(int k = -range; k<=range; k++) {
							//bigger danger detected
							if(j+k<map.getHeight() && j+k>=0) { //out of bounds detection
								if(dangerZone[i][j+k] == 0 || dangerZone[i][j+k] > bombTimer) {
									dangerZone[i][j+k] = bombTimer;
								}
							}
							
						}
					}
				}
			}
		}	

		/*System.out.println("Width: "+map.getWidth());
		System.out.println("Height: "+map.getHeight());
		for(int j=0; j<map.getHeight(); j++) {
			for(int i=0; i<map.getWidth(); i++) {
				System.out.print("|"+dangerZone[i][j]+"|");
			}
			System.out.println("");
		}
		System.out.println("_______________________________________");*/
		return dangerZone;		
	}
}

package com.zerulus.game.tiles;

import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.Vector2f;
import java.awt.Graphics2D;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class TileMap {

    private Sprite sprite;
    private HashMap<String, Block> blocks;
    private int view = 0;
    private int size = 0;
    private String name = "";

    private TileManager tm;

    public TileMap(String file, int width, int height) {
        sprite = new Sprite(file, width, height);
        findName(file);
        blocks = new HashMap<String, Block>();
        size = Math.max(width, height);
    }

    public TileMap(File file, int width, int height) {
        sprite = new Sprite(file, width, height);
        findName(file.toString());
        blocks = new HashMap<String, Block>();
        size = Math.max(width, height);
    }

    public void setTileManager(TileManager tm) { this.tm = tm; }

    public void findName(String file) {
        for(int i = file.length() - 1; i > 0; i--) {
            if(file.charAt(i) == '\\') {
                name = file.substring(i + 1, file.length());
                System.out.println("Name Found is: " + name + "," + file.length());
                return;
            }
        }
    }

    public void setView(int view) { this.view = view; }
    public int getView() { return view; }

    public int getSize() { return size; }
    public void setSize(int w, int h) {
        size = Math.max(w, h);
        sprite.setSize(w, h);
    }

    public Sprite getTileSprite() { return sprite; }

    public void addBlock(int imageX, int imageY, Vector2f pos) {
        if(view == 0) {
            addBlockObj(imageX, imageY, pos);
        }
        addBlockNorm(imageX, imageY, pos);
    }

    private void addBlockNorm(int imageX, int imageY, Vector2f pos) {
        if(blocks.containsKey(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) pos.y / size))) {
            if(blocks.get(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) pos.y / size)).getId() != (imageX + imageY * size)) {

                System.out.println("Removing current block!");
                removeBlock((int) pos.x, (int) pos.y);
                blocks.put(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) pos.y / size),
                                new Block(imageX, imageY, size, pos, this));

            } else {
                System.out.println("Why? Its the same block!");
            }
    	} else {
            if(size > TileManager.minBlockSize) {
                for(int i = 0; i < Math.pow((size / TileManager.minBlockSize), 2); i++) {
                    if(i != 0) {
                        imageX = -1;
                        imageY = 0;
                    }
                    int xt = ((int) pos.x / TileManager.minBlockSize) + (i % (size / TileManager.minBlockSize) );
                    int yt = ((int) pos.y / TileManager.minBlockSize) + ((int) (i / (size / TileManager.minBlockSize)) );

                    blocks.put(Integer.toString(xt) + "," + Integer.toString(yt),
                                    new Block(imageX, imageY, size, pos, this));
                }
    		} else {
                    System.out.println("Key: " + (pos.x / size) + ", " + ((pos.y / size)) + "\nWorldLocation: " + pos);
                    blocks.put(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) (pos.y / size)),
                                    new Block(imageX, imageY, size, pos, this));
    		}
    	}
    }

    public void addBlockObj(int imageX, int imageY, Vector2f pos) {
        if(tm.objBlocks.containsKey(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) pos.y / size))) {
            if(tm.objBlocks.get(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) pos.y / size)).getId() != (imageX + imageY * size)) {

                System.out.println("Removing current block!");
                removeBlock((int) pos.x, (int) pos.y);
                tm.objBlocks.put(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) pos.y / size),
                                new Block(imageX, imageY, size, pos, this));

            } else {
                System.out.println("Why? Its the same block!");
            }
    	} else {
            if(size > TileManager.minBlockSize) {
                for(int i = 0; i < Math.pow((size / TileManager.minBlockSize), 2); i++) {
                    if(i != 0) {
                        imageX = -1;
                        imageY = 0;
                    }
                    int xt = ((int) pos.x / TileManager.minBlockSize) + (i % (size / TileManager.minBlockSize) );
                    int yt = ((int) pos.y / TileManager.minBlockSize) + ((int) (i / (size / TileManager.minBlockSize)) );

                    tm.objBlocks.put(Integer.toString(xt) + "," + Integer.toString(yt),
                                    new Block(imageX, imageY, size, pos, this));
                }
    		} else {
                    System.out.println("Key: " + (pos.x / size) + ", " + ((pos.y / size)) + "\nWorldLocation: " + pos);
                    tm.objBlocks.put(Integer.toString((int) pos.x / size) + "," + Integer.toString((int) (pos.y / size)),
                                    new Block(imageX, imageY, size, pos, this));
    		}
    	}
    }

    /* TODO: fix removeBlock method
     * If the minBlockSize is 16, the method will only
     * remove one part of 32 by 32 tile (for example).
     *
     * One solution is to have the blocks keep track of
     * the other blocks, so when one of them is remove, all
     * of the sibling blocks would be removed as well.
     * */
    public boolean removeBlock(int x, int y) {
    	if(blocks.containsKey(Integer.toString((x / TileManager.minBlockSize)) + "," + Integer.toString((y / TileManager.minBlockSize)))) {
    		blocks.remove(Integer.toString((x / TileManager.minBlockSize)) + "," + Integer.toString((y / TileManager.minBlockSize)));
            if(view == 0) {
                tm.objBlocks.remove(Integer.toString((x / TileManager.minBlockSize)) + "," + Integer.toString((y / TileManager.minBlockSize)));
            }
    		return true;
    	}

    	return false;
    }

    public boolean getBlock(int x, int y){
        return blocks.containsKey(Integer.toString(x) + "," + Integer.toString(y));
    }

    public HashMap<String, Block> getBlocks () { return blocks; }

    public void render(Graphics2D g) {
    	// ArrayList would probably be better
    	// however, it is easier to get individual blocks
    	// using a hash map.
        for(Block block: blocks.values()) {
            block.render(g);
        }
    }

    public String toString() {
        return name;
    }
}

package com.zerulus.game.util;

import com.zerulus.game.tiles.TileManager;
import com.zerulus.game.tiles.TileMap;
import com.zerulus.game.entity.Enemy;

//Sort of
public class AABB {

    private Vector2f pos;
    private float xOffset = 0;
    private float yOffset = 0;
    private float w;
    private float h;
    private float r;
    private int size;
    private Enemy e;

    public AABB(Vector2f pos, int w, int h) {
        this.pos = pos;
        this.w = w;
        this.h = h;

        size = Math.max(w, h);
    }

    public AABB(Vector2f pos, int r, Enemy e) {
        this.pos = pos;
        this.pos.x = pos.x - r / 2 + e.getSize() / 2;
        this.pos.y = pos.y - r / 2 + e.getSize() / 2;
        this.r = r;
        this.e = e;

        size = r;
    }

    public void setBox(Vector2f pos, int w, int h) {
        this.pos = pos;
        this.w = w;
        this.h = h;

        size = Math.max(w, h);
    }

    public void setCircle(Vector2f pos, int r) {
        this.pos = pos;
        this.r = r;

        size = r;
    }

    public Vector2f getPos() { return pos; }

    public float getRadius() { return r; }
    public float getWidth() { return w; }
    public float getHeight() { return h; }
    public float getXOffset() { return xOffset; }
    public float getYOffset() { return yOffset; }

    public void setWidth(float f) {
    	if(f < TileManager.minBlockSize) {
            System.out.println("width cannot be smaller than the smallest tile width");
            w = TileManager.minBlockSize;
    	} else {
            w = f;
    	}

    }
    public void setHeight(float f) {
    	if(f < TileManager.minBlockSize) {
            System.out.println("height cannot be smaller than the smallest tile height");
            h = TileManager.minBlockSize;
    	} else {
            h = f;
    	}

    }

    public void addX(float f) { pos.x += f; }
    public void addY(float f) { pos.y += f; }
    public void setX(float f) { pos.x = f; }
    public void setY(float f) { pos.y = f; }

    // the offset are for tile to player collision
    public void setXOffset(float f) { xOffset = f; }
    public void setYOffset(float f) { yOffset = f; }

    public boolean collides(AABB bBox) {
        float ax = ((pos.getWorldVar().x + (xOffset)) + (w / 2));
        float ay = ((pos.getWorldVar().y + (yOffset)) + (h / 2));
        float bx = ((bBox.pos.getWorldVar().x + (bBox.xOffset / 2)) + (w / 2));
        float by = ((bBox.pos.getWorldVar().y + (bBox.yOffset / 2)) + (h / 2));

        if(Math.abs(ax - bx) < (this.w / 2) + (bBox.w / 2)) {
            if(Math.abs(ay - by) < (this.h / 2) + (bBox.h / 2)) {
                return true;
            }
        }

        return false;
    }

    public boolean colCircleBox(AABB aBox) {

        float cx = (float) (this.pos.getWorldVar().x + r / Math.sqrt(2) - e.getSize() / Math.sqrt(2));
        float cy = (float) (this.pos.getWorldVar().y + r / Math.sqrt(2) - e.getSize() / Math.sqrt(2));

        float xDelta = cx - Math.max(aBox.pos.getWorldVar().x + (aBox.getWidth() / 2), Math.min(cx, aBox.pos.getWorldVar().x));
        float yDelta = cy - Math.max(aBox.pos.getWorldVar().y + (aBox.getWidth() / 2), Math.min(cy, aBox.pos.getWorldVar().y));

        if((xDelta * xDelta + yDelta * yDelta) < ((this.r / Math.sqrt(2)) * (this.r / Math.sqrt(2)))) {
			return true;
		}

		return false;
    }

    /* TODO: Improve collision design
     * currently collision is detected by corner collision.
     * So, that means creating boxes that accounts for an entity
     * being larger than the tiles.
     *
     * For example, entity A = 32px and tiles = 16px
     * therefore, entity A needs four collision boxes that are 16 by 16
     * if entity A = 64px, then
     * 16 collision boxes
     *
     * Therefore, this method becomes a problem really fast.
     * One solution is using SAT collision detection and storing
     * the tiles in an array list. However, that means you have to
     * loop through the entire list just to check if one block collided
     * with an entity.
     * */
    public boolean collisionTile(float ax, float ay, TileManager tm) {
        int boxes = (int) Math.pow((int) (size / TileManager.minBlockSize), 2);
        for(int b = 0; b < boxes; b++) {
            for(int c = 0; c < 4; c++) {

                int xt = 0;
                int yt = 0;

                if(boxes > 1) {
                    xt = (int) (( (pos.x + (b % (boxes / 2)) * TileManager.minBlockSize) + ax) + (c % 2) *
                        (w - TileManager.minBlockSize) + xOffset) / TileManager.minBlockSize;

                    yt = (int) (( (pos.y + ((int)(b / (boxes / 2))) * TileManager.minBlockSize) + ay) + ((int)(c / 2)) *
                        (h - TileManager.minBlockSize) + yOffset) / TileManager.minBlockSize;
                } else {
                    xt = (int) (( (pos.x + ax) + (c % 2) * (w) + xOffset)) / TileManager.minBlockSize;

                    yt = (int) (( (pos.y + ay) + ((int)(c / 2)) * (h) + yOffset)) / TileManager.minBlockSize;
                }

                if(tm.objBlocks.containsKey(String.valueOf(xt) + "," + String.valueOf(yt))) {
                    return true;
                }
            }
        }
        return false;
    }
}

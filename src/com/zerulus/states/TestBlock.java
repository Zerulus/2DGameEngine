package com.zerulus.states;

import com.zerulus.tiles.TileManager;
import com.zerulus.util.InputHandler;
import com.zerulus.util.MouseHandler;

public class TestBlock {

    private TileManager tm;

    private boolean notClicked = true;
    private boolean canEdit = false;

    private int tile = 0;
    private String num = "";

    public TestBlock(TileManager tm) {
        this.tm = tm;
    }

    private void addTile(int i) {
        num = num + String.valueOf(i);
    }

    private void makeTile() {
        if(num != "")
            tile = Integer.valueOf(num);
        else
            System.out.println("Tile Sprite not selected");

        num = "";
    }

    public void input(InputHandler keys, MouseHandler mouse) {

        keys.tick();

        if(canEdit) {
            if(keys.zero.clicked)
                addTile(0);
            if(keys.one.clicked)
                addTile(1);
            if(keys.two.clicked)
                addTile(2);
            if(keys.three.clicked)
                addTile(3);
            if(keys.four.clicked)
                addTile(4);
            if(keys.five.clicked)
                addTile(5);
            if(keys.six.clicked)
                addTile(6);
            if(keys.seven.clicked)
                addTile(7);
            if(keys.eight.clicked)
                addTile(8);
            if(keys.nine.clicked)
                addTile(9);
        }

        if(keys.enter.clicked) {
            makeTile();
        }


        if(keys.edit.clicked && canEdit == false) {
            System.out.println("Edit mode on");
            canEdit = true;
        }
        else if(keys.edit.clicked && canEdit == true) {
            System.out.println("Edit mode off");
            canEdit = false;
        }


        if(mouse.getButton() == 1 && notClicked) {

            tm.addBlock(tile, (mouse.getX() - (mouse.getX() % 32)) / 2, (mouse.getY() - (mouse.getY() % 32)) / 2, 0);
            notClicked = false;
        }

        if(mouse.getButton() == -1) {
            notClicked = true;
        }

    }

}

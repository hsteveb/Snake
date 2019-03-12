package com.bignerdranch.android.animations.Model;

import com.bignerdranch.android.animations.Shapes.Pencil;

import java.util.ArrayList;

public class Snake {

    private int mSnakeLength;

    private ArrayList<Pencil> mSnakeodyCoordinates;

    public Snake()
    {
        mSnakeodyCoordinates= new ArrayList<>();
        mSnakeLength = 5;
    }


    public void initialPosition(int width, int height)
    {
        mSnakeodyCoordinates.add(new Pencil(width/2, height/2));
    }

    public void addSnakeCoordinate(int x, int y)
    {
        mSnakeodyCoordinates.add(new Pencil(x, y));
    }

    public void checkSnakeLength()
    {
        if(mSnakeodyCoordinates.size() > mSnakeLength)
        mSnakeodyCoordinates.remove(0);
    }

    public void  addSnakeLength(int value)
    {
        mSnakeLength += value;
    }

    public ArrayList<Pencil> getSnakebodycoordinates() {
        return mSnakeodyCoordinates;
    }


}

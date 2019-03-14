package com.bignerdranch.android.animations.Model;

import java.util.ArrayList;

public class Snake {

    private int mSnakeLength;

    private ArrayList<Coordinates> mSnakeodyCoordinates;

    public Snake()
    {
        mSnakeodyCoordinates= new ArrayList<>();
        mSnakeLength = 20;
    }

    /*This method is used to position the snake in the middle of the screen*/
    public void initialPosition(int width, int height)
    {
        mSnakeodyCoordinates.add(new Coordinates(width/2, height/2));
    }

    public void addSnakeCoordinates(int x, int y)
    {
        mSnakeodyCoordinates.add(new Coordinates(x, y));
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

    public void addSnakeCoordinates(Coordinates coordinates)
    {
        mSnakeodyCoordinates.add(coordinates);
    }

    public ArrayList<Coordinates> getSnakebodycoordinates() {
        return mSnakeodyCoordinates;
    }


    public void reset()
    {
        mSnakeodyCoordinates.clear();
        mSnakeLength = 20;
    }
}

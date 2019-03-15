package com.bignerdranch.android.animations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import com.bignerdranch.android.animations.Model.Snake;
import com.bignerdranch.android.animations.Shapes.Line;
import com.bignerdranch.android.animations.Model.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/* The entire game engine is found here but can change in the future to make it easier to read if we need to.
 */
public class PlayView extends SurfaceView implements GestureDetector.OnGestureListener, Runnable, SurfaceHolder.Callback {


    private static final String TAG = "DrawingView";
    private Paint mBackgroundColor, shapecolor, fruitcolor;
    private Context mContext;
    private boolean running, newswipe, newgame, killeditself;
    private int height, width, mCoordinatesRange, x , y, score, radius;
    private Coordinates fruit;
    private Snake mSnake;
    private Random mRandom, direction;
    private GestureDetectorCompat mCompat;
    private Direction mDirection;
    private Thread mThread;
    private SurfaceHolder mSurfaceHolder;
    private Fragment mFragment;

    public PlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mSnake = new Snake();
        mBackgroundColor = new Paint();
        shapecolor = new Paint();
        fruitcolor = new Paint();
        mBackgroundColor.setColor(getResources().getColor(R.color.darkGreen3));
        shapecolor.setColor(getResources().getColor(R.color.darkRed5));
        fruitcolor.setColor(Color.BLUE);
        running = false;
        newswipe = false;
        newgame = false;
        killeditself = false;
        height = getResources().getDisplayMetrics().heightPixels;
        width = getResources().getDisplayMetrics().widthPixels;
        //Log.d(TAG, "Height: " + height + "Width: " + width);
        mRandom = new Random(System.currentTimeMillis());
        direction = new Random(System.currentTimeMillis());
        mCompat = new GestureDetectorCompat(getContext(), this);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mCoordinatesRange = 10;
        score = 0;
        radius = 10;

    }

    /*Checks the phone screen height and width and saves the values for later use*/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        //Log.d(TAG, "height: " + h + " width: " + w);
    }

    /*draws the view of our game*/
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.save();
        canvas.drawPaint(mBackgroundColor);
        canvas.restore();
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    /*Thread where the snake is given the initial mCoordinatesRange and
            where those mCoordinatesRange are saved and displayed on the screen.
            Invalidate is used to call onDraw again*/
    /*Variables x and y are used to update the position of the snake head while*/
    @Override
    public void run() {

        if(newgame)
        {
            fruit = fruitcoordinates();
            mSnake.initialPosition(width, height);
            x = mSnake.getSnakebodycoordinates().get(0).getX();
            y = mSnake.getSnakebodycoordinates().get(0).getY();

            mFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((InfoInterface)mFragment).info(0);
                }
            });

            /*Direction that the snake is going to go first*/
            switch (direction.nextInt(4)) {
                case 0:
                    mDirection = Direction.up;
                    break;
                case 1:
                    mDirection = Direction.left;
                    break;
                case 2:
                    mDirection = Direction.down;
                    break;
                default:
                    mDirection = Direction.right;
                    break;
            }

        }

        try {
            while(running) {
                if (mSurfaceHolder.getSurface().isValid()) {

                    Canvas canvas;
                    canvas = mSurfaceHolder.lockCanvas();
                    if(canvas == null)
                        break;

                    canvas.save();
                    canvas.drawPaint(mBackgroundColor);
                    canvas.drawCircle(fruit.getX(), fruit.getY(), radius, fruitcolor);

                    //Log.d(TAG, "size: " + mSnake.getSnakebodycoordinates().size());

                    /*This part prevents the snake from turning back around. It looks like he's going through himself*/
                    if (mSnake.getSnakebodycoordinates().size() > 1 && newswipe) {
                        Coordinates prevpoint = mSnake.getSnakebodycoordinates().get(mSnake.getSnakebodycoordinates().size() - 2);
                        //Log.d(TAG, "x: " + x + " y: " + y + " prevx: " + prevpoint.getX() + "prevy: " + prevpoint.getY());

                        if (mDirection == Direction.up) {
                            if (prevpoint.getX() == x && prevpoint.getY() == y - this.mCoordinatesRange) {
                                mDirection = Direction.down;
                            }
                        } else if (mDirection == Direction.down) {
                            if (prevpoint.getX() == x && prevpoint.getY() == y + this.mCoordinatesRange)
                                mDirection = Direction.up;


                        } else if (mDirection == Direction.left) {
                            if (prevpoint.getX() == x - this.mCoordinatesRange && prevpoint.getY() == y)
                                mDirection = Direction.right;
                        } else {
                            if (prevpoint.getX() == x + this.mCoordinatesRange && prevpoint.getY() == y)
                                mDirection = Direction.left;
                        }

                    }

                    Coordinates snakeHead = new Coordinates(x, y);
                    if(newgame)
                    {
                        newgame = false;
                    }
                    else
                    {
                        switch (mDirection) {
                            case up:
                                y -= mCoordinatesRange;
                                break;
                            case left:
                                x -= mCoordinatesRange;
                                break;
                            case down:
                                y += mCoordinatesRange;
                                break;
                            case right:
                                x += mCoordinatesRange;
                                break;
                            default:
                                break;
                        }

                        snakeHead = new Coordinates(x, y);
                        mSnake.addSnakeCoordinates(x, y);
                    }




                    mSnake.checkSnakeLength();

                    /* Puts the coordinates to make the snake body and also checks if the snake ate itself*/
                    if (mSnake.getSnakebodycoordinates().size() == 1) {

                        Coordinates point = mSnake.getSnakebodycoordinates().get(0);
                        canvas.drawCircle(point.getX(), point.getY(), radius, shapecolor);

                    } else {
                        for (int i = 0; i < mSnake.getSnakebodycoordinates().size(); i++) {
                            Coordinates coordinates1 = mSnake.getSnakebodycoordinates().get(i);
                            canvas.drawCircle(coordinates1.getX(), coordinates1.getY(), 10, shapecolor);

                            if (snakeHead.getX() == coordinates1.getX() && snakeHead.getY() == coordinates1.getY() && i != mSnake.getSnakebodycoordinates().size() - 1)
                                killeditself = true;
                        }
                    }

                    /* Checks if the snake ate the fruit. Might use circle arc coordinates for this check.*/
                    eatfruit(snakeHead);


                    canvas.restore();
                    mSurfaceHolder.unlockCanvasAndPost(canvas);

                    /*Checks to see if snake is out of bounds and if so it's pretty much dead*/
                    if (snakeHead.getY() <= 0 && snakeHead.getX() >= 0 && snakeHead.getX() <= width || snakeHead.getX() <= 0 && snakeHead.getY() >= 0 && snakeHead.getY() <= height ||
                            snakeHead.getY() >= height && snakeHead.getX() >= 0 && snakeHead.getX() <= width || snakeHead.getX() >= width && snakeHead.getY() >= 0 && snakeHead.getY() <= height)
                        killeditself = true;

                    /*If snake died, game over*/
                    if (killeditself) {
                        killeditself = false;
                        running = false;

                        mFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((InfoInterface) mFragment).reset();
                            }
                        });
                    }

                }

                //Thread.sleep(500);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

    }

    /*Used to put the fruit in a random spot on the screen*/
    private Coordinates fruitcoordinates()
    {
        Coordinates coordinates = new Coordinates(mRandom.nextInt(width), mRandom.nextInt(height));
        while(coordinates.getX() <= this.mCoordinatesRange && coordinates.getX() >= (width - this.mCoordinatesRange) && coordinates.getY() <= this.mCoordinatesRange && coordinates.getY() >= (height - this.mCoordinatesRange))
        {
            coordinates.setX(mRandom.nextInt(width));
            coordinates.setY(mRandom.nextInt(height));
            /*if((coordinates.getX() % this.mCoordinatesRange) != 0 && (coordinates.getY() % this.mCoordinatesRange) != 0)
            {
                coordinates.setX(mRandom.nextInt(width));
                coordinates.setY(mRandom.nextInt(height));
            }
            else if((coordinates.getX() % this.mCoordinatesRange) != 0)
            {
                coordinates.setX(mRandom.nextInt(width));
            }
            else
            {
                coordinates.setY(mRandom.nextInt(height));
            }*/
        }

        return coordinates;
    }

    private void eatfruit(Coordinates snakeHead)
    {
        /*int Sangle1 = 0, Sangle2 = 0, Hangle1 = 0, Hangle2 = 0;
        switch (mDirection) {
            case up:
                Sangle1 = 0;
                Sangle2 = 180;
                Hangle1 = 180;
                Hangle2 = 360;
                break;
            case left:
                Sangle1 = 90;
                Sangle2 = 270;
                Hangle1 = 270;
                Hangle2 = 450;
                break;
            case down:
                Sangle1 = 180;
                Sangle2 = 360;
                Hangle1 = 0;
                Hangle2 = 180;
                break;
            case right:
                Sangle1 = 270;
                Sangle2 = 450;
                Hangle1 = 180;
                Hangle2 = 270;
                break;
            default:
                break;
        }

        Map<Integer, Integer> angles = new HashMap<>();

        for(int i = Sangle1; i <= Sangle2; i++)
        {
            int x = (int) (snakeHead.getX() + (radius / 2) * Math.cos(Math.toRadians(i % 360)));
            int y = (int)(snakeHead.getY() - (radius / 2) * Math.sin(Math.toRadians(i % 360)));
            //Log.d(TAG, String.format("i: %d snake x: %f y: %f head x: %d y: %d", i, x, y, snakeHead.getX(), snakeHead.getY()));

            angles.put(new Integer(x), new Integer(y));
        }

        for(int i = Hangle1; i <= Hangle2; i++)
        {
            double x = (fruit.getX() + radius * Math.cos(Math.toRadians(i % 360)));
            double y = (fruit.getY() - radius * Math.sin(Math.toRadians(i % 360)));


            if(angles.get(new Integer(fruit.getX())) != null)
            {
                if(angles.get(new Integer(fruit.getX())).compareTo(new Integer(fruit.getY())) == y)
                    Log.d(TAG, "did touch the fruit");
                    //Log.d(TAG, String.format("i: %d fruit x: %f y: %f ", i, x, y));
            }
            if(angles.containsKey(new Double(x)) && angles.get(new Double(x)).intValue() == y)
            {
                mSnake.addSnakeLength(5);
                score += 5;

                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((InfoInterface) mFragment).info(score);
                    }
                });

                fruit = fruitcoordinates();
                break;
            }
        }
        stopgame();*/

        if (snakeHead.getX() >= (fruit.getX() - radius) && snakeHead.getX() <= (fruit.getX() + radius) && snakeHead.getY() >= (fruit.getY() - radius) && snakeHead.getY() <= (fruit.getY() + radius))
        {
            mSnake.addSnakeLength(5);
            score += 5;

            mFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((InfoInterface) mFragment).info(score);
                }
            });

            fruit = fruitcoordinates();
        }
    }


    /*used to start the game*/
    public void startgame()
    {
        if(!running)
        {
            mSnake.reset();
            score = 0;
            newgame = true;
            running = true;
            mThread = new Thread(this);
            mThread.start();
        }

    }

    /*Stops the snake for now
    * Might change the it to pause so that we don't have to restart
    * the game all over again.*/
    public void stopgame()
    {
        running = false;
        killeditself = false;

        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resumegame()
    {
        running = true;
        mThread = new Thread(this);
        mThread.start();
    }

    public void setupitem(Button item, Fragment fragment)
    {
        mFragment = fragment;
    }

    public boolean playing()
    {
        return running;
    }



    /*Gesture implementations used to detect swiping patterns that are going to be used in moving the snake*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mCompat.onTouchEvent(event))
            return true;

        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        //Log.d(TAG, motionEvent.getX() + " whats up!®");

        if(running)
        {
            float x1 = motionEvent.getX();
            float y1 = motionEvent.getY();

            float x2 = motionEvent1.getX();
            float y2 = motionEvent1.getY();

            mDirection = getDirection(x1,y1,x2,y2);
            newswipe = true;
        }

        return onSwipe(mDirection);

    }

    public enum Direction {
        up,
        down,
        left,
        right;

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         * <p>
         * Up: [45, 135]
         * Right: [0,45] and [315, 360]
         * Down: [225, 315]
         * Left: [135, 225]
         *
         * @param angle an angle from 0 to 360 - e
         * @return the direction of an angle
         */
        public static Direction get(double angle) {
            if (inRange(angle, 45, 135)) {
                return Direction.up;
            } else if (inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
                return Direction.right;
            } else if (inRange(angle, 225, 315)) {
                return Direction.down;
            } else {
                return Direction.left;
            }

        }
    }

    private static boolean inRange(double angle, float init, float end){
        return (angle >= init) && (angle < end);
    }

    public double getAngle(float x1, float y1, float x2, float y2) {

        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }

    public Direction getDirection(float x1, float y1, float x2, float y2){
        double angle = getAngle(x1, y1, x2, y2);
        return Direction.get(angle);
    }

    public boolean onSwipe(Direction direction){
        return false;
    }
}

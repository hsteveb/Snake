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

import com.bignerdranch.android.animations.Shapes.Line;
import com.bignerdranch.android.animations.Shapes.Pencil;

import java.util.ArrayList;
import java.util.Random;

/* The entire game engine is found here but can change in the future to make it easier to read if we need to.
 */
public class PlayView extends SurfaceView implements GestureDetector.OnGestureListener, Runnable, SurfaceHolder.Callback {


    private static final String TAG = "DrawingView";
    private Paint mBackgroundColor, shapecolor, fruitcolor;
    private Context mContext;
    private ArrayList<Pencil> mPencilArrayList;
    private ArrayList<Line>mLineArrayList;
    private boolean running, newswipe, newgame, killeditself;
    private int height, width, snakelength, coordinates, x , y, score;
    private Pencil fruit, snake;
    private Random mRandom, direction;
    private GestureDetectorCompat mCompat;
    private Direction mDirection;
    private Thread mThread;
    private SurfaceHolder mSurfaceHolder;
    private Fragment mFragment;


    public PlayView(Context context) {
        this(context, null);
        mContext = context;
    }

    public PlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mBackgroundColor = new Paint();
        shapecolor = new Paint();
        fruitcolor = new Paint();
        mBackgroundColor.setColor(getResources().getColor(R.color.darkGreen3));
        shapecolor.setColor(getResources().getColor(R.color.darkRed5));
        fruitcolor.setColor(Color.BLUE);
        mPencilArrayList = new ArrayList<>();
        mLineArrayList = new ArrayList<>();
        running = false;
        newswipe = false;
        newgame = false;
        killeditself = false;
        height = getResources().getDisplayMetrics().heightPixels;
        width = getResources().getDisplayMetrics().widthPixels;
        Log.d(TAG, "Height: " + height + "Width: " + width);
        mRandom = new Random();
        direction = new Random();
        mCompat = new GestureDetectorCompat(getContext(), this);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        coordinates = 10;
        score = 0;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        Log.d(TAG, "height: " + h + " width: " + w);
    }

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

    /*Thread where the snake is given the initial coordinates and
            where those coordinates are saved and displayed on the screen.
            Invalidate is used to call onDraw again*/
    /*Variables x and y are used to update the position of the snake head while*/
    @Override
    public void run() {

        Canvas canvas;
        if(newgame)
        {
            fruit = fruitcoordinates();
            snake = initialposition();

            mFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((InfoInterface)mFragment).info(0);
                }
            });

            //int x = mRandom.nextInt(height);
            x = (int)snake.getX();
            //int y = mRandom.nextInt(width);
            y = (int)snake.getY();

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

            newgame = false;
        }



        try {
            while(running) {
                if (mSurfaceHolder.getSurface().isValid()) {

                    canvas = mSurfaceHolder.lockCanvas();
                    if(canvas == null)
                        break;
                    canvas.save();
                    canvas.drawPaint(mBackgroundColor);
                    canvas.drawCircle(fruit.getX(), fruit.getY(), 10, fruitcolor);

                    Log.d(TAG, "size: " + mPencilArrayList.size());

                    if (mPencilArrayList.size() > 1 && newswipe) {
                        Pencil prevpoint = mPencilArrayList.get(mPencilArrayList.size() - 2);
                        Log.d(TAG, "x: " + x + " y: " + y + " prevx: " + prevpoint.getX() + "prevy: " + prevpoint.getY());

                        if (mDirection == Direction.up) {
                            if (prevpoint.getX() == x && prevpoint.getY() == y - coordinates) {
                                mDirection = Direction.down;
                            }
                        } else if (mDirection == Direction.down) {
                            if (prevpoint.getX() == x && prevpoint.getY() == y + coordinates)
                                mDirection = Direction.up;


                        } else if (mDirection == Direction.left) {
                            if (prevpoint.getX() == x - coordinates && prevpoint.getY() == y)
                                mDirection = Direction.right;
                        } else {
                            if (prevpoint.getX() == x + coordinates && prevpoint.getY() == y)
                                mDirection = Direction.left;
                        }

                    }

                    switch (mDirection) {
                        case up:
                            y -= coordinates;
                            break;
                        case left:
                            x -= coordinates;
                            break;
                        case down:
                            y += coordinates;
                            break;
                        case right:
                            x += coordinates;
                            break;
                        default:
                            break;
                    }


                    Pencil pencil = new Pencil(x, y);
                    mPencilArrayList.add(pencil);

                    if (mPencilArrayList.size() == 1) {
                        Line line = new Line(x, y, x, y);
                        mLineArrayList.add(line);
                    } else if (newswipe) {
                        Line prev = mLineArrayList.get(mLineArrayList.size() - 1);

                        Line line = new Line(prev.getX2(), prev.getY2(), x, y);
                        mLineArrayList.add(line);
                        newswipe = false;
                    } else {
                        Line line = mLineArrayList.get(mLineArrayList.size() - 1);
                        line.setX2(x);
                        line.setY2(y);
                        mLineArrayList.add(0, line);
                    }

                    if (mPencilArrayList.size() > snakelength) {
                        mPencilArrayList.remove(0);
                    }


                    if (mPencilArrayList.size() == 1) {
                        Pencil point = mPencilArrayList.get(0);
                        canvas.drawCircle(point.getX(), point.getY(), 10, shapecolor);
                    } else {
                        for (int i = 0; i < mPencilArrayList.size(); i++) {
                            Pencil pencil1 = mPencilArrayList.get(i);
                            canvas.drawCircle(pencil1.getX(), pencil1.getY(), 10, shapecolor);

                            if (pencil.getX() == pencil1.getX() && pencil.getY() == pencil1.getY() && i != mPencilArrayList.size() - 1)
                                killeditself = true;
                            //Line line = mLineArrayList.get(i);
                            //canvas.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), shapecolor);
                        }

                    }

                    if (pencil.getX() >= (fruit.getX() - 10) && pencil.getX() <= (fruit.getX() + 10) && pencil.getY() >= (fruit.getY() - 10) && pencil.getY() <= (fruit.getY() + 10)) {
                        snakelength += 5;
                        score += 5;

                        mFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((InfoInterface) mFragment).info(score);
                            }
                        });

                        fruit = fruitcoordinates();
                    }

                    canvas.restore();
                    mSurfaceHolder.unlockCanvasAndPost(canvas);

                    if (pencil.getY() <= 0 && pencil.getX() >= 0 && pencil.getX() <= width || pencil.getX() <= 0 && pencil.getY() >= 0 && pencil.getY() <= height ||
                            pencil.getY() >= height && pencil.getX() >= 0 && pencil.getX() <= width || pencil.getX() >= width && pencil.getY() >= 0 && pencil.getY() <= height)
                        killeditself = true;

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
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

    }


    /*This method is used to position the snake in the middle of the screen*/
    private Pencil initialposition()
    {
        return new Pencil(width/2, height/2);
    }

    /*Used to put the fruit in a random spot on the screen*/
    private Pencil fruitcoordinates()
    {
        Pencil pencil = new Pencil(mRandom.nextInt(width), mRandom.nextInt(height));
        while((pencil.getX() % coordinates) != 0 && (pencil.getY() % coordinates) != 0)
        {
            if((pencil.getX() % coordinates) != 0 && (pencil.getY() % coordinates) != 0)
            {
                pencil.setX(mRandom.nextInt(width));
                pencil.setY(mRandom.nextInt(height));
            }
            else if((pencil.getX() % coordinates) != 0)
            {
                pencil.setX(mRandom.nextInt(width));
            }
            else
            {
                pencil.setY(mRandom.nextInt(height));
            }
        }

        return pencil;
    }


    /*used to start the game*/
    public void startgame()
    {
        if(!running)
        {
            if(mPencilArrayList.size() > 0)
            {
                mPencilArrayList.clear();
                mLineArrayList.clear();
            }

            score = 0;
            snakelength = 20;
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

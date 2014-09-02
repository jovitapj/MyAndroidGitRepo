package com.jovita.cyongeethu;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MyWallpaperService extends WallpaperService {

	 Bitmap bitmap,heartbitmap;
  @Override
  public Engine onCreateEngine() {
	   bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.smallportraitback);
	   heartbitmap=BitmapFactory.decodeResource(getResources(), R.drawable.heart);
    return new MyWallpaperEngine();
  }

  private class MyWallpaperEngine extends Engine {
    private final Handler handler = new Handler();
    private final Runnable drawRunner = new Runnable() {
      @Override
      public void run() {
        draw();
      }

    };
    private List<MyPoint> circles;
    private Paint paint = new Paint();
    private int width;
    int height;
    private boolean visible = true;
    private int maxNumber;
    private boolean touchEnabled;

    public MyWallpaperEngine() {
      SharedPreferences prefs = PreferenceManager
          .getDefaultSharedPreferences(MyWallpaperService.this);
      maxNumber = Integer
          .valueOf(prefs.getString("numberOfCircles", "5"));
      touchEnabled = prefs.getBoolean("touch", true);
      circles = new ArrayList<MyPoint>();
      paint.setAntiAlias(true);
      paint.setColor(Color.BLUE);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeJoin(Paint.Join.BEVEL);
      paint.setStrokeWidth(5f);
      handler.post(drawRunner);
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
      this.visible = visible;
      if (visible) {
        handler.post(drawRunner);
      } else {
        handler.removeCallbacks(drawRunner);
      }
    }

    


  @Override
  public void onSurfaceDestroyed(SurfaceHolder holder) {
    super.onSurfaceDestroyed(holder);
    this.visible = false;
    handler.removeCallbacks(drawRunner);
  }

  @Override
  public void onSurfaceChanged(SurfaceHolder holder, int format,
      int width, int height) {
    this.width = width;
    this.height = height;
    super.onSurfaceChanged(holder, format, width, height);
  }

  @Override
  public void onTouchEvent(MotionEvent event) {
    if (touchEnabled) {

      float x = event.getX();
      float y = event.getY();
      SurfaceHolder holder = getSurfaceHolder();
  // Bitmap pic=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
  
      Canvas canvas = null;
     
     
      try {
        canvas = holder.lockCanvas();
        if (canvas != null) {
        	
       //   canvas.drawColor(Color.BLACK);
        //	canvas.drawBitmap(pic, 0,0, null);
        	
         
          circles.clear();
          circles.add(new MyPoint(String.valueOf(circles.size() + 1), (int)x, (int)y));
          drawCircles(canvas, circles);

        }
      } finally {
        if (canvas != null)
          holder.unlockCanvasAndPost(canvas);
      }
      super.onTouchEvent(event);
    }
  }

  private void draw() {
    SurfaceHolder holder = getSurfaceHolder();
    
    Canvas canvas = null;
    try {
      canvas = holder.lockCanvas();
     
     
     
    	
      if (canvas != null) {
        if (circles.size() >= maxNumber) {
          circles.clear();
        }
        int x = (int) (width * Math.random());
        int y = (int) (height * Math.random());
        circles.add(new MyPoint(String.valueOf(circles.size() + 1),
            x, y));
        drawCircles(canvas, circles);
      }
    } finally {
      if (canvas != null)
        holder.unlockCanvasAndPost(canvas);
    }
    handler.removeCallbacks(drawRunner);
    if (visible) {
      handler.postDelayed(drawRunner, 5000);
    }
  }

  // Surface view requires that all elements are drawn completely
  private void drawCircles(Canvas canvas, List<MyPoint> circles) {
	
	  //canvas.drawColor(Color.WHITE);
	  bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
	  heartbitmap=Bitmap.createScaledBitmap(heartbitmap, 70, 70, true);
	  canvas.drawBitmap(bitmap, 0, 20, null);
	 
	  
    for (MyPoint point : circles) {
      //canvas.drawCircle(point.x, point.y, 20.0f, paint);
    	canvas.drawBitmap(heartbitmap, point.x, point.y, null);
		
    }
  }
} 
}
package com.example.instagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {

	
	private ScaleGestureDetector detector;
	private float scale = 1f;
	private static float minZoom = 0.5f, maxZoom = 3f;
	private Matrix transform;

	
	private int activePointerID = -1;
	private float lastX, lastY;
	private int offsetX = 0, offsetY = 0;

		private int width, height;
	private int imageWidth, imageHeight;

	public ZoomImageView(Context context, AttributeSet set, int defStyle) {
		super(context, set, defStyle);
	}

	public ZoomImageView(Context context, AttributeSet set) {
		super(context, set);
	}

	public ZoomImageView(Context context) {
		super(context);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		
		imageWidth = bm.getWidth();
		imageHeight = bm.getHeight();
		
		setupPinchZoom();
		
		Log.i("crb", "image size = (" + imageWidth + ", " + imageHeight + ")");
	}
	public void setupPinchZoom() {
		
		DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		
		if (width > height) {
			scale = (float) height / imageHeight;
		} else {
			scale = (float) width / imageWidth;
		}

		
		if (scale > maxZoom)
			maxZoom = scale;
		if (scale < minZoom)
			minZoom = scale;

		
		transform = new Matrix();
		transform.setScale(scale, scale);
		transform.postTranslate(-306 * scale, -306 * scale);

				detector = new ScaleGestureDetector(getContext(), new ScaleListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		
		if (detector != null)
			detector.onTouchEvent(event);

		
		final int action = MotionEventCompat.getActionMasked(event);

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event);
			final float x = MotionEventCompat.getX(event, pointerIndex);
			final float y = MotionEventCompat.getY(event, pointerIndex);

			
			lastX = x;
			lastY = y;
			activePointerID = MotionEventCompat.getPointerId(event, pointerIndex);

			break; }

		case MotionEvent.ACTION_MOVE: {
						final int pointerIndex = MotionEventCompat.findPointerIndex(event, activePointerID);

			final float x = MotionEventCompat.getX(event, pointerIndex);
			final float y = MotionEventCompat.getY(event, pointerIndex);

			
			if (detector != null && !detector.isInProgress()) {
				final float dx = x - lastX;
				final float dy = y - lastY;

				
				offsetX += dx;
				offsetY += dy;

				
				lastX = x;
				lastY = y;

				invalidate();
			}

			break; }

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			activePointerID = -1;			
			break; }

		case MotionEvent.ACTION_POINTER_UP: {
			
			final int pointerIndex = MotionEventCompat.getActionIndex(event);
			final int pointerID = MotionEventCompat.getPointerId(event, pointerIndex);

			
			if (pointerID == activePointerID) {
				final int newPointerIndex = (pointerIndex == 0 ? 1 : 0);
				lastX = MotionEventCompat.getX(event, newPointerIndex);
				lastY = MotionEventCompat.getY(event, newPointerIndex);
				activePointerID = MotionEventCompat.getPointerId(event, newPointerIndex);
			}
			break; }
		}


		fitToScreen();
		return true;
	}
	
	public void fitToScreen() {
		
		int edgeX;
		if (scale * imageWidth >= width)
			edgeX = (int) ((scale * imageWidth - width) / 2);
		else
			edgeX = (int) ((width - scale * imageWidth) / 2);
		
		if (offsetX > edgeX)
			offsetX = edgeX;
		else if (offsetX < -edgeX)
			offsetX = -edgeX;
		
		int edgeY;
		if (scale * imageHeight <= height)
			edgeY = (int) ((scale * imageHeight - height) / 2);
		else
			edgeY = (int) ((height - scale * imageHeight) / 2);
		
		if (offsetY < edgeY)
			offsetY = edgeY;
		else if (offsetY > -edgeY)
			offsetY = -edgeY;
	}

	@Override
	public void onDraw(Canvas canvas) {
		BitmapDrawable draw = (BitmapDrawable) this.getDrawable();
		if (draw != null) {		
			
			canvas.translate(width / 2 + offsetX, height / 2 + offsetY);
			canvas.drawBitmap(draw.getBitmap(), transform, null);
		}
	}

		private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			
			scale *= detector.getScaleFactor();
			scale = Math.max(minZoom, Math.min(scale, maxZoom));

			
			transform.setScale(scale, scale);
			transform.postTranslate(-306 * scale, -306 * scale);

			
			invalidate();
			return true;
		}
	}
}

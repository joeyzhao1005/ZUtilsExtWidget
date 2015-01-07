package com.kit.widget.imageview;

import com.kit.extend.widget.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {

	private Paint paint;
	private int round = 5;

	private Paint paint2;

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public RoundImageView(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {

		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		paint2 = new Paint();

		// 去锯齿
		paint2.setAntiAlias(true);
		paint2.setFilterBitmap(true);

		paint2.setXfermode(null);
	}

	@Override
	public void draw(Canvas canvas) {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Config.ARGB_8888);
		Canvas canvas2 = new Canvas(bitmap);
		super.draw(canvas2);

		int roundHeight = getHeight() / 2;
		int roundWidth = getWidth() / 2;

		if (roundHeight >= roundWidth) {
			round = roundWidth;
		} else {
			round = roundHeight;
		}

		drawLiftUp(canvas2);
		drawRightUp(canvas2);
		drawLiftDown(canvas2);
		drawRightDown(canvas2);
		canvas.drawBitmap(bitmap, 0, 0, paint2);
		bitmap.recycle();
	}

	private void drawLiftUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, round);
		path.lineTo(0, 0);
		path.lineTo(round, 0);
		path.arcTo(new RectF(0, 0, round * 2, round * 2), -90, -90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawLiftDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, getHeight() - round);
		path.lineTo(0, getHeight());
		path.lineTo(round, getHeight());
		path.arcTo(new RectF(0, getHeight() - round * 2, 0 + round * 2,
				getWidth()), 90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawRightDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth() - round, getHeight());
		path.lineTo(getWidth(), getHeight());
		path.lineTo(getWidth(), getHeight() - round);
		path.arcTo(new RectF(getWidth() - round * 2, getHeight() - round * 2,
				getWidth(), getHeight()), 0, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawRightUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth(), round);
		path.lineTo(getWidth(), 0);
		path.lineTo(getWidth() - round, 0);
		path.arcTo(new RectF(getWidth() - round * 2, 0, getWidth(),
				0 + round * 2), -90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}
}

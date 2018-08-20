package com.kit.widget.imageview.shapeimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kit.extend.widget.R;

public class ShapeImageView extends ImageView {

    private Bitmap bitmap;
    private Bitmap bitmapPadding;

    private Bitmap shapeLayerBitmap;
    private Drawable shapeLayerDrawable;

    private int shapeColor;
    private boolean isShapeImageShow;
    private int viewWidth;
    private int viewHeight;
    private float contentPadding;
    private Paint paint;


    public ShapeImageView(Context context) {
        this(context, null, 0);
        init(context, null);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView);
            shapeLayerDrawable = ta.getDrawable(R.styleable.ShapeImageView_shape_src);
            shapeColor = ta.getColor(R.styleable.ShapeImageView_shape_color, Color.TRANSPARENT);
            isShapeImageShow = ta.getBoolean(R.styleable.ShapeImageView_is_shape_image_show, false);
            contentPadding = ta.getDimension(R.styleable.ShapeImageView_content_padding, 0f);
            ta.recycle();
        }

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
//        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (shapeLayerDrawable == null && shapeLayerBitmap == null) {
            super.onDraw(canvas);
        } else {
            bitmap = createImage();
            if (null != bitmap) {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }

    }

    private Bitmap createImage() {
        if (shapeLayerDrawable != null) {
            shapeLayerBitmap = getBitmapFromDrawable(shapeLayerDrawable);
        }
        Bitmap showLayerBitmap = getBitmapFromDrawable(getDrawable());
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Bitmap finalBmp = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBmp);


        if (contentPadding != 0) {
            if (null != shapeLayerBitmap) {
                shapeLayerBitmap = getCenterInsideBitmap(shapeLayerBitmap, viewWidth, viewHeight);
                paint.setColorFilter(new PorterDuffColorFilter(shapeColor, PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(shapeLayerBitmap, 0, 0, paint);
            }

            if (null != showLayerBitmap) {
                showLayerBitmap = getCenterCropBitmap(showLayerBitmap, viewWidth, viewHeight);
                paint.setColorFilter(new PorterDuffColorFilter(shapeColor, PorterDuff.Mode.CLEAR));
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                canvas.drawBitmap(showLayerBitmap, 0 + contentPadding / 2, 0 + contentPadding / 2, paint);
            }

        } else {
            if (null != shapeLayerBitmap) {
                shapeLayerBitmap = getCenterInsideBitmap(shapeLayerBitmap, viewWidth, viewHeight);
                canvas.drawBitmap(shapeLayerBitmap, 0, 0, paint);
            }

            if (null != showLayerBitmap) {
                showLayerBitmap = getCenterCropBitmap(showLayerBitmap, viewWidth, viewHeight);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(showLayerBitmap, 0, 0, paint);
            }

            if (null != shapeLayerBitmap) {
                if (shapeColor != Color.TRANSPARENT && !isShapeImageShow) {
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                    paint.setColorFilter(new PorterDuffColorFilter(shapeColor, PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(shapeLayerBitmap, 0, 0, paint);
                } else if (isShapeImageShow) {
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                    canvas.drawBitmap(shapeLayerBitmap, 0, 0, paint);
                }

            }
        }

        return finalBmp;
    }

    /**
     * 类比ScaleType.CENTER_INSIDE
     */
    private Bitmap getCenterInsideBitmap(Bitmap src, float sideLength) {
        float srcWidth = src.getWidth();
        float srcHeight = src.getHeight();
        float scaleWidth = 0;
        float scaleHeight = 0;

        if (srcWidth > srcHeight) {
            scaleWidth = sideLength;
            scaleHeight = (sideLength / srcWidth) * srcHeight;
        } else if (srcWidth < srcHeight) {
            scaleWidth = (sideLength / srcHeight) * srcWidth;
            scaleHeight = sideLength;
        } else {
            scaleWidth = scaleHeight = sideLength;
        }

        return Bitmap.createScaledBitmap(src, (int) scaleWidth, (int) scaleHeight, false);
    }


    /**
     * 类比ScaleType.CENTER_INSIDE
     */
    private Bitmap getCenterInsideBitmap(Bitmap src, float rectWidth, float rectHeight) {

        float srcRatio = ((float) src.getWidth()) / src.getHeight();
        float rectRadio = rectWidth / rectHeight;
        if (srcRatio < rectRadio) {
            return getCenterInsideBitmap(src, rectHeight);
        } else {
            return getCenterInsideBitmap(src, rectWidth);
        }
    }


    /**
     * 类比ScaleType.CENTER_CROP
     */
    private Bitmap getCenterCropBitmap(Bitmap src, float rectWidth, float rectHeight) {

        rectWidth = rectWidth - contentPadding;
        rectHeight = rectHeight - contentPadding;

        float srcRatio = ((float) src.getWidth()) / src.getHeight();
        float rectRadio = rectWidth / rectHeight;
        if (srcRatio < rectRadio) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(src, (int) rectWidth, (int) ((rectWidth / src.getWidth()) * src.getHeight()), false);
            return Bitmap.createBitmap(scaledBitmap, 0, (int) ((scaledBitmap.getHeight() - rectHeight) / 2), (int) rectWidth, (int) rectHeight);
        } else {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(src, (int) ((rectHeight / src.getHeight()) * src.getWidth()), (int) rectHeight, false);
            return Bitmap.createBitmap(scaledBitmap, (int) ((scaledBitmap.getWidth() - rectWidth) / 2), 0, (int) rectWidth, (int) rectHeight);
        }
    }

    /**
     * Drawable转Bitmap
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


    public Drawable getShapeDrawable() {
        return shapeLayerDrawable;
    }

    public void setShapeDrawable(Drawable shapeLayerDrawable) {
        this.shapeLayerDrawable = shapeLayerDrawable;
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int shapeColor) {
        this.shapeColor = shapeColor;
    }

    public boolean isShapeImageShow() {
        return isShapeImageShow;
    }

    public void setShapeImageShow(boolean shapeImageShow) {
        isShapeImageShow = shapeImageShow;
    }

    public float getContentPadding() {
        return contentPadding;
    }

    public void setContentPadding(float contentPadding) {
        this.contentPadding = contentPadding;
    }

    //    @Override
//    public void setImageBitmap(Bitmap bm) {
//        super.setImageBitmap(bm);
//        bitmap = bm;
//    }
//
//    @Override
//    public void setImageDrawable(Drawable drawable) {
//        super.setImageDrawable(drawable);
//        bitmap = getBitmapFromDrawable(drawable);
//    }
//
//    @Override
//    public void setImageResource(int resId) {
//        super.setImageResource(resId);
//        bitmap = getBitmapFromDrawable(getDrawable());
//    }
//
//    @Override
//    public void setImageURI(Uri uri) {
//        super.setImageURI(uri);
//        bitmap = getBitmapFromDrawable(getDrawable());
//    }

    //    private void setBitmaps() {
//        if (null == getBackground()) {
//            throw new IllegalArgumentException(String.format("background is null."));
//        } else {
//            backgroundBitmap = getBitmapFromDrawable(getBackground());
//            invalidate();
//        }
//    }

}
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
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.kit.extend.widget.R;
import com.kit.utils.DensityUtils;

public class ShapeImageView extends AppCompatImageView {

    private Bitmap bitmap;

    private Bitmap shapeBitmap;
    private Drawable shapeDrawable;
    private Bitmap bgRectBmp = null;
    private Bitmap srourceBitmap;
    private Bitmap shadowBitmap;

    private int shapeColor;
    private boolean isShapeImageShow;
    private int viewWidth;
    private int viewHeight;
    private int contentPadding;
    private Paint paint;
    private int shadowRadius;
    private int shadowColor = 0x80000000;
//    private int shadowDx = 0;
//    private int shadowDy = 0;

//    private Canvas canvas;


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
            shapeDrawable = ta.getDrawable(R.styleable.ShapeImageView_ShapeImageView_shape_src);
            shapeColor = ta.getColor(R.styleable.ShapeImageView_ShapeImageView_shape_color, Color.TRANSPARENT);
            isShapeImageShow = ta.getBoolean(R.styleable.ShapeImageView_ShapeImageView_is_shape_image_show, false);
            contentPadding = ta.getDimensionPixelSize(R.styleable.ShapeImageView_ShapeImageView_content_padding, 0);
            shadowRadius = ta.getDimensionPixelSize(R.styleable.ShapeImageView_ShapeImageView_shadow_radius, 0);
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

        if (shapeDrawable == null || shapeBitmap == null || shadowRadius == 0 || getDrawable() == null) {
            super.onDraw(canvas);
        } else {

            setupSource();
            bitmap = createImage();
            if (null != bitmap) {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        setup();
    }

    private void setupBgRectBmp() {
        if (viewWidth <= 0 || viewHeight <= 0) {
            return;
        }
        bgRectBmp = Bitmap.createBitmap((viewHeight - shadowRadius), (viewHeight - shadowRadius), Bitmap.Config.ARGB_8888);
    }

    private void setupSource() {
        if (getDrawable() == null) {
            return;
        }
        if (getDrawable() instanceof BitmapDrawable) {
            srourceBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        } else {
            srourceBitmap = getBitmapFromDrawable(getDrawable());
        }
        if (srourceBitmap != null) {
            srourceBitmap = getCenterCropBitmap(srourceBitmap, viewWidth - shadowRadius, viewHeight - shadowRadius);
        }
    }

    private void setupShapeIfNeed() {
        if (shapeBitmap == null) {
            setupShape();
        }
    }

    private void setupShape() {
        if (shapeDrawable != null) {
            shapeBitmap = getBitmapFromDrawable(shapeDrawable);
            if (shapeBitmap != null) {
                shapeBitmap = getCenterInsideBitmap(shapeBitmap, (int) (viewWidth), (int) (viewHeight - shadowRadius));
                shadowBitmap = shapeBitmap.extractAlpha();
            }
        }
    }


    private void setup() {
        if (viewWidth <= 0 || viewHeight <= 0 || getDrawable() == null) {
            return;
        }

        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
        }


        if (bgRectBmp == null) {
            setupBgRectBmp();
        }

        setupSource();

        setupShapeIfNeed();

        setupShapeIfNeed();


//        if (canvas == null) {
//            canvas = new Canvas(bitmap);
//        }

    }

    private Bitmap createImage() {
        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        paint.setXfermode(null);
        paint.setColorFilter(null);
        paint.clearShadowLayer();

        if (null != shapeBitmap) {
            if (shadowRadius > 0) {
                bgRectBmp.eraseColor(Color.BLACK);
                canvas.drawBitmap(bgRectBmp, (0 + shadowRadius / 2), (0), paint);
            } else {
                bitmap.eraseColor(Color.BLACK);
                canvas.drawBitmap(bitmap, 0, 0, paint);
            }


            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            if (shapeColor != Color.TRANSPARENT) {
                paint.setColorFilter(new PorterDuffColorFilter(shapeColor, PorterDuff.Mode.SRC_IN));
            }
            canvas.drawBitmap(shapeBitmap, (0 + shadowRadius / 2), (0), paint);
        }

        if (null != srourceBitmap) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            paint.setColorFilter(null);
            canvas.drawBitmap(srourceBitmap, 0 + contentPadding + shadowRadius / 2, 0 + contentPadding, paint);
        }
        if (shadowRadius > 0) {
            if (shadowBitmap != null) {
                paint.setShadowLayer(shadowRadius / DensityUtils.getScale(), 0, shadowRadius / DensityUtils.getScale(), shadowColor);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                paint.setColorFilter(null);
                canvas.drawBitmap(shadowBitmap, shadowRadius / 2, 0, paint);
            }
        }


        return bitmap;
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

        rectWidth = rectWidth - contentPadding * 2;
        rectHeight = rectHeight - contentPadding * 2;

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
        if (viewWidth <= 0 || viewHeight <= 0) {
            return null;
        }
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
        return shapeDrawable;
    }

    public void setShapeDrawable(Drawable shapeLayerDrawable) {
        this.shapeDrawable = shapeLayerDrawable;
        setupShapeIfNeed();
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

    public int getContentPadding() {
        return contentPadding;
    }

    public void setContentPadding(int contentPadding) {
        this.contentPadding = contentPadding;
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(int radius) {
        int old = shadowRadius;
        this.shadowRadius = radius;
        if (old != shadowRadius) {
            setupShape();
            setupBgRectBmp();
        }
    }

    //    public int getShadowDx() {
//        return shadowDx;
//    }
//
//    public void setShadowDx(int shadowDx) {
//        this.shadowDx = shadowDx;
//        this.shadowRadius = (int) Math.floor(DensityUtils.getScale() * (shadowDy > 0 && shadowDx > shadowDy ? shadowDy : shadowDx == 0 ? shadowDy : 0));
//    }
//
//    public int getShadowDy() {
//        return shadowDy;
//    }
//
//    public void setShadowDy(int shadowDy) {
//        this.shadowDy = shadowDy;
//        this.shadowRadius = (int) Math.floor(DensityUtils.getScale() * (shadowDy > 0 && shadowDx > shadowDy ? shadowDy : shadowDx == 0 ? shadowDy : 0));
//    }


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
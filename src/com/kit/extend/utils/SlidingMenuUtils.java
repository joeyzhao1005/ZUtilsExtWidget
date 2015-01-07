package com.kit.extend.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.slidingmenu.lib.SlidingMenu;

public class SlidingMenuUtils {

	/**
	 * 
	 * @Title createMenu
	 * @Description 创建menu
	 * 
	 * @param context
	 *            上下文
	 * @param mode
	 *            左滑右滑还是双滑 SlidingMenu.LEFT_RIGHT
	 * @param shadowWidth
	 *            阴影边距
	 * @param leftShadowDrawable
	 *            左边阴影
	 * @param rightShadowDrawable
	 *            右边阴影
	 * @param behindOffset
	 *            菜单占屏幕的比例
	 * @param touchModeAbove
	 *            触碰屏幕的范围
	 * @param fadeEnabled
	 *            滑动时菜单的是否淡入淡出
	 * @param fadeDegree
	 *            淡入淡出的比例
	 * 
	 * @param behindScrollScale
	 *            滑动时拖拽效果
	 * 
	 * @param menuLayout
	 *            左边menu视图
	 * @param secondaryMenuLayout
	 *            右边menu视图
	 * 
	 * @param context
	 *            上下文
	 * @param context
	 *            上下文
	 * @param context
	 *            上下文
	 * 
	 * 
	 * @return SlidingMenu 返回类型
	 */
	public static SlidingMenu createMenu(Context context, int mode,
			int shadowWidth, Drawable leftShadowDrawable,
			Drawable rightShadowDrawable, int behindOffset, int touchModeAbove,
			boolean fadeEnabled, float fadeDegree, float behindScrollScale,
			View menuLayout, View secondaryMenuLayout) {

		SlidingMenu menu = new SlidingMenu(context);
		// 设置是左滑还是右滑，还是左右都可以滑
		menu.setMode(mode);
		// 设置阴影宽度
		menu.setShadowWidth(shadowWidth);
		// 设置左菜单阴影图片
		menu.setShadowDrawable(leftShadowDrawable);
		// 设置右菜单阴影图片
		menu.setSecondaryShadowDrawable(rightShadowDrawable);
		// 设置菜单占屏幕的比例
		menu.setBehindOffset(behindOffset);
		// 设置滑动时菜单的是否淡入淡出
		menu.setFadeEnabled(fadeEnabled);
		// 设置淡入淡出的比例
		menu.setFadeDegree(fadeDegree);
		// 设置滑动时拖拽效果
		menu.setBehindScrollScale(behindScrollScale);
		// 设置要使菜单滑动，触碰屏幕的范围
		menu.setTouchModeAbove(touchModeAbove);
		menu.attachToActivity((Activity) context, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(menuLayout);
		menu.setSecondaryMenu(secondaryMenuLayout);

		return menu;

	}

}

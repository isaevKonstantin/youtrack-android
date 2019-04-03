package com.konstantinisaev.youtrack.core.rv

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics

@Suppress("JoinDeclarationAndAssignment")
class PieProgressDrawable : Drawable() {

	private var mPaint: Paint
	private lateinit var mBoundsF: RectF
	private lateinit var mInnerBoundsF: RectF
	private val startAngle = 0f
	private var mDrawTo: Float = 0.toFloat()

	init {
		mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
	}

	/**
	 * Set the border width.
	 * @param widthDp in dip for the pie border
	 */
	fun setBorderWidth(widthDp: Float, dm: DisplayMetrics) {
		val borderWidth = widthDp * dm.density
		mPaint.strokeWidth = borderWidth
	}

	/**
	 * @param color you want the pie to be drawn in
	 */
	fun setColor(color: Int) {
		mPaint.color = color
	}

	override fun draw(canvas: Canvas) {
		// Rotate the canvas around the center of the pie by 90 degrees
		// counter clockwise so the pie stars at 12 o'clock.
		canvas.rotate(-90f, bounds.centerX().toFloat(), bounds.centerY().toFloat())
		mPaint.style = Paint.Style.STROKE
		canvas.drawOval(mBoundsF, mPaint)
		mPaint.style = Paint.Style.FILL
		canvas.drawArc(mInnerBoundsF, startAngle, mDrawTo, true, mPaint)

		// Draw inner oval and text on top of the pie (or add any other
		// decorations such as a stroke) here..
		// Don't forget to rotate the canvas back if you plan to add text!
		// ...
	}

	override fun onBoundsChange(bounds: Rect) {
		super.onBoundsChange(bounds)
		mInnerBoundsF = RectF(bounds)
		mBoundsF = mInnerBoundsF
		val halfBorder = (mPaint.strokeWidth / 2f + 0.5f).toInt()
		mInnerBoundsF.inset(halfBorder.toFloat(), halfBorder.toFloat())
	}

	override fun onLevelChange(level: Int): Boolean {
		val drawTo = startAngle + 360.toFloat() * level / 100f
		val update = drawTo != mDrawTo
		mDrawTo = drawTo
		return update
	}

	override fun setAlpha(alpha: Int) {
		mPaint.alpha = alpha
	}

	override fun setColorFilter(cf: ColorFilter?) {
		mPaint.colorFilter = cf
	}

	override fun getOpacity(): Int {
		return mPaint.alpha
	}
}
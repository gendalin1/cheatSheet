package com.example.cheatsheet.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class ProgressLoad @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val speedInPercentage = 1.5f
    private var xMovement: Float = 0.0f
    private val rightDrawable: GradientDrawable = GradientDrawable()
    private val leftDrawable: GradientDrawable = GradientDrawable()
    private var roundX = 10f
    private var roundY = 10f

    init {
        if (isInEditMode)
            setGradientColors(intArrayOf(gray.toInt(), lightGray.toInt()), 10f, 10f)
        rightDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT;
        rightDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        rightDrawable.shape = GradientDrawable.RECTANGLE;
        leftDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT;
        leftDrawable.orientation = GradientDrawable.Orientation.RIGHT_LEFT
        leftDrawable.shape = GradientDrawable.RECTANGLE;
    }

    fun setGradientColors(colors: IntArray, x: Float, y: Float) {
        this.roundX = x
        this.roundY = y
        rightDrawable.colors = colors
        leftDrawable.colors = colors
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        rightDrawable.setBounds(0, 0, widthSize, heightSize)
        leftDrawable.setBounds(0, 0, widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        val path = Path().apply {
            addRoundRect(
                RectF(0f,0f,width.toFloat(),height.toFloat()),
                this@ProgressLoad.roundX,
                this@ProgressLoad.roundY,
                Path.Direction.CCW
            )
        }
        canvas.clipPath(path)
        if (xMovement < width) {
            canvas.translate(xMovement, 0.0f)
            rightDrawable.draw(canvas)
            canvas.translate(-width.toFloat(), 0.0f)
            leftDrawable.draw(canvas)
        } else {
            //now the left one is actually on the right
            canvas.translate(xMovement - width, 0.0f)
            leftDrawable.draw(canvas)
            canvas.translate(-width.toFloat(), 0.0f)
            rightDrawable.draw(canvas)
        }
        canvas.restore()
        xMovement += speedInPercentage * width / 100.0f
        if (isInEditMode)
            return
        if (xMovement >= width * 2.0f)
            xMovement = 0.0f
        invalidate()
    }

    companion object{
        @ColorInt
        val gray = 0xFFC7C7C7

        @ColorInt
        val lightGray = 0xFFEFF0F5

        val DEFAULT_COLOR = intArrayOf(gray.toInt(), lightGray.toInt())
    }
}
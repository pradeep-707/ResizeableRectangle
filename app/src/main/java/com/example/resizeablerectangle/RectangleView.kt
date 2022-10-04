package com.example.resizeablerectangle

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class RectangleView : View {
    private lateinit var mRect: RectF
    private lateinit var mPaint: Paint

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(set: AttributeSet?) {
        mRect = RectF()
        mRect.top = 100F
        mRect.left = 100F
        mRect.bottom = mRect.top + 400F
        mRect.right = mRect.left + 500F
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = Color.rgb(255, 128, 0)
    }

    companion object {
        private const val RESIZE_MARGIN = 50
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val returnValue = super.onTouchEvent(event)

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {

                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y

                val corner = getCorner(x, y)

                when (corner) {
                    Corner.TOP_LEFT -> {
                        mRect.top = y
                        mRect.left = x
                    }
                    Corner.TOP_RIGHT -> {
                        mRect.top = y
                        mRect.right = x
                    }
                    Corner.BOTTOM_LEFT -> {
                        mRect.bottom = y
                        mRect.left = x
                    }
                    Corner.BOTTOM_RIGHT -> {
                        mRect.bottom = y
                        mRect.right = x
                    }
                    Corner.TOP -> {
                        mRect.top = y
                    }
                    Corner.LEFT -> {
                        mRect.left = x
                    }
                    Corner.RIGHT -> {
                        mRect.right = x
                    }
                    Corner.BOTTOM -> {
                        mRect.bottom = y
                    }
                }

                if (corner != Corner.NO_CORNER) {
                    postInvalidate()
                }

                if (x < mRect.right && x > mRect.left && y < mRect.bottom && y > mRect.top) {
                    val width = mRect.right - mRect.left
                    val height = mRect.bottom - mRect.top
                    mRect.top = y - height / 2
                    mRect.bottom = mRect.top + height
                    mRect.left = x - width / 2
                    mRect.right = mRect.left + width
                    postInvalidate()
                }

                return true
            }
        }

        return returnValue
    }

    private fun getCorner(x: Float, y: Float): Corner {
        if (y - mRect.top < RESIZE_MARGIN) {
            if (x - mRect.left < RESIZE_MARGIN) {
                return Corner.TOP_LEFT
            } else if (mRect.right - x < RESIZE_MARGIN) {
                return Corner.TOP_RIGHT
            }
            return Corner.TOP
        }
        if (mRect.bottom - y < RESIZE_MARGIN) {
            if (x - mRect.left < RESIZE_MARGIN) {
                return Corner.BOTTOM_LEFT
            } else if (mRect.right - x < RESIZE_MARGIN) {
                return Corner.BOTTOM_RIGHT
            }
            return Corner.BOTTOM
        }

        if (x - mRect.left < RESIZE_MARGIN) {
            return Corner.LEFT
        }

        if (mRect.right - x < RESIZE_MARGIN) {
            return Corner.RIGHT
        }

        return Corner.NO_CORNER
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        canvas.drawRect(mRect, mPaint)
    }

    enum class Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP,
        LEFT,
        RIGHT,
        BOTTOM,
        NO_CORNER
    }
}

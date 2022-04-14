package com.example.clockapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min

class Clock(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    private var secondHandSize: Float = 0.0f
    private var minuteHandSize: Float = 0.0f
    private var hourHandSize: Float = 0.0f

    private var paintSecondHand: Paint = Paint()
    private var paintMinuteHand: Paint = Paint()
    private var paintHourHand: Paint = Paint()

    private var height: Int? = 0
    private var width: Int? = 0

    private var fontSize = 0
    private var radius = 0
    private var paintCircle: Paint? = null

    private var isInit = false


    init {
        initAttr(context = context, attributeSet = attributeSet)
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawCenter(canvas)
        drawMarks(canvas)
        secondPaint()
        minutePaint()
        hourPaint()
        drawHands(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun initClock() {
        height = getHeight()
        width = getWidth()
        fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics)
                .toInt()
        val min = min(height!!, width!!)
        radius = min / 2 - 20
        paintCircle = Paint()

        isInit = true
    }

    private fun initAttr(context: Context?, attributeSet: AttributeSet?) {
        if (attributeSet == null) return
        val typedArray = context!!.obtainStyledAttributes(attributeSet, R.styleable.AnalogClock)

        val colorSecondHand =
            typedArray.getColor(R.styleable.AnalogClock_colorSecondHand, Color.RED)
        paintSecondHand.color = colorSecondHand
        secondHandSize = typedArray.getDimension(R.styleable.AnalogClock_sizeSecondHand, 150f)


        val colorMinuteHand =
            typedArray.getColor(R.styleable.AnalogClock_colorMinuteHand, Color.BLUE)
        paintMinuteHand.color = colorMinuteHand
        minuteHandSize = typedArray.getDimension(R.styleable.AnalogClock_sizeMinuteHand, 200f)


        val colorHourHand = typedArray.getColor(R.styleable.AnalogClock_colorHourHand, Color.BLACK)
        paintHourHand.color = colorHourHand
        hourHandSize = typedArray.getDimension(R.styleable.AnalogClock_sizeHourHand, 250f)

        typedArray.recycle()
    }


    private fun drawCircle(canvas: Canvas) {
        paintCircle!!.apply {
            reset()
            color = resources.getColor(R.color.black)
            strokeWidth = 25f
            style = Paint.Style.STROKE
            isAntiAlias = true

        }

        canvas.drawCircle(
            (width!! / 2).toFloat(),
            (height!! / 2).toFloat(),
            (radius).toFloat(),
            paintCircle!!
        )


    }

    private fun drawCenter(canvas: Canvas) {
        paintCircle!!.style = Paint.Style.FILL
        canvas.drawCircle((width!! / 2).toFloat(), (height!! / 2).toFloat(), 12f, paintCircle!!)
    }

    private fun drawMarks(canvas: Canvas) {
        val arr = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val rect = Rect()
        arr.forEach {
            val angel = PI / 6 * (it - 3)
            val x1 = (width!! / 2 + cos(angel) * (radius) - rect.width() / 2).toFloat()
            val y1 = (height!! / 2 + sin(angel) * (radius) - rect.height() / 2).toFloat()

            val x2 = (width!! / 2 + cos(angel) * (radius) / 1.15 - rect.width() / 2).toFloat()
            val y2 = (height!! / 2 + sin(angel) * (radius) / 1.15 - rect.height() / 2).toFloat()

            canvas.drawLine(x1, y1, x2, y2, paintCircle!!)
        }
    }

    private fun secondPaint() {
        paintSecondHand.apply {
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

    private fun minutePaint() {
        paintMinuteHand.apply {
            strokeWidth = 15f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

    private fun hourPaint() {
        paintHourHand.apply {
            strokeWidth = 20f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

    private fun drawHands(canvas: Canvas) {
        val c: Calendar = Calendar.getInstance()
        drawHourHand(canvas, ((c.get(Calendar.MINUTE) / 60) * 5).toFloat(), paintHourHand)
        drawMinuteHand(canvas, c.get(Calendar.MINUTE).toFloat(), paintMinuteHand)
        drawSecondHand(canvas, c.get(Calendar.SECOND).toFloat(), paintSecondHand)
    }

    private fun drawHourHand(canvas: Canvas, loc: Float, paint: Paint?) {
        val angel: Double = Math.PI * loc / 30 - Math.PI / 2
        var mainSize: Float = 0f
        var secondSize: Float = 0f
        if (hourHandSize * 0.7 > radius) {
            mainSize = (radius * 0.7).toFloat()
            secondSize = (radius * 0.3).toFloat()
        } else {
            mainSize = (hourHandSize * 0.7).toFloat()
            secondSize = (hourHandSize * 0.3).toFloat()
        }

        canvas.drawLine(
            (width!! / 2).toFloat(),
            (height!! / 2).toFloat(),
            ((width!! / 2 + cos(angel) * mainSize).toFloat()),
            (height!! / 2 + sin(angel) * mainSize).toFloat(),
            paint!!
        )
        canvas.drawLine(
            (width!! / 2).toFloat(),
            (height!! / 2).toFloat(),
            (width!! / 2 + -cos(angel) * secondSize).toFloat(),
            (height!! / 2 + -sin(angel) * secondSize).toFloat(),
            paint
        )
    }

    private fun drawMinuteHand(canvas: Canvas, loc: Float, paint: Paint?) {
        val angel: Double = Math.PI * loc / 30 - Math.PI / 2
        var mainSize: Float = 0f
        var secondSize: Float = 0f
        if (minuteHandSize * 0.7 > radius) {
            mainSize = (radius * 0.7).toFloat()
            secondSize = (radius * 0.3).toFloat()
        } else {
            mainSize = (minuteHandSize * 0.7).toFloat()
            secondSize = (minuteHandSize * 0.3).toFloat()
        }

        canvas.drawLine(
            (width!! / 2).toFloat(),
            (height!! / 2).toFloat(),
            (width!! / 2 + cos(angel) * mainSize).toFloat(),
            (height!! / 2 + sin(angel) * mainSize).toFloat(),
            paint!!
        )
        canvas.drawLine(
            (width!! / 2).toFloat(),
            (height!! / 2).toFloat(),
            (width!! / 2 + -cos(angel) * secondSize).toFloat(),
            (height!! / 2 + -sin(angel) * secondSize).toFloat(),
            paint
        )
    }

    private fun drawSecondHand(canvas: Canvas, loc: Float, paint: Paint?) {
        val angel: Double = Math.PI * loc / 30 - Math.PI / 2
        var mainSize: Float = 0f
        var secondSize: Float = 0f
        if (secondHandSize * 0.7 > radius) {
            mainSize = (radius * 0.7).toFloat()
            secondSize = (radius * 0.3).toFloat()
        } else {
            mainSize = (secondHandSize * 0.7).toFloat()
            secondSize = (secondHandSize * 0.3).toFloat()
        }


        canvas.drawLine(
            (width!! / 2).toFloat(),
            (height!! / 2).toFloat(),
            (width!! / 2 + cos(angel) * mainSize).toFloat(),
            (height!! / 2 + sin(angel) * mainSize).toFloat(),
            paint!!
        )
        canvas.drawLine(
            (width!! / 2).toFloat(),
            (height!! / 2).toFloat(),
            (width!! / 2 + -cos(angel) * secondSize).toFloat(),
            (height!! / 2 + -sin(angel) * secondSize).toFloat(),
            paint
        )
    }

}
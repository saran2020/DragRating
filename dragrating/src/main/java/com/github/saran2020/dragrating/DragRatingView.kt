package com.github.saran2020.dragrating

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.ImageView
import java.util.SortedMap
import kotlin.math.ceil
import kotlin.math.floor

open class DragRatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var mIsDragging = false
    private var mScaledTouchSlop = 0
    private var mTouchDownX = 0f
    private var ratingSpace = 0.0f
    private var maxRating = 5
    private var _currentRating = 0f
        set(value) {
            if (value > maxRating) {
                throw IllegalArgumentException("Rating cannot be more than max rating")
            } else if (value < 0) {
                throw IllegalArgumentException("Rating cannot be less than 0")
            }

            val previousRating = field
            val newRating = roundOffRating(value)

            if (previousRating == newRating) {
                return
            }

            field = newRating
            callback?.onRatingChange(previousRating, field)

            Log.d("buggy_bug", "current rating $_currentRating")
            refreshRatingView()
        }

    var rating: Float
        get() = _currentRating
        set(value) {
            _currentRating = value
        }

    private fun refreshRatingView() {
        for (i in 0..childCount) {
            val childAt = getChildAt(i)
            if (childAt != null) {
                setRatingResource(childAt as ImageView, i)
            }
        }
    }

    var callback: RatingChangeCallback? = null

    // We multiply the decimal value by 100 so that we can convert the
    // decimal to ints It gives us more control on rounding off to the
    // nearest available.
    private fun roundOffRating(value: Float): Float {
        val decimalMultiplied = (value - floor(value)) * 100

        val keys = assetMap.keys.toList()
        var previous = 0
        for (current in 1 until assetMap.size) {
            val currentMultiple = keys[current] * 100
            val previousMultiple = keys[previous] * 100

            if (decimalMultiplied > previousMultiple && decimalMultiplied < currentMultiple) {
                val differPreviousMultiple = decimalMultiplied - previousMultiple
                val differCurrentMultiple = currentMultiple - decimalMultiplied

                val minDifference = Math.min(differCurrentMultiple, differPreviousMultiple)
                return when {
                    differPreviousMultiple == differCurrentMultiple -> floor(value) + keys[current]
                    minDifference == differPreviousMultiple -> floor(value) + keys[previous]
                    else -> floor(value) + keys[current]
                }
            }

            previous = current
        }

        return value
    }

    private var assetMap: SortedMap<Float, Drawable> = convertToDrawableMap(
        mapOf(
            0f to R.drawable.ic_star_empty,
            0.5f to R.drawable.ic_star_half,
            1f to R.drawable.ic_star_full
        )
    )

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.DragRatingView, 0, 0
            )

            ratingSpace = typedArray.getDimension(
                R.styleable.DragRatingView_rating_space, 0f
            )
            maxRating = typedArray.getInt(
                R.styleable.DragRatingView_max_rating, 5
            )
            _currentRating = typedArray.getFloat(
                R.styleable.DragRatingView_initial_rating, 0f
            )

            typedArray.recycle()
        }

        isClickable = true
        isFocusable = true
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        addViews()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isInScrollingContainer()) {
                    mTouchDownX = event.x
                } else {
                    startDrag(event)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (mIsDragging) {
                    trackTouchEvent(event)
                } else {
                    val x = event.x
                    if (Math.abs(x - mTouchDownX) > mScaledTouchSlop) {
                        startDrag(event)
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                if (mIsDragging) {
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                    isPressed = false
                } else {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch()
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (mIsDragging) {
                    onStopTrackingTouch()
                    isPressed = false
                }
            }
        }

        return true
    }

    private fun startDrag(event: MotionEvent) {
        isPressed = true
        onStartTrackingTouch()
        trackTouchEvent(event)
    }

    private fun isInScrollingContainer(): Boolean {
        var p = parent
        while (p != null && p is ViewGroup) {
            if (p.shouldDelayChildPressedState()) {
                return true
            }
            p = p.parent
        }

        return false
    }

    protected fun trackTouchEvent(event: MotionEvent) {
        val x = Math.round(event.x)

        for (i in 0..childCount) {
            val child: View = getChildAt(i) ?: return

            if (x > child.left && x < (child.left + child.width)) {

                val dragOnView = x - child.left
                val ratioCross = dragOnView / child.width.toFloat()

                _currentRating = i + ratioCross
            }
        }
    }

    private fun onStartTrackingTouch() {
        mIsDragging = true
    }

    private fun onStopTrackingTouch() {
        mIsDragging = false
    }

    private fun addViews() {
        for (i in 0 until maxRating) {
            addRatingViews(i)
        }
    }

    private fun addRatingViews(pos: Int) {

        val imageView = getImageView(pos)
        addView(imageView)
    }

    private fun getImageView(pos: Int): ImageView {
        val imageView = ImageView(context)
        val layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        layoutParams.marginEnd = if (pos != (maxRating - 1)) ratingSpace.toInt() else 0

        imageView.layoutParams = layoutParams
        setRatingResource(imageView, pos)

        return imageView
    }

    private fun setRatingResource(imageView: ImageView, index: Int) {
        val pos = index + 1

        imageView.setImageDrawable(
            when {
                pos <= floor(_currentRating) -> assetMap[1f]!!
                pos == ceil(_currentRating).toInt() -> {
                    val decimal = _currentRating - floor(_currentRating)
                    assetMap[decimal]!!
                }
                else -> assetMap[0f]!!
            }
        )
    }

    private fun convertToDrawableMap(map: Map<Float, Int>): SortedMap<Float, Drawable> {

        val sortedMap: SortedMap<Float, Drawable> = sortedMapOf()
        for (entry in map) {
            val drawable = ResourcesCompat.getDrawable(resources, entry.value, null)!!
            sortedMap[entry.key] = drawable
        }

        return sortedMap
    }

    fun setDrawableAssetMap(map: Map<Float, Drawable>) {
        assetMap = map.toSortedMap()

        // Need to redraw since the asset have been updated.
        refreshRatingView()
    }

    fun setDrawableResourceAssetMap(map: Map<Float, Int>) {
        assetMap = convertToDrawableMap(map)

        // Need to redraw since the asset have been updated.
        refreshRatingView()
    }

    interface RatingChangeCallback {
        fun onRatingChange(previous: Float, current: Float)
    }
}
package com.github.saran2020.sliderating

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import java.util.*
import kotlin.math.ceil

class SlideRatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var ratingSpace = 0.0f
    private var maxRating = 5
    private var currentRating = 0f
        set(value) {
            if (value > maxRating) {
                throw IllegalArgumentException("Rating cannot be more than max rating")
            } else if (value < 0) {
                throw IllegalArgumentException("Rating cannot be less than 0")
            }

            field = value
        }

    private var assetMap = sortedMapOf(
        0f to R.drawable.ic_star_empty,
        0.5f to R.drawable.ic_star_half,
        1f to R.drawable.ic_star_full
    )

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.SlideRatingView, 0, 0
            )

            ratingSpace = typedArray.getDimension(
                R.styleable.SlideRatingView_rating_space, 0f
            )
            maxRating = typedArray.getInt(
                R.styleable.SlideRatingView_max_rating, 5
            )
            currentRating = typedArray.getFloat(
                R.styleable.SlideRatingView_initial_rating, 0f
            )

            typedArray.recycle()
        }

        addViews()
    }

    private fun addViews() {
        for (i in 1..maxRating) {
            addRatingViews(i)
        }
    }

    private fun addRatingViews(pos: Int) {

        val imageView = getImageView()
        imageView.setImageResource(
            when {
                pos < floor(currentRating) -> R.drawable.ic_star_full
                pos == floor(currentRating).toInt() -> R.drawable.ic_star_half
                else -> R.drawable.ic_star_empty
            }
        )

        addView(imageView)
    }

    private fun getImageView(): ImageView {
        val imageView = ImageView(context)
        val layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)

        imageView.layoutParams = layoutParams
        return imageView
    }
}
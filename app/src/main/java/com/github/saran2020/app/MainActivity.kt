package com.github.saran2020.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.github.saran2020.dragrating.DragRatingView

class MainActivity : AppCompatActivity() {

    private var ratingView: DragRatingView? = null
    private var ratingTextView: TextView? = null

    private var ratingViewHeart: DragRatingView? = null
    private var ratingTextViewHeart: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ratingView = findViewById(R.id.slide_rating)
        ratingTextView = findViewById(R.id.rating_text)

        ratingView?.callback = object : DragRatingView.RatingChangeCallback {
            override fun onRatingChange(previous: Float, current: Float) {
                ratingTextView?.text = "$current"
            }
        }

        ratingViewHeart = findViewById(R.id.slide_rating_heart)
        ratingTextViewHeart = findViewById(R.id.rating_text_heart)

        ratingViewHeart?.apply {
            setDrawableResourceAssetMap(
                mapOf(
                    0f to R.drawable.ic_empty_filled_heart,
                    0.5f to R.drawable.ic_half_filled_heart,
                    1f to R.drawable.ic_filled_heart
                )
            )

            callback = object : DragRatingView.RatingChangeCallback {
                override fun onRatingChange(previous: Float, current: Float) {
                    ratingTextViewHeart?.text = "$current"
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val rating = ratingView?.rating ?: 0
        Log.d("buggy_bug", "current rating is $rating")
    }

    override fun onResume() {
        super.onResume()
        ratingView?.rating = 4f
    }
}

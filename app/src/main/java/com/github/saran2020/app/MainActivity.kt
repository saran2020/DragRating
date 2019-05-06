package com.github.saran2020.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.github.saran2020.sliderating.SlideRatingView

class MainActivity : AppCompatActivity() {

    private var ratingView: SlideRatingView? = null
    private var ratingTextView: TextView? = null

    private var ratingView5In1: SlideRatingView? = null
    private var ratingTextView5In1: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ratingView = findViewById(R.id.slide_rating)
        ratingTextView = findViewById(R.id.rating_text)

        ratingView?.callback = object : SlideRatingView.RatingChangeCallback {
            override fun onRatingChanged(previous: Float, current: Float) {
                ratingTextView?.text = "$current"
            }
        }

        ratingView5In1 = findViewById(R.id.slide_rating_5_in_1)
        ratingTextView5In1 = findViewById(R.id.rating_text_5_in_1)

        ratingView5In1?.setDrawableResourceAssetMap(
            mapOf(
                0f to R.drawable.ic_hearts_with_0_filled,
                0.2f to R.drawable.ic_hearts_with_1_filled,
                0.4f to R.drawable.ic_hearts_with_2_filled,
                0.6f to R.drawable.ic_hearts_with_3_filled,
                0.8f to R.drawable.ic_hearts_with_4_filled,
                1f to R.drawable.ic_hearts_with_5_filled
            )
        )

        ratingView5In1?.callback = object : SlideRatingView.RatingChangeCallback {
            override fun onRatingChanged(previous: Float, current: Float) {
                ratingTextView5In1?.text = "$current"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val rating = ratingView?.getRating() ?: 0
        Log.d("buggy_bug", "current rating is $rating")
    }

    override fun onResume() {
        super.onResume()
        ratingView?.setRating(4f)
    }
}

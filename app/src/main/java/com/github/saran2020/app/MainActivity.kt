package com.github.saran2020.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.github.saran2020.sliderating.SlideRatingView

class MainActivity : AppCompatActivity() {

    private var ratingView: SlideRatingView? = null
    private var ratingTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ratingView = findViewById(R.id.slide_rating)
        ratingTextView = findViewById(R.id.rating_text)

        ratingView?.callback = object : SlideRatingView.RatingChangeCallback {
            override fun onRatingChanged(previous: Float, new: Float) {
                ratingTextView?.text = "$new"
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

package com.github.saran2020.sliderating

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ratingView = findViewById<SlideRatingView>(R.id.slide_rating)
        val ratingTextView = findViewById<TextView>(R.id.rating_text)

        ratingView.callback = object : SlideRatingView.RatingChangeCallback {
            override fun onRatingChanged(previous: Float, new: Float) {
                ratingTextView.text = "$new"
            }
        }
    }
}

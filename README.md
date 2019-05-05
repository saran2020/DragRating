###### SlideRating
---

Slide rating is a rating view inspired of Slide Review from Book My Show app. The purpose of this library is to create simple and easy to use Slide Rating which will allow you to supply custom assets.

To include this library you will have to add
```Gradle
dependencies {
    implementation 'com.github.saran2020:SlideRating:0.2'
}
```

**layout.xml**
```xml
<com.github.saran2020.sliderating.SlideRatingView
    android:id="@+id/slide_rating"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:initial_rating="2.5"
    app:rating_space="8dp"
    app:max_rating="5" />
```
To get the current rating use the method `getRating()` and to set the rating use method `setRating(3.5f)`.


If you want to provide custom asset for the view, you need to set a `Map` with the multiplier for the asset and the asset like shown below.

For passing asset resource id in kotlin
```kotlin
ratingView.setDrawableResourceAssetMap(
    mapOf(
        0f to R.drawable.ic_star_empty,
        0.5f to R.drawable.ic_star_half,
        1f to R.drawable.ic_star_full
    )
)
```

For passing asset as `Drawable` in kotlin
```kotlin
ratingView.setDrawableAssetMap(
    mapOf(
        0f to emptyRating,
        0.5f to halfRating,
        1f to fullRating
    )
)
```
**NOTE: You should only provide the multiplier between 0 and 1. If the current rating is 1.5 it will automatically fill one rating with the asset mapped with `1f` and one rating with asset mapped with `0.5f`**

To get a callback when user is sliding the rating 
```kotlin
ratingView.callback = object : SlideRatingView.RatingChangeCallback {
    override fun onRatingChanged(previous: Float, new: Float) {
        Log.d(TAG, "previous rating = $previous new rating = $current")
    }
}
```
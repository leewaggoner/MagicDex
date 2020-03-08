package com.wreckingball.magicdex.ui.dashboard

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.wreckingball.magicdex.R
import com.wreckingball.magicdex.models.Card
import com.wreckingball.magicdex.utils.MagicUtil
import java.util.*

class DashboardViewModel : ViewModel() {
    lateinit var card: Card

    fun getManaImage(context: Context, mana: String) : ImageView {
        val imageSize = MagicUtil().dpToPixels(context, 16)
        var cleanMana = mana.replace("{", "")
        cleanMana = cleanMana.replace("/", "")
        val imageView = ImageView(context)
        if (cleanMana.isNotEmpty()) {
            imageView.layoutParams = LinearLayout.LayoutParams(imageSize, imageSize)
            imageView.setImageResource(mapManaToResource(context!!, cleanMana.toLowerCase(Locale.US)))
        }
        return imageView
    }

    fun addImagesToText(context: Context, text: String?) : SpannableStringBuilder {
        val ssb = SpannableStringBuilder(text)
        data class ImageMap(val start: Int, val end: Int, val imageId: Int)
        text?.let {
            val imageMap: MutableList<ImageMap> = mutableListOf()
            val imageSize = MagicUtil().dpToPixels(context, 16)
            var start = it.indexOf("{")
            do {
                if (start != -1) {
                    val end = it.indexOf("}", start) + 1
                    var imageText = it.subSequence(start, end).toString()
                    imageText = imageText.replace("{", "")
                    imageText = imageText.replace("/", "")
                    imageText = imageText.replace("}", "")
                    val id = mapManaToResource(context, imageText.toLowerCase(Locale.US))
                    imageMap.add(ImageMap(start, end, id))
                    start = it.indexOf("{", end - 1)
                }
            } while (start != -1)
            for (image in imageMap) {
                val drawable = ContextCompat.getDrawable(context, image.imageId)
                drawable?.let {
                    drawable.setBounds(0, 0, imageSize, imageSize)
                    val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                    ssb.setSpan(imageSpan, image.start, image.end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
        }
        return ssb
    }

    fun mapManaToResource(context: Context, mana: String) : Int {
        var value = context.resources.getIdentifier("mana_$mana", "drawable", context.packageName)
        if (value == 0) {
            value = R.drawable.mana_unexpected
        }
        return value
    }

    fun buildLayouts(context: Context, firstText: String, secondText: String) : LinearLayout {
        //set up horizontal layout
        val magicUtil = MagicUtil()
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = magicUtil.dpToPixels(context,8)
        layout.layoutParams = layoutParams
        layout.isFocusable = true

        //set up first TextView
        val dateText = TextView(context)
        val dateParams = TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 2.0f)
        dateText.layoutParams = dateParams
        dateText.text = firstText
        dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16.0f)


        //set up second text box
        val ruleText = TextView(context)
        val ruleParams = TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1.0f)
        ruleText.layoutParams = ruleParams
        ruleText.text = secondText
        ruleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16.0f)

        layout.addView(dateText)
        layout.addView(ruleText)

        return layout
    }
}
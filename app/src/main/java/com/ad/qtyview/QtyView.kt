package com.ad.qtyview

import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import android.graphics.drawable.Drawable
import com.ad.qtyview.QtyView.OnQtyChangeListener
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import com.ad.qtyview.R
import android.graphics.drawable.GradientDrawable
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.LinearLayout
import android.util.TypedValue
import android.os.Build
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.ImageView

class QtyView : FrameLayout, View.OnClickListener {
    private var btnPlus: ImageView? = null
    private var btnMinus: ImageView? = null
    private var txtQty: TextView? = null
    private var btnAdd: MaterialButton? = null
    private var addBtnText: String? = null
    private var addBtnIcon: Drawable? = null
    private var btnPlusIcon: Drawable? = null
    private var btnMinusIcon: Drawable? = null
    private var addBtnTextColor = 0
    private var addBtnStrokeColor = 0
    private var txtQtyTextColor = 0
    private var quantity = 0
    private var maxQuantity = Int.MAX_VALUE
    private var minQuantity = Int.MAX_VALUE

    interface OnQtyChangeListener {
        fun onQuantityChanged(oldQuantity: Int, newQuantity: Int, programmatically: Boolean)
        fun onLimitReached()
    }

    //getter and setter
    var onQtyChangeListener: OnQtyChangeListener? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet
    ) {
        init(attributeSet, defStyle)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.QtyView, defStyle, 0)
        addBtnText = resources.getString(R.string.qty_add)
        if (a.hasValue(R.styleable.QtyView_qty_add_button_text)) {
            addBtnText = a.getString(R.styleable.QtyView_qty_add_button_text)
        }
        addBtnStrokeColor =
            a.getColor(R.styleable.QtyView_qty_add_button_text_stroke_color, Color.BLACK)
        addBtnTextColor = a.getColor(R.styleable.QtyView_qty_add_button_text_color, Color.BLACK)
        addBtnIcon =
            if (a.hasValue(R.styleable.QtyView_qty_add_button_icon)) a.getDrawable(R.styleable.QtyView_qty_add_button_icon) else resources.getDrawable(
                R.drawable.ic_plus
            )
        btnPlusIcon =
            if (a.hasValue(R.styleable.QtyView_qty_plus_button_icon)) a.getDrawable(R.styleable.QtyView_qty_plus_button_icon) else resources.getDrawable(
                R.drawable.ic_plus
            )
        btnMinusIcon =
            if (a.hasValue(R.styleable.QtyView_qty_minus_button_icon)) a.getDrawable(R.styleable.QtyView_qty_minus_button_icon) else resources.getDrawable(
                R.drawable.ic_minus
            )
        txtQtyTextColor = a.getColor(R.styleable.QtyView_qty_quantity_text_color, Color.BLACK)
        quantity = a.getInt(R.styleable.QtyView_qty_quantity, 0)
        maxQuantity = a.getInt(R.styleable.QtyView_qty_max_quantity, Int.MAX_VALUE)
        minQuantity = a.getInt(R.styleable.QtyView_qty_min_quantity, 0)
        a.recycle()

        //get dp values
        val dp10 = pxFromDp(10f)
        val dp12 = pxFromDp(12f)
        val dp1 = pxFromDp(1f)

        //shape
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(Color.WHITE)
        shape.cornerRadius = dp12.toFloat()
        shape.setStroke(dp1, ColorStateList.valueOf(addBtnStrokeColor))
        this.background = shape
        val linearLayout = LinearLayout(context)
        btnPlus = ImageView(context)
        btnPlus!!.setPadding(dp10, dp10, dp10, dp10)
        btnPlus!!.minimumHeight = 0
        btnPlus!!.minimumWidth = 0
        btnPlus!!.isClickable = true
        btnPlus!!.isFocusable = true
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnPlus!!.foreground = ContextCompat.getDrawable(context, outValue.resourceId)
        }
        btnPlus!!.setImageDrawable(btnPlusIcon)
        btnMinus = ImageView(context)
        btnMinus!!.setPadding(dp10, dp10, dp10, dp10)
        btnMinus!!.minimumHeight = 0
        btnMinus!!.minimumWidth = 0
        btnMinus!!.isClickable = true
        btnMinus!!.isFocusable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnMinus!!.foreground = ContextCompat.getDrawable(context, outValue.resourceId)
        }
        btnMinus!!.setImageDrawable(btnMinusIcon)
        txtQty = TextView(context)
        txtQty!!.gravity = Gravity.CENTER
        txtQty!!.text = "1"
        txtQty!!.textSize = 18f
        txtQty!!.setTextColor(txtQtyTextColor)
        linearLayout.gravity = Gravity.CENTER
        linearLayout.orientation = LinearLayout.HORIZONTAL
        btnAdd = MaterialButton(context, null, R.attr.borderlessButtonStyle)
        btnAdd!!.text = addBtnText
        btnAdd!!.isAllCaps = false
        btnAdd!!.setTextColor(addBtnTextColor)
        btnAdd!!.icon = addBtnIcon
        btnAdd!!.iconGravity = MaterialButton.ICON_GRAVITY_END
        btnAdd!!.insetBottom = 0
        btnAdd!!.insetTop = 0
        btnMinus!!.visibility = INVISIBLE
        txtQty!!.visibility = INVISIBLE
        btnPlus!!.visibility = INVISIBLE
        val param = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            1.0f
        )
        linearLayout.addView(btnMinus, param)
        linearLayout.addView(txtQty, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        linearLayout.addView(btnPlus, param)
        addView(linearLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(btnAdd, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        btnAdd!!.setOnClickListener(this)
        btnMinus!!.setOnClickListener(this)
        btnPlus!!.setOnClickListener(this)
    }

    private fun pxFromDp(dp: Float): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun dpFromPx(px: Float): Int {
        return (px / resources.displayMetrics.density).toInt()
    }

    override fun onClick(view: View) {
        if (view === btnPlus) {
            if (quantity + 1 > maxQuantity) {
                if (onQtyChangeListener != null) onQtyChangeListener!!.onLimitReached()
            } else {
                val oldQty = quantity
                quantity += 1
                txtQty!!.text = quantity.toString()
                if (onQtyChangeListener != null) onQtyChangeListener!!.onQuantityChanged(
                    oldQty,
                    quantity,
                    false
                )
            }
        } else if (view === btnMinus) {
            if (quantity - 1 < minQuantity) { // 1-1 =0 < 0
                if (onQtyChangeListener != null) onQtyChangeListener!!.onLimitReached()
                invisibleControls()
            } else {
                val oldQty = quantity
                quantity -= 1
                txtQty!!.text = quantity.toString()
                if (onQtyChangeListener != null) onQtyChangeListener!!.onQuantityChanged(
                    oldQty,
                    quantity,
                    false
                )
                if (quantity == minQuantity) {
                    invisibleControls()
                }
            }
        } else if (view === btnAdd) {
            val oldQty = quantity
            quantity = 1
            visibleControls()
            txtQty!!.text = quantity.toString()
            if (onQtyChangeListener != null) onQtyChangeListener!!.onQuantityChanged(
                oldQty,
                quantity,
                false
            )
        }
    }

    private fun visibleControls() {
        btnPlus!!.visibility = VISIBLE
        btnMinus!!.visibility = VISIBLE
        txtQty!!.visibility = VISIBLE
        btnAdd!!.visibility = INVISIBLE
    }

    private fun invisibleControls() {
        btnPlus!!.visibility = INVISIBLE
        btnMinus!!.visibility = INVISIBLE
        txtQty!!.visibility = INVISIBLE
        btnAdd!!.visibility = VISIBLE
    }

    var qty: Int
        get() = quantity
        set(newQuantity) {
            var newQuantity = newQuantity
            var limitReached = false
            if (newQuantity > maxQuantity) {
                newQuantity = maxQuantity
                limitReached = true
            }
            if (newQuantity < minQuantity) {
                newQuantity = minQuantity
                limitReached = true
            }
            if (!limitReached) {
                visibleControls()
                quantity = newQuantity
                txtQty!!.text = quantity.toString()
            } else {
                invisibleControls()
                if (onQtyChangeListener != null) onQtyChangeListener!!.onLimitReached()
            }
        }
}
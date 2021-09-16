package com.ad.qtyview;

import static androidx.core.content.ContextCompat.getDrawable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;


public class QtyView extends FrameLayout implements View.OnClickListener {
    private ImageView btnPlus, btnMinus;
    private TextView txtQty;
    private MaterialButton btnAdd;

    private String addBtnText;
    private Drawable addBtnIcon, btnPlusIcon, btnMinusIcon;
    private int addBtnTextColor, addBtnStrokeColor, txtQtyTextColor;
    private int quantity;
    private int maxQuantity = Integer.MAX_VALUE, minQuantity = Integer.MAX_VALUE;


    public interface OnQtyChangeListener {
        void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically);

        void onLimitReached();
    }

    private OnQtyChangeListener onQtyChangeListener;


    public QtyView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public QtyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }


    public QtyView(@NonNull Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet);
        init(attributeSet, defStyle);
    }

    @SuppressLint({"UseCompatLoadingForDrawables"})
    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QtyView, defStyle, 0);

        addBtnText = getResources().getString(R.string.qty_add);
        if (a.hasValue(R.styleable.QtyView_qty_add_button_text)) {
            addBtnText = a.getString(R.styleable.QtyView_qty_add_button_text);
        }
        addBtnStrokeColor = a.getColor(R.styleable.QtyView_qty_add_button_text_stroke_color, Color.BLACK);
        addBtnTextColor = a.getColor(R.styleable.QtyView_qty_add_button_text_color, Color.BLACK);
        addBtnIcon = a.hasValue(R.styleable.QtyView_qty_add_button_icon) ? a.getDrawable(R.styleable.QtyView_qty_add_button_icon) : getResources().getDrawable(R.drawable.ic_plus);

        btnPlusIcon = a.hasValue(R.styleable.QtyView_qty_plus_button_icon) ? a.getDrawable(R.styleable.QtyView_qty_plus_button_icon) : getResources().getDrawable(R.drawable.ic_plus);


        btnMinusIcon = a.hasValue(R.styleable.QtyView_qty_minus_button_icon) ? a.getDrawable(R.styleable.QtyView_qty_minus_button_icon) : getResources().getDrawable(R.drawable.ic_minus);


        txtQtyTextColor = a.getColor(R.styleable.QtyView_qty_quantity_text_color, Color.BLACK);

        quantity = a.getInt(R.styleable.QtyView_qty_quantity, 0);
        maxQuantity = a.getInt(R.styleable.QtyView_qty_max_quantity, Integer.MAX_VALUE);
        minQuantity = a.getInt(R.styleable.QtyView_qty_min_quantity, 0);


        a.recycle();

        //get dp values
        int dp10 = pxFromDp(10);
        int dp12 = pxFromDp(12);
        int dp1 = pxFromDp(1);

        //shape
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.WHITE);
        shape.setCornerRadius(dp12);
        shape.setStroke(dp1, ColorStateList.valueOf(addBtnStrokeColor));
        this.setBackground(shape);

        LinearLayout linearLayout = new LinearLayout(getContext());


        btnPlus = new ImageView(getContext());
        btnPlus.setPadding(dp10, dp10, dp10, dp10);
        btnPlus.setMinimumHeight(0);
        btnPlus.setMinimumWidth(0);
        btnPlus.setClickable(true);
        btnPlus.setFocusable(true);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnPlus.setForeground(getDrawable(getContext(), outValue.resourceId));
        }
        btnPlus.setImageDrawable(btnPlusIcon);

        btnMinus = new ImageView(getContext());
        btnMinus.setPadding(dp10, dp10, dp10, dp10);
        btnMinus.setMinimumHeight(0);
        btnMinus.setMinimumWidth(0);
        btnMinus.setClickable(true);
        btnMinus.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnMinus.setForeground(getDrawable(getContext(), outValue.resourceId));
        }
        btnMinus.setImageDrawable(btnMinusIcon);

        txtQty = new TextView(getContext());
        txtQty.setGravity(Gravity.CENTER);
        txtQty.setText("1");
        txtQty.setTextSize(18);
        txtQty.setTextColor(txtQtyTextColor);

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        btnAdd = new MaterialButton(getContext(), null, R.attr.borderlessButtonStyle);
        btnAdd.setText(addBtnText);
        btnAdd.setAllCaps(false);
        btnAdd.setTextColor(addBtnTextColor);
        btnAdd.setIcon(addBtnIcon);
        btnAdd.setIconGravity(MaterialButton.ICON_GRAVITY_END);
        btnAdd.setInsetBottom(0);
        btnAdd.setInsetTop(0);

        btnMinus.setVisibility(INVISIBLE);
        txtQty.setVisibility(INVISIBLE);
        btnPlus.setVisibility(INVISIBLE);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                1.0f
        );

        linearLayout.addView(btnMinus, param);
        linearLayout.addView(txtQty, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        linearLayout.addView(btnPlus, param);


        addView(linearLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(btnAdd, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        btnAdd.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
    }


    private int pxFromDp(final float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private int dpFromPx(final float px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View view) {
        if (view == btnPlus) {
            if (quantity + 1 > maxQuantity) {
                if (onQtyChangeListener != null) onQtyChangeListener.onLimitReached();
            } else {
                int oldQty = quantity;
                quantity += 1;
                txtQty.setText(String.valueOf(quantity));

                if (onQtyChangeListener != null)
                    onQtyChangeListener.onQuantityChanged(oldQty, quantity, false);
            }
        } else if (view == btnMinus) {

            if (quantity - 1 < minQuantity) { // 1-1 =0 < 0
                if (onQtyChangeListener != null) onQtyChangeListener.onLimitReached();
                invisibleControls();
            } else {
                int oldQty = quantity;
                quantity -= 1;
                txtQty.setText(String.valueOf(quantity));

                if (onQtyChangeListener != null)
                    onQtyChangeListener.onQuantityChanged(oldQty, quantity, false);
                if (quantity == minQuantity) {
                    invisibleControls();
                }
            }
        } else if (view == btnAdd) {
            int oldQty = quantity;
            quantity = 1;
            visibleControls();
            txtQty.setText(String.valueOf(quantity));

            if (onQtyChangeListener != null)
                onQtyChangeListener.onQuantityChanged(oldQty, quantity, false);

        }
    }

    private void visibleControls() {
        btnPlus.setVisibility(VISIBLE);
        btnMinus.setVisibility(VISIBLE);
        txtQty.setVisibility(VISIBLE);
        btnAdd.setVisibility(View.INVISIBLE);
    }

    private void invisibleControls() {
        btnPlus.setVisibility(INVISIBLE);
        btnMinus.setVisibility(INVISIBLE);
        txtQty.setVisibility(INVISIBLE);
        btnAdd.setVisibility(View.VISIBLE);
    }

    //getter and setter
    public OnQtyChangeListener getOnQtyChangeListener() {
        return onQtyChangeListener;
    }

    public void setOnQtyChangeListener(OnQtyChangeListener onQtyChangeListener) {
        this.onQtyChangeListener = onQtyChangeListener;
    }

    public int getQty() {
        return quantity;
    }

    public void setQty(int newQuantity) {
        boolean limitReached = false;

        if (newQuantity > maxQuantity) {
            newQuantity = maxQuantity;
            limitReached = true;
        }
        if (newQuantity < minQuantity) {
            newQuantity = minQuantity;
            limitReached = true;
        }
        if (!limitReached) {
            visibleControls();
            this.quantity = newQuantity;
            txtQty.setText(String.valueOf(this.quantity));
        } else {
            invisibleControls();
            if (onQtyChangeListener != null) onQtyChangeListener.onLimitReached();
        }
    }
}

/*
 * Copyright (C) 2016 The CyanogenMod Project
 *               2017-2022 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.derp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.android.settings.R;

/**
 * Custom SeekBar that allows setting both a minimum and maximum value.
 * This also handles floating point values (to 2 decimal places) through
 * integer conversions.
 */
public class IntervalSeekBar extends SeekBar {
    private float mMin;
    private float mMax;
    private final float mDefault;
    private final float mMultiplier;

    public IntervalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray seekBarType = context.obtainStyledAttributes(attrs,
                R.styleable.IntervalSeekBar, 0, 0);

        mMax = seekBarType.getFloat(R.styleable.IntervalSeekBar_maxValue, 1.5f);
        mMin = seekBarType.getFloat(R.styleable.IntervalSeekBar_minValue, 0.5f);
        mDefault = seekBarType.getFloat(R.styleable.IntervalSeekBar_defaultValue, 1.0f);

        int digits = seekBarType.getInt(R.styleable.IntervalSeekBar_digits, 0);
        mMultiplier = (float) Math.pow(10, digits);

        if (mMin > mMax) {
            float temp = mMax;
            mMax = mMin;
            mMin = temp;
        }

        setMax(convertFloatToProgress(mMax));
        setProgressFloat(mDefault);

        seekBarType.recycle();
    }

    /*
     * Converts from SeekBar units (which the SeekBar uses), to scale units
     *  (which are saved).
     *  This operation is the inverse of setFontScaling.
     */
    public float getProgressFloat() {
        return (getProgress() / mMultiplier) + mMin;
    }

    /*
     * Converts from scale units (which are saved), to SeekBar units
     * (which the SeekBar uses). This also sets the SeekBar progress.
     * This operation is the inverse of getProgressFloat.
     */
    public void setProgressFloat(float progress) {
        setProgress(convertFloatToProgress(progress));
    }

    private int convertFloatToProgress(float value) {
        return Math.round((value - mMin) * mMultiplier);
    }

    public float getMinimum() {
        return mMin;
    }

    public float getMaximum() {
        return mMax;
    }

    public float getDefault() {
        return mDefault;
    }

    public void setMaximum(float max) {
        mMax = max;
        setMax(convertFloatToProgress(mMax));
    }

    public void setMinimum(float min) {
        mMin = min;
    }
}

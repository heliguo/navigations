package com.library.rnbanner.indicator;

import android.view.View;
import com.library.rnbanner.listener.*;
import com.library.rnbanner.config.*;

import androidx.annotation.NonNull;


public interface Indicator extends OnPageChangeListener {
    @NonNull
    View getIndicatorView();

    IndicatorConfig getIndicatorConfig();

    void onPageChanged(int count, int currentPosition);

}

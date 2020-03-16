/*
 * Copyright (c) 2020 Damiano Giusti
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

package com.damianogiusti.navigation.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.transition.TransitionManager;

import java.lang.ref.WeakReference;

/**
 * The OnDestinationChangedListener specifically for keeping a Toolbar updated.
 * This handles both updating the title and updating the Up Indicator, transitioning between
 * the drawer icon and up arrow as needed.
 * <p>
 * Created by Damiano Giusti on 16/03/2020.
 */
public class ToolbarOnDestinationChangedListener extends AbstractAppBarOnDestinationChangedListener {

    private final WeakReference<Toolbar> mToolbarWeakReference;
    private final boolean mAnimationsEnabled;

    public ToolbarOnDestinationChangedListener(
            @NonNull Toolbar toolbar,
            @NonNull AppBarConfiguration configuration,
            boolean animationsEnabled
    ) {
        super(toolbar.getContext(), configuration);
        mToolbarWeakReference = new WeakReference<>(toolbar);
        mAnimationsEnabled = animationsEnabled;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
        Toolbar toolbar = mToolbarWeakReference.get();
        if (toolbar == null) {
            controller.removeOnDestinationChangedListener(this);
            return;
        }
        super.onDestinationChanged(controller, destination, arguments);
    }

    @Override
    protected void setTitle(CharSequence title) {
        mToolbarWeakReference.get().setTitle(title);
    }

    @Override
    protected void setNavigationIcon(Drawable icon,
                                     @StringRes int contentDescription) {
        Toolbar toolbar = mToolbarWeakReference.get();
        if (toolbar != null) {
            toolbar.setNavigationIcon(icon);
            toolbar.setNavigationContentDescription(contentDescription);
            boolean useTransition = mAnimationsEnabled && icon == null && toolbar.getNavigationIcon() != null;
            if (useTransition) {
                TransitionManager.beginDelayedTransition(toolbar);
            }
        }
    }
}

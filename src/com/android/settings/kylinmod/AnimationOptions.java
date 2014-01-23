/*
 * Copyright (C) 2014 The KylinMod OpenSource project
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

package com.android.settings.kylinmod;

import static android.provider.Settings.System.SCREEN_OFF_ANIMATION;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class AnimationOptions extends SettingsPreferenceFragment  implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "AnimationOptions";

    // ListView Animations Key
    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";

    // Screen Off Animation Key
    private static final String KEY_SCREEN_OFF_ANIMATION = "screen_off_animation";

    // ListView Animations Preference
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;

    // Screen Off Animation
    private ListPreference mScreenOffAnimationPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();

        addPreferencesFromResource(R.xml.animation_options);
        PreferenceScreen prefScreen = getPreferenceScreen();

        // ListView Animations
        mListViewAnimation = (ListPreference) findPreference(KEY_LISTVIEW_ANIMATION);
        int listviewanimation = Settings.System.getInt(getContentResolver(),
                Settings.System.LISTVIEW_ANIMATION, 0);
        mListViewAnimation.setValue(String.valueOf(listviewanimation));
        mListViewAnimation.setSummary(mListViewAnimation.getEntry());
        mListViewAnimation.setOnPreferenceChangeListener(this);

        mListViewInterpolator = (ListPreference) findPreference(KEY_LISTVIEW_INTERPOLATOR);
        int listviewinterpolator = Settings.System.getInt(getContentResolver(),
                Settings.System.LISTVIEW_INTERPOLATOR, 0);
        mListViewInterpolator.setValue(String.valueOf(listviewinterpolator));
        mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
        mListViewInterpolator.setOnPreferenceChangeListener(this);

        // Screen Off Animation
        mScreenOffAnimationPreference = (ListPreference) findPreference(KEY_SCREEN_OFF_ANIMATION);
        final int currentAnimation = Settings.System.getInt(resolver, SCREEN_OFF_ANIMATION,
                1 /* CRT-off */);
        mScreenOffAnimationPreference.setValue(String.valueOf(currentAnimation));
        mScreenOffAnimationPreference.setOnPreferenceChangeListener(this);
        updateScreenOffAnimationPreferenceDescription(currentAnimation);
    }

    private void updateScreenOffAnimationPreferenceDescription(int currentAnim) {
        ListPreference preference = mScreenOffAnimationPreference;
        String summary;
        if (currentAnim < 0) {
            // Unsupported value
            summary = "";
        } else {
            final CharSequence[] entries = preference.getEntries();
            final CharSequence[] values = preference.getEntryValues();
            if (entries == null || entries.length == 0) {
                summary = "";
            } else {
                summary = entries[currentAnim].toString();
            }
        }
        preference.setSummary(summary);
   }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getContentResolver();
        final String key = preference.getKey();
        if (preference == mListViewAnimation) {
            int listviewanimation = Integer.valueOf((String) objValue);
            int index = mListViewAnimation.findIndexOfValue((String) objValue);
            Settings.System.putInt(resolver, Settings.System.LISTVIEW_ANIMATION,
                    listviewanimation);
            mListViewAnimation.setSummary(mListViewAnimation.getEntries()[index]);
            return true;
        } else if (preference == mListViewInterpolator) {
            int listviewinterpolator = Integer.valueOf((String) objValue);
            int index = mListViewInterpolator.findIndexOfValue((String) objValue);
            Settings.System.putInt(resolver, Settings.System.LISTVIEW_INTERPOLATOR,
                    listviewinterpolator);
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntries()[index]);
            return true; 
        } else if (KEY_SCREEN_OFF_ANIMATION.equals(key)) {
            int value = Integer.parseInt((String) objValue);
            try {
                Settings.System.putInt(getContentResolver(), SCREEN_OFF_ANIMATION, value);
                updateScreenOffAnimationPreferenceDescription(value);
            } catch (NumberFormatException e) {
                Log.e(TAG, "could not persist screen-off animation setting", e);
            }
            return true; 
        }

        return false;
    }
}

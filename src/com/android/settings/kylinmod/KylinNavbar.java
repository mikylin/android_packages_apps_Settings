/*
* Copyright (C) 2014 The KylinMod Open Source Project
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

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class KylinNavbar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "KylinNavbar";
	
    private static final String KEY_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";

    private ListPreference mNavButtonsHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.navbar_settings);
        PreferenceScreen prefScreen = getPreferenceScreen();

		//Navigation Bar Height
        mNavButtonsHeight = (ListPreference) findPreference(KEY_NAVIGATION_BAR_HEIGHT);
        mNavButtonsHeight.setOnPreferenceChangeListener(this);

        int statusNavButtonsHeight = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NAVIGATION_BAR_HEIGHT, 48);
        mNavButtonsHeight.setValue(String.valueOf(statusNavButtonsHeight));
        mNavButtonsHeight.setSummary(mNavButtonsHeight.getEntry());

    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getApplicationContext().getContentResolver();
        if (preference == mNavButtonsHeight) {
            int index = mNavButtonsHeight.findIndexOfValue((String) objValue);
            Settings.System.putInt(resolver, Settings.System.NAVIGATION_BAR_HEIGHT, 
                    Integer.valueOf((String) objValue));
            mNavButtonsHeight.setSummary(mNavButtonsHeight.getEntries()[index]);
            return true;
        }
		
        return false;
    }
}

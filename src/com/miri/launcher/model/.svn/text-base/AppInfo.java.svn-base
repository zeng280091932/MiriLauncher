/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.miri.launcher.model;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Represents a launchable application. An application is made of a name (or
 * title), an intent and an icon.
 */
public class AppInfo {

	/**
	 * The application name.
	 */
	public CharSequence title;

	/**
	 * The intent used to start the application.
	 */
	public Intent intent;

	/**
	 * The application icon.
	 */
	public Drawable icon;

	/**
	 * When set to true, indicates that the icon has been resized.
	 */
	public boolean filtered;
	
	/**
	 * Creates the application intent based on a component name and various
	 * launch flags.
	 * 
	 * @param className
	 *            the class name of the component representing the intent
	 * @param launchFlags
	 *            the launch flags
	 */
	public final void setActivity(ComponentName className, int launchFlags) {
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(className);
		intent.setFlags(launchFlags);
	}

	@Override
	public String toString() {
		return "AppInfo [title=" + title + ", intent=" + intent + ", icon="
				+ icon + ", filtered=" + filtered + "]";
	}

}

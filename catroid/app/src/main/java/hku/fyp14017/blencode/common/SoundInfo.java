/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
 * (<http://developer.fyp14017.hku/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.fyp14017.hku/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.hku/licenses/>.
 */
package hku.fyp14017.blencode.common;

import android.util.Log;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.io.StorageHandler;
import hku.fyp14017.blencode.utils.Utils;

import java.io.IOException;
import java.io.Serializable;

import hku.fyp14017.blencode.ProjectManager;

public class SoundInfo implements Serializable, Comparable<SoundInfo>, Cloneable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = SoundInfo.class.getSimpleName();

	private String name;
	private String fileName;
	public transient boolean isPlaying;

	public SoundInfo() {

	}

	@Override
	public SoundInfo clone() {
		SoundInfo cloneSoundInfo = new SoundInfo();

		cloneSoundInfo.name = this.name;
		cloneSoundInfo.fileName = this.fileName;

		return cloneSoundInfo;
	}

	public SoundInfo copySoundInfoForSprite(Sprite sprite) {
		SoundInfo cloneSoundInfo = new SoundInfo();

		cloneSoundInfo.name = this.name;

		try {
			cloneSoundInfo.fileName = StorageHandler
					.getInstance()
					.copySoundFile(
							Utils.buildPath(
									Utils.buildProjectPath(ProjectManager.getInstance().getCurrentProject().getName()),
									Constants.SOUND_DIRECTORY, fileName)).getName();
		} catch (IOException ioException) {
			Log.e(TAG, Log.getStackTraceString(ioException));
		}

		return cloneSoundInfo;
	}

	public String getAbsolutePath() {
		if (fileName != null) {
			return Utils.buildPath(getPathToSoundDirectory(), fileName);
		} else {
			return null;
		}
	}

	public String getAbsolutePathBackPack() {
		if (fileName != null) {
			return Utils.buildPath(getPathToBackPackDirectory(), fileName);
		} else {
			return null;
		}
	}

	public String getAbsolutePathBackPackSound() {
		if (fileName != null) {
			return Utils.buildPath(getPathToBackPackSoundDirectory(), fileName);
		} else {
			return null;
		}
	}

	public String getTitle() {
		return name;
	}

	public void setTitle(String title) {
		this.name = title;
	}

	public void setSoundFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSoundFileName() {
		return fileName;
	}

	public String getChecksum() {
		if (fileName == null) {
			return null;
		}
		return fileName.substring(0, 32);
	}

	private String getPathToSoundDirectory() {
		return Utils.buildPath(Utils.buildProjectPath(ProjectManager.getInstance().getCurrentProject().getName()),
				Constants.SOUND_DIRECTORY);
	}

	private String getPathToBackPackDirectory() {
		Log.d("TAG", "getPathToBackPackDirectory() called!");
		return Utils.buildPath(Utils.buildProjectPath(ProjectManager.getInstance().getCurrentProject().getName()),
				Constants.BACKPACK_DIRECTORY);
	}

	private String getPathToBackPackSoundDirectory() {
		Log.d("TAG", "getPathToBackPackSoundDirectory() called!");
		return Utils.buildPath(Utils.buildProjectPath(Constants.DEFAULT_ROOT + "/" + Constants.BACKPACK_DIRECTORY + "/"
				+ Constants.BACKPACK_SOUND_DIRECTORY));
	}

	@Override
	public int compareTo(SoundInfo soundInfo) {
		return name.compareTo(soundInfo.name);
	}

	@Override
	public String toString() {
		return name;
	}
}

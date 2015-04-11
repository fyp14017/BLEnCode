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
package hku.fyp14017.blencode.io;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.common.FileChecksumContainer;
import hku.fyp14017.blencode.common.LookData;
import hku.fyp14017.blencode.common.SoundInfo;
import hku.fyp14017.blencode.content.BroadcastScript;
import hku.fyp14017.blencode.content.Project;
import hku.fyp14017.blencode.content.Script;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.StartScript;
import hku.fyp14017.blencode.content.WhenScript;
import hku.fyp14017.blencode.content.XmlHeader;
import hku.fyp14017.blencode.content.bricks.BrickBaseType;
import hku.fyp14017.blencode.content.bricks.BroadcastBrick;
import hku.fyp14017.blencode.content.bricks.BroadcastReceiverBrick;
import hku.fyp14017.blencode.content.bricks.BroadcastWaitBrick;
import hku.fyp14017.blencode.content.bricks.ChangeBrightnessByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeGhostEffectByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeSizeByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeVariableBrick;
import hku.fyp14017.blencode.content.bricks.ChangeVolumeByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeXByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeYByNBrick;
import hku.fyp14017.blencode.content.bricks.ClearGraphicEffectBrick;
import hku.fyp14017.blencode.content.bricks.ComeToFrontBrick;
import hku.fyp14017.blencode.content.bricks.DroneFlipBrick;
import hku.fyp14017.blencode.content.bricks.DroneLandBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveBackwardBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveDownBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveForwardBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveLeftBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveRightBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveUpBrick;
import hku.fyp14017.blencode.content.bricks.DronePlayLedAnimationBrick;
import hku.fyp14017.blencode.content.bricks.DroneTakeOffBrick;
import hku.fyp14017.blencode.content.bricks.ForeverBrick;
import hku.fyp14017.blencode.content.bricks.FormulaBrick;
import hku.fyp14017.blencode.content.bricks.GlideToBrick;
import hku.fyp14017.blencode.content.bricks.GoNStepsBackBrick;
import hku.fyp14017.blencode.content.bricks.HideBrick;
import hku.fyp14017.blencode.content.bricks.IfLogicBeginBrick;
import hku.fyp14017.blencode.content.bricks.IfLogicElseBrick;
import hku.fyp14017.blencode.content.bricks.IfLogicEndBrick;
import hku.fyp14017.blencode.content.bricks.IfOnEdgeBounceBrick;
import hku.fyp14017.blencode.content.bricks.LedOffBrick;
import hku.fyp14017.blencode.content.bricks.LedOnBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorActionBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorStopBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorTurnAngleBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtPlayToneBrick;
import hku.fyp14017.blencode.content.bricks.LoopBeginBrick;
import hku.fyp14017.blencode.content.bricks.LoopEndBrick;
import hku.fyp14017.blencode.content.bricks.LoopEndlessBrick;
import hku.fyp14017.blencode.content.bricks.MoveNStepsBrick;
import hku.fyp14017.blencode.content.bricks.NextLookBrick;
import hku.fyp14017.blencode.content.bricks.NoteBrick;
import hku.fyp14017.blencode.content.bricks.PlaceAtBrick;
import hku.fyp14017.blencode.content.bricks.PlaySoundBrick;
import hku.fyp14017.blencode.content.bricks.PointInDirectionBrick;
import hku.fyp14017.blencode.content.bricks.PointToBrick;
import hku.fyp14017.blencode.content.bricks.RepeatBrick;
import hku.fyp14017.blencode.content.bricks.SetBrightnessBrick;
import hku.fyp14017.blencode.content.bricks.SetGhostEffectBrick;
import hku.fyp14017.blencode.content.bricks.SetLookBrick;
import hku.fyp14017.blencode.content.bricks.SetSizeToBrick;
import hku.fyp14017.blencode.content.bricks.SetVariableBrick;
import hku.fyp14017.blencode.content.bricks.SetVolumeToBrick;
import hku.fyp14017.blencode.content.bricks.SetXBrick;
import hku.fyp14017.blencode.content.bricks.SetYBrick;
import hku.fyp14017.blencode.content.bricks.ShowBrick;
import hku.fyp14017.blencode.content.bricks.SpeakBrick;
import hku.fyp14017.blencode.content.bricks.StopAllSoundsBrick;
import hku.fyp14017.blencode.content.bricks.TurnLeftBrick;
import hku.fyp14017.blencode.content.bricks.TurnRightBrick;
import hku.fyp14017.blencode.content.bricks.UserBrick;
import hku.fyp14017.blencode.content.bricks.UserBrickParameter;
import hku.fyp14017.blencode.content.bricks.UserScriptDefinitionBrick;
import hku.fyp14017.blencode.content.bricks.UserScriptDefinitionBrickElement;
import hku.fyp14017.blencode.content.bricks.UserScriptDefinitionBrickElements;
import hku.fyp14017.blencode.content.bricks.VibrationBrick;
import hku.fyp14017.blencode.content.bricks.WaitBrick;
import hku.fyp14017.blencode.content.bricks.WhenBrick;
import hku.fyp14017.blencode.content.bricks.WhenStartedBrick;
import hku.fyp14017.blencode.formulaeditor.UserVariable;
import hku.fyp14017.blencode.formulaeditor.UserVariablesContainer;
import hku.fyp14017.blencode.utils.ImageEditing;
import hku.fyp14017.blencode.utils.UtilFile;
import hku.fyp14017.blencode.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.common.FileChecksumContainer;
import hku.fyp14017.blencode.common.LookData;
import hku.fyp14017.blencode.common.SoundInfo;
import hku.fyp14017.blencode.content.BroadcastScript;
import hku.fyp14017.blencode.content.Project;
import hku.fyp14017.blencode.content.Script;
import hku.fyp14017.blencode.content.StartScript;
import hku.fyp14017.blencode.content.bricks.BrickBaseType;
import hku.fyp14017.blencode.content.bricks.BroadcastBrick;
import hku.fyp14017.blencode.content.bricks.BroadcastReceiverBrick;
import hku.fyp14017.blencode.content.bricks.BroadcastWaitBrick;
import hku.fyp14017.blencode.content.bricks.ChangeBrightnessByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeSizeByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeVariableBrick;
import hku.fyp14017.blencode.content.bricks.ChangeVolumeByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeYByNBrick;
import hku.fyp14017.blencode.content.bricks.ClearGraphicEffectBrick;
import hku.fyp14017.blencode.content.bricks.ComeToFrontBrick;
import hku.fyp14017.blencode.content.bricks.DroneFlipBrick;
import hku.fyp14017.blencode.content.bricks.DroneLandBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveBackwardBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveDownBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveLeftBrick;
import hku.fyp14017.blencode.content.bricks.DronePlayLedAnimationBrick;
import hku.fyp14017.blencode.content.bricks.DroneTakeOffBrick;
import hku.fyp14017.blencode.content.bricks.ForeverBrick;
import hku.fyp14017.blencode.content.bricks.FormulaBrick;
import hku.fyp14017.blencode.content.bricks.GlideToBrick;
import hku.fyp14017.blencode.content.bricks.GoNStepsBackBrick;
import hku.fyp14017.blencode.content.bricks.HideBrick;
import hku.fyp14017.blencode.content.bricks.IfLogicBeginBrick;
import hku.fyp14017.blencode.content.bricks.IfLogicEndBrick;
import hku.fyp14017.blencode.content.bricks.IfOnEdgeBounceBrick;
import hku.fyp14017.blencode.content.bricks.LedOffBrick;
import hku.fyp14017.blencode.content.bricks.LedOnBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorActionBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorTurnAngleBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtPlayToneBrick;
import hku.fyp14017.blencode.content.bricks.LoopBeginBrick;
import hku.fyp14017.blencode.content.bricks.LoopEndBrick;
import hku.fyp14017.blencode.content.bricks.LoopEndlessBrick;
import hku.fyp14017.blencode.content.bricks.MoveNStepsBrick;
import hku.fyp14017.blencode.content.bricks.NextLookBrick;
import hku.fyp14017.blencode.content.bricks.NoteBrick;
import hku.fyp14017.blencode.content.bricks.PlaceAtBrick;
import hku.fyp14017.blencode.content.bricks.PointInDirectionBrick;
import hku.fyp14017.blencode.content.bricks.PointToBrick;
import hku.fyp14017.blencode.content.bricks.RepeatBrick;
import hku.fyp14017.blencode.content.bricks.SetBrightnessBrick;
import hku.fyp14017.blencode.content.bricks.SetGhostEffectBrick;
import hku.fyp14017.blencode.content.bricks.SetLookBrick;
import hku.fyp14017.blencode.content.bricks.SetSizeToBrick;
import hku.fyp14017.blencode.content.bricks.SetVariableBrick;
import hku.fyp14017.blencode.content.bricks.SetVolumeToBrick;
import hku.fyp14017.blencode.content.bricks.SetXBrick;
import hku.fyp14017.blencode.content.bricks.SetYBrick;
import hku.fyp14017.blencode.content.bricks.ShowBrick;
import hku.fyp14017.blencode.content.bricks.SpeakBrick;
import hku.fyp14017.blencode.content.bricks.StopAllSoundsBrick;
import hku.fyp14017.blencode.content.bricks.TurnLeftBrick;
import hku.fyp14017.blencode.content.bricks.TurnRightBrick;
import hku.fyp14017.blencode.content.bricks.UserBrick;
import hku.fyp14017.blencode.content.bricks.UserBrickParameter;
import hku.fyp14017.blencode.content.bricks.UserScriptDefinitionBrickElement;
import hku.fyp14017.blencode.content.bricks.WaitBrick;
import hku.fyp14017.blencode.content.bricks.WhenBrick;
import hku.fyp14017.blencode.content.bricks.WhenStartedBrick;
import hku.fyp14017.blencode.formulaeditor.UserVariable;
import hku.fyp14017.blencode.formulaeditor.UserVariablesContainer;
import hku.fyp14017.blencode.utils.ImageEditing;
import hku.fyp14017.blencode.utils.UtilFile;
import hku.fyp14017.blencode.utils.Utils;

import static hku.fyp14017.blencode.common.Constants.BACKPACK_DIRECTORY;
import static hku.fyp14017.blencode.common.Constants.BACKPACK_IMAGE_DIRECTORY;
import static hku.fyp14017.blencode.common.Constants.BACKPACK_SOUND_DIRECTORY;
import static hku.fyp14017.blencode.common.Constants.DEFAULT_ROOT;
import static hku.fyp14017.blencode.common.Constants.IMAGE_DIRECTORY;
import static hku.fyp14017.blencode.common.Constants.NO_MEDIA_FILE;
import static hku.fyp14017.blencode.common.Constants.PROJECTCODE_NAME;
import static hku.fyp14017.blencode.common.Constants.PROJECTCODE_NAME_TMP;
import static hku.fyp14017.blencode.common.Constants.SOUND_DIRECTORY;
import static hku.fyp14017.blencode.utils.Utils.buildPath;
import static hku.fyp14017.blencode.utils.Utils.buildProjectPath;

public final class StorageHandler {
	private static final StorageHandler INSTANCE;
	private static final String TAG = StorageHandler.class.getSimpleName();
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
	private static final int JPG_COMPRESSION_SETTING = 95;

	private XStreamToSupportCatrobatLanguageVersion092AndBefore xstream;

	private File backPackSoundDirectory;
	private FileInputStream fileInputStream;

	private Lock loadSaveLock = new ReentrantLock();

	// TODO: Since the StorageHandler constructor throws an exception, the member INSTANCE couldn't be assigned
	// directly and therefore we need this static block. Should be refactored and removed in the future.
	static {
		try {
			INSTANCE = new StorageHandler();
		} catch (IOException ioException) {
			throw new RuntimeException("Initialize StorageHandler failed");
		}
	}

	private StorageHandler() throws IOException {
		xstream = new XStreamToSupportCatrobatLanguageVersion092AndBefore(new PureJavaReflectionProvider(new FieldDictionary(new CatroidFieldKeySorter())));
		xstream.processAnnotations(Project.class);
		xstream.processAnnotations(XmlHeader.class);
		xstream.processAnnotations(UserVariablesContainer.class);
		xstream.registerConverter(new XStreamConcurrentFormulaHashMapConverter());
		xstream.registerConverter(new XStreamUserVariableConverter());
		xstream.registerConverter(new XStreamBrickConverter(xstream.getMapper(), xstream.getReflectionProvider()));
		xstream.registerConverter(new XStreamScriptConverter(xstream.getMapper(), xstream.getReflectionProvider()));
		setXstreamAliases();

		if (!Utils.externalStorageAvailable()) {
			throw new IOException("Could not read external storage");
		}
		createCatroidRoot();
	}

	public static StorageHandler getInstance() {
		return INSTANCE;
	}

	public static void saveBitmapToImageFile(File outputFile, Bitmap bitmap) throws FileNotFoundException {
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		try {
			if (outputFile.getName().toLowerCase(Locale.US).endsWith(".jpg")
					|| outputFile.getName().toLowerCase(Locale.US).endsWith(".jpeg")) {
				bitmap.compress(CompressFormat.JPEG, JPG_COMPRESSION_SETTING, outputStream);
			} else {
				bitmap.compress(CompressFormat.PNG, 0, outputStream);
			}
			outputStream.flush();
		} catch (IOException ioException) {
			Log.e(TAG, Log.getStackTraceString(ioException));
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				Log.e(TAG, "Could not close outputstream.", e);
			}
		}
	}

	private void setXstreamAliases() {
		xstream.alias("look", LookData.class);
		xstream.alias("sound", SoundInfo.class);
		xstream.alias("userVariable", UserVariable.class);

		xstream.alias("script", Script.class);
		xstream.alias("object", Sprite.class);

		xstream.alias("script", StartScript.class);
		xstream.alias("script", WhenScript.class);
		xstream.alias("script", BroadcastScript.class);

		xstream.alias("brick", BroadcastBrick.class);
		xstream.alias("brick", BroadcastReceiverBrick.class);
		xstream.alias("brick", BroadcastWaitBrick.class);
		xstream.alias("brick", ChangeBrightnessByNBrick.class);
		xstream.alias("brick", ChangeGhostEffectByNBrick.class);
		xstream.alias("brick", ChangeSizeByNBrick.class);
		xstream.alias("brick", ChangeVariableBrick.class);
		xstream.alias("brick", ChangeVolumeByNBrick.class);
		xstream.alias("brick", ChangeXByNBrick.class);
		xstream.alias("brick", ChangeYByNBrick.class);
		xstream.alias("brick", ClearGraphicEffectBrick.class);
		xstream.alias("brick", ComeToFrontBrick.class);
		xstream.alias("brick", ForeverBrick.class);
		xstream.alias("brick", GlideToBrick.class);
		xstream.alias("brick", GoNStepsBackBrick.class);
		xstream.alias("brick", HideBrick.class);
		xstream.alias("brick", IfLogicBeginBrick.class);
		xstream.alias("brick", IfLogicElseBrick.class);
		xstream.alias("brick", IfLogicEndBrick.class);
		xstream.alias("brick", IfOnEdgeBounceBrick.class);
		xstream.alias("brick", LedOffBrick.class);
		xstream.alias("brick", LedOnBrick.class);
		xstream.alias("brick", LegoNxtMotorActionBrick.class);
		xstream.alias("brick", LegoNxtMotorStopBrick.class);
		xstream.alias("brick", LegoNxtMotorTurnAngleBrick.class);
		xstream.alias("brick", LegoNxtPlayToneBrick.class);
		xstream.alias("brick", LoopBeginBrick.class);
		xstream.alias("brick", LoopEndBrick.class);
		xstream.alias("brick", LoopEndlessBrick.class);
		xstream.alias("brick", MoveNStepsBrick.class);
		xstream.alias("brick", NextLookBrick.class);
		xstream.alias("brick", NoteBrick.class);
		xstream.alias("brick", PlaceAtBrick.class);
		xstream.alias("brick", PlaySoundBrick.class);
		xstream.alias("brick", PointInDirectionBrick.class);
		xstream.alias("brick", PointToBrick.class);
		xstream.alias("brick", RepeatBrick.class);
		xstream.alias("brick", SetBrightnessBrick.class);
		xstream.alias("brick", SetGhostEffectBrick.class);
		xstream.alias("brick", SetLookBrick.class);
		xstream.alias("brick", SetSizeToBrick.class);
		xstream.alias("brick", SetVariableBrick.class);
		xstream.alias("brick", SetVolumeToBrick.class);
		xstream.alias("brick", SetXBrick.class);
		xstream.alias("brick", SetYBrick.class);
		xstream.alias("brick", ShowBrick.class);
		xstream.alias("brick", SpeakBrick.class);
		xstream.alias("brick", StopAllSoundsBrick.class);
		xstream.alias("brick", TurnLeftBrick.class);
		xstream.alias("brick", TurnRightBrick.class);
		xstream.alias("brick", UserBrick.class);
		xstream.alias("brick", UserScriptDefinitionBrick.class);
		xstream.alias("brick", VibrationBrick.class);
		xstream.alias("brick", WaitBrick.class);
		xstream.alias("brick", WhenBrick.class);
		xstream.alias("brick", WhenStartedBrick.class);

		xstream.alias("brick", DronePlayLedAnimationBrick.class);
		xstream.alias("brick", DroneFlipBrick.class);
		xstream.alias("brick", DroneTakeOffBrick.class);
		xstream.alias("brick", DroneLandBrick.class);
		xstream.alias("brick", DroneMoveForwardBrick.class);
		xstream.alias("brick", DroneMoveBackwardBrick.class);
		xstream.alias("brick", DroneMoveUpBrick.class);
		xstream.alias("brick", DroneMoveDownBrick.class);
		xstream.alias("brick", DroneMoveLeftBrick.class);
		xstream.alias("brick", DroneMoveRightBrick.class);

		xstream.alias("userBrickElements", UserScriptDefinitionBrickElements.class);
		xstream.alias("userBrickElement", UserScriptDefinitionBrickElement.class);
		xstream.alias("userBrickParameter", UserBrickParameter.class);

		xstream.aliasField("formulaList", FormulaBrick.class, "formulaMap");
		xstream.aliasField("object", BrickBaseType.class, "sprite");
	}

	private void createCatroidRoot() {
		File catroidRoot = new File(Constants.DEFAULT_ROOT);
		if (!catroidRoot.exists()) {
			catroidRoot.mkdirs();
		}
	}

	public File getBackPackSoundDirectory() {
		return backPackSoundDirectory;
	}

	public Project loadProject(String projectName) {
		codeFileSanityCheck(projectName);

		Log.d(TAG, "loadProject " + projectName);

		loadSaveLock.lock();
		try {
			File projectCodeFile = new File(Utils.buildProjectPath(projectName), Constants.PROJECTCODE_NAME);
			Log.d(TAG, "path: " + projectCodeFile.getAbsolutePath());
			fileInputStream = new FileInputStream(projectCodeFile);
			return (Project) xstream.getProjectFromXML(projectCodeFile);
		} catch (Exception exception) {
			Log.e(TAG, "Loading project " + projectName + " failed.", exception);
			return null;
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException ioException) {
					Log.e(TAG, "can't close fileStream.", ioException);
				}
			}
			loadSaveLock.unlock();
		}
	}

	public boolean cancelLoadProject() {
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
				return true;
			} catch (IOException ioException) {
				Log.e(TAG, "can't close fileStream.", ioException);
			}
		}
		return false;
	}


	public boolean saveProject(Project project) {
		BufferedWriter writer = null;

		if (project == null) {
			Log.d(TAG, "project is null!");
			return false;
		}

		Log.d(TAG, "saveProject " + project.getName());

		codeFileSanityCheck(project.getName());

		loadSaveLock.lock();

		String projectXml;
		File tmpCodeFile = null;
		File currentCodeFile = null;

		try {
			projectXml = XML_HEADER.concat(xstream.toXML(project));
			tmpCodeFile = new File(Utils.buildProjectPath(project.getName()), Constants.PROJECTCODE_NAME_TMP);
			currentCodeFile = new File(Utils.buildProjectPath(project.getName()), Constants.PROJECTCODE_NAME);

			if (currentCodeFile.exists()) {
				try {
					String oldProjectXml = Files.toString(currentCodeFile, Charsets.UTF_8);

					if (oldProjectXml.equals(projectXml)) {
						Log.d(TAG, "Project version is the same. Do not update " + currentCodeFile.getName());
						return false;
					}
					Log.d(TAG, "Project version differ <" + oldProjectXml.length() + "> <"
							+ projectXml.length() + ">. update " + currentCodeFile.getName());

				} catch (Exception exception) {
					Log.e(TAG, "Opening old project " + currentCodeFile.getName() + " failed.", exception);
					return false;
				}
			}

			File projectDirectory = new File(Utils.buildProjectPath(project.getName()));
			createProjectDataStructure(projectDirectory);

			writer = new BufferedWriter(new FileWriter(tmpCodeFile), Constants.BUFFER_8K);
			writer.write(projectXml);
			writer.flush();
			return true;
		} catch (Exception exception) {
			Log.e(TAG, "Saving project " + project.getName() + " failed.", exception);
			return false;
		} finally {
			if (writer != null) {
				try {
					writer.close();

					if (currentCodeFile.exists() && !currentCodeFile.delete()) {
						Log.e(TAG, "Could not delete " + currentCodeFile.getName());
					}

					if (!tmpCodeFile.renameTo(currentCodeFile)) {
						Log.e(TAG, "Could not rename " + currentCodeFile.getName());
					}

				} catch (IOException ioException) {
					Log.e(TAG, "Failed closing the buffered writer", ioException);
				}
			}

			loadSaveLock.unlock();
		}
	}

	public void codeFileSanityCheck(String projectName) {
		loadSaveLock.lock();

		try {
			File tmpCodeFile = new File(Utils.buildProjectPath(projectName), Constants.PROJECTCODE_NAME_TMP);

			if (tmpCodeFile.exists()) {
				File currentCodeFile = new File(Utils.buildProjectPath(projectName), Constants.PROJECTCODE_NAME);
				if (currentCodeFile.exists()) {
					Log.w(TAG, "TMP File probably corrupted. Both files exist. Discard " + tmpCodeFile.getName());

					if (!tmpCodeFile.delete()) {
						Log.e(TAG, "Could not delete " + tmpCodeFile.getName());
					}

					return;
				}

				Log.w(TAG, "Process interrupted before renaming. Rename " + Constants.PROJECTCODE_NAME_TMP +
						" to " + Constants.PROJECTCODE_NAME);

				if (!tmpCodeFile.renameTo(currentCodeFile)) {
					Log.e(TAG, "Could not rename " + tmpCodeFile.getName());
				}

			}
		} catch (Exception exception) {
			Log.e(TAG, "Exception " + exception);
		} finally {
			loadSaveLock.unlock();
		}
	}

	private void createProjectDataStructure(File projectDirectory) throws IOException {
		Log.d(TAG, "create Project Data structure");
		createCatroidRoot();
		projectDirectory.mkdir();

		File imageDirectory = new File(projectDirectory, Constants.IMAGE_DIRECTORY);
		imageDirectory.mkdir();

		File noMediaFile = new File(imageDirectory, Constants.NO_MEDIA_FILE);
		noMediaFile.createNewFile();

		File soundDirectory = new File(projectDirectory, Constants.SOUND_DIRECTORY);
		soundDirectory.mkdir();

		noMediaFile = new File(soundDirectory, Constants.NO_MEDIA_FILE);
		noMediaFile.createNewFile();

		File backPackDirectory = new File(Constants.DEFAULT_ROOT, Constants.BACKPACK_DIRECTORY);
		backPackDirectory.mkdir();

		noMediaFile = new File(backPackDirectory, Constants.NO_MEDIA_FILE);
		noMediaFile.createNewFile();

		backPackSoundDirectory = new File(backPackDirectory, Constants.BACKPACK_SOUND_DIRECTORY);
		backPackSoundDirectory.mkdir();

		noMediaFile = new File(backPackSoundDirectory, Constants.NO_MEDIA_FILE);
		noMediaFile.createNewFile();

		File backPackImageDirectory = new File(backPackDirectory, Constants.BACKPACK_IMAGE_DIRECTORY);
		backPackImageDirectory.mkdir();

		noMediaFile = new File(backPackImageDirectory, Constants.NO_MEDIA_FILE);
		noMediaFile.createNewFile();
	}

	public void clearBackPackSoundDirectory() {
		if (backPackSoundDirectory.listFiles().length > 1) {
			for (File node : backPackSoundDirectory.listFiles()) {
				if (!(node.getName().equals(".nomedia"))) {
					node.delete();
				}
			}
		}
	}

	public void deleteProject(String projectName) throws IllegalArgumentException, IOException {
		boolean success;
		if (projectName == null || !projectExists(projectName)) {
			throw new IllegalArgumentException("Project with name " + projectName + " does not exist");
		}
		success = UtilFile.deleteDirectory(new File(Utils.buildProjectPath(projectName)));
		if (!success) {
			throw new IOException("Error at deleting project " + projectName);
		}
	}

	public boolean projectExists(String projectName) {
		List<String> projectNameList = UtilFile.getProjectNames(new File(Constants.DEFAULT_ROOT));
		for (String projectNameIterator : projectNameList) {
			if ((projectNameIterator.equals(projectName))) {
				return true;
			}
		}
		return false;
	}

	public File copySoundFile(String path) throws IOException, IllegalArgumentException {
		String currentProject = ProjectManager.getInstance().getCurrentProject().getName();
		File soundDirectory = new File(Utils.buildPath(Utils.buildProjectPath(currentProject), Constants.SOUND_DIRECTORY));

		File inputFile = new File(path);
		if (!inputFile.exists() || !inputFile.canRead()) {
			throw new IllegalArgumentException("file " + path + " doesn`t exist or can`t be read");
		}
		String inputFileChecksum = Utils.md5Checksum(inputFile);

		FileChecksumContainer fileChecksumContainer = ProjectManager.getInstance().getFileChecksumContainer();
		if (fileChecksumContainer.containsChecksum(inputFileChecksum)) {
			fileChecksumContainer.addChecksum(inputFileChecksum, null);
			return new File(fileChecksumContainer.getPath(inputFileChecksum));
		}
		File outputFile = new File(Utils.buildPath(soundDirectory.getAbsolutePath(),
                inputFileChecksum + "_" + inputFile.getName()));

		return copyFileAddCheckSum(outputFile, inputFile);
	}

	public File copySoundFileBackPack(SoundInfo selectedSoundInfo) throws IOException, IllegalArgumentException {

		String path = selectedSoundInfo.getAbsolutePath();

		File inputFile = new File(path);
		if (!inputFile.exists() || !inputFile.canRead()) {
			throw new IllegalArgumentException("file " + path + " doesn`t exist or can`t be read");
		}
		String inputFileChecksum = Utils.md5Checksum(inputFile);

		String currentProject = ProjectManager.getInstance().getCurrentProject().getName();

		File outputFile = new File(Utils.buildPath(Constants.DEFAULT_ROOT, Constants.BACKPACK_DIRECTORY, Constants.BACKPACK_SOUND_DIRECTORY, currentProject
                + "_" + selectedSoundInfo.getTitle() + "_" + inputFileChecksum));

		return copyFileAddCheckSum(outputFile, inputFile);
	}

	public File copyImage(String currentProjectName, String inputFilePath, String newName) throws IOException {
		String newFilePath;
		File imageDirectory = new File(Utils.buildPath(Utils.buildProjectPath(currentProjectName), Constants.IMAGE_DIRECTORY));

		File inputFile = new File(inputFilePath);
		if (!inputFile.exists() || !inputFile.canRead()) {
			return null;
		}

		int[] imageDimensions = new int[2];
		imageDimensions = ImageEditing.getImageDimensions(inputFilePath);
		FileChecksumContainer checksumCont = ProjectManager.getInstance().getFileChecksumContainer();

		File outputFileDirectory = new File(imageDirectory.getAbsolutePath());
		if (outputFileDirectory.exists() == false) {
			outputFileDirectory.mkdirs();
		}

		Project project = ProjectManager.getInstance().getCurrentProject();

		if ((imageDimensions[0] > project.getXmlHeader().virtualScreenWidth)
				&& (imageDimensions[1] > project.getXmlHeader().virtualScreenHeight)) {
			File outputFile = new File(Utils.buildPath(imageDirectory.getAbsolutePath(), inputFile.getName()));
			return copyAndResizeImage(outputFile, inputFile, imageDirectory);
		} else {
			String checksumSource = Utils.md5Checksum(inputFile);

			if (newName != null) {
				newFilePath = Utils.buildPath(imageDirectory.getAbsolutePath(), checksumSource + "_" + newName);
			} else {
				newFilePath = Utils.buildPath(imageDirectory.getAbsolutePath(), checksumSource + "_" + inputFile.getName());
				if (checksumCont.containsChecksum(checksumSource)) {
					checksumCont.addChecksum(checksumSource, newFilePath);
					return new File(checksumCont.getPath(checksumSource));
				}
			}

			File outputFile = new File(newFilePath);
			return copyFileAddCheckSum(outputFile, inputFile);
		}
	}

	public File makeTempImageCopy(String inputFilePath) throws IOException {
		File tempDirectory = new File(Constants.TMP_PATH);

		File inputFile = new File(inputFilePath);
		if (!inputFile.exists() || !inputFile.canRead()) {
			return null;
		}

		File outputFileDirectory = new File(tempDirectory.getAbsolutePath());
		if (outputFileDirectory.exists() == false) {
			outputFileDirectory.mkdirs();
		}

		File outputFile = new File(Constants.TMP_IMAGE_PATH);

		File copiedFile = UtilFile.copyFile(outputFile, inputFile);

		return copiedFile;
	}

	public void deleteTempImageCopy() {
		File temporaryPictureFileInPocketPaint = new File(Constants.TMP_IMAGE_PATH);
		if (temporaryPictureFileInPocketPaint.exists()) {
			temporaryPictureFileInPocketPaint.delete();
		}
	}

	private File copyAndResizeImage(File outputFile, File inputFile, File imageDirectory) throws IOException {
		Project project = ProjectManager.getInstance().getCurrentProject();
		Bitmap bitmap = ImageEditing.getScaledBitmapFromPath(inputFile.getAbsolutePath(),
				project.getXmlHeader().virtualScreenWidth, project.getXmlHeader().virtualScreenHeight,
				ImageEditing.ResizeType.FILL_RECTANGLE_WITH_SAME_ASPECT_RATIO, true);

		saveBitmapToImageFile(outputFile, bitmap);

		String checksumCompressedFile = Utils.md5Checksum(outputFile);

		FileChecksumContainer fileChecksumContainer = ProjectManager.getInstance().getFileChecksumContainer();
		String newFilePath = Utils.buildPath(imageDirectory.getAbsolutePath(),
                checksumCompressedFile + "_" + inputFile.getName());

		if (!fileChecksumContainer.addChecksum(checksumCompressedFile, newFilePath)) {
			if (!outputFile.getAbsolutePath().equalsIgnoreCase(inputFile.getAbsolutePath())) {
				outputFile.delete();
			}
			return new File(fileChecksumContainer.getPath(checksumCompressedFile));
		}

		File compressedFile = new File(newFilePath);
		outputFile.renameTo(compressedFile);

		return compressedFile;
	}

	public void deleteFile(String filepath) {
		FileChecksumContainer container = ProjectManager.getInstance().getFileChecksumContainer();
		try {
			if (container.decrementUsage(filepath)) {
				File toDelete = new File(filepath);
				toDelete.delete();
			}
		} catch (FileNotFoundException fileNotFoundException) {
			Log.e(TAG, Log.getStackTraceString(fileNotFoundException));
			//deleteFile(filepath);
		}
	}

	public void fillChecksumContainer() {
		//FileChecksumContainer container = ProjectManager.getInstance().getFileChecksumContainer();
		//if (container == null) {
		ProjectManager.getInstance().setFileChecksumContainer(new FileChecksumContainer());
		//}
		FileChecksumContainer container = ProjectManager.getInstance().getFileChecksumContainer();

		Project newProject = ProjectManager.getInstance().getCurrentProject();
		List<Sprite> currentSpriteList = newProject.getSpriteList();

		for (Sprite currentSprite : currentSpriteList) {
			for (SoundInfo soundInfo : currentSprite.getSoundList()) {
				container.addChecksum(soundInfo.getChecksum(), soundInfo.getAbsolutePath());
			}

			for (LookData lookData : currentSprite.getLookDataList()) {
				container.addChecksum(lookData.getChecksum(), lookData.getAbsolutePath());
			}
		}
	}

	public String getXMLStringOfAProject(Project project) {
		loadSaveLock.lock();
		String xmlProject = "";
		try {
			xmlProject = xstream.toXML(project);
		} finally {
			loadSaveLock.unlock();
		}
		return xmlProject;
	}

	private File copyFileAddCheckSum(File destinationFile, File sourceFile) throws IOException {
		File copiedFile = UtilFile.copyFile(destinationFile, sourceFile);
		addChecksum(destinationFile, sourceFile);

		return copiedFile;
	}

	private void addChecksum(File destinationFile, File sourceFile) {
		String checksumSource = Utils.md5Checksum(sourceFile);
		FileChecksumContainer fileChecksumContainer = ProjectManager.getInstance().getFileChecksumContainer();
		fileChecksumContainer.addChecksum(checksumSource, destinationFile.getAbsolutePath());
	}

}

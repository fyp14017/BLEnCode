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
package hku.fyp14017.blencode.transfers;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.io.StorageHandler;
import hku.fyp14017.blencode.utils.StatusBarNotificationManager;
import hku.fyp14017.blencode.utils.UtilDeviceInfo;
import hku.fyp14017.blencode.utils.UtilZip;
import hku.fyp14017.blencode.utils.Utils;
import hku.fyp14017.blencode.web.ServerCalls;
import hku.fyp14017.blencode.web.WebconnectionException;

import java.io.File;
import java.io.IOException;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.utils.StatusBarNotificationManager;
import hku.fyp14017.blencode.utils.UtilDeviceInfo;
import hku.fyp14017.blencode.utils.UtilZip;
import hku.fyp14017.blencode.web.ServerCalls;
import hku.fyp14017.blencode.web.WebconnectionException;

public class ProjectUploadService extends IntentService {

	private static final String TAG = ProjectUploadService.class.getSimpleName();
	private static final String UPLOAD_FILE_NAME = "upload" + Constants.CATROBAT_EXTENSION;

	private String projectPath;
	private String projectName;
	private String projectDescription;
	private String token;
	private String serverAnswer;
	private boolean result;
	public ResultReceiver receiver;
	private Integer notificationId;
	private String username;

	public ProjectUploadService() {
		super("ProjectUploadService");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startID) {
		int returnCode = super.onStartCommand(intent, flags, startID);
		this.projectPath = intent.getStringExtra("projectPath");
		this.projectName = intent.getStringExtra("uploadName");
		this.projectDescription = intent.getStringExtra("projectDescription");
		this.token = intent.getStringExtra("token");
		this.username = intent.getStringExtra("username");
		this.serverAnswer = "";
		this.result = true;
		this.notificationId = intent.getIntExtra("notificationId", 0);

		return returnCode;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		StorageHandler.getInstance().saveProject(ProjectManager.getInstance().getCurrentProject());

		receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
		try {
			if (projectPath == null) {
				result = false;
				Log.e(TAG, "project path is null");
				return;
			}

			File directoryPath = new File(projectPath);
			String[] paths = directoryPath.list();

			if (paths == null) {
				result = false;
				Log.e(TAG, "project path is not valid");
				return;
			}

			for (int i = 0; i < paths.length; i++) {
				paths[i] = Utils.buildPath(directoryPath.getAbsolutePath(), paths[i]);
			}

			String zipFileString = Utils.buildPath(Constants.TMP_PATH, UPLOAD_FILE_NAME);
			File zipFile = new File(zipFileString);
			if (!zipFile.exists()) {
				zipFile.getParentFile().mkdirs();
				zipFile.createNewFile();
			}
			if (!UtilZip.writeToZipFile(paths, zipFileString)) {
				zipFile.delete();
				result = false;
				return;
			}

			//String deviceIMEI = UtilDeviceInfo.getDeviceIMEI(context);
			String userEmail = UtilDeviceInfo.getUserEmail(this);
			String language = UtilDeviceInfo.getUserLanguageCode(this);

			Context context = getApplicationContext();
			ServerCalls.getInstance().uploadProject(projectName, projectDescription, zipFileString, userEmail,
					language, token, username, receiver, notificationId, context);

			zipFile.delete();
		} catch (IOException ioException) {
			Log.e(TAG, Log.getStackTraceString(ioException));
			result = false;
		} catch (WebconnectionException webconnectionException) {
			serverAnswer = webconnectionException.getMessage();
			Log.e(TAG, serverAnswer);
			result = false;
		}
	}

	@Override
	public void onDestroy() {
		if (!result) {
			Toast.makeText(this, getResources().getText(hku.fyp14017.blencode.R.string.error_project_upload).toString() + " " + serverAnswer,
					Toast.LENGTH_SHORT).show();
			StatusBarNotificationManager.getInstance().abortProgressNotificationWithMessage(notificationId,
					getResources().getString(hku.fyp14017.blencode.R.string.notification_upload_rejected));
		} else {
			Toast.makeText(this, hku.fyp14017.blencode.R.string.notification_upload_finished, Toast.LENGTH_SHORT).show();
		}
		super.onDestroy();
	}

}

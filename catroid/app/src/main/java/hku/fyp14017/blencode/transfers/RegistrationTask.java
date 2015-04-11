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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.ui.dialogs.CustomAlertDialogBuilder;
import hku.fyp14017.blencode.utils.UtilDeviceInfo;
import hku.fyp14017.blencode.utils.Utils;
import hku.fyp14017.blencode.web.ServerCalls;
import hku.fyp14017.blencode.web.WebconnectionException;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.ui.dialogs.CustomAlertDialogBuilder;
import hku.fyp14017.blencode.utils.UtilDeviceInfo;
import hku.fyp14017.blencode.utils.Utils;
import hku.fyp14017.blencode.web.ServerCalls;
import hku.fyp14017.blencode.web.WebconnectionException;

public class RegistrationTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = RegistrationTask.class.getSimpleName();

	private Context context;
	private ProgressDialog progressDialog;
	private String username;
	private String password;

	private String message;
	private boolean userRegistered;

	private OnRegistrationCompleteListener onRegistrationCompleteListener;

	public RegistrationTask(Context activity, String username, String password) {
		this.context = activity;
		this.username = username;
		this.password = password;
	}

	public void setOnRegistrationCompleteListener(OnRegistrationCompleteListener listener) {
		onRegistrationCompleteListener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (context == null) {
			return;
		}
		String title = context.getString(hku.fyp14017.blencode.R.string.please_wait);
		String message = context.getString(hku.fyp14017.blencode.R.string.loading);
		progressDialog = ProgressDialog.show(context, title, message);
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		try {
			if (!Utils.isNetworkAvailable(context)) {
				return false;
			}

			String email = UtilDeviceInfo.getUserEmail(context);
			String language = UtilDeviceInfo.getUserLanguageCode(context);
			String country = UtilDeviceInfo.getUserCountryCode(context);
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			String token = sharedPreferences.getString(Constants.TOKEN, Constants.NO_TOKEN);

			userRegistered = ServerCalls.getInstance().registerOrCheckToken(username, password, email, language,
					country, token, context);

			return true;

		} catch (WebconnectionException webconnectionException) {
			Log.e(TAG, Log.getStackTraceString(webconnectionException));
			message = webconnectionException.getMessage();
		}
		return false;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		if (!result) {
			showDialog(hku.fyp14017.blencode.R.string.error_internet_connection);
			return;
		}

		if (context == null) {
			return;
		}

		if (userRegistered) {
			Toast.makeText(context, hku.fyp14017.blencode.R.string.new_user_registered, Toast.LENGTH_SHORT).show();
		}

		if (onRegistrationCompleteListener != null) {
			onRegistrationCompleteListener.onRegistrationComplete();
		}
	}

	private void showDialog(int messageId) {
		if (context == null) {
			return;
		}
		if (message == null) {
			new CustomAlertDialogBuilder(context).setTitle(hku.fyp14017.blencode.R.string.register_error).setMessage(messageId)
					.setPositiveButton(hku.fyp14017.blencode.R.string.ok, null).show();
		} else {
			new CustomAlertDialogBuilder(context).setTitle(hku.fyp14017.blencode.R.string.register_error).setMessage(message)
					.setPositiveButton(hku.fyp14017.blencode.R.string.ok, null).show();
		}
	}

	public interface OnRegistrationCompleteListener {

		void onRegistrationComplete();

	}
}

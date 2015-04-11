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
package hku.fyp14017.blencode.ui.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.ui.ScriptActivity;
import hku.fyp14017.blencode.utils.Utils;

public class RenameLookDialog extends TextDialog {

	private static final String BUNDLE_ARGUMENTS_OLD_LOOK_NAME = "old_look_name";
	public static final String EXTRA_NEW_LOOK_NAME = "new_look_name";
	public static final String DIALOG_FRAGMENT_TAG = "dialog_rename_look";

	private String oldLookName;

	public static RenameLookDialog newInstance(String oldLookName) {
		RenameLookDialog dialog = new RenameLookDialog();

		Bundle arguments = new Bundle();
		arguments.putString(BUNDLE_ARGUMENTS_OLD_LOOK_NAME, oldLookName);
		dialog.setArguments(arguments);

		return dialog;
	}

	@Override
	protected void initialize() {
		oldLookName = getArguments().getString(BUNDLE_ARGUMENTS_OLD_LOOK_NAME);
		input.setText(oldLookName);
		inputTitle.setText(R.string.lookname);
	}

	@Override
	protected boolean handleOkButton() {
		String newLookName = input.getText().toString().trim();

		if (newLookName.equals(oldLookName)) {
			dismiss();
		}

		if (newLookName != null && !newLookName.equalsIgnoreCase("")) {
			newLookName = Utils.getUniqueLookName(newLookName);
		} else {
			Utils.showErrorDialog(getActivity(), R.string.lookname_invalid);
			dismiss();
		}

		Intent intent = new Intent(ScriptActivity.ACTION_LOOK_RENAMED);
		intent.putExtra(EXTRA_NEW_LOOK_NAME, newLookName);
		getActivity().sendBroadcast(intent);
		return true;
	}

	@Override
	protected String getTitle() {
		return getString(R.string.rename_look_dialog);
	}

	@Override
	protected String getHint() {
		return null;
	}

	@Override
	protected TextWatcher getInputTextChangedListener(final Button buttonPositive) {
		return new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 0 || (s.length() == 1 && s.charAt(0) == '.')) {
					Toast.makeText(getActivity(), R.string.notification_invalid_text_entered, Toast.LENGTH_SHORT)
							.show();
					buttonPositive.setEnabled(false);
				} else {
					buttonPositive.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
	}
}

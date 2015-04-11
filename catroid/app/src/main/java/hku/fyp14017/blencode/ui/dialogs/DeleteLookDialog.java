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

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.LookData;
import hku.fyp14017.blencode.io.StorageHandler;
import hku.fyp14017.blencode.ui.ScriptActivity;

import java.util.ArrayList;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.LookData;
import hku.fyp14017.blencode.ui.ScriptActivity;

public class DeleteLookDialog extends DialogFragment {

	private static final String BUNDLE_ARGUMENTS_SELECTED_POSITION = "selected_position";
	public static final String DIALOG_FRAGMENT_TAG = "dialog_delete_look";

	public static DeleteLookDialog newInstance(int selectedPosition) {
		DeleteLookDialog dialog = new DeleteLookDialog();

		Bundle arguments = new Bundle();
		arguments.putInt(BUNDLE_ARGUMENTS_SELECTED_POSITION, selectedPosition);
		dialog.setArguments(arguments);

		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int selectedPosition = getArguments().getInt(BUNDLE_ARGUMENTS_SELECTED_POSITION);

		Dialog dialog = new CustomAlertDialogBuilder(getActivity()).setTitle(hku.fyp14017.blencode.R.string.delete_look_dialog)
				.setNegativeButton(hku.fyp14017.blencode.R.string.cancel_button, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				}).setPositiveButton(hku.fyp14017.blencode.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handleDeleteLook(selectedPosition);
					}
				}).create();

		dialog.setCanceledOnTouchOutside(true);

		return dialog;
	}

	private void handleDeleteLook(int position) {
		ArrayList<LookData> lookDataList = ProjectManager.getInstance().getCurrentSprite().getLookDataList();

		StorageHandler.getInstance().deleteFile(lookDataList.get(position).getAbsolutePath());
		lookDataList.remove(position);

		getActivity().sendBroadcast(new Intent(ScriptActivity.ACTION_LOOK_DELETED));
	}
}

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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.ui.controller.LookController;
import hku.fyp14017.blencode.ui.fragment.LookFragment;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.ui.fragment.LookFragment;

public class NewLookDialog extends DialogFragment {

	public static final String TAG = "dialog_new_look";

	private LookFragment fragment = null;

	public static NewLookDialog newInstance() {
		return new NewLookDialog();
	}

	public void showDialog(Fragment fragment) {
		if (!(fragment instanceof LookFragment)) {
			throw new RuntimeException("This dialog (NewLookDialog) can only be called by the LookFragment.");
		}
		this.fragment = (LookFragment) fragment;
		show(fragment.getActivity().getSupportFragmentManager(), TAG);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View dialogView = LayoutInflater.from(getActivity()).inflate(hku.fyp14017.blencode.R.layout.dialog_new_look, null);
		setupPaintroidButton(dialogView);
		setupGalleryButton(dialogView);
		setupCameraButton(dialogView);

		AlertDialog dialog;
		AlertDialog.Builder dialogBuilder = new CustomAlertDialogBuilder(getActivity()).setView(dialogView).setTitle(
				hku.fyp14017.blencode.R.string.new_look_dialog_title);

		dialog = createDialog(dialogBuilder);
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	private AlertDialog createDialog(AlertDialog.Builder dialogBuilder) {
		return dialogBuilder.create();
	}

	private void setupPaintroidButton(View parentView) {
		View paintroidButton = parentView.findViewById(hku.fyp14017.blencode.R.id.dialog_new_look_paintroid);

		final Intent intent = new Intent("android.intent.action.MAIN");
		intent.setComponent(new ComponentName(Constants.POCKET_PAINT_PACKAGE_NAME,
				Constants.POCKET_PAINT_INTENT_ACTIVITY_NAME));

		paintroidButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (LookController.getInstance().checkIfPocketPaintIsInstalled(intent, getActivity())) {
					fragment.addLookDrawNewImage();
					NewLookDialog.this.dismiss();
				}
			}
		});
	}

	private void setupGalleryButton(View parentView) {
		View galleryButton = parentView.findViewById(hku.fyp14017.blencode.R.id.dialog_new_look_gallery);

		galleryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				fragment.addLookChooseImage();
				NewLookDialog.this.dismiss();
			}
		});
	}

	private void setupCameraButton(View parentView) {
		View cameraButton = parentView.findViewById(hku.fyp14017.blencode.R.id.dialog_new_look_camera);

		cameraButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				fragment.addLookFromCamera();
				NewLookDialog.this.dismiss();
			}
		});
	}
}

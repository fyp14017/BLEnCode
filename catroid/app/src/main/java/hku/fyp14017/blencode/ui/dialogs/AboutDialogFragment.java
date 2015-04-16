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
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.utils.Utils;

public class AboutDialogFragment extends DialogFragment {
	public static final String DIALOG_FRAGMENT_TAG = "dialog_about_pocketcode";

	@Override
	public Dialog onCreateDialog(Bundle bundle) {
		View view = LayoutInflater.from(getActivity()).inflate(hku.fyp14017.blencode.R.layout.dialog_about, null);

        TextView license = (TextView) view.findViewById(R.id.dialog_about_text_view_license_info);
        license.setMovementMethod(LinkMovementMethod.getInstance());
        String licenseHTML = getString(hku.fyp14017.blencode.R.string.about_link_template,"https://github.com/fyp14017/BLEnCode/blob/master/Acknowledgements.txt",
                "Pocket Code Acknowledgements");
        license.setText(Html.fromHtml(licenseHTML));

		TextView aboutUrlTextView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.dialog_about_text_view_url);
		aboutUrlTextView.setMovementMethod(LinkMovementMethod.getInstance());

		String aboutUrl = getString(hku.fyp14017.blencode.R.string.about_link_template, Constants.ABOUT_POCKETCODE_LICENSE_URL,
				getString(hku.fyp14017.blencode.R.string.dialog_about_pocketcode_license_link_text));

		aboutUrlTextView.setText(Html.fromHtml(aboutUrl));

		TextView aboutUrlCatrobatView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.dialog_about_text_catrobat_url);
		aboutUrlCatrobatView.setMovementMethod(LinkMovementMethod.getInstance());

		String aboutCatrobatUrl = getString(hku.fyp14017.blencode.R.string.about_link_template, Constants.CATROBAT_ABOUT_URL,
				getString(hku.fyp14017.blencode.R.string.dialog_about_catrobat_link_text));

		aboutUrlCatrobatView.setText(Html.fromHtml(aboutCatrobatUrl));

		TextView aboutVersionNameTextView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.dialog_about_text_view_version_name);
		String versionName = this.getString(hku.fyp14017.blencode.R.string.android_version_prefix) + Utils.getVersionName(getActivity());
		aboutVersionNameTextView.setText(versionName);

		Dialog aboutDialog = new AlertDialog.Builder(getActivity()).setView(view).setTitle("About BLEnCode")
				.setNeutralButton(hku.fyp14017.blencode.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).create();
		aboutDialog.setCanceledOnTouchOutside(true);

		return aboutDialog;
	}
}

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

public class AcceptTermsOfUseDialogFragment extends DialogFragment {
	public static final String DIALOG_FRAGMENT_TAG = "dialog_accept_terms_of_use";

	@Override
	public Dialog onCreateDialog(Bundle bundle) {
		View view = LayoutInflater.from(getActivity()).inflate(hku.fyp14017.blencode.R.layout.dialog_terms_of_use, null);

		TextView termsOfUseUrlTextView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.dialog_terms_of_use_text_view_url);
		termsOfUseUrlTextView.setMovementMethod(LinkMovementMethod.getInstance());

		String termsOfUseUrl = getString(hku.fyp14017.blencode.R.string.terms_of_use_link_template, Constants.CATROBAT_TERMS_OF_USE_URL,
				getString(hku.fyp14017.blencode.R.string.dialog_terms_of_use_link_text));

		termsOfUseUrlTextView.setText(Html.fromHtml(termsOfUseUrl));

		Dialog termsOfUseDialog = new AlertDialog.Builder(getActivity()).setView(view)
				.setTitle(hku.fyp14017.blencode.R.string.dialog_terms_of_use_title)
				.setNeutralButton(hku.fyp14017.blencode.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).create();
		termsOfUseDialog.setCanceledOnTouchOutside(true);

		return termsOfUseDialog;
	}
}

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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import hku.fyp14017.blencode.R;

public class CustomAlertDialogBuilder extends AlertDialog.Builder {

	private TextView textView;

	public CustomAlertDialogBuilder(Context context) {
		super(context);
		View dialogView = LayoutInflater.from(context).inflate(hku.fyp14017.blencode.R.layout.dialog_custom_alert_dialog, null);
		textView = (TextView) dialogView.findViewById(hku.fyp14017.blencode.R.id.dialog_text_text_view);
		setView(dialogView);
	}

	@Override
	public CustomAlertDialogBuilder setMessage(int messageId) {
		textView.setText(messageId);
		return this;
	}

	@Override
	public CustomAlertDialogBuilder setMessage(CharSequence message) {
		textView.setText(message);
		return this;
	}
}

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
package hku.fyp14017.blencode.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;

import hku.fyp14017.blencode.common.BrickValues;

import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.InterpretationException;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;
import hku.fyp14017.blencode.utils.Utils;

import java.util.List;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.BrickValues;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.InterpretationException;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;
import hku.fyp14017.blencode.utils.Utils;

public class VibrationBrick extends FormulaBrick implements OnClickListener {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	private VibrationBrick() {
		addAllowedBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS);
	}

	public VibrationBrick(Formula vibrateDurationInSecondsFormula) {
		initializeBrickFields(vibrateDurationInSecondsFormula);
	}

	public VibrationBrick(int vibrationDurationInMilliseconds) {
		initializeBrickFields(new Formula(vibrationDurationInMilliseconds / 1000.0));
	}

	private void initializeBrickFields(Formula vibrateDurationInSecondsFormula) {
		addAllowedBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS);
		setFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS, vibrateDurationInSecondsFormula);
	}

	@Override
	public int getRequiredResources() {
		return VIBRATOR;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_vibration, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_vibration_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView textSeconds = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_prototype_text_view_seconds);
		TextView editSeconds = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_edit_seconds_text);
		getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS)
				.setTextFieldId(hku.fyp14017.blencode.R.id.brick_vibration_edit_seconds_text);
		getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS).refreshTextField(view);

		TextView times = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_second_text_view);

		if (getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS).isSingleNumberFormula()) {
			try {
				times.setText(view.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.second_plural,
						Utils.convertDoubleToPluralInteger(getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS)
                                .interpretDouble(ProjectManager.getInstance().getCurrentSprite()))));
			} catch (InterpretationException interpretationException) {
				Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
			}

		} else {
			times.setText(view.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.second_plural,
					Utils.TRANSLATION_PLURAL_OTHER_INTEGER));
		}

		textSeconds.setVisibility(View.GONE);
		editSeconds.setVisibility(View.VISIBLE);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_vibration, null);
		TextView textSeconds = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_prototype_text_view_seconds);
		textSeconds.setText(String.valueOf(BrickValues.VIBRATE_MILLISECONDS));
		TextView times = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_second_text_view);
		times.setText(context.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.second_plural,
				Utils.convertDoubleToPluralInteger(BrickValues.VIBRATE_MILLISECONDS)));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {
		if (view != null) {
			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textVibrationLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_label);
			TextView textVibrationSeconds = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_second_text_view);
			TextView editSeconds = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_vibration_edit_seconds_text);

			textVibrationLabel.setTextColor(textVibrationLabel.getTextColors().withAlpha(alphaValue));
			textVibrationSeconds.setTextColor(textVibrationSeconds.getTextColors().withAlpha((alphaValue)));

			editSeconds.setTextColor(editSeconds.getTextColors().withAlpha(alphaValue));
			editSeconds.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS));
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.vibrate(sprite,
				getFormulaWithBrickField(BrickField.VIBRATE_DURATION_IN_SECONDS)));
		return null;
	}
}
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;

import hku.fyp14017.blencode.common.BrickValues;

import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;

import java.util.List;

import hku.fyp14017.blencode.common.BrickValues;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;

public class LegoNxtPlayToneBrick extends FormulaBrick implements OnClickListener {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	private transient TextView editFreq;

	public LegoNxtPlayToneBrick() {
		addAllowedBrickField(BrickField.LEGO_NXT_FREQUENCY);
		addAllowedBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS);
	}

	public LegoNxtPlayToneBrick(int frequencyValue, int durationValue) {
		initializeBrickFields(new Formula(frequencyValue), new Formula(durationValue));
	}

	public LegoNxtPlayToneBrick(Formula frequencyFormula, Formula durationFormula) {
		initializeBrickFields(frequencyFormula, durationFormula);
	}

	private void initializeBrickFields(Formula frequencyFormula, Formula durationFormula) {
		addAllowedBrickField(BrickField.LEGO_NXT_FREQUENCY);
		addAllowedBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS);
		setFormulaWithBrickField(BrickField.LEGO_NXT_FREQUENCY, frequencyFormula);
		setFormulaWithBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS, durationFormula);
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_LEGO_NXT | getFormulaWithBrickField(BrickField.LEGO_NXT_FREQUENCY).getRequiredResources() | getFormulaWithBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS).getRequiredResources();
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_nxt_play_tone, null);
		TextView textDuration = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_duration_text_view);
		textDuration.setText(String.valueOf(BrickValues.LEGO_DURATION));
		TextView textFreq = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_freq_text_view);
		textFreq.setText(String.valueOf(BrickValues.LEGO_FREQUENCY));
		return prototypeView;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}
		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_nxt_play_tone, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_nxt_play_tone_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView textDuration = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_duration_text_view);
		TextView editDuration = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_duration_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS)
				.setTextFieldId(hku.fyp14017.blencode.R.id.nxt_tone_duration_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS).refreshTextField(view);

		textDuration.setVisibility(View.GONE);
		editDuration.setVisibility(View.VISIBLE);

		editDuration.setOnClickListener(this);

		TextView textFreq = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_freq_text_view);
		editFreq = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_freq_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_NXT_FREQUENCY).setTextFieldId(hku.fyp14017.blencode.R.id.nxt_tone_freq_edit_text);
		getFormulaWithBrickField(BrickField.LEGO_NXT_FREQUENCY).refreshTextField(view);

		textFreq.setVisibility(View.GONE);
		editFreq.setVisibility(View.VISIBLE);

		editFreq.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		switch (view.getId()) {
			case hku.fyp14017.blencode.R.id.nxt_tone_freq_edit_text:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.LEGO_NXT_FREQUENCY));
				break;
			case hku.fyp14017.blencode.R.id.nxt_tone_duration_edit_text:
				FormulaEditorFragment.showFragment(view, this,
						getFormulaWithBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS));
				break;
		}
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_nxt_play_tone_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textLegoPlayToneLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_nxt_play_tone_label);
			TextView textLegoPlayToneDuration = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_nxt_play_tone_duration);
			TextView textLegoPlayToneDurationTextView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_duration_text_view);
			TextView textLegoPlayToneSeconds = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_nxt_play_tone_seconds);
			TextView textLegoPlayToneFrequency = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_nxt_play_tone_frequency);
			TextView textLegoPlayToneOz = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_nxt_play_tone_hundred_hz);

			TextView editLegoDuration = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_duration_edit_text);
			TextView editLegoFrequency = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.nxt_tone_freq_edit_text);
			textLegoPlayToneLabel.setTextColor(textLegoPlayToneLabel.getTextColors().withAlpha(alphaValue));
			textLegoPlayToneDuration.setTextColor(textLegoPlayToneDuration.getTextColors().withAlpha(alphaValue));
			textLegoPlayToneDurationTextView.setTextColor(textLegoPlayToneDurationTextView.getTextColors().withAlpha(
					alphaValue));
			textLegoPlayToneSeconds.setTextColor(textLegoPlayToneSeconds.getTextColors().withAlpha(alphaValue));
			textLegoPlayToneFrequency.setTextColor(textLegoPlayToneFrequency.getTextColors().withAlpha(alphaValue));
			textLegoPlayToneOz.setTextColor(textLegoPlayToneOz.getTextColors().withAlpha(alphaValue));

			editLegoFrequency.setTextColor(editLegoFrequency.getTextColors().withAlpha(alphaValue));
			editLegoFrequency.getBackground().setAlpha(alphaValue);
			editLegoDuration.setTextColor(editLegoDuration.getTextColors().withAlpha(alphaValue));
			editLegoDuration.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.legoNxtPlayTone(sprite,
				getFormulaWithBrickField(BrickField.LEGO_NXT_FREQUENCY),
				getFormulaWithBrickField(BrickField.LEGO_NXT_DURATION_IN_SECONDS)));
		return null;
	}
}

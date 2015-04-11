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

public class TurnLeftBrick extends FormulaBrick implements OnClickListener {

	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public TurnLeftBrick() {
		addAllowedBrickField(BrickField.TURN_LEFT_DEGREES);
	}

	public TurnLeftBrick(double degreesValue) {
		initializeBrickFields(new Formula(degreesValue));
	}

	public TurnLeftBrick(Formula degrees) {
		initializeBrickFields(degrees);
	}

	private void initializeBrickFields(Formula degrees) {
		addAllowedBrickField(BrickField.TURN_LEFT_DEGREES);
		setFormulaWithBrickField(BrickField.TURN_LEFT_DEGREES, degrees);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.TURN_LEFT_DEGREES).getRequiredResources();
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_turn_left, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_turn_left_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView textDegrees = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_prototype_text_view);
		TextView editDegrees = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_edit_text);
		getFormulaWithBrickField(BrickField.TURN_LEFT_DEGREES).setTextFieldId(hku.fyp14017.blencode.R.id.brick_turn_left_edit_text);
		getFormulaWithBrickField(BrickField.TURN_LEFT_DEGREES).refreshTextField(view);

		textDegrees.setVisibility(View.GONE);
		editDegrees.setVisibility(View.VISIBLE);
		editDegrees.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_turn_left, null);
		TextView textDegrees = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_prototype_text_view);
		textDegrees.setText(String.valueOf(BrickValues.TURN_DEGREES));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView turnLeftLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_label);
			TextView textDegrees = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_prototype_text_view);
			TextView times = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_degree_text_view);
			TextView editDegrees = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_turn_left_edit_text);

			textDegrees.setTextColor(textDegrees.getTextColors().withAlpha(alphaValue));
			turnLeftLabel.setTextColor(turnLeftLabel.getTextColors().withAlpha(alphaValue));
			times.setTextColor(times.getTextColors().withAlpha(alphaValue));
			editDegrees.setTextColor(editDegrees.getTextColors().withAlpha(alphaValue));
			editDegrees.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.TURN_LEFT_DEGREES));
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.turnLeft(sprite, getFormulaWithBrickField(BrickField.TURN_LEFT_DEGREES)));
		return null;
	}
}

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

public class PlaceAtBrick extends FormulaBrick implements OnClickListener {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public PlaceAtBrick() {
		addAllowedBrickField(BrickField.X_POSITION);
		addAllowedBrickField(BrickField.Y_POSITION);
	}

	public PlaceAtBrick(int xPositionValue, int yPositionValue) {
		initializeBrickFields(new Formula(xPositionValue), new Formula(yPositionValue));
	}

	public PlaceAtBrick(Formula xPosition, Formula yPosition) {
		initializeBrickFields(xPosition, yPosition);
	}

	public void setXPosition(Formula xPosition) {
		setFormulaWithBrickField(BrickField.X_POSITION, xPosition);
	}

	public void setYPosition(Formula yPosition) {
		setFormulaWithBrickField(BrickField.Y_POSITION, yPosition);
	}

	private void initializeBrickFields(Formula xPosition, Formula yPosition) {
		addAllowedBrickField(BrickField.X_POSITION);
		addAllowedBrickField(BrickField.Y_POSITION);
		setFormulaWithBrickField(BrickField.X_POSITION, xPosition);
		setFormulaWithBrickField(BrickField.Y_POSITION, yPosition);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.Y_POSITION).getRequiredResources()|getFormulaWithBrickField(BrickField.X_POSITION).getRequiredResources();
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_place_at, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_place_at_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView textX = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_prototype_text_view_x);
		TextView editX = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_edit_text_x);
		getFormulaWithBrickField(BrickField.X_POSITION).setTextFieldId(hku.fyp14017.blencode.R.id.brick_place_at_edit_text_x);
		getFormulaWithBrickField(BrickField.X_POSITION).refreshTextField(view);

		textX.setVisibility(View.GONE);
		editX.setVisibility(View.VISIBLE);
		editX.setOnClickListener(this);

		TextView textY = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_prototype_text_view_y);
		TextView editY = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_edit_text_y);
		getFormulaWithBrickField(BrickField.Y_POSITION).setTextFieldId(hku.fyp14017.blencode.R.id.brick_place_at_edit_text_y);
		getFormulaWithBrickField(BrickField.Y_POSITION).refreshTextField(view);
		textY.setVisibility(View.GONE);
		editY.setVisibility(View.VISIBLE);
		editY.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_place_at, null);
		TextView textX = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_prototype_text_view_x);
		textX.setText(String.valueOf(BrickValues.X_POSITION));
		TextView textY = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_prototype_text_view_y);
		textY.setText(String.valueOf(BrickValues.Y_POSITION));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView placeAtLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_label);
			TextView placeAtX = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_x_textview);
			TextView placeAtY = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_y_textview);
			TextView editX = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_edit_text_x);
			TextView editY = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_place_at_edit_text_y);
			placeAtLabel.setTextColor(placeAtLabel.getTextColors().withAlpha(alphaValue));
			placeAtX.setTextColor(placeAtX.getTextColors().withAlpha(alphaValue));
			placeAtY.setTextColor(placeAtY.getTextColors().withAlpha(alphaValue));
			editX.setTextColor(editX.getTextColors().withAlpha(alphaValue));
			editX.getBackground().setAlpha(alphaValue);
			editY.setTextColor(editY.getTextColors().withAlpha(alphaValue));
			editY.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		switch (view.getId()) {
			case hku.fyp14017.blencode.R.id.brick_place_at_edit_text_x:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.X_POSITION));
				break;

			case hku.fyp14017.blencode.R.id.brick_place_at_edit_text_y:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.Y_POSITION));
				break;
		}
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.placeAt(sprite, getFormulaWithBrickField(BrickField.X_POSITION),
				getFormulaWithBrickField(BrickField.Y_POSITION)));
		return null;
	}
}

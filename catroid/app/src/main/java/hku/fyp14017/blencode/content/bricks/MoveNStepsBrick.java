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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.InterpretationException;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;

public class MoveNStepsBrick extends FormulaBrick implements OnClickListener {

	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public MoveNStepsBrick() {
		addAllowedBrickField(BrickField.STEPS);
	}

	public MoveNStepsBrick(double stepsValue) {
		initializeBrickFields(new Formula(stepsValue));
	}

	public MoveNStepsBrick(Formula steps) {
		initializeBrickFields(steps);
	}

	private void initializeBrickFields(Formula steps) {
		addAllowedBrickField(BrickField.STEPS);
		setFormulaWithBrickField(BrickField.STEPS, steps);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.STEPS).getRequiredResources();
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_move_n_steps, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_move_n_steps_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView text = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_prototype_text_view);
		TextView edit = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_edit_text);

		getFormulaWithBrickField(BrickField.STEPS).setTextFieldId(hku.fyp14017.blencode.R.id.brick_move_n_steps_edit_text);
		getFormulaWithBrickField(BrickField.STEPS).refreshTextField(view);

		TextView times = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_step_text_view);

		if (getFormulaWithBrickField(BrickField.STEPS).isSingleNumberFormula()) {
            try{
				times.setText(view.getResources().getQuantityString(
						hku.fyp14017.blencode.R.plurals.brick_move_n_step_plural,
						Utils.convertDoubleToPluralInteger(getFormulaWithBrickField(BrickField.STEPS).interpretDouble(
								ProjectManager.getInstance().getCurrentSprite()))
				));
            }catch(InterpretationException interpretationException){
                Log.d(getClass().getSimpleName(), "Couldn't interpret Formula.", interpretationException);
            }
		} else {

			// Random Number to get into the "other" keyword for values like 0.99 or 2.001 seconds or degrees
			// in hopefully all possible languages
			times.setText(view.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.brick_move_n_step_plural,
					Utils.TRANSLATION_PLURAL_OTHER_INTEGER));
		}

		text.setVisibility(View.GONE);
		edit.setVisibility(View.VISIBLE);
		edit.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		prototypeView = inflater.inflate(hku.fyp14017.blencode.R.layout.brick_move_n_steps, null);
		TextView textSteps = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_prototype_text_view);
		textSteps.setText(String.valueOf(BrickValues.MOVE_STEPS));
		TextView times = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_step_text_view);
        times.setText(context.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.brick_move_n_step_plural,
                Utils.convertDoubleToPluralInteger(BrickValues.MOVE_STEPS)));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView moveNStepsLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_label);
			TextView times = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_step_text_view);
			TextView moveNStepsEdit = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_move_n_steps_edit_text);
			moveNStepsLabel.setTextColor(moveNStepsLabel.getTextColors().withAlpha(alphaValue));
			times.setTextColor(times.getTextColors().withAlpha(alphaValue));
			moveNStepsEdit.setTextColor(moveNStepsEdit.getTextColors().withAlpha(alphaValue));
			moveNStepsEdit.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.STEPS));
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.moveNSteps(sprite, getFormulaWithBrickField(BrickField.STEPS)));
		return null;
	}
}

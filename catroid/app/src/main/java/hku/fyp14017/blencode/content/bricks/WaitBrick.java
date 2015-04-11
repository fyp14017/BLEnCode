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

public class WaitBrick extends FormulaBrick implements OnClickListener {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public WaitBrick() {
		addAllowedBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS);
	}

	public WaitBrick(int timeToWaitInMillisecondsValue) {
		initializeBrickFields(new Formula(timeToWaitInMillisecondsValue / 1000.0));
	}

	public WaitBrick(Formula timeToWaitInSecondsFormula) {
		initializeBrickFields(timeToWaitInSecondsFormula);
	}

	private void initializeBrickFields(Formula timeToWaitInSeconds) {
		addAllowedBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS);
		setFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS, timeToWaitInSeconds);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS).getRequiredResources();
	}

	public Formula getTimeToWait() {
		return getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS);
	}

	public void setTimeToWait(Formula timeToWaitInSeconds) {
		setFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS, timeToWaitInSeconds);
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_wait, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_wait_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView text = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_wait_prototype_text_view);
		TextView edit = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_wait_edit_text);
		getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS).setTextFieldId(hku.fyp14017.blencode.R.id.brick_wait_edit_text);
		getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS).refreshTextField(view);

		TextView times = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_wait_second_text_view);

		if (getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS).isSingleNumberFormula()) {
            try{
				times.setText(view.getResources().getQuantityString(
						hku.fyp14017.blencode.R.plurals.second_plural,
						Utils.convertDoubleToPluralInteger(getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS)
								.interpretDouble(ProjectManager.getInstance().getCurrentSprite()))
				));
            }catch(InterpretationException interpretationException){
                Log.d(getClass().getSimpleName(), "Couldn't interpret Formula.", interpretationException);
            }
		} else {

			// Random Number to get into the "other" keyword for values like 0.99 or 2.001 seconds or degrees
			// in hopefully all possible languages
			times.setText(view.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.second_plural,
					Utils.TRANSLATION_PLURAL_OTHER_INTEGER));
		}

		text.setVisibility(View.GONE);
		edit.setVisibility(View.VISIBLE);
		edit.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_wait, null);
		TextView textWait = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_wait_prototype_text_view);
		textWait.setText(String.valueOf(BrickValues.WAIT/1000));
		TextView times = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_wait_second_text_view);
        times.setText(context.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.second_plural,
                Utils.convertDoubleToPluralInteger(BrickValues.WAIT/1000)));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_wait_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textWaitLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_wait_label);
			TextView textWaitSeconds = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_wait_second_text_view);
			TextView editWait = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_wait_edit_text);

			textWaitLabel.setTextColor(textWaitLabel.getTextColors().withAlpha(alphaValue));
			textWaitSeconds.setTextColor(textWaitSeconds.getTextColors().withAlpha(alphaValue));
			editWait.setTextColor(editWait.getTextColors().withAlpha(alphaValue));
			editWait.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

    public String brickTutorial(){
        return "Allows the user to pause the running of the app by the specified amount of time in seconds.";
    }

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS));
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.delay(sprite, getFormulaWithBrickField(BrickField.TIME_TO_WAIT_IN_SECONDS)));
		return null;
	}
}

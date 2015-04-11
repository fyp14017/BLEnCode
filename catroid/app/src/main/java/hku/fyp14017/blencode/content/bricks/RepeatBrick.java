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
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.Action;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.formulaeditor.InterpretationException;

public class RepeatBrick extends FormulaBrick implements LoopBeginBrick, OnClickListener {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	protected transient LoopEndBrick loopEndBrick;
	private transient long beginLoopTime;

	private transient LoopBeginBrick copy;

	public RepeatBrick() {
		addAllowedBrickField(BrickField.TIMES_TO_REPEAT);
	}

	public RepeatBrick(int timesToRepeatValue) {
		initializeBrickFields(new Formula(timesToRepeatValue));
	}

    @Override
    public String brickTutorial(){
        return "Allows the user to repeat a set of actions for a specified number of times.";
    }

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT).getRequiredResources();
	}
	
	public RepeatBrick(Formula timesToRepeat) {
		initializeBrickFields(timesToRepeat);
	}

	private void initializeBrickFields(Formula timesToRepeat) {
		addAllowedBrickField(BrickField.TIMES_TO_REPEAT);
		setFormulaWithBrickField(BrickField.TIMES_TO_REPEAT, timesToRepeat);
	}

	@Override
	public Brick clone()  {
		return new RepeatBrick(getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT).clone());
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_repeat, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_repeat_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				checked = !checked;
				if (!checked) {
					for (Brick currentBrick : adapter.getCheckedBricks()) {
						currentBrick.setCheckedBoolean(false);
					}
				}
				adapter.handleCheck(brickInstance, checked);
			}
		});

		TextView text = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_prototype_text_view);
		TextView edit = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_edit_text);
		getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT).setTextFieldId(hku.fyp14017.blencode.R.id.brick_repeat_edit_text);
		getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT).refreshTextField(view);

		TextView times = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_time_text_view);

		if (getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT).isSingleNumberFormula()) {
            try{
				times.setText(view.getResources().getQuantityString(
						hku.fyp14017.blencode.R.plurals.time_plural,
						Utils.convertDoubleToPluralInteger(getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT)
								.interpretDouble(ProjectManager.getInstance().getCurrentSprite()))
				));
            }catch(InterpretationException interpretationException){
                Log.d(getClass().getSimpleName(), "Couldn't interpret Formula", interpretationException);
            }
		} else {

			// Random Number to get into the "other" keyword for values like 0.99 or 2.001 seconds or degrees
			// in hopefully all possible languages
			times.setText(view.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.time_plural,
					Utils.TRANSLATION_PLURAL_OTHER_INTEGER));
		}

		text.setVisibility(View.GONE);
		edit.setVisibility(View.VISIBLE);

		edit.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_repeat, null);
		TextView textRepeat = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_prototype_text_view);
		TextView times = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_time_text_view);
        textRepeat.setText(String.valueOf(BrickValues.REPEAT));
        times.setText(context.getResources().getQuantityString(hku.fyp14017.blencode.R.plurals.time_plural,
                    Utils.convertDoubleToPluralInteger(BrickValues.REPEAT)));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView repeatLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_label);
			TextView editRepeat = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_edit_text);
			TextView times = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_repeat_time_text_view);
			repeatLabel.setTextColor(repeatLabel.getTextColors().withAlpha(alphaValue));
			times.setTextColor(times.getTextColors().withAlpha(alphaValue));
			editRepeat.setTextColor(editRepeat.getTextColors().withAlpha(alphaValue));
			editRepeat.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT));
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		SequenceAction repeatSequence = ExtendedActions.sequence();
		Action action = ExtendedActions.repeat(sprite, getFormulaWithBrickField(BrickField.TIMES_TO_REPEAT),
				repeatSequence);
		sequence.addAction(action);
		LinkedList<SequenceAction> returnActionList = new LinkedList<SequenceAction>();
		returnActionList.add(repeatSequence);
		return returnActionList;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		//loopEndBrick will be set in the LoopEndBrick's copyBrickForSprite method
		RepeatBrick copyBrick = (RepeatBrick) clone();
		copy = copyBrick;
		return copyBrick;
	}

	@Override
	public long getBeginLoopTime() {
		return beginLoopTime;
	}

	@Override
	public void setBeginLoopTime(long beginLoopTime) {
		this.beginLoopTime = beginLoopTime;
	}

	@Override
	public LoopEndBrick getLoopEndBrick() {
		return loopEndBrick;
	}

	@Override
	public void setLoopEndBrick(LoopEndBrick loopEndBrick) {
		this.loopEndBrick = loopEndBrick;
	}

	@Override
	public LoopBeginBrick getCopy() {
		return copy;
	}

	@Override
	public boolean isInitialized() {
		return (loopEndBrick != null);
	}

	@Override
	public void initialize() {
		loopEndBrick = new LoopEndBrick(this);
	}

	@Override
	public boolean isDraggableOver(Brick brick) {
		return (loopEndBrick != null);
	}

	@Override
	public List<NestingBrick> getAllNestingBrickParts(boolean sorted) {
		List<NestingBrick> nestingBrickList = new ArrayList<NestingBrick>();
		nestingBrickList.add(this);
		nestingBrickList.add(loopEndBrick);

		return nestingBrickList;
	}
}

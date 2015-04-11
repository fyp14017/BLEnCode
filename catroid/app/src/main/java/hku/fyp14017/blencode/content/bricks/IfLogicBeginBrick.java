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

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.BrickValues;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hku.fyp14017.blencode.common.BrickValues;
import hku.fyp14017.blencode.formulaeditor.Formula;

public class IfLogicBeginBrick extends FormulaBrick implements NestingBrick, OnClickListener {
	private static final long serialVersionUID = 1L;
	private static final String TAG = IfLogicBeginBrick.class.getSimpleName();
	public static final int EXECUTE_ELSE_PART = -1;
	protected transient IfLogicElseBrick ifElseBrick;
	protected transient IfLogicEndBrick ifEndBrick;
	private transient IfLogicBeginBrick copy;

	public IfLogicBeginBrick() {
		addAllowedBrickField(BrickField.IF_CONDITION);
	}

	public IfLogicBeginBrick(int condition) {
		initializeBrickFields(new Formula(condition));
	}

	public IfLogicBeginBrick(Formula condition) {
		initializeBrickFields(condition);
	}

	private void initializeBrickFields(Formula ifCondition) {
		addAllowedBrickField(BrickField.IF_CONDITION);
		setFormulaWithBrickField(BrickField.IF_CONDITION, ifCondition);
	}

    @Override
    public String brickTutorial(){
        return "Allows the user to specify a condition, depending on which a particular action should take place. " +
                "\n\nHelps when the application is in a state where it faces multiple scenarios and has to choose one. " +
                "\n\nIf the first condition is true, then it skips all other conditions in the if-else-if block.";
    }

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.IF_CONDITION).getRequiredResources();
	}

	public IfLogicElseBrick getIfElseBrick() {
		return ifElseBrick;
	}

	public IfLogicEndBrick getIfEndBrick() {
		return ifEndBrick;
	}

	public IfLogicBeginBrick getCopy() {
		return copy;
	}

	public void setIfElseBrick(IfLogicElseBrick elseBrick) {
		this.ifElseBrick = elseBrick;
	}

	public void setIfEndBrick(IfLogicEndBrick ifEndBrick) {
		this.ifEndBrick = ifEndBrick;
	}

	@Override
	public Brick clone() {
		return new IfLogicBeginBrick(getFormulaWithBrickField(BrickField.IF_CONDITION).clone());
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}

		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_if_begin_if, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_if_begin_checkbox);
		final Brick brickInstance = this;

		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView prototypeTextView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_if_begin_prototype_text_view);
		TextView ifBeginTextView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_if_begin_edit_text);

		getFormulaWithBrickField(BrickField.IF_CONDITION).setTextFieldId(hku.fyp14017.blencode.R.id.brick_if_begin_edit_text);
		getFormulaWithBrickField(BrickField.IF_CONDITION).refreshTextField(view);

		prototypeTextView.setVisibility(View.GONE);
		ifBeginTextView.setVisibility(View.VISIBLE);

		ifBeginTextView.setOnClickListener(this);

		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_if_begin_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView ifLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.if_label);
			TextView ifLabelEnd = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.if_label_second_part);
			TextView editX = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_if_begin_edit_text);
			ifLabel.setTextColor(ifLabel.getTextColors().withAlpha(alphaValue));
			ifLabelEnd.setTextColor(ifLabelEnd.getTextColors().withAlpha(alphaValue));
			editX.setTextColor(editX.getTextColors().withAlpha(alphaValue));
			editX.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_if_begin_if, null);
		TextView textIfBegin = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_if_begin_prototype_text_view);
		textIfBegin.setText(String.valueOf(BrickValues.IF_CONDITION));
		return prototypeView;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.IF_CONDITION));
	}

	@Override
	public boolean isInitialized() {
		if (ifElseBrick == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void initialize() {
		ifElseBrick = new IfLogicElseBrick(this);
		ifEndBrick = new IfLogicEndBrick(ifElseBrick, this);
		Log.w(TAG, "Creating if logic stuff");
	}

	@Override
	public List<NestingBrick> getAllNestingBrickParts(boolean sorted) {
		//TODO: handle sorting
		List<NestingBrick> nestingBrickList = new ArrayList<NestingBrick>();
		if (sorted) {
			nestingBrickList.add(this);
			nestingBrickList.add(ifElseBrick);
			nestingBrickList.add(ifEndBrick);
		} else {
			nestingBrickList.add(this);
			nestingBrickList.add(ifEndBrick);
		}

		return nestingBrickList;
	}

	@Override
	public boolean isDraggableOver(Brick brick) {
		if (brick == ifElseBrick) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		SequenceAction ifAction = ExtendedActions.sequence();
		SequenceAction elseAction = ExtendedActions.sequence();
		Action action = ExtendedActions.ifLogic(sprite, getFormulaWithBrickField(BrickField.IF_CONDITION), ifAction,
				elseAction); //TODO finish!!!
		sequence.addAction(action);

		LinkedList<SequenceAction> returnActionList = new LinkedList<SequenceAction>();
		returnActionList.add(elseAction);
		returnActionList.add(ifAction);

		return returnActionList;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		//ifEndBrick and ifElseBrick will be set in the copyBrickForSprite method of IfLogicEndBrick
		IfLogicBeginBrick copyBrick = (IfLogicBeginBrick) clone(); //Using the clone method because of its flexibility if new fields are added
		copyBrick.ifElseBrick = null; //if the Formula gets a field sprite, a separate copy method will be needed
		copyBrick.ifEndBrick = null;
		this.copy = copyBrick;
		return copyBrick;
	}

}

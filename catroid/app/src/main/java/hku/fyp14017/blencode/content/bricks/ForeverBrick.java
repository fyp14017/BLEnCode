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
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ForeverBrick extends BrickBaseType implements LoopBeginBrick {
	private static final long serialVersionUID = 1L;

	protected transient LoopEndBrick loopEndBrick;
	private transient long beginLoopTime;

	private transient LoopBeginBrick copy;

	public ForeverBrick() {

	}

    @Override
    public String brickTutorial(){
        return "Allows the user to repeat a set of actions continuously throughout the running of the application.";
    }

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public Brick clone() {
		return new ForeverBrick();
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_forever, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_forever_checkbox);
		final Brick brickInstance = this;

		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				if (!checked) {
					for (Brick currentBrick : adapter.getCheckedBricks()) {
						currentBrick.setCheckedBoolean(false);
					}
				}
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_forever_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView ifForeverLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_forever_label);
			ifForeverLabel.setTextColor(ifForeverLabel.getTextColors().withAlpha(alphaValue));

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		return View.inflate(context, hku.fyp14017.blencode.R.layout.brick_forever, null);
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		SequenceAction foreverSequence = ExtendedActions.sequence();
		Action action = ExtendedActions.forever(sprite, foreverSequence);
		sequence.addAction(action);
		LinkedList<SequenceAction> returnActionList = new LinkedList<SequenceAction>();
		returnActionList.add(foreverSequence);
		return returnActionList;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		//loopEndBrick will be set in the LoopEndBrick's copyBrickForSprite method
		ForeverBrick copyBrick = (ForeverBrick) clone();
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
		loopEndBrick = new LoopEndlessBrick(this);
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

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
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;

import java.util.List;

public class DroneLandBrick extends BrickBaseType {
	private static final long serialVersionUID = 1L;

	private transient AdapterView<?> adapterView;

	public DroneLandBrick() {

	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		DroneLandBrick copyBrick = (DroneLandBrick) clone();
		return copyBrick;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_drone_land, null);

		return prototypeView;
	}

	@Override
	public Brick clone() {
		return new DroneLandBrick();
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}
		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_drone_land, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_drone_land_checkbox);
		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {
		if (view != null) {
			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_drone_land);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textLegoMotorStopLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.ValueTextView);
			textLegoMotorStopLabel.setTextColor(textLegoMotorStopLabel.getTextColors().withAlpha(alphaValue));
			ColorStateList color = textLegoMotorStopLabel.getTextColors().withAlpha(alphaValue);
			if (adapterView != null) {
				((TextView) adapterView.getChildAt(0)).setTextColor(color);
			}
			this.alphaValue = (alphaValue);
		}
		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.droneTakeOff()); // same command
		return null;
	}

	@Override
	public int getRequiredResources() {
		return super.getRequiredResources() | Brick.ARDRONE_SUPPORT;
	}
}

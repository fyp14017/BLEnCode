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

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;

import java.util.List;

public class StopAllSoundsBrick extends BrickBaseType {
	private static final long serialVersionUID = 1L;

	public StopAllSoundsBrick() {

	}

    @Override
    public String brickTutorial(){
        return "Used to stop all the sounds that had started playing by using “Play Sound”.";
    }

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_stop_all_sounds, null);
		view = getViewWithAlpha(alphaValue);
		setCheckboxView(hku.fyp14017.blencode.R.id.brick_stop_all_sounds_checkbox);

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
	public Brick copyBrickForSprite(Sprite sprite) {
		StopAllSoundsBrick copyBrick = (StopAllSoundsBrick) clone();
		return copyBrick;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_stop_all_sounds_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);
			this.alphaValue = (alphaValue);

			TextView textStop = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_stop_all_sounds_label);
			textStop.setTextColor(textStop.getTextColors().withAlpha(alphaValue));

		}

		return view;
	}

	@Override
	public Brick clone() {
		return new StopAllSoundsBrick();
	}

	@Override
	public View getPrototypeView(Context context) {
		return View.inflate(context, hku.fyp14017.blencode.R.layout.brick_stop_all_sounds, null);
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.stopAllSounds());
		return null;
	}

}

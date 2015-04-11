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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;

import java.util.List;

import hku.fyp14017.blencode.ProjectManager;

public class NextLookBrick extends BrickBaseType {

	private static final long serialVersionUID = 1L;

	public NextLookBrick() {

	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		NextLookBrick copyBrick = (NextLookBrick) clone();
		return copyBrick;
	}

	@Override
	public View getPrototypeView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(hku.fyp14017.blencode.R.layout.brick_next_look, null);

		if (ProjectManager.getInstance().getCurrentSprite().getName().equals(context.getString(hku.fyp14017.blencode.R.string.background))) {
			TextView textField = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_next_look_text_view);
			textField.setText(hku.fyp14017.blencode.R.string.brick_next_background);
		}
		return view;
	}

	@Override
	public Brick clone() {
		return new NextLookBrick();
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_next_look, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_next_look_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		if (ProjectManager.getInstance().getCurrentSprite().getName().equals(context.getString(hku.fyp14017.blencode.R.string.background))) {
			TextView textField = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_next_look_text_view);
			textField.setText(hku.fyp14017.blencode.R.string.brick_next_background);
		}

		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_next_look_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView nextLookLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_next_look_text_view);
			nextLookLabel.setTextColor(nextLookLabel.getTextColors().withAlpha(alphaValue));

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.nextLook(sprite));
		return null;
	}
}

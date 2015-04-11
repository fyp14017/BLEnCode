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
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;
import hku.fyp14017.blencode.formulaeditor.Formula;

import java.util.List;

import hku.fyp14017.blencode.formulaeditor.Formula;

public class DroneTurnRightMagnetoBrick extends DroneMoveBrick {

	private static final long serialVersionUID = 1L;

	public DroneTurnRightMagnetoBrick(int durationInMilliseconds, int powerInPercent) {
		super(durationInMilliseconds, powerInPercent);
	}

	public DroneTurnRightMagnetoBrick(Formula durationInMilliseconds, Formula powerInPercent) {
		super(durationInMilliseconds, powerInPercent);
	}

	public DroneTurnRightMagnetoBrick() {
		super();
	}

	@Override
	protected String getBrickLabel(View view) {
		return view.getResources().getString(hku.fyp14017.blencode.R.string.brick_drone_turn_right_magneto);
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.droneTurnRightMagneto(sprite,
				getFormulaWithBrickField(BrickField.DRONE_TIME_TO_FLY_IN_SECONDS),
				getFormulaWithBrickField(BrickField.DRONE_POWER_IN_PERCENT)));
		return null;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		View brickView = super.getView(context, brickId, baseAdapter);
		TextView editTextView = (TextView) brickView.findViewById(hku.fyp14017.blencode.R.id.brick_drone_move_text_view_power);
		editTextView.setText(hku.fyp14017.blencode.R.string.brick_drone_angle);
		return brickView;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = super.getPrototypeView(context);
		TextView textView = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_drone_move_text_view_power);
		textView.setText(hku.fyp14017.blencode.R.string.brick_drone_angle);
		return prototypeView;
	}
}

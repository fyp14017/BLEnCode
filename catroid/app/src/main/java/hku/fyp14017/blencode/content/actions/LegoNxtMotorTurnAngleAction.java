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
package hku.fyp14017.blencode.content.actions;

import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorTurnAngleBrick.Motor;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.InterpretationException;
import hku.fyp14017.blencode.legonxt.LegoNXT;
import hku.fyp14017.blencode.formulaeditor.InterpretationException;
import hku.fyp14017.blencode.legonxt.LegoNXT;

public class LegoNxtMotorTurnAngleAction extends TemporalAction {

	private static final int NO_DELAY = 0;
	private Motor motorEnum;
	private Formula degrees;
	private Sprite sprite;

	@Override
	protected void update(float percent) {
		int degreesValue;
		try {
			degreesValue = degrees.interpretInteger(sprite);
        } catch (InterpretationException interpretationException) {
            degreesValue = 0;
            Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
        }

		int tmpAngle = degreesValue;
		int direction = 1;
		if (degreesValue < 0) {
			direction = -1;
			tmpAngle = degreesValue + (-2 * degreesValue);
		}

		if (motorEnum.equals(Motor.MOTOR_A_C)) {
			LegoNXT.sendBTCMotorMessage(NO_DELAY, Motor.MOTOR_A.ordinal(), -1 * direction * 30, tmpAngle);
			LegoNXT.sendBTCMotorMessage(NO_DELAY, Motor.MOTOR_C.ordinal(), direction * 30, tmpAngle);
		} else {
			LegoNXT.sendBTCMotorMessage(NO_DELAY, motorEnum.ordinal(), direction * 30, tmpAngle);
		}

		/*
		 * if (inverse == false) {
		 * LegoNXT.sendBTCMotorMessage(NO_DELAY, motor, 30, angle);
		 * } else {
		 * LegoNXT.sendBTCMotorMessage(NO_DELAY, motor, -30, angle);
		 * }
		 */
	}

	public void setMotorEnum(Motor motorEnum) {
		this.motorEnum = motorEnum;
	}

	public void setDegrees(Formula degrees) {
		this.degrees = degrees;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}

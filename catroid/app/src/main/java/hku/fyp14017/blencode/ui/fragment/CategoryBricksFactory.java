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
package hku.fyp14017.blencode.ui.fragment;

import android.content.Context;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.ble.BLECard;
import hku.fyp14017.blencode.common.BrickValues;
import hku.fyp14017.blencode.common.MessageContainer;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.bricks.Brick;
import hku.fyp14017.blencode.content.bricks.BroadcastBrick;
import hku.fyp14017.blencode.content.bricks.BroadcastReceiverBrick;
import hku.fyp14017.blencode.content.bricks.BroadcastWaitBrick;
import hku.fyp14017.blencode.content.bricks.CardBuzzerBrick;
import hku.fyp14017.blencode.content.bricks.CardLedBrick;
import hku.fyp14017.blencode.content.bricks.ChangeBrightnessByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeGhostEffectByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeSizeByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeVariableBrick;
import hku.fyp14017.blencode.content.bricks.ChangeVolumeByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeXByNBrick;
import hku.fyp14017.blencode.content.bricks.ChangeYByNBrick;
import hku.fyp14017.blencode.content.bricks.ClearGraphicEffectBrick;
import hku.fyp14017.blencode.content.bricks.ComeToFrontBrick;
import hku.fyp14017.blencode.content.bricks.ConnectCardBrick;
import hku.fyp14017.blencode.content.bricks.ConnectSensorTagBrick;
import hku.fyp14017.blencode.content.bricks.DroneFlipBrick;
import hku.fyp14017.blencode.content.bricks.DroneLandBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveBackwardBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveDownBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveForwardBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveLeftBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveRightBrick;
import hku.fyp14017.blencode.content.bricks.DroneMoveUpBrick;
import hku.fyp14017.blencode.content.bricks.DroneTakeOffBrick;
import hku.fyp14017.blencode.content.bricks.DroneTurnLeftBrick;
import hku.fyp14017.blencode.content.bricks.DroneTurnRightBrick;
import hku.fyp14017.blencode.content.bricks.ForeverBrick;
import hku.fyp14017.blencode.content.bricks.GlideToBrick;
import hku.fyp14017.blencode.content.bricks.GoNStepsBackBrick;
import hku.fyp14017.blencode.content.bricks.HideBrick;
import hku.fyp14017.blencode.content.bricks.IfLogicBeginBrick;
import hku.fyp14017.blencode.content.bricks.IfOnEdgeBounceBrick;
import hku.fyp14017.blencode.content.bricks.LedOffBrick;
import hku.fyp14017.blencode.content.bricks.LedOnBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorActionBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorStopBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtMotorTurnAngleBrick;
import hku.fyp14017.blencode.content.bricks.LegoNxtPlayToneBrick;
import hku.fyp14017.blencode.content.bricks.MonitorSensorBrick;
import hku.fyp14017.blencode.content.bricks.MoveNStepsBrick;
import hku.fyp14017.blencode.content.bricks.NextLookBrick;
import hku.fyp14017.blencode.content.bricks.NoteBrick;
import hku.fyp14017.blencode.content.bricks.PlaceAtBrick;
import hku.fyp14017.blencode.content.bricks.PlaySoundBrick;
import hku.fyp14017.blencode.content.bricks.PointInDirectionBrick;
import hku.fyp14017.blencode.content.bricks.PointInDirectionBrick.Direction;
import hku.fyp14017.blencode.content.bricks.PointToBrick;
import hku.fyp14017.blencode.content.bricks.ProximityBrick;
import hku.fyp14017.blencode.content.bricks.RepeatBrick;
import hku.fyp14017.blencode.content.bricks.ScriptBrick;
import hku.fyp14017.blencode.content.bricks.SetBrightnessBrick;
import hku.fyp14017.blencode.content.bricks.SetGhostEffectBrick;
import hku.fyp14017.blencode.content.bricks.SetLookBrick;
import hku.fyp14017.blencode.content.bricks.SetSizeToBrick;
import hku.fyp14017.blencode.content.bricks.SetVariableBrick;
import hku.fyp14017.blencode.content.bricks.SetVolumeToBrick;
import hku.fyp14017.blencode.content.bricks.SetXBrick;
import hku.fyp14017.blencode.content.bricks.SetYBrick;
import hku.fyp14017.blencode.content.bricks.ShowBrick;
import hku.fyp14017.blencode.content.bricks.SpeakBrick;
import hku.fyp14017.blencode.content.bricks.StopAllSoundsBrick;
import hku.fyp14017.blencode.content.bricks.TurnLeftBrick;
import hku.fyp14017.blencode.content.bricks.TurnRightBrick;
import hku.fyp14017.blencode.content.bricks.UserBrick;
import hku.fyp14017.blencode.content.bricks.VibrationBrick;
import hku.fyp14017.blencode.content.bricks.WaitBrick;
import hku.fyp14017.blencode.content.bricks.WhenBrick;
import hku.fyp14017.blencode.content.bricks.WhenStartedBrick;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.FormulaElement;
import hku.fyp14017.blencode.formulaeditor.FormulaElement.ElementType;
import hku.fyp14017.blencode.formulaeditor.Operators;
import hku.fyp14017.blencode.ui.UserBrickScriptActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.MessageContainer;
import hku.fyp14017.blencode.content.bricks.ChangeYByNBrick;
import hku.fyp14017.blencode.content.bricks.ComeToFrontBrick;
import hku.fyp14017.blencode.content.bricks.LedOnBrick;
import hku.fyp14017.blencode.content.bricks.ScriptBrick;
import hku.fyp14017.blencode.content.bricks.StopAllSoundsBrick;
import hku.fyp14017.blencode.content.bricks.WaitBrick;

public class CategoryBricksFactory {

	public List<Brick> getBricks(String category, Sprite sprite, Context context) {
		UserBrickScriptActivity activity;
		try {
			activity = (UserBrickScriptActivity) context;
		} catch (ClassCastException e) {
			activity = null;
		}
		boolean isUserScriptMode = activity != null;
		List<Brick> tempList = new LinkedList<Brick>();
		List<Brick> toReturn = new ArrayList<Brick>();
		if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_control))) {
			tempList = setupControlCategoryList(context);
		} else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_motion))) {
			tempList = setupMotionCategoryList(sprite);
		} else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_sound))) {
			tempList = setupSoundCategoryList();
		} else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_looks))) {
			tempList = setupLooksCategoryList();
		} else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_variables))) {
			tempList = setupVariablesCategoryList();
		}else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_ble_sensors))) {
            return setupBLESensorsCategoryList(sprite);
        }else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_user_bricks))) {
			tempList = setupUserBricksCategoryList();
		} else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_lego_nxt))) {
			tempList = setupLegoNxtCategoryList();
		} else if (category.equals(context.getString(hku.fyp14017.blencode.R.string.category_drone))) {
			tempList = setupDroneCategoryList();
		}
		for (Brick brick : tempList) {
			ScriptBrick brickAsScriptBrick;
			try {
				brickAsScriptBrick = (ScriptBrick) brick;
			} catch (ClassCastException e) {
				brickAsScriptBrick = null;
			}
			if (!isUserScriptMode || brickAsScriptBrick == null) {
				toReturn.add(brick);
			}
		}
		return toReturn;
	}


	private List<Brick> setupControlCategoryList(Context context) {
		List<Brick> controlBrickList = new ArrayList<Brick>();
		controlBrickList.add(new WhenStartedBrick(null));
		controlBrickList.add(new WhenBrick(WhenBrick.ScriptAction.TAPPED, WhenBrick.Keys.LEFT_BUTTON, WhenBrick.SensorTag.TAG1, "0"));
		controlBrickList.add(new WaitBrick(BrickValues.WAIT));

		final String broadcastMessage = MessageContainer.getFirst(context);
		controlBrickList.add(new BroadcastReceiverBrick(broadcastMessage));
		controlBrickList.add(new BroadcastBrick(broadcastMessage));
		controlBrickList.add(new BroadcastWaitBrick(broadcastMessage));

		controlBrickList.add(new NoteBrick(context.getString(hku.fyp14017.blencode.R.string.brick_note_default_value)));
		controlBrickList.add(new ForeverBrick());
		controlBrickList.add(new IfLogicBeginBrick(0));
		controlBrickList.add(new RepeatBrick(BrickValues.REPEAT));

		return controlBrickList;
	}

	private List<Brick> setupUserBricksCategoryList() {
		List<UserBrick> userBrickList = ProjectManager.getInstance().getCurrentSprite().getUserBrickList();
		ArrayList<Brick> newList = new ArrayList<Brick>();

//		UserBrick userBrickWeAreAddingTo = ProjectManager.getInstance().getCurrentUserBrick();
//		if (userBrickWeAreAddingTo != null) {
//			// Maintain a Directed Acyclic Graph of UserBrick call order: Don't allow cycles.
//			for (UserBrick brick : userBrickList) {
//				if (!checkForCycle(brick, userBrickWeAreAddingTo)) {
//					newList.add(brick);

//				}
//			}
//		} else {
		if (userBrickList != null) {
			for (UserBrick brick : userBrickList) {
				newList.add(brick);
			}
		}
//		}
		return newList;
	}

//	public boolean checkForCycle(UserBrick currentBrick, UserBrick parentBrick) {
//		if (parentBrick.getId() == currentBrick.getId()) {
//			return true;
//		}
//
//		for (Brick childBrick : currentBrick.getDefinitionBrick().getUserScript().getBrickList()) {
//			if (childBrick instanceof UserBrick && checkForCycle(((UserBrick) childBrick), parentBrick)) {
//				return true;
//			}
//		}
//
//		return false;
//	}

	private List<Brick> setupMotionCategoryList(Sprite sprite) {
		List<Brick> motionBrickList = new ArrayList<Brick>();
		motionBrickList.add(new PlaceAtBrick(BrickValues.X_POSITION, BrickValues.Y_POSITION));
		motionBrickList.add(new SetXBrick(BrickValues.X_POSITION));
		motionBrickList.add(new SetYBrick(BrickValues.Y_POSITION));
		motionBrickList.add(new ChangeXByNBrick(BrickValues.CHANGE_X_BY));
		motionBrickList.add(new ChangeYByNBrick(BrickValues.CHANGE_Y_BY));

		if (!isBackground(sprite)) {
			motionBrickList.add(new IfOnEdgeBounceBrick());
		}

		motionBrickList.add(new MoveNStepsBrick(BrickValues.MOVE_STEPS));
		motionBrickList.add(new TurnLeftBrick(BrickValues.TURN_DEGREES));
		motionBrickList.add(new TurnRightBrick(BrickValues.TURN_DEGREES));
		motionBrickList.add(new PointInDirectionBrick(Direction.RIGHT));
		motionBrickList.add(new PointToBrick(null));
		motionBrickList.add(new GlideToBrick(BrickValues.X_POSITION, BrickValues.Y_POSITION,
				BrickValues.GLIDE_SECONDS));

		if (!isBackground(sprite)) {
			motionBrickList.add(new GoNStepsBackBrick(BrickValues.GO_BACK));
			motionBrickList.add(new ComeToFrontBrick());
		}
		motionBrickList.add(new VibrationBrick(BrickValues.VIBRATE_MILLISECONDS));

		return motionBrickList;
	}

	private List<Brick> setupSoundCategoryList() {
		List<Brick> soundBrickList = new ArrayList<Brick>();
		soundBrickList.add(new PlaySoundBrick());
		soundBrickList.add(new StopAllSoundsBrick());
		soundBrickList.add(new SetVolumeToBrick(BrickValues.SET_VOLUME_TO));

		// workaround to set a negative default value for a Brick
		float positiveDefaultValueChangeVolumeBy = Math.abs(BrickValues.CHANGE_VOLUME_BY);
		FormulaElement defaultValueChangeVolumeBy = new FormulaElement(ElementType.OPERATOR, Operators.MINUS.name(),
				null, null, new FormulaElement(ElementType.NUMBER, String.valueOf(positiveDefaultValueChangeVolumeBy),
						null));
		soundBrickList.add(new ChangeVolumeByNBrick(new Formula(defaultValueChangeVolumeBy)));

		soundBrickList.add(new SpeakBrick(BrickValues.SPEAK));

		return soundBrickList;
	}

    private List<Brick> setupBLESensorsCategoryList(Sprite sprite) {
        List<Brick> BLESensorsBrickList = new ArrayList<Brick>();

        BLESensorsBrickList.add(new ConnectSensorTagBrick(sprite, ConnectSensorTagBrick.SensorTag.TAG1));
        BLESensorsBrickList.add(new MonitorSensorBrick(sprite, MonitorSensorBrick.Sensor.AMBIENT_TEMPERATURE, MonitorSensorBrick.SensorTag.TAG1));
        BLESensorsBrickList.add(new ConnectCardBrick(sprite,BLECard.CARD1));
        BLESensorsBrickList.add(new CardBuzzerBrick(sprite, 1, BLECard.CARD1));
        BLESensorsBrickList.add(new CardLedBrick(sprite,1,1,1,1, BLECard.CARD1));
        BLESensorsBrickList.add(new ProximityBrick(sprite));
        return BLESensorsBrickList;
    }

	private List<Brick> setupLooksCategoryList() {
		List<Brick> looksBrickList = new ArrayList<Brick>();
		looksBrickList.add(new SetLookBrick());
		looksBrickList.add(new NextLookBrick());
		looksBrickList.add(new SetSizeToBrick(BrickValues.SET_SIZE_TO));
		looksBrickList.add(new ChangeSizeByNBrick(BrickValues.CHANGE_SIZE_BY));
		looksBrickList.add(new HideBrick());
		looksBrickList.add(new ShowBrick());
		looksBrickList.add(new SetGhostEffectBrick(BrickValues.SET_GHOST_EFFECT));
		looksBrickList.add(new ChangeGhostEffectByNBrick(BrickValues.CHANGE_GHOST_EFFECT));
		looksBrickList.add(new SetBrightnessBrick(BrickValues.SET_BRIGHTNESS_TO));
		looksBrickList.add(new ChangeBrightnessByNBrick(BrickValues.CHANGE_BRITHNESS_BY));
		looksBrickList.add(new ClearGraphicEffectBrick());
	    looksBrickList.add(new LedOffBrick());
		looksBrickList.add(new LedOnBrick());
		return looksBrickList;
	}

	private List<Brick> setupVariablesCategoryList() {
		List<Brick> userVariablesBrickList = new ArrayList<Brick>();
		userVariablesBrickList.add(new SetVariableBrick(0));
		userVariablesBrickList.add(new ChangeVariableBrick(0));
		return userVariablesBrickList;
	}

	private List<Brick> setupLegoNxtCategoryList() {
		List<Brick> legoNXTBrickList = new ArrayList<Brick>();
		legoNXTBrickList.add(new LegoNxtMotorTurnAngleBrick(LegoNxtMotorTurnAngleBrick.Motor.MOTOR_A,
				BrickValues.LEGO_ANGLE));
		legoNXTBrickList.add(new LegoNxtMotorStopBrick(LegoNxtMotorStopBrick.Motor.MOTOR_A));
		legoNXTBrickList.add(new LegoNxtMotorActionBrick(LegoNxtMotorActionBrick.Motor.MOTOR_A,
				BrickValues.LEGO_SPEED));
		legoNXTBrickList.add(new LegoNxtPlayToneBrick(BrickValues.LEGO_FREQUENCY, BrickValues.LEGO_DURATION));

		return legoNXTBrickList;
	}

	private List<Brick> setupDroneCategoryList() {
		List<Brick> droneBrickList = new ArrayList<Brick>();
		droneBrickList.add(new DroneTakeOffBrick());
		droneBrickList.add(new DroneLandBrick());
		droneBrickList.add(new DroneFlipBrick());
		droneBrickList.add(new DroneMoveUpBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));
		droneBrickList.add(new DroneMoveDownBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));
		droneBrickList.add(new DroneMoveLeftBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));
		droneBrickList.add(new DroneMoveRightBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));
		droneBrickList.add(new DroneMoveForwardBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));
		droneBrickList.add(new DroneMoveBackwardBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));
		droneBrickList.add(new DroneTurnLeftBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));
		droneBrickList.add(new DroneTurnRightBrick(BrickValues.DRONE_MOVE_BRICK_DEFAULT_TIME_MILLISECONDS, (int) (BrickValues.DRONE_MOVE_BRICK_DEFAULT_MOVE_POWER_PERCENT * 100)));

		return droneBrickList;
	}

	private boolean isBackground(Sprite sprite) {
		if (ProjectManager.getInstance().getCurrentProject().getSpriteList().indexOf(sprite) == 0) {
			return true;
		}
		return false;
	}
}

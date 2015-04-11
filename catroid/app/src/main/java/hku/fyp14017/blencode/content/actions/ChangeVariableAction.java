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

import com.badlogic.gdx.scenes.scene2d.Action;

import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.UserVariable;

import hku.fyp14017.blencode.formulaeditor.Formula;

public class ChangeVariableAction extends Action {

	private Sprite sprite;
	private Formula changeVariable;
	private UserVariable userVariable;

	@Override
	public boolean act(float delta) {
		if (userVariable == null) {
			return true;
		}
		Object originalValue = userVariable.getValue();
		Object value = changeVariable == null ? 0d : changeVariable.interpretObject(sprite);

		if (originalValue instanceof String || value instanceof String) {
			return true;
		}

		if (originalValue instanceof Double && value instanceof Double) {
			originalValue = ((Double) originalValue).isNaN() ? 0d : originalValue;
			value = ((Double) value).isNaN() ? 0d : value;
			userVariable.setValue(((Double) originalValue) + ((Double) value));
			return true;
		}
		return true;
	}

	public void setUserVariable(UserVariable userVariable) {
		this.userVariable = userVariable;
	}

	public void setChangeVariable(Formula changeVariable) {
		this.changeVariable = changeVariable;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}

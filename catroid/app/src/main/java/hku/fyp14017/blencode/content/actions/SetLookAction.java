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

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import hku.fyp14017.blencode.common.LookData;
import hku.fyp14017.blencode.content.Sprite;

import hku.fyp14017.blencode.common.LookData;

public class SetLookAction extends TemporalAction {

	private LookData look;
	private Sprite sprite;

	@Override
	protected void update(float percent) {
		if (look != null && sprite != null && sprite.getLookDataList().contains(look)) {
			sprite.look.setLookData(look);
		}
	}

	@Override
	public void reset() {
		super.reset();
		sprite = null;
		look = null;
	}

	public void setLookData(LookData look) {
		this.look = look;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}

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

import hku.fyp14017.blencode.formulaeditor.Formula;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentFormulaHashMap extends ConcurrentHashMap<Brick.BrickField, Formula> implements Cloneable {

	private static final long serialVersionUID = 9030965461744658052L;

	@Override
	protected ConcurrentFormulaHashMap clone() throws CloneNotSupportedException {
		ConcurrentFormulaHashMap copiedMap = new ConcurrentFormulaHashMap();
		for (Brick.BrickField key : keySet()) {
			Formula value = get(key);
			if (value != null) {
				copiedMap.putIfAbsent(key, value.clone());
			}
		}
		return copiedMap;
	}

}

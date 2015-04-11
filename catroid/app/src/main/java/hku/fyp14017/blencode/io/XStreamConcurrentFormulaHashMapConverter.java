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
package hku.fyp14017.blencode.io;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import hku.fyp14017.blencode.content.bricks.Brick;
import hku.fyp14017.blencode.content.bricks.ConcurrentFormulaHashMap;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.FormulaElement;

import hku.fyp14017.blencode.content.bricks.Brick;
import hku.fyp14017.blencode.content.bricks.ConcurrentFormulaHashMap;
import hku.fyp14017.blencode.formulaeditor.Formula;

public class XStreamConcurrentFormulaHashMapConverter implements Converter {

	private static final String FORMULA = "formula";
	private static final String CATEGORY = "category";

	@Override
	public boolean canConvert(Class type) {
		return type.equals(ConcurrentFormulaHashMap.class);
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter hierarchicalStreamWriter,
			MarshallingContext marshallingContext) {
		ConcurrentFormulaHashMap concurrentFormulaHashMap = (ConcurrentFormulaHashMap) object;
		for (Brick.BrickField brickField : concurrentFormulaHashMap.keySet()) {
			hierarchicalStreamWriter.startNode(FORMULA);
			hierarchicalStreamWriter.addAttribute(CATEGORY, brickField.toString());
			marshallingContext.convertAnother(concurrentFormulaHashMap.get(brickField).getRoot());
			hierarchicalStreamWriter.endNode();
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
		ConcurrentFormulaHashMap concurrentFormulaHashMap = new ConcurrentFormulaHashMap();
		while (hierarchicalStreamReader.hasMoreChildren()) {
			hierarchicalStreamReader.moveDown();
			Brick.BrickField brickField = Brick.BrickField.valueOf(hierarchicalStreamReader.getAttribute(CATEGORY));
			Formula formula;
			if (FORMULA.equals(hierarchicalStreamReader.getNodeName())) {
				FormulaElement rootFormula = (FormulaElement) unmarshallingContext.convertAnother(concurrentFormulaHashMap,
						FormulaElement.class);
				formula = new Formula(rootFormula);
			} else {
				formula = new Formula(0);
			}
			hierarchicalStreamReader.moveUp();

			concurrentFormulaHashMap.putIfAbsent(brickField, formula);
		}
		return concurrentFormulaHashMap;
	}
}

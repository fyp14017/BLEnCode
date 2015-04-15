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

import android.util.Log;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import hku.fyp14017.blencode.content.Script;
import hku.fyp14017.blencode.content.Script;

public class XStreamScriptConverter extends ReflectionConverter {

	private static final String TAG = XStreamScriptConverter.class.getSimpleName();
	private static final String SCRIPTS_PACKAGE_NAME = "hku.fyp14017.blencode.content";
	private static final String TYPE = "type";

	public XStreamScriptConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
	}

	@Override
	public boolean canConvert(Class type) {
		return Script.class.isAssignableFrom(type);
	}

	@Override
	protected void doMarshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.addAttribute(TYPE, source.getClass().getSimpleName());
		super.doMarshal(source, writer, context);
	}

	@Override
	public Object doUnmarshal(Object result, HierarchicalStreamReader reader, UnmarshallingContext context) {
		String type = reader.getAttribute(TYPE);
		if (type != null) {
			try {
				Class cls = Class.forName(SCRIPTS_PACKAGE_NAME + "." + type);
				Script script = (Script) reflectionProvider.newInstance(cls);
				return super.doUnmarshal(script, reader, context);
			} catch (ClassNotFoundException exception) {
				Log.e(TAG, "Script class not found : " + result.toString(), exception);
			}
		}
		return super.doUnmarshal(result, reader, context);
	}

}
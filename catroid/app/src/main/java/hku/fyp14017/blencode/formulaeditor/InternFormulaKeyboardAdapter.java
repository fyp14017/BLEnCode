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
package hku.fyp14017.blencode.formulaeditor;

import hku.fyp14017.blencode.R;

import java.util.LinkedList;
import java.util.List;

public class InternFormulaKeyboardAdapter {

	public List<InternToken> createInternTokenListByResourceId(int resource, String name) {

		//USER VARIABLES
		if ((resource == 0) && !name.isEmpty()) {
			return buildUserVariable(name);
		}

		//STRING
		if ((resource == hku.fyp14017.blencode.R.id.formula_editor_keyboard_string)) {
			return buildString(name);
		}

		switch (resource) {
		// NUMBER:
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_0:
				return buildNumber("0");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_1:
				return buildNumber("1");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_2:
				return buildNumber("2");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_3:
				return buildNumber("3");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_4:
				return buildNumber("4");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_5:
				return buildNumber("5");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_6:
				return buildNumber("6");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_7:
				return buildNumber("7");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_8:
				return buildNumber("8");
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_9:
				return buildNumber("9");

				//FUNCTIONS:
			case hku.fyp14017.blencode.R.string.formula_editor_function_sin:
				return buildSingleParameterFunction(Functions.SIN, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_cos:
				return buildSingleParameterFunction(Functions.COS, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_tan:
				return buildSingleParameterFunction(Functions.TAN, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_ln:
				return buildSingleParameterFunction(Functions.LN, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_log:
				return buildSingleParameterFunction(Functions.LOG, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_pi:
				return buildFunctionWithoutParametersAndBrackets(Functions.PI);
			case hku.fyp14017.blencode.R.string.formula_editor_function_sqrt:
				return buildSingleParameterFunction(Functions.SQRT, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_rand:
				return buildDoubleParameterFunction(Functions.RAND, InternTokenType.NUMBER, "0",
						InternTokenType.NUMBER, "1");
			case hku.fyp14017.blencode.R.string.formula_editor_function_abs:
				return buildSingleParameterFunction(Functions.ABS, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_round:
				return buildSingleParameterFunction(Functions.ROUND, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_mod:
				return buildDoubleParameterFunction(Functions.MOD, InternTokenType.NUMBER, "1", InternTokenType.NUMBER,
						"1");
			case hku.fyp14017.blencode.R.string.formula_editor_function_arcsin:
				return buildSingleParameterFunction(Functions.ARCSIN, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_arccos:
				return buildSingleParameterFunction(Functions.ARCCOS, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_arctan:
				return buildSingleParameterFunction(Functions.ARCTAN, InternTokenType.NUMBER, "0");
			case hku.fyp14017.blencode.R.string.formula_editor_function_exp:
				return buildSingleParameterFunction(Functions.EXP, InternTokenType.NUMBER, "1");
			case hku.fyp14017.blencode.R.string.formula_editor_function_max:
				return buildDoubleParameterFunction(Functions.MAX, InternTokenType.NUMBER, "0", InternTokenType.NUMBER,
						"1");
			case hku.fyp14017.blencode.R.string.formula_editor_function_min:
				return buildDoubleParameterFunction(Functions.MIN, InternTokenType.NUMBER, "0", InternTokenType.NUMBER,
						"1");
			case hku.fyp14017.blencode.R.string.formula_editor_function_true:
				return buildFunctionWithoutParametersAndBrackets(Functions.TRUE);
			case hku.fyp14017.blencode.R.string.formula_editor_function_false:
				return buildFunctionWithoutParametersAndBrackets(Functions.FALSE);
			case hku.fyp14017.blencode.R.string.formula_editor_function_letter:
				return buildDoubleParameterFunction(Functions.LETTER, InternTokenType.NUMBER, "1",
						InternTokenType.STRING, "hello world");
			case hku.fyp14017.blencode.R.string.formula_editor_function_length:
				return buildSingleParameterFunction(Functions.LENGTH, InternTokenType.STRING, "hello world");
			case hku.fyp14017.blencode.R.string.formula_editor_function_join:
				return buildDoubleParameterFunction(Functions.JOIN, InternTokenType.STRING, "hello",
						InternTokenType.STRING, " world");

				//SENSOR

			case hku.fyp14017.blencode.R.string.formula_editor_sensor_x_acceleration:
				return buildSensor(Sensors.X_ACCELERATION);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_y_acceleration:
				return buildSensor(Sensors.Y_ACCELERATION);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_z_acceleration:
				return buildSensor(Sensors.Z_ACCELERATION);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_compass_direction:
				return buildSensor(Sensors.COMPASS_DIRECTION);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_x_inclination:
				return buildSensor(Sensors.X_INCLINATION);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_y_inclination:
				return buildSensor(Sensors.Y_INCLINATION);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_loudness:
				return buildSensor(Sensors.LOUDNESS);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_face_detected:
				return buildSensor(Sensors.FACE_DETECTED);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_face_size:
				return buildSensor(Sensors.FACE_SIZE);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_face_x_position:
				return buildSensor(Sensors.FACE_X_POSITION);
			case hku.fyp14017.blencode.R.string.formula_editor_sensor_face_y_position:
				return buildSensor(Sensors.FACE_Y_POSITION);
            case hku.fyp14017.blencode.R.string.ir_temperature:
                return buildSensor(Sensors.IR_TEMPERATURE, name);
            case hku.fyp14017.blencode.R.string.sensor_temperature:
                return buildSensor(Sensors.SENSOR_TAG_TEMPERATURE , name);
            case hku.fyp14017.blencode.R.string.sensor_accelerometer_abs:
                return buildSensor(Sensors.ACCELEROMETER_ABS, name);
            case hku.fyp14017.blencode.R.string.sensor_accelerometer_x:
                return buildSensor(Sensors.ACCELEROMETER_X, name);
            case hku.fyp14017.blencode.R.string.sensor_accelerometer_y:
                return buildSensor(Sensors.ACCELEROMETER_Y, name);
            case hku.fyp14017.blencode.R.string.sensor_accelerometer_z:
                return buildSensor(Sensors.ACCELEROMETER_Z, name);
            case hku.fyp14017.blencode.R.string.sensor_gyroscope_x:
                return buildSensor(Sensors.GYROSCOPE_X, name);
            case hku.fyp14017.blencode.R.string.sensor_gyroscope_y:
                return buildSensor(Sensors.GYROSCOPE_Y, name);
            case hku.fyp14017.blencode.R.string.sensor_gyroscope_z:
                return buildSensor(Sensors.GYROSCOPE_Z, name);
            case hku.fyp14017.blencode.R.string.sensor_magnetometer_abs:
                return buildSensor(Sensors.MAGNETOMETER_ABS, name);
            case hku.fyp14017.blencode.R.string.sensor_magnetometer_x:
                return buildSensor(Sensors.MAGNETOMETER_X, name);
            case hku.fyp14017.blencode.R.string.sensor_magnetometer_y:
                return buildSensor(Sensors.MAGNETOMETER_Y, name);
            case hku.fyp14017.blencode.R.string.sensor_magnetometer_z:
                return buildSensor(Sensors.MAGNETOMETER_Z, name);
            case hku.fyp14017.blencode.R.string.sensor_pressure:
                return buildSensor(Sensors.PRESSURE, name);
            case hku.fyp14017.blencode.R.string.sensor_humidity:
                return buildSensor(Sensors.HUMIDITY, name);

				//PERIOD
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_decimal_mark:
				return buildPeriod();

				//OPERATOR

			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_plus:
				return buildOperator(Operators.PLUS);
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_minus:
				return buildOperator(Operators.MINUS);
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_mult:
				return buildOperator(Operators.MULT);
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_divide:
				return buildOperator(Operators.DIVIDE);
			case hku.fyp14017.blencode.R.string.formula_editor_operator_power:
				return buildOperator(Operators.POW);
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_equal:
				return buildOperator(Operators.EQUAL);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_equal:
				return buildOperator(Operators.EQUAL);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_notequal:
				return buildOperator(Operators.NOT_EQUAL);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_lesserthan:
				return buildOperator(Operators.SMALLER_THAN);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_leserequal:
				return buildOperator(Operators.SMALLER_OR_EQUAL);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_greaterthan:
				return buildOperator(Operators.GREATER_THAN);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_greaterequal:
				return buildOperator(Operators.GREATER_OR_EQUAL);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_and:
				return buildOperator(Operators.LOGICAL_AND);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_or:
				return buildOperator(Operators.LOGICAL_OR);
			case hku.fyp14017.blencode.R.string.formula_editor_logic_not:
				return buildOperator(Operators.LOGICAL_NOT);

				//BRACKETS

			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_bracket_open:
				return buildBracketOpen();
			case hku.fyp14017.blencode.R.id.formula_editor_keyboard_bracket_close:
				return buildBracketClose();

				//COSTUME

			case hku.fyp14017.blencode.R.string.formula_editor_object_x:
				return buildObject(Sensors.OBJECT_X);
			case hku.fyp14017.blencode.R.string.formula_editor_object_y:
				return buildObject(Sensors.OBJECT_Y);
			case hku.fyp14017.blencode.R.string.formula_editor_object_ghosteffect:
				return buildObject(Sensors.OBJECT_GHOSTEFFECT);
			case hku.fyp14017.blencode.R.string.formula_editor_object_brightness:
				return buildObject(Sensors.OBJECT_BRIGHTNESS);
			case hku.fyp14017.blencode.R.string.formula_editor_object_size:
				return buildObject(Sensors.OBJECT_SIZE);
			case hku.fyp14017.blencode.R.string.formula_editor_object_rotation:
				return buildObject(Sensors.OBJECT_ROTATION);
			case hku.fyp14017.blencode.R.string.formula_editor_object_layer:
				return buildObject(Sensors.OBJECT_LAYER);

		}

		return null;

	}

	private List<InternToken> buildBracketOpen() {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.BRACKET_OPEN));
		return returnList;
	}

	private List<InternToken> buildBracketClose() {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.BRACKET_CLOSE));
		return returnList;
	}

	private List<InternToken> buildUserVariable(String userVariableName) {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.USER_VARIABLE, userVariableName));
		return returnList;
	}

	private List<InternToken> buildPeriod() {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.PERIOD));
		return returnList;
	}

	private List<InternToken> buildNumber(String numberValue) {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.NUMBER, numberValue));
		return returnList;
	}

	private List<InternToken> buildObject(Sensors sensors) {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.SENSOR, sensors.name()));
		return returnList;
	}

	private List<InternToken> buildOperator(Operators operator) {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.OPERATOR, operator.name()));
		return returnList;
	}

    private List<InternToken> buildSensor(Sensors sensor, String name) {
        List<InternToken> returnList = new LinkedList<InternToken>();
        returnList.add(new InternToken(InternTokenType.SENSOR, name));
        return returnList;
    }

    private List<InternToken> buildSensor(Sensors sensor) {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.SENSOR, sensor.name()));
		return returnList;
	}

	private List<InternToken> buildDoubleParameterFunction(Functions function, InternTokenType firstParameter,
			String firstParameterNumberValue, InternTokenType secondParameter, String secondParameterNumberValue) {

		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.FUNCTION_NAME, function.name()));
		returnList.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		returnList.add(new InternToken(firstParameter, firstParameterNumberValue));
		returnList.add(new InternToken(InternTokenType.FUNCTION_PARAMETER_DELIMITER));
		returnList.add(new InternToken(secondParameter, secondParameterNumberValue));
		returnList.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));

		return returnList;

	}

	private List<InternToken> buildSingleParameterFunction(Functions function, InternTokenType firstParameter,
			String parameterNumberValue) {

		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.FUNCTION_NAME, function.name()));
		returnList.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_OPEN));
		returnList.add(new InternToken(firstParameter, parameterNumberValue));
		returnList.add(new InternToken(InternTokenType.FUNCTION_PARAMETERS_BRACKET_CLOSE));
		return returnList;
	}

	private List<InternToken> buildFunctionWithoutParametersAndBrackets(Functions function) {

		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.FUNCTION_NAME, function.name()));
		return returnList;
	}

	private List<InternToken> buildString(String myString) {
		List<InternToken> returnList = new LinkedList<InternToken>();
		returnList.add(new InternToken(InternTokenType.STRING, myString));
		return returnList;
	}

}

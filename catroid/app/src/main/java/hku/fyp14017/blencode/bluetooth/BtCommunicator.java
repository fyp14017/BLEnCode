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
package hku.fyp14017.blencode.bluetooth;

import android.os.Handler;

// TODO Not needed for LegoNXT anymore, functionality now in LegoNXTBtCommunicator! Maybe refactor similary for arduino or move to arduino package...
public interface BtCommunicator {

	// this is the only OUI registered by LEGO, see http://standards.ieee.hku/regauth/oui/index.shtml
	String OUI_LEGO = "00:16:53";

	void setMACAddress(String mMACaddress);

	boolean isConnected();

	Handler getHandler();
}

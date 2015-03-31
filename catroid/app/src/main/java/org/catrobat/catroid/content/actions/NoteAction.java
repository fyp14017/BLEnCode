/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.actions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.os.Handler;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.utils.LedUtil;
import org.catrobat.catroid.utils.VibratorUtil;

@SuppressLint("NewApi")
public class NoteAction extends TemporalAction {

    String comment;

	@Override
	protected void update(float percent) {

        Handler mHandler = new Handler(StageActivity.ctx.getMainLooper());
        Runnable r = new Runnable() {
            @Override
            public void run() {
                SensorHandler.stopSensorListeners();
                StageActivity.stageListener.menuPause();
                LedUtil.pauseLed();
                VibratorUtil.pauseVibrator();

                AlertDialog.Builder print = new AlertDialog.Builder(StageActivity.ctx);
                print.setTitle("Console")
                        .setMessage(comment)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                StageActivity.stageListener.menuResume();
                                LedUtil.resumeLed();
                                VibratorUtil.resumeVibrator();
                                SensorHandler.startSensorListener(StageActivity.ctx);

                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        };

        mHandler.post(r);
	}

    public void setString(String comment){
        this.comment = comment;
    }
}

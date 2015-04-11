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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;

import hku.fyp14017.blencode.common.BrickValues;

import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;

import java.util.List;

import hku.fyp14017.blencode.common.BrickValues;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;

public class ChangeGhostEffectByNBrick extends FormulaBrick implements OnClickListener {

	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public ChangeGhostEffectByNBrick() {
		addAllowedBrickField(BrickField.TRANSPARENCY_CHANGE);
	}

	public ChangeGhostEffectByNBrick(double changeGhostEffectValue) {
		initializeBrickFields(new Formula(changeGhostEffectValue));
	}

	public ChangeGhostEffectByNBrick(Formula changeGhostEffect) {
		initializeBrickFields(changeGhostEffect);
	}

	private void initializeBrickFields(Formula changeGhostEffect) {
		addAllowedBrickField(BrickField.TRANSPARENCY_CHANGE);
		setFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE, changeGhostEffect);
	}

    @Override
    public String brickTutorial(){
        return "Used to adjust the transparency of the object. " +
                "\n\nHigher the percentage, more transparent the object would be.";
    }

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE).getRequiredResources();
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_change_ghost_effect, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_checkbox);
		final Brick brickInstance = this;

		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});
		TextView textX = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_prototype_text_view);
		TextView editX = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_edit_text);
		getFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE)
				.setTextFieldId(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_edit_text);
		getFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE).refreshTextField(view);

		textX.setVisibility(View.GONE);
		editX.setVisibility(View.VISIBLE);
		editX.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_change_ghost_effect, null);
		TextView textChangeGhostEffect = (TextView) prototypeView
				.findViewById(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_prototype_text_view);
        textChangeGhostEffect.setText(String.valueOf(BrickValues.CHANGE_GHOST_EFFECT));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textGhost = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_label);
			TextView textGhostBy = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_by);
			TextView editGhostEffect = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_change_ghost_effect_edit_text);
			textGhost.setTextColor(textGhost.getTextColors().withAlpha(alphaValue));
			textGhostBy.setTextColor(textGhostBy.getTextColors().withAlpha(alphaValue));
			editGhostEffect.setTextColor(editGhostEffect.getTextColors().withAlpha(alphaValue));
			editGhostEffect.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE));
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.changeGhostEffectByN(sprite,
				getFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE)));
		return null;
	}

}

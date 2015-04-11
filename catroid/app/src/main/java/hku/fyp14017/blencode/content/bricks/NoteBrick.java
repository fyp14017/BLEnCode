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
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class NoteBrick extends FormulaBrick implements OnClickListener{
	private static final long serialVersionUID = 1L;
	private transient View prototypeView;
    private String comment;

	public NoteBrick() {
		addAllowedBrickField(BrickField.NOTE);
	}

	public NoteBrick(String note) {
		comment = note;
        initializeBrickFields(new Formula(note));
	}

	public NoteBrick(Formula note) {
		initializeBrickFields(note);
	}

	private void initializeBrickFields(Formula note) {
		addAllowedBrickField(BrickField.NOTE);
		setFormulaWithBrickField(BrickField.NOTE, note);
	}

    @Override
    public String brickTutorial(){
        return "Add some comment to the script for future reference." +
                "\n\n Does not affect the running of the application.";
    }

	@Override
	public View getView(final Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_note, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(hku.fyp14017.blencode.R.id.brick_note_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView textHolder = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_note_prototype_text_view);
		TextView textField = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_note_edit_text);
		getFormulaWithBrickField(BrickField.NOTE).setTextFieldId(hku.fyp14017.blencode.R.id.brick_note_edit_text);
		getFormulaWithBrickField(BrickField.NOTE).refreshTextField(view);
        comment = getFormulaWithBrickField(BrickField.NOTE).getDisplayString(context).substring(1,getFormulaWithBrickField(BrickField.NOTE).getDisplayString(context).length()-2);
		textHolder.setVisibility(View.GONE);
		textField.setVisibility(View.VISIBLE);
		textField.setOnClickListener(this);

		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_note_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView noteLabel = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_note_text_view);
			TextView noteTextView = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.brick_note_edit_text);
			noteLabel.setTextColor(noteLabel.getTextColors().withAlpha(alphaValue));
			noteTextView.setTextColor(noteTextView.getTextColors().withAlpha(alphaValue));
			noteTextView.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_note, null);
		TextView textSpeak = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.brick_note_prototype_text_view);
		textSpeak.setText(context.getString(hku.fyp14017.blencode.R.string.brick_note_default_value));
		return prototypeView;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
        sequence.addAction(ExtendedActions.noteAction(comment));
		return null;
	}

	@Override
	public Formula getFormula() {
		return getFormulaWithBrickField(BrickField.NOTE);
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		switch (view.getId()) {
			case hku.fyp14017.blencode.R.id.brick_note_edit_text:
				FormulaEditorFragment.showFragment(view, this, getFormulaWithBrickField(BrickField.NOTE));
				break;
			default:
				break;
		}
	}
}

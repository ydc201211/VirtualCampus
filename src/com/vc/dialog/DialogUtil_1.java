package com.vc.dialog;

import com.vc.ui.R;

import android.app.Dialog;
import android.content.Context;

public class DialogUtil_1 {
	private Dialog pDialog;
	private Context context;
	
	public DialogUtil_1(Context context){
		this.context = context;
	}
	public void hidePDialog() {
		if (pDialog != null)
			pDialog.dismiss();
		pDialog = null;
	}

	public void showPDialog() {
		pDialog = new Dialog(this.context, R.style.new_circle_progress);
		pDialog.setContentView(R.layout.layout_progressbar);
		pDialog.show();
	}
}

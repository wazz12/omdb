package com.scb.omdb.utils

import android.app.Activity
import android.app.Dialog
import android.view.Window
import com.pnikosis.materialishprogress.ProgressWheel
import com.scb.omdb.R
import kotlinx.android.synthetic.main.layout_progress_spinner.*

class ProgressSpinnerDialog(private val activity: Activity) {
    private var dialog: Dialog? = null

    fun showDialog() {
        if (dialog == null) {
            dialog = Dialog(activity)
            dialog?.let {
                it.requestWindowFeature(Window.FEATURE_NO_TITLE)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(false)
                it.setContentView(R.layout.layout_progress_spinner)
                it.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                val progressWheel = it.custom_loading_progresswheel as ProgressWheel
                progressWheel.spin()
            }
        }
        dialog!!.show()
    }

    fun hideDialog() {
        if (dialog != null) dialog!!.dismiss()
    }
}
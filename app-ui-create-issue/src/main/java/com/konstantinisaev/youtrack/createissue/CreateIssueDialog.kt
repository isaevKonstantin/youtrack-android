package com.konstantinisaev.youtrack.createissue

import android.app.Dialog
import android.util.DisplayMetrics
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils

class CreateIssueDialog : BottomSheetDialogFragment(){

    private lateinit var nsvCreateIssueBody: NestedScrollView
    private lateinit var tvAttach: TextView
    private lateinit var tvConfirmIssue: TextView
    private lateinit var tvPriority: TextView
    private lateinit var tvCurrentProject: TextView
    private lateinit var tvType: TextView
    private lateinit var tvAssignee: TextView
    private lateinit var tvSprint: TextView
    private lateinit var tvState: TextView
    private lateinit var tlEstimation: TextInputLayout
    private lateinit var tlSpentTime: TextInputLayout

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        val inflatedView = View.inflate(context, R.layout.dialog_create_issue, null)

        val toolbar = inflatedView.findViewById<Toolbar>(R.id.tlbCreateIssue)
        val tlSummary = inflatedView.findViewById<TextInputLayout>(R.id.tlSummary)
        val tlDesc = inflatedView.findViewById<TextInputLayout>(R.id.tlDesc)
        tvCurrentProject = inflatedView.findViewById(R.id.tvCurrentProject)
        tvPriority = inflatedView.findViewById(R.id.tvPriority)
        tvType = inflatedView.findViewById(R.id.tvType)
        tvAssignee = inflatedView.findViewById(R.id.tvAssignee)
        tvAttach = inflatedView.findViewById(R.id.tvAttach)
        tvConfirmIssue = inflatedView.findViewById(R.id.tvConfirmIssue)
        tvSprint = inflatedView.findViewById(R.id.tvSprint)
        nsvCreateIssueBody = inflatedView.findViewById(R.id.nsvCreateIssueBody)
        tlEstimation = inflatedView.findViewById(R.id.tlEstimation)
        tlSpentTime = inflatedView.findViewById(R.id.tlSpentTime)
        tvState = inflatedView.findViewById(R.id.tvState)

        val pbCreateIssue = inflatedView.findViewById<ProgressBar>(R.id.pbCreateIssue)
        val tvCreateIssueError = inflatedView.findViewById<TextView>(R.id.tvCreateIssueError)

        toolbar.title = getString(R.string.create_issue_fragm_toolbar_title)
        toolbar.navigationIcon = ContextCompat.getDrawable(context!!,R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { dismiss() }

        dialog?.setContentView(inflatedView)

        val params = (inflatedView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(bottomSheetBehaviorCallback)
        }

        val parent = inflatedView.parent as View
        val bottomSheetBehavior = BottomSheetBehavior.from(parent)
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels - DeviceUtils.convertDpToPixel(24f,context!!)
        bottomSheetBehavior.peekHeight = screenHeight

        if (params.behavior is BottomSheetBehavior<*>) {
            (params.behavior as BottomSheetBehavior<*>).setBottomSheetCallback(bottomSheetBehaviorCallback)
        }
        params.height = screenHeight
        parent.layoutParams = params

    }

}
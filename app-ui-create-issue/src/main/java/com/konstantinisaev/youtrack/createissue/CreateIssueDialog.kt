package com.konstantinisaev.youtrack.createissue

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.konstantinisaev.youtrack.ui.base.models.Issue
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewModelFactory
import com.konstantinisaev.youtrack.ui.base.viewmodels.ViewState
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNCHECKED_CAST")
class CreateIssueDialog : BottomSheetDialogFragment(){

    @field:[Inject Named("baseFactory")]
    lateinit var baseViewModelFactory: ViewModelFactory

    @field:[Inject Named("featureFactory")]
    lateinit var featureViewModelFactory: ViewModelFactory

    lateinit var draftViewModel: DraftViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CreateIssueDiProvider.getInstance().injectFragment(this)
        draftViewModel = ViewModelProviders.of(this,featureViewModelFactory)[DraftViewModel::class.java]
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

        draftViewModel.observe(this, Observer {viewState ->
            pbCreateIssue.visibility = View.GONE
            if(viewState is ViewState.Error){
                return@Observer
            }
            if (viewState is ViewState.ValidationError) {
                Toast.makeText(context,viewState.msgId,Toast.LENGTH_SHORT).show()
                return@Observer
            }

            val issue = (viewState as ViewState.Success<Issue>).data
            setValue(tvPriority,issue.priority.value.name,issue.priority.projectCustomField.emptyFieldText.orEmpty())
            setValue(tvAssignee,issue.assignee.value.name,issue.assignee.projectCustomField.emptyFieldText.orEmpty())
            setValue(tvCurrentProject,issue.project?.name.orEmpty(),"")
            setValue(tvType,issue.type.value.name,issue.type.projectCustomField.emptyFieldText.orEmpty())
            setValue(tvSprint,issue.sprint.value.name,issue.sprint.projectCustomField.emptyFieldText.orEmpty())
            setValue(tvState,issue.state.value.name,issue.state.projectCustomField.emptyFieldText.orEmpty())
            setValue(tlEstimation.editText!!,issue.estimation.value.presentation,issue.estimation.projectCustomField.emptyFieldText.orEmpty())
            setValue(tlSpentTime.editText!!,issue.spentTime.value.presentation,issue.spentTime.projectCustomField.emptyFieldText.orEmpty())
            switchVisibilityOfMainView(View.VISIBLE)

        })

        pbCreateIssue.visibility = View.VISIBLE
        switchVisibilityOfMainView(View.INVISIBLE)
        draftViewModel.doAsyncRequest()

    }

    private fun switchVisibilityOfMainView(visibility: Int){
        tvConfirmIssue.visibility = visibility
        nsvCreateIssueBody.visibility = visibility
        tvAttach.visibility = visibility
    }

    private fun setValue(tv: TextView, value: String, emptyLabel: String){
        if(value.isNotEmpty()){
            tv.text = value
        }else{
            tv.text = emptyLabel
        }
    }

}
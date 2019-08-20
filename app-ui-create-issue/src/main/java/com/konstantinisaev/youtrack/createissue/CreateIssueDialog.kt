package com.konstantinisaev.youtrack.createissue

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.EditText
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
import com.konstantinisaev.youtrack.core.api.CustomFieldAdminDTO
import com.konstantinisaev.youtrack.core.api.ProjectCustomFieldDto
import com.konstantinisaev.youtrack.core.api.UserDTO
import com.konstantinisaev.youtrack.core.rv.BaseSelectRvItem
import com.konstantinisaev.youtrack.createissue.viewmodels.*
import com.konstantinisaev.youtrack.ui.base.models.EnumValue
import com.konstantinisaev.youtrack.ui.base.models.FieldContainer
import com.konstantinisaev.youtrack.ui.base.models.Issue
import com.konstantinisaev.youtrack.ui.base.models.UserValue
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.utils.gone
import com.konstantinisaev.youtrack.ui.base.utils.visible
import com.konstantinisaev.youtrack.ui.base.viewmodels.GetProjectsViewModel
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

    private lateinit var draftViewModel: DraftViewModel
    private lateinit var getProjectsViewModel: GetProjectsViewModel
    private lateinit var getFieldSettingsViewModel: GetFieldSettingsViewModel
    private lateinit var getFieldUserSettingsViewModel: GetFieldUserSettingsViewModel
    private lateinit var updateDraftFieldViewModel: UpdateDraftFieldViewModel
    private lateinit var updateDraftViewModel: UpdateDraftViewModel

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
    private lateinit var pbCreateIssue: View
    private lateinit var edtSummary: EditText
    private lateinit var edtDesc: EditText


    private val bundleMap = mutableMapOf<Int,Pair<String,String>>()
    private val updateViews = mutableMapOf<Int,TextView>()
    private var clickedItemId = 0

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            tvCurrentProject.id -> {
                clickedItemId = view.id
                val data = getProjectsViewModel.projects
                val items = data.map { project -> BaseSelectRvItem(project.id.orEmpty(),project.name.orEmpty()) }
                showSelectDialog(BaseSelectListDialog.Param(getSelectedTitle(clickedItemId),items)){ selectedId ->
                    data.find { it.id.orEmpty() == selectedId }?.let{ project ->
                        val issue = draftViewModel.draftIssue ?: return@let
                        pbCreateIssue.visible()
                        switchVisibilityOfMainView(View.INVISIBLE)
                        updateDraftViewModel.doAsyncRequest(issue.copy(project = project))
                    }
                }
            }
            else -> {
                bundleMap[view.id]?.let {
                    clickedItemId = view.id
                    val param = CreateIssueFieldParam(it.first, it.second)
                    if(clickedItemId == tvAssignee.id){
                        getFieldUserSettingsViewModel.doAsyncRequest(param)
                    }else{
                        getFieldSettingsViewModel.doAsyncRequest(param)
                    }
                }
            }
        }
    }

    private val selectedFieldListener: (String) -> Unit = { selectedId ->
        pbCreateIssue.visible()
        switchVisibilityOfMainView(View.INVISIBLE)
        val tv = updateViews.getValue(clickedItemId)
        updateDraftFieldViewModel.doAsyncRequest(UpdateDraftField(tv.tag?.toString().orEmpty(),selectedId))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CreateIssueDiProvider.getInstance().injectFragment(this)
        draftViewModel = ViewModelProviders.of(this,featureViewModelFactory)[DraftViewModel::class.java]
        getProjectsViewModel = ViewModelProviders.of(this,baseViewModelFactory)[GetProjectsViewModel::class.java]
        getFieldSettingsViewModel = ViewModelProviders.of(this,featureViewModelFactory)[GetFieldSettingsViewModel::class.java]
        getFieldUserSettingsViewModel = ViewModelProviders.of(this,featureViewModelFactory)[GetFieldUserSettingsViewModel::class.java]
        updateDraftFieldViewModel = ViewModelProviders.of(this,featureViewModelFactory)[UpdateDraftFieldViewModel::class.java]
        updateDraftViewModel = ViewModelProviders.of(this,featureViewModelFactory)[UpdateDraftViewModel::class.java]
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        val inflatedView = View.inflate(context, R.layout.dialog_create_issue, null)

        val toolbar = inflatedView.findViewById<Toolbar>(R.id.tlbCreateIssue)
        val tlSummary = inflatedView.findViewById<TextInputLayout>(R.id.tlSummary)
        val tlDesc = inflatedView.findViewById<TextInputLayout>(R.id.tlDesc)
        edtSummary = requireNotNull(tlSummary.editText)
        edtDesc = requireNotNull(tlDesc.editText)
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
        pbCreateIssue = inflatedView.findViewById(R.id.pbCreateIssue)
        val pbCreateIssue = inflatedView.findViewById<ProgressBar>(R.id.pbCreateIssue)
        val tvCreateIssueError = inflatedView.findViewById<TextView>(R.id.tvCreateIssueError)

        updateViews[tvCurrentProject.id] = tvCurrentProject
        updateViews[tvPriority.id] = tvPriority
        updateViews[tvType.id] = tvType
        updateViews[tvAssignee.id] = tvAssignee
        updateViews[tvSprint.id] = tvSprint
        updateViews[tvState.id] = tvState

        toolbar.title = getString(R.string.create_issue_fragm_toolbar_title)
        toolbar.navigationIcon = ContextCompat.getDrawable(context!!,R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { dismiss() }

        edtSummary.setOnFocusChangeListener { _, hasFocus ->
            val issue = draftViewModel.draftIssue ?: return@setOnFocusChangeListener
            if(!hasFocus && issue.summary != edtSummary.text?.toString()){
                pbCreateIssue.visible()
                switchVisibilityOfMainView(View.INVISIBLE)
                updateDraftViewModel.doAsyncRequest(issue.copy(summary = edtSummary.text.toString()))
            }
        }

        edtDesc.setOnFocusChangeListener { _, hasFocus ->
            val issue = draftViewModel.draftIssue ?: return@setOnFocusChangeListener
            if(!hasFocus && issue.description != edtDesc.text.toString()){
                pbCreateIssue.visible()
                switchVisibilityOfMainView(View.INVISIBLE)
                updateDraftViewModel.doAsyncRequest(issue.copy(description = edtDesc.text.toString()))
            }
        }

        dialog?.setContentView(inflatedView)

        val params = (inflatedView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(bottomSheetBehaviorCallback)
        }

        val parent = inflatedView.parent as View
        val bottomSheetBehavior = BottomSheetBehavior.from(parent)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels - DeviceUtils.convertDpToPixel(24f,requireContext())
        bottomSheetBehavior.peekHeight = screenHeight

        if (params.behavior is BottomSheetBehavior<*>) {
            (params.behavior as BottomSheetBehavior<*>).setBottomSheetCallback(bottomSheetBehaviorCallback)
        }
        params.height = screenHeight
        parent.layoutParams = params

        tvPriority.setOnClickListener(clickListener)
        tvAssignee.setOnClickListener(clickListener)
        tvState.setOnClickListener(clickListener)
        tvSprint.setOnClickListener(clickListener)
        tvType.setOnClickListener(clickListener)
        tvCurrentProject.setOnClickListener(clickListener)

        draftViewModel.observe(this, Observer {viewState ->
            pbCreateIssue.gone()
            if(viewState is ViewState.Error){
                return@Observer
            }
            if (viewState is ViewState.ValidationError) {
                Toast.makeText(context,viewState.msgId,Toast.LENGTH_SHORT).show()
                return@Observer
            }

            val issue = (viewState as ViewState.Success<Issue>).data
            updateIssue(issue)
        })

        getFieldSettingsViewModel.observe(this, Observer { viewState ->
            if(viewState is ViewState.Success<*>){
                val data = viewState.data as List<CustomFieldAdminDTO>
                val param = BaseSelectListDialog.Param(getSelectedTitle(clickedItemId),data.map { BaseSelectRvItem(it.id.orEmpty(),it.name.orEmpty()) })
                showSelectDialog(param,selectedFieldListener)
            }
        })

        getFieldUserSettingsViewModel.observe(this, Observer { viewState ->
            if(viewState is ViewState.Success<*>){
                val data = viewState.data as List<UserDTO>
                val param = BaseSelectListDialog.Param(getSelectedTitle(clickedItemId),data.map { BaseSelectRvItem(it.id.orEmpty(),it.name.orEmpty()) })
                showSelectDialog(param,selectedFieldListener)
            }
        })

        updateDraftFieldViewModel.observe(this, Observer { viewState ->
            pbCreateIssue.gone()
            switchVisibilityOfMainView(View.VISIBLE)
            if(viewState is ViewState.Success<*>){
                val data = (viewState.data as FieldContainer<*>).value
                val tv = updateViews.getValue(clickedItemId)
                when(data){
                    is EnumValue ->{
                        setValue(tv, data.name,"")
                    }
                    is UserValue -> {
                        setValue(tv, data.name,"")
                    }
                }
            }
        })
        updateDraftViewModel.observe(this, Observer { viewState ->
            pbCreateIssue.gone()
            switchVisibilityOfMainView(View.VISIBLE)
            if(viewState is ViewState.Success<*>){
                val issue = (viewState.data as Issue)
                updateIssue(issue)
            }
        })

        pbCreateIssue.visible()
        switchVisibilityOfMainView(View.INVISIBLE)

        draftViewModel.doAsyncRequest()
    }

    private fun updateIssue(issue: Issue) {
        setValue(tvPriority, issue.priority.value.name, issue.priority.projectCustomField.emptyFieldText.orEmpty())
        setValue(tvAssignee, issue.assignee.value.name, issue.assignee.projectCustomField.emptyFieldText.orEmpty())
        setValue(tvCurrentProject, issue.project?.name.orEmpty(), "")
        setValue(tvType, issue.type.value.name, issue.type.projectCustomField.emptyFieldText.orEmpty())
        setValue(tvSprint, issue.sprint.value.name, issue.sprint.projectCustomField.emptyFieldText.orEmpty())
        setValue(tvState, issue.state.value.name, issue.state.projectCustomField.emptyFieldText.orEmpty())
        setValue(
            tlEstimation.editText!!,
            issue.estimation.value.presentation,
            issue.estimation.projectCustomField.emptyFieldText.orEmpty()
        )
        setValue(tlSpentTime.editText!!,issue.spentTime.value.presentation,issue.spentTime.projectCustomField.emptyFieldText.orEmpty())
        setValue(edtSummary,issue.summary,"")
        setValue(edtDesc,issue.description,"")

        bundleMap.clear()
        setBundle(tvPriority.id, issue.priority.projectCustomField)
        setBundle(tvType.id, issue.type.projectCustomField)
        setBundle(tvSprint.id, issue.sprint.projectCustomField)
        setBundle(tvState.id, issue.state.projectCustomField)
        setBundle(tvAssignee.id, issue.assignee.projectCustomField)

        tvPriority.tag = issue.priority.projectCustomField.id.orEmpty()
        tvAssignee.tag = issue.assignee.projectCustomField.id.orEmpty()
        tvType.tag = issue.type.projectCustomField.id.orEmpty()
        tvSprint.tag = issue.sprint.projectCustomField.id.orEmpty()
        tvState.tag = issue.state.projectCustomField.id.orEmpty()
        switchVisibilityOfMainView(View.VISIBLE)
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

    private fun setBundle(id: Int, projectCustomFieldDto: ProjectCustomFieldDto){
        projectCustomFieldDto.bundle?.let {
            bundleMap.put(id,Pair(projectCustomFieldDto.field?.fieldType?.valueType.orEmpty(),it.id.orEmpty()))
        }
    }

    private fun updateBundle(textView: TextView, selectedId: String) {
        val bundleValue = bundleMap[textView.id]?.copy(second = selectedId)
        bundleValue?.apply {
            bundleMap[textView.id] = this
        }
    }

    private fun showSelectDialog(data: BaseSelectListDialog.Param,listener: (String) -> Unit) {
        val dialog = BaseSelectListDialog.Builder().setParam(data)
            .setSelectorListener(listener) .build()
        dialog.show(activity?.supportFragmentManager, "")
    }

    private fun getSelectedTitle(id: Int) =
        when(id) {
            tvPriority.id -> getString(R.string.create_issue_fragm_select_field_format,getString(R.string.create_issue_fragm_priority_hint).toLowerCase())
            tvState.id -> getString(R.string.create_issue_fragm_select_field_format,getString(R.string.create_issue_fragm_state_hint).toLowerCase())
            tvType.id -> getString(R.string.create_issue_fragm_select_field_format,getString(R.string.create_issue_fragm_type_hint).toLowerCase())
            tvAssignee.id -> getString(R.string.create_issue_fragm_select_field_format,getString(R.string.create_issue_fragm_assignee_hint).toLowerCase())
            tvCurrentProject.id -> getString(R.string.create_issue_fragm_select_field_format,getString(R.string.create_issue_fragm_project_hint).toLowerCase())
            tvSprint.id -> getString(R.string.create_issue_fragm_select_field_format,getString(R.string.create_issue_fragm_sprint_hint).toLowerCase())
            else -> ""
        }


}
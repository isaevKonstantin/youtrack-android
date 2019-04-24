package com.konstantinisaev.youtrack.core.rv

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.konstantinisaev.youtrack.ui.base.models.FieldColor
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.android.synthetic.main.vh_issue_compact.tvIssueOwners
import kotlinx.android.synthetic.main.vh_issue_compact.tvIssueState
import kotlinx.android.synthetic.main.vh_issue_compact.tvIssueType
import kotlinx.android.synthetic.main.vh_issue_detailed.imgIssueIndicator
import kotlinx.android.synthetic.main.vh_issue_detailed.tvIssueCommentCounter
import kotlinx.android.synthetic.main.vh_issue_detailed.tvIssueDescription
import kotlinx.android.synthetic.main.vh_issue_detailed.tvIssueSpentTime
import kotlinx.android.synthetic.main.vh_issue_filter_suggestion.*
import kotlinx.android.synthetic.main.vh_issue_filter_suggestion_child.*
import kotlinx.android.synthetic.main.vh_issue_single.imgIssueFavorites
import kotlinx.android.synthetic.main.vh_issue_single.tvIssueNumber
import kotlinx.android.synthetic.main.vh_issue_single.tvIssuePriority
import kotlinx.android.synthetic.main.vh_issue_single.tvIssueTime
import kotlinx.android.synthetic.main.vh_issue_single.tvIssueTitle
import kotlinx.android.synthetic.main.vh_select_list.*
import kotlinx.android.synthetic.main.vh_select_user.*
import kotlinx.android.synthetic.main.vh_suggestion_full_search.*
import kotlinx.android.synthetic.main.vh_text_header.*
import kotlin.math.roundToInt

class IssueMainViewHolder(override val containerView: View) : BaseRvViewHolder<IssueRvItem>(containerView), LayoutContainer {

	override fun bind(rvItem: IssueRvItem, clickListener: BaseRvClickListener?) {
		itemView.setOnClickListener {
			clickListener?.onItemClickListener(rvItem)
		}
		setText(tvIssueTitle,rvItem.issueCommonRvData.summary)
		setText(tvIssueTime,rvItem.issueCommonRvData.updated)
		setText(tvIssuePriority,rvItem.issueCommonRvData.priority)
		handleIssueFavorites(imgIssueFavorites,rvItem.issueCommonRvData.inFavorites)
		handleIssueColor(tvIssuePriority,rvItem.issueCommonRvData.priorityColor)
		handleIsDoneState(rvItem.issueCommonRvData.name,rvItem.issueCommonRvData.isDone,tvIssueNumber,tvIssueTitle)
	}
}

class TextHeaderViewHolder(override val containerView: View) : BaseRvViewHolder<TextRvItem>(containerView),LayoutContainer{

	override fun bind(rvItem: TextRvItem, clickListener: BaseRvClickListener?) {
		tvTextHeader.text = rvItem.text
	}
}

class IssueCompactViewHolder(override val containerView: View) : BaseRvViewHolder<IssueCompactRvItem>(containerView),LayoutContainer{

	override fun bind(rvItem: IssueCompactRvItem, clickListener: BaseRvClickListener?) {
		itemView.setOnClickListener {
			clickListener?.onItemClickListener(rvItem)
		}
		setText(tvIssueTitle,rvItem.issueCommonRvData.summary)
		setText(tvIssueTime,rvItem.issueCommonRvData.updated)
		setText(tvIssuePriority,rvItem.issueCommonRvData.priority)
		setText(tvIssueType,rvItem.issueStateRvData.type)
		setText(tvIssueState,rvItem.issueStateRvData.state)
		val issueReporterAndAssignee = containerView.context.resources.getString(R.string.issues_list_fragm_owners_formatted,rvItem.issueStateRvData.reporterName,rvItem.issueStateRvData.assigneeName)
		setText(tvIssueOwners,issueReporterAndAssignee)
		handleIssueFavorites(imgIssueFavorites,rvItem.issueCommonRvData.inFavorites)
		handleIssueColor(tvIssuePriority,rvItem.issueCommonRvData.priorityColor)
		handleIssueColor(tvIssueType,rvItem.issueStateRvData.typeColor)
		handleIssueColor(tvIssueState,rvItem.issueStateRvData.stateColor)
		handleIsDoneState(rvItem.issueCommonRvData.name,rvItem.issueCommonRvData.isDone,tvIssueNumber,tvIssueTitle)
	}

}

class IssueDetailedViewHolder(override val containerView: View) : BaseRvViewHolder<IssueDetailedRvItem>(containerView),LayoutContainer{

	override fun bind(rvItem: IssueDetailedRvItem, clickListener: BaseRvClickListener?) {
		itemView.setOnClickListener {
			clickListener?.onItemClickListener(rvItem)
		}
		setText(tvIssueTitle,rvItem.issueCommonRvData.summary)
		setText(tvIssueTime,rvItem.issueCommonRvData.updated)
		setText(tvIssuePriority,rvItem.issueCommonRvData.priority)
		setText(tvIssueType,rvItem.issueStateRvData.type)
		setText(tvIssueState,rvItem.issueStateRvData.state)
		val issueReporterAndAssignee = containerView.context.resources.getString(R.string.issues_list_fragm_owners_formatted,rvItem.issueStateRvData.reporterName,rvItem.issueStateRvData.assigneeName)
		setText(tvIssueOwners,issueReporterAndAssignee)
		setText(tvIssueCommentCounter,rvItem.issueDetailsRvData.commentsCount)
		if(rvItem.issueDetailsRvData.spentTimeText.isNotEmpty()){
			setText(tvIssueSpentTime,rvItem.issueDetailsRvData.spentTimeText)
			if(rvItem.issueDetailsRvData.spentTimeMinutes > rvItem.issueDetailsRvData.estimationMinutes ){
				imgIssueIndicator.setImageDrawable(ContextCompat.getDrawable(tvIssueTitle.context,R.drawable.drw_issue_indicator))
			}else {
				val drw = PieProgressDrawable()
				drw.setColor(ContextCompat.getColor(tvIssueTitle.context,R.color.primary))
				drw.setBorderWidth(DeviceUtils.convertDpToPixel(1f,tvIssueTitle.context).toFloat(),tvIssueTitle.context.resources.displayMetrics)
				val estimationMinutes = rvItem.issueDetailsRvData.estimationMinutes
				val spentTimeMinutes = rvItem.issueDetailsRvData.spentTimeMinutes
				val level = if(spentTimeMinutes == 0){
					0
				}else{
					100f.div(estimationMinutes.toFloat().div(spentTimeMinutes.toFloat())).roundToInt()
				}
				imgIssueIndicator.setImageDrawable(drw)
				drw.level = level
				imgIssueIndicator.invalidate()
			}
			imgIssueIndicator.visibility = View.VISIBLE
			tvIssueSpentTime.visibility = View.VISIBLE
		}else{
			imgIssueIndicator.visibility = View.INVISIBLE
			tvIssueSpentTime.visibility = View.INVISIBLE
		}
		if(rvItem.issueDetailsRvData.description.isEmpty()){
			tvIssueDescription.visibility = View.GONE
		}else{
			setText(tvIssueDescription,rvItem.issueDetailsRvData.description)
			tvIssueDescription.visibility = View.VISIBLE
		}
		handleIssueFavorites(imgIssueFavorites,rvItem.issueCommonRvData.inFavorites)
		handleIssueColor(tvIssuePriority,rvItem.issueCommonRvData.priorityColor)
		handleIssueColor(tvIssueType,rvItem.issueStateRvData.typeColor)
		handleIssueColor(tvIssueState,rvItem.issueStateRvData.stateColor)
		handleIsDoneState(rvItem.issueCommonRvData.name,rvItem.issueCommonRvData.isDone,tvIssueNumber,tvIssueTitle)
	}
}

class IssueFilterMainSuggestionViewHolder(override val containerView: View) : BaseRvViewHolder<IssueFilterSuggestionRvItem>(containerView),LayoutContainer{

	override fun bind(rvItem: IssueFilterSuggestionRvItem, clickListener: BaseRvClickListener?) {
		setText(tvIssueSortFilter,rvItem.issueFilterSuggestionRvData.name)
		setIcon(rvItem)
		tvIssueSortFilter.setOnClickListener {
			rvItem.issueFilterSuggestionRvData.checked = !rvItem.issueFilterSuggestionRvData.checked
			setIcon(rvItem)
			clickListener?.onItemClickListener(rvItem)
		}
	}

	private fun setIcon(rvItem: IssueFilterSuggestionRvItem){
		if(rvItem.issueFilterSuggestionRvData.checked){
			tvIssueSortFilter.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_more_black_24dp,0)
		}else{
			tvIssueSortFilter.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_less_black_24dp,0)
		}
	}
}

class IssueFilterChildSuggestionViewHolder(override val containerView: View) : BaseRvViewHolder<IssueFilterSuggestionChildRvItem>(containerView),LayoutContainer{

	override fun bind(rvItem: IssueFilterSuggestionChildRvItem, clickListener: BaseRvClickListener?) {
		chIssueSuggestion.text = rvItem.issueFilterSuggestionRvData.name
		chIssueSuggestion.isChecked = rvItem.issueFilterSuggestionRvData.checked
		chIssueSuggestion.setOnClickListener {
			rvItem.issueFilterSuggestionRvData.checked = !rvItem.issueFilterSuggestionRvData.checked
			chIssueSuggestion.isChecked = rvItem.issueFilterSuggestionRvData.checked
			clickListener?.onItemClickListener(rvItem)
		}
	}
}

class IssueSuggestionFullSearchViewHolder(override val containerView: View) : BaseRvViewHolder<IssueFullSearchSuggestionRvItem>(containerView),LayoutContainer {

	override fun bind(rvItem: IssueFullSearchSuggestionRvItem, clickListener: BaseRvClickListener?) {
		tvSuggestionFullSearch.text = TextUtils.concat(rvItem.prefix,rvItem.option,rvItem.suffix)
		itemView.setOnClickListener {
			clickListener?.onItemClickListener(rvItem)
		}
	}

}

class EmptyViewHolder(override val containerView: View) : BaseRvViewHolder<IssueFilterSuggestionEmpty>(containerView),LayoutContainer {
	override fun bind(rvItem: IssueFilterSuggestionEmpty, clickListener: BaseRvClickListener?) {}
}

class SelectListViewHolder(override val containerView: View) : BaseRvViewHolder<BaseSelectRvItem>(containerView),LayoutContainer{

	override fun bind(rvItem: BaseSelectRvItem, clickListener: BaseRvClickListener?) {
		tvSelectListHeader.text = rvItem.name
		itemView.setOnClickListener { clickListener?.onItemClickListener(rvItem) }
	}
}



class SelectUserViewHolder(override val containerView: View) : BaseRvViewHolder<SelectUserRvItem>(containerView), LayoutContainer {

	override fun bind(rvItem: SelectUserRvItem, clickListener: BaseRvClickListener?) {
		if(rvItem.avatarUrl.isNotEmpty()){
			Picasso.with(itemView.context).load(rvItem.avatarUrl)
				.fit()
				.centerCrop()
				.transform(CropCircleTransformation())
				.into(imgUserLogo,object : Callback {
					override fun onSuccess() {}

					override fun onError() {
						setInitials(imgUserLogo,tvUserInitials,rvItem.initials)
					}
				})
			tvUserInitials.visibility = View.GONE
			imgUserLogo.visibility = View.VISIBLE
		}else{
			setInitials(imgUserLogo,tvUserInitials,rvItem.initials)
		}
		tvUserName.text = rvItem.name
		itemView.setOnClickListener { clickListener?.onItemClickListener(rvItem) }
	}
}



data class IssueRvItem(val issueCommonRvData: IssueCommonRvData) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)

}

data class IssueCompactRvItem(val issueCommonRvData: IssueCommonRvData,
                              val issueStateRvData: IssueStateRvData) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)

}

data class IssueDetailedRvItem(val issueCommonRvData: IssueCommonRvData,
                               val issueStateRvData: IssueStateRvData,
                               val issueDetailsRvData: IssueDetailsRvData
) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)

}

data class IssueCommonRvData(val name: String, val summary: String, val priority: String, val updated: String, val inFavorites: Boolean, val priorityColor: FieldColor, val isDone: Boolean)

data class IssueStateRvData(val type: String, val state: String, val assigneeName: String, val reporterName: String, val typeColor: FieldColor, val stateColor: FieldColor)

data class IssueDetailsRvData(val commentsCount: String, val spentTimeText: String, val description: String,val spentTimeMinutes: Int,val estimationMinutes: Int)

data class IssueFilterSuggestionRvItem(val issueFilterSuggestionRvData: IssueFilterSuggestionRvData) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)

}

data class IssueFilterSuggestionChildRvItem(val parentUuid: String,val issueFilterSuggestionRvData: IssueFilterSuggestionRvData) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)

}

data class IssueFilterSuggestionEmpty(val parentUuid: String) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)
}

data class IssueFilterSuggestionRvData(val name: String,val uuid: String,var checked: Boolean)

data class TextRvItem(val text: String) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)

}

data class IssueFullSearchSuggestionRvItem(val option: String,val prefix: String,val suffix: String) : BaseRvItem(){

	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)

}

@Parcelize
data class BaseSelectListResult(val id: String,val name: String,val options: @RawValue Map<String,Any> = mapOf()) :
	Parcelable

@Parcelize
data class BaseSelectRvItem(val id: String,val name:String) : ParcelableRvItem(){
	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)
}


@Parcelize
data class SelectUserRvItem(val id: String,val name:String,val avatarUrl: String,val initials: String) : ParcelableRvItem() {
	override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)
}


private fun setText(tvTextView: TextView,text: String){
	tvTextView.text = text
}

private fun handleIssueFavorites(imageView: ImageView,isInFavorites: Boolean){
	if(isInFavorites){
		imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context,R.drawable.ic_star_favorites_24dp))
	}else{
		imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context,R.drawable.ic_star_not_favorites_24dp))
	}
}

private fun handleIsDoneState(issueNumber: String,isDone: Boolean, tvIssueNumber: TextView, tvIssueTitle: TextView){
	if(isDone){
		tvIssueNumber.setTextColor(ContextCompat.getColor(tvIssueNumber.context,R.color.secondary_text))
		tvIssueTitle.setTextColor(ContextCompat.getColor(tvIssueNumber.context,R.color.secondary_text))
		val span = SpannableString(issueNumber)
		span.setSpan(StrikethroughSpan(),0,span.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
		tvIssueNumber.text = span
	}else{
		tvIssueNumber.text = issueNumber
		tvIssueTitle.setTextColor(ContextCompat.getColor(tvIssueNumber.context,R.color.primary_text))
		tvIssueNumber.setTextColor(ContextCompat.getColor(tvIssueNumber.context,R.color.primary_text))
	}

}

private fun handleIssueColor(tv: TextView, color: FieldColor){
	val drawable = tv.background as GradientDrawable
	if(color.default){
		drawable.setColor(ContextCompat.getColor(tv.context, R.color.primary))
		tv.setTextColor(ContextCompat.getColor(tv.context,android.R.color.white))
	}else{
		if(color.isWhiteBackground){
			drawable.setColor(ContextCompat.getColor(tv.context,android.R.color.white))
			tv.setTextColor(ContextCompat.getColor(tv.context,R.color.primary_text))
		}else{
			if(color.background.isNotEmpty()){
				drawable.setColor(Color.parseColor(color.background))
			}
			if(color.foreground.isNotEmpty()){
				tv.setTextColor(ColorStateList.valueOf(Color.parseColor(color.foreground)))
			}

		}
	}
}


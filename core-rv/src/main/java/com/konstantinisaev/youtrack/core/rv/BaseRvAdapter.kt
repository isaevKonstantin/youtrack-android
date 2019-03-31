package com.konstantinisaev.youtrack.core.rv

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BaseRvAdapter(private var clickListener: BaseRvClickListener? = null) : RecyclerView.Adapter<BaseRvViewHolder<BaseRvItem>>() {

    private val data = mutableListOf<BaseRvItem>()
    private val rvTypeFactory = RvTypeFactory()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseRvViewHolder<BaseRvItem>, position: Int) {
        holder.bind(data[position],clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder<BaseRvItem> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return createViewHolder(viewType, view)
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type(rvTypeFactory)
    }

    fun addAll(items: List<BaseRvItem>){
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun addAllByPosition(position: Int,items: List<BaseRvItem>){
        data.addAll(position,items)
        notifyItemRangeChanged(position,position + items.size)
    }

    fun addByPosition(position: Int,item: BaseRvItem){
        data.add(position,item)
        notifyItemChanged(position)
    }

    fun add(item: BaseRvItem){
        data.add(item)
        notifyItemChanged(data.size - 1)
    }

    fun update(position: Int, rvItem: BaseRvItem){
        data[position] = rvItem
        notifyItemChanged(position)
    }

    fun isNotEmpty() = data.isNotEmpty()

    fun isEmpty() = data.isEmpty()

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun find(predicate: (BaseRvItem?) -> Boolean) = data.find(predicate)

    fun findPosition(predicate: (BaseRvItem?) -> Boolean) = data.indexOfLast(predicate)

    fun filter(predicate: (BaseRvItem) -> Boolean) = data.filter(predicate)

    fun removeAll(predicate: (BaseRvItem) -> Boolean){
        data.removeAll(predicate)
        notifyDataSetChanged()
    }

    fun getAll() = data
}

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
private fun createViewHolder(type: Int, view: View) : BaseRvViewHolder<BaseRvItem> {
    val holder = when(type){
//        R.layout.vh_nav_profile -> NavProfileViewHolder(view)
        R.layout.vh_nav_text_item -> NavTextViewHolder(view)
//        R.layout.vh_issue_single -> IssueMainViewHolder(view)
//        R.layout.vh_text_header -> TextHeaderViewHolder(view)
//        R.layout.vh_issue_compact -> IssueCompactViewHolder(view)
//        R.layout.vh_issue_detailed -> IssueDetailedViewHolder(view)
//        R.layout.vh_issue_filter_suggestion -> IssueFilterMainSuggestionViewHolder(view)
//        R.layout.vh_issue_filter_suggestion_child -> IssueFilterChildSuggestionViewHolder(view)
//        R.layout.vh_issue_filter_suggestion_empty -> EmptyViewHolder(view)
//        R.layout.vh_suggestion_full_search -> IssueSuggestionFullSearchViewHolder(view)
//        R.layout.vh_select_list -> SelectListViewHolder(view)
//        R.layout.vh_select_user -> SelectUserViewHolder(view)
        else -> throw IllegalArgumentException("Wrong type of view holder!")
    }
    return holder as BaseRvViewHolder<BaseRvItem>

}

interface BaseRvClickListener {

    fun onItemClickListener(rvItem: BaseRvItem)
}

abstract class BaseRvItem{

    abstract fun type(rvTypeFactory: RvTypeFactory) : Int
}

abstract class ParcelableRvItem : BaseRvItem(), Parcelable

abstract class BaseRvViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView){

    abstract fun bind(rvItem: T,clickListener: BaseRvClickListener? = null)

    protected fun setInitials(imgLogo: ImageView, tvInitials: TextView, initial: String){
        tvInitials.text = initial
        tvInitials.visibility = View.VISIBLE
        imgLogo.visibility = View.INVISIBLE
    }

}

@Suppress("UNUSED_PARAMETER")
class RvTypeFactory{

//    fun type(rvItem: NavProfileRvItem) = R.layout.vh_nav_profile
//
    fun type(rvItem: NavTextRvItem) = R.layout.vh_nav_text_item
//
//    fun type(rvItem: IssueRvItem) = R.layout.vh_issue_single
//
//    fun type(rvItem: TextRvItem) = R.layout.vh_text_header
//
//    fun type(rvItem: IssueCompactRvItem) = R.layout.vh_issue_compact
//
//    fun type(rvItem: IssueDetailedRvItem) = R.layout.vh_issue_detailed
//
//    fun type(rvItem: IssueFilterSuggestionRvItem) = R.layout.vh_issue_filter_suggestion
//
//    fun type(rvItem: IssueFilterSuggestionChildRvItem) = R.layout.vh_issue_filter_suggestion_child
//
//    fun type(rvItem: IssueFilterSuggestionEmpty) = R.layout.vh_issue_filter_suggestion_empty
//
//    fun type(rvItem: IssueFullSearchSuggestionRvItem) = R.layout.vh_suggestion_full_search
//
//    fun type(rvItem: BaseSelectRvItem) = R.layout.vh_select_list
//
//    fun type(rvItem: SelectUserRvItem) = R.layout.vh_select_user

}


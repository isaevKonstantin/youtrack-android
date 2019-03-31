package com.konstantinisaev.youtrack.core.rv

import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_nav_text_item.*

data class NavTextRvItem(val text: String) : BaseRvItem(){

    override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)
}

class NavTextViewHolder(override val containerView: View) : BaseRvViewHolder<NavTextRvItem>(containerView),
    LayoutContainer {

    override fun bind(rvItem: NavTextRvItem, clickListener: BaseRvClickListener?) {
        tvNavText.text = rvItem.text
        itemView.setOnClickListener{ clickListener?.onItemClickListener(rvItem) }
    }
}


package com.konstantinisaev.youtrack.core.rv

import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_nav_profile.*
import kotlinx.android.synthetic.main.vh_nav_text_item.*

data class NavTextRvItem(val text: String) : BaseRvItem(){

    override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)
}

data class NavProfileRvItem(val name: String = "", val initials: String = "", val imageUrl: String = "") : BaseRvItem(){

    override fun type(rvTypeFactory: RvTypeFactory) = rvTypeFactory.type(this)
}

class NavTextViewHolder(override val containerView: View) : BaseRvViewHolder<NavTextRvItem>(containerView),LayoutContainer {

    override fun bind(rvItem: NavTextRvItem, clickListener: BaseRvClickListener?) {
        tvNavText.text = rvItem.text
        itemView.setOnClickListener{ clickListener?.onItemClickListener(rvItem) }
    }
}

class NavProfileViewHolder(override val containerView: View) : BaseRvViewHolder<NavProfileRvItem>(containerView),LayoutContainer {

    override fun bind(rvItem: NavProfileRvItem, clickListener: BaseRvClickListener?) {
        if(rvItem.name.isEmpty() && rvItem.imageUrl.isEmpty() && rvItem.initials.isEmpty()){
            return
        }
        if(rvItem.imageUrl.isNotEmpty()){
            Picasso.with(itemView.context).load(rvItem.imageUrl)
                .fit()
                .centerCrop()
                .transform(CropCircleTransformation())
                .into(imgProfileLogo,object : Callback {
                    override fun onSuccess() {}

                    override fun onError() {
                        setInitials(imgProfileLogo,tvProfileInitials,rvItem.initials)
                    }
                })
            tvProfileInitials.visibility = View.GONE
            imgProfileLogo.visibility = View.VISIBLE
        }else{
            setInitials(imgProfileLogo,tvProfileInitials,rvItem.initials)
        }
        tvNavProfileName.text = rvItem.name
    }

}



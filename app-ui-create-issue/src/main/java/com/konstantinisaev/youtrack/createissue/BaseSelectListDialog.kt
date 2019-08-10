package com.konstantinisaev.youtrack.createissue

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import kotlinx.android.synthetic.main.dialog_select_list.*

class BaseSelectListDialog : DialogFragment(){

	private lateinit var selectorListener: (String) -> Unit
	private lateinit var param: Param

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.dialog_select_list,container,false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		tlbBaseSelect.title = param.title
		rvSelectList.layoutManager = LinearLayoutManager(context!!)
		rvSelectList.addItemDecoration(object : RecyclerView.ItemDecoration() {
			override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
				super.getItemOffsets(outRect, view, parent, state)
				outRect.set(DeviceUtils.convertDpToPixel(16f, checkNotNull(context)), 0, DeviceUtils.convertDpToPixel(16f, checkNotNull(context)), 0)
			}
		})
		val itemDecor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		rvSelectList.addItemDecoration(itemDecor)
		val adapter = BaseRvAdapter(clickListener = object : BaseRvClickListener {
			override fun onItemClickListener(rvItem: BaseRvItem) {
				if(rvItem is BaseSelectRvItem){
					selectorListener.invoke(rvItem.id)
				}else if(rvItem is SelectUserRvItem){
					selectorListener.invoke(rvItem.id)
				}
				dismiss()
			}
		})
		val data = param.data
		rvSelectList.adapter = adapter
		adapter.addAll(data)
	}

	class Builder {

		private lateinit var param: Param
		private lateinit var selectorListener: (String) -> Unit

		fun setParam(param: Param): Builder {
			this.param = param
			return this
		}

		fun setSelectorListener(listener: (String) -> Unit): Builder {
			this.selectorListener = listener
			return this
		}

		fun build(): BaseSelectListDialog {
			val dialog = BaseSelectListDialog()
			dialog.let {
				it.param = param
				it.selectorListener = selectorListener
			}
			return dialog
		}

	}

	class Param(val title: String,val data: List<BaseRvItem>)
}

package com.konstantinisaev.youtrack.createissue

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konstantinisaev.youtrack.core.rv.*
import com.konstantinisaev.youtrack.ui.base.utils.DeviceUtils
import com.konstantinisaev.youtrack.ui.base.utils.Extra
import kotlinx.android.synthetic.main.dialog_select_list.*
import java.io.Serializable


class BaseSelectListDialog : DialogFragment(){

	private val data = mutableListOf<ParcelableRvItem>()
	private var title = ""

	@Suppress("UNCHECKED_CAST")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		title = arguments?.getString(Extra.SELECT_LIST_TITLE).orEmpty()
		val args = arguments?.getParcelableArrayList<ParcelableRvItem>(Extra.SELECT_LIST_ITEMS).orEmpty()
		data.addAll(args)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.dialog_select_list,container,false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		tlbBaseSelect.title = title
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
					handleClick(rvItem.id,rvItem.name)
				}else if(rvItem is SelectUserRvItem){
					handleClick(rvItem.id,rvItem.name)
				}

			}
		})
		adapter.addAll(data)
		rvSelectList.adapter = adapter
	}

	@Suppress("UNCHECKED_CAST")
	private fun handleClick(id: String,name: String) {
		val intent = Intent()
		intent.putExtra(RESULT, BaseSelectListResult(id, name, arguments?.getSerializable(Extra.SELECT_LIST_OPTIONS) as @kotlinx.android.parcel.RawValue Map<String, Any>))
		targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
		dismiss()
	}

	companion object {

		const val RESULT = "result"

		fun newInstance(title:String, data: List<ParcelableRvItem>, options: Map<String, Any>) = BaseSelectListDialog().apply {
			val bundle = Bundle()
			bundle.putString(Extra.SELECT_LIST_TITLE, title)
			bundle.putParcelableArrayList(Extra.SELECT_LIST_ITEMS, ArrayList(data) as ArrayList<out Parcelable>)
			bundle.putSerializable(Extra.SELECT_LIST_OPTIONS, options as Serializable)
			this.arguments = bundle
		}
	}
}

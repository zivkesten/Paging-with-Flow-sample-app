package com.zk.testapp.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zk.testapp.mainLIst.OnItemClickListener
import com.zk.testapp.mainLIst.VerticalSpaceItemDecoration
import com.zk.testapp.R
import com.zk.testapp.databinding.FragmentPhotoListBinding
import com.zk.testapp.mainLIst.PhotosRecyclerViewAdapter
import com.zk.testapp.model.Event
import com.zk.testapp.model.ListViewState
import com.zk.testapp.model.Photo
import com.zk.testapp.viewModel.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
class PhotoListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	companion object {
		fun newInstance(): PhotoListFragment {
			return PhotoListFragment()
		}
	}

	private lateinit var scrollListener: RecyclerView.OnScrollListener

	private lateinit var binding: FragmentPhotoListBinding

	private val photosAdapter: PhotosRecyclerViewAdapter = PhotosRecyclerViewAdapter(listener = this)

	// Lazy Inject ViewModel
	private val viewModel by sharedViewModel<MainViewModel>()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentPhotoListBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupBinding()
		observeViewState()
		if (savedInstanceState == null) {
			lifecycleScope.launch {
				viewModel.onSuspendedEvent(Event.ScreenLoad)
			}
		}
	}

	private fun setScrollToTopWHenRefreshedFromNetwork() {
		// Scroll to top when the list is refreshed from network.
		lifecycleScope.launch {
			photosAdapter.loadStateFlow
				// Only emit when REFRESH LoadState for RemoteMediator changes.
				.distinctUntilChangedBy { it.refresh }
				// Only react to cases where Remote REFRESH completes i.e., NotLoading.
				.filter { it.refresh is LoadState.NotLoading }
				.collect { binding.list.scrollToPosition(0) }
		}
	}

	private fun setupBinding() {
		binding.swiperefresh.setOnRefreshListener(this)
		binding.list.apply {
			layoutManager = LinearLayoutManager(context)
			addItemDecoration(VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_decoration)))
			adapter = photosAdapter

		}
		//setRecyclerViewScrollListener(binding.list)
		// TODO: detect scroll to end and send EVENT.LoadNextPageEvent
	}

//	--- Here i was supposed to listen to recycler view end point --

//	private fun setRecyclerViewScrollListener(list: RecyclerView) {
//		scrollListener = object : RecyclerView.OnScrollListener() {
//			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//				super.onScrollStateChanged(list, newState)
//				val totalItemCount = list.layoutManager?.itemCount
//				if (totalItemCount == lastVisibleItemPosition + 1) {
//					Log.d("Zivi", "Load new list")
//					list.removeOnScrollListener(scrollListener)
//				}
//			}
//		}
//		list.addOnScrollListener(scrollListener)
//	}

	private fun observeViewState() {
		viewModel.obtainState.observe(viewLifecycleOwner, {
			Log.d("Zivi", "observeViewState obtainState result: ${it.adapterList.size}")
			render(it)
		})
	}

	private fun render(state: ListViewState) {
		state.loadingStateVisibility?.let { binding.progressBar.visibility = it }
		lifecycleScope.launch {
			state.page?.let { photosAdapter.submitData(it) }
		}
	}

	override fun onRefresh() {
		viewModel.onEvent(Event.SwipeToRefreshEvent)
	}

	override fun onItemClick(item: Photo) {
		// TODO: implement click
		//viewModel.event(Event.ListItemClicked(item))
	}
}

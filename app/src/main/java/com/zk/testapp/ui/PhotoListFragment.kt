package com.zk.testapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zk.testapp.R
import com.zk.testapp.databinding.FragmentPhotoListBinding
import com.zk.testapp.ui.list.callback.OnItemClickListener
import com.zk.testapp.ui.list.PhotosLoadStateAdapter
import com.zk.testapp.ui.list.adapter.PhotosRecyclerViewAdapter
import com.zk.testapp.ui.list.style.VerticalSpaceItemDecoration
import com.zk.testapp.model.Event
import com.zk.testapp.model.ListViewState
import com.zk.testapp.model.Photo
import com.zk.testapp.viewModel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
class PhotoListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	companion object {
		private val TAG = PhotoListFragment::class.qualifiedName
		fun newInstance(): PhotoListFragment {
			return PhotoListFragment()
		}
	}

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

	private fun setupBinding() {
		binding.swiperefresh.setOnRefreshListener(this)
		binding.list.apply {
			layoutManager = LinearLayoutManager(context)
			addItemDecoration(VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_decoration)))
			initAdapter()
		}
		binding.retryButton.setOnClickListener { Log.d("Zivi", "click") }
	}

	private fun initAdapter() {
		binding.list.adapter = photosAdapter.withLoadStateHeaderAndFooter(
			header = PhotosLoadStateAdapter { photosAdapter.retry() },
			footer = PhotosLoadStateAdapter { photosAdapter.retry() }
		)
		photosAdapter.addLoadStateListener {
			Log.d("Zivi", "loading state: ${it.toString()}")
			viewModel.onEvent(Event.LoadState(it))
		}
		setScrollToTopWHenRefreshedFromNetwork()
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

	private fun observeViewState() {
		viewModel.obtainState.observe(viewLifecycleOwner, {
			Log.d(TAG, "observeViewState obtainState result: ${it.adapterList.size}")
			render(it)
		})
	}

	private fun render(state: ListViewState) {
		binding.swiperefresh.isRefreshing = false
		state.loadingStateVisibility?.let { binding.progressBar.visibility = it }
		lifecycleScope.launch {
			state.page?.let { photosAdapter.submitData(it) }
		}
		state.errorVisibility?.let {
			binding.mainListErrorMsg.visibility = it
			binding.retryButton.visibility = it
			state.errorMessage?.let { binding.mainListErrorMsg.text = state.errorMessage }
			state.errorMessageResource?.let { binding.mainListErrorMsg.text = getString(state.errorMessageResource) }
		}
	}

	override fun onRefresh() {
		lifecycleScope.launch {
			viewModel.onSuspendedEvent(Event.ScreenLoad)
		}
	}

	override fun onItemClick(item: Photo) {
		// TODO: implement click
		//viewModel.event(Event.ListItemClicked(item))
	}
}

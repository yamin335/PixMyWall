package mollah.yamin.pixmywall.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mollah.yamin.pixmywall.databinding.FragmentPixWallBinding
import mollah.yamin.pixmywall.models.PixData
import mollah.yamin.pixmywall.models.UiAction
import mollah.yamin.pixmywall.models.UiState
import mollah.yamin.pixmywall.paging3.RemotePresentationState
import mollah.yamin.pixmywall.paging3.asRemotePresentationState
import mollah.yamin.pixmywall.ui.vm.GalleryViewModel
import mollah.yamin.pixmywall.ui.adapters.PixDataPagingAdapter
import mollah.yamin.pixmywall.ui.adapters.PixDataPagingLoadStateAdapter
import mollah.yamin.pixmywall.ui.base.BaseFragment
import mollah.yamin.pixmywall.ui.dialog.CmnUserConsentDialog
import mollah.yamin.pixmywall.utils.hideKeyboard

@AndroidEntryPoint
class PixWallFragment : BaseFragment() {
    private lateinit var binding: FragmentPixWallBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var consentDialog: CmnUserConsentDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                showClosingDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPixWallBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        
        registerToolbar(binding.toolbar)

        // bind the state
        binding.bindState(
            uiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )
    }

    /**
     * Binds the [UiState] provided  by the [GalleryViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun FragmentPixWallBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<PixData>>,
        uiActions: (UiAction) -> Unit
    ) {
        val orientation = resources.configuration.orientation

        val staggeredGridLayoutManager = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } else {
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        }
        val repoAdapter = PixDataPagingAdapter { pixData ->
            pixData?.let {
                showDetails(it)
            }
        }
        val header = PixDataPagingLoadStateAdapter { repoAdapter.retry() }
        pixWallRecycler.layoutManager = staggeredGridLayoutManager
        pixWallRecycler.adapter = repoAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = PixDataPagingLoadStateAdapter { repoAdapter.retry() }
        )
        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            header = header,
            repoAdapter = repoAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
        swipeRefresh.setOnRefreshListener {
            repoAdapter.refresh()
        }
    }

    private fun showDetails(pixData: PixData) {
        consentDialog = CmnUserConsentDialog(object : CmnUserConsentDialog.UserConsentActionListener {
            override fun onCancelPressed() {
                consentDialog.dismiss()
            }

            override fun onOkPressed() {
                navigateTo(PixWallFragmentDirections.actionPixWallFragmentToPreviewFragment(pixData))
                consentDialog.dismiss()
            }
        }, title = "Full Image Preview", subTitle = "Want to see more details with large photo preview?")
        consentDialog.show(parentFragmentManager, "#user_consent_dialog")
    }

    private fun showClosingDialog() {
        consentDialog = CmnUserConsentDialog(object : CmnUserConsentDialog.UserConsentActionListener {
            override fun onCancelPressed() {
                consentDialog.dismiss()
            }

            override fun onOkPressed() {
                consentDialog.dismiss()
                requireActivity().finish()
            }
        }, title = "Are you sure?", subTitle = "Are you sure to leave now?")
        consentDialog.show(parentFragmentManager, "#user_consent_closing_dialog")
    }

    private fun FragmentPixWallBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                hideKeyboard()
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        searchInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        // For activity use only `lifecycleScope`
        viewLifecycleOwner.lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(searchInput::setText)
        }
    }

    private fun FragmentPixWallBinding.updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        searchInput.text?.trim().let {
            if (!it.isNullOrBlank()) {
                pixWallRecycler.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString().trim()))
            }
        }
    }

    private fun FragmentPixWallBinding.bindList(
        header: PixDataPagingLoadStateAdapter,
        repoAdapter: PixDataPagingAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<PixData>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        retryButton.setOnClickListener { repoAdapter.retry() }
        pixWallRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                hideKeyboard()
                searchInputLayout.clearFocus()
            }
        })
        val notLoading = repoAdapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            pagingData.collectLatest(repoAdapter::submitData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) pixWallRecycler.scrollToPosition(0)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repoAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && repoAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty = loadState.refresh is LoadState.NotLoading && repoAdapter.itemCount == 0
                // show empty list
                emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                pixWallRecycler.isVisible =  loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                swipeRefresh.isRefreshing = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error && repoAdapter.itemCount == 0
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        mContext,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
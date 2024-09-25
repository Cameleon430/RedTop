package com.example.redtop.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.redtop.R
import com.example.redtop.databinding.FragmentHomeBinding
import com.example.redtop.presenter.base.PublicationViewState
import com.example.redtop.presenter.home.HomeViewModel.ActionState
import com.example.redtop.presenter.home.HomeViewModel.ActionState.None
import com.example.redtop.presenter.home.HomeViewModel.ActionState.PublicationView
import com.example.redtop.presenter.home.HomeViewModel.ActionState.ShowMessage
import com.example.redtop.presenter.image.ImageFragment

class HomeFragment : Fragment(), HomeViewItemAdapter.OnItemSelectListener {

    //region Properties

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = HomeViewItemAdapter(this)

    //endregion

    //region LifeCycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewModel()
        initializeRecyclerView()
        viewModel.responsePublications(URL)
    }

    //endregion

    //region Actions

    private fun initializeRecyclerView() {
        with(binding){
            publicationsRecyclerView.adapter = adapter
        }
    }

    private fun initializeViewModel() {
        viewModel.actionState.observe(viewLifecycleOwner, this::onActionStateChanged)
        viewModel.viewItems.observe(viewLifecycleOwner, this::onViewItemsChanged)
        viewModel.onUpdateViewState()
    }

    private fun onViewItemsChanged(publications: List<PublicationViewState>) {
        adapter.updateItemsView(publications)
    }

    private fun onActionStateChanged(actionState: ActionState) {
        when(actionState){
            is PublicationView -> {
                onNavigateToImageFragment(actionState.publicationID)
            }
            None -> {
                //Ignoring this case
            }
            ShowMessage -> {
                showMessage("Message")
            }
        }
    }

    private fun onNavigateToImageFragment(publicationID: Int) {
        val fragment = ImageFragment.newInstance(publicationID)
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragment_container_view, fragment)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }
    //endregion

    //region Nested
    companion object {
        const val URL: String = "https://www.reddit.com/top.json?t=today&limit=100"
    }

    override fun onClick(publication: PublicationViewState){
        viewModel.onItemView(publication)
    }
    //endregion

}
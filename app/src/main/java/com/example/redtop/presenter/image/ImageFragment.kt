package com.example.redtop.presenter.image

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.redtop.R
import com.example.redtop.databinding.FragmentImageBinding
import com.example.redtop.presenter.image.ImageViewModel.ActionState
import com.squareup.picasso.Picasso


class ImageFragment : Fragment() {

    //region Properties

    private lateinit var binding: FragmentImageBinding
    private val viewModel by viewModels<ImageViewModel>()

    //endregion

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewModel()
        initializeListeners()
    }

    //endregion

    //region Actions

    private fun initializeListeners(){
        with(binding){
            saveImageButton.setOnClickListener{
                viewModel.onSaveImage()
            }
        }
    }


    private fun initializeViewModel() {
        val publicationID = arguments?.getInt(PUBLICATION_ID) ?: throw IllegalStateException("Missing group id")
        viewModel.onUpdateViewState(publicationID)

        viewModel.actionState.observe(viewLifecycleOwner, this::onActionStateChanged)
    }

    private fun onActionStateChanged(actionState: ActionState){
        when(actionState){
            is ActionState.SetImage -> {
                setImage(actionState.publicationMedia)
            }
            ActionState.None -> {
                //This case ignored
            }
            ActionState.ShowMessage -> {
                Toast.makeText(context, getString(R.string.button_toast_template), Toast.LENGTH_SHORT).show()
            }
            ActionState.Back -> {
                parentFragmentManager.popBackStack()
            }
            is ActionState.SaveImage -> {
                saveImage(actionState.publicationMedia)
            }
        }
    }

    private fun saveImage(publicationMedia: String) {
        val request = DownloadManager.Request(Uri.parse(publicationMedia))
        val title = URLUtil.guessFileName(publicationMedia, null, null)
        request.setTitle(title)
        request.setDescription("Downloading...")
        val cookie = CookieManager.getInstance().getCookie(publicationMedia)
        request.addRequestHeader("cookie",cookie)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)

        val downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun setImage(publicationMedia: String) {
        Picasso.get().load(publicationMedia).into(binding.publicationMediaImageView)
    }

    //endregion

    //region Nested

    companion object{
        private const val PUBLICATION_ID = "PUBLICATION_ID"

        fun newInstance(publicationId: Int): ImageFragment {
            return ImageFragment().apply{
                arguments = bundleOf(PUBLICATION_ID to publicationId)
            }
        }
    }

    //endregion
}
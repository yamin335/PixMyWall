package mollah.yamin.pixmywall.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import mollah.yamin.pixmywall.databinding.FragmentPreviewBinding
import mollah.yamin.pixmywall.ui.base.BaseFragment

class PreviewFragment : BaseFragment() {
    private lateinit var binding: FragmentPreviewBinding
    private val args: PreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPreviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //registerToolbar(binding.toolbar)
        binding.btnBack.setOnClickListener { popBackStack() }
    }
}
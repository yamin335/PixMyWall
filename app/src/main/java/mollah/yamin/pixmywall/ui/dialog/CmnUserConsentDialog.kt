package mollah.yamin.pixmywall.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mollah.yamin.pixmywall.R
import mollah.yamin.pixmywall.databinding.CmnUserConsentDialogBinding

class CmnUserConsentDialog constructor(
    private val callback: UserConsentActionListener,
    private val title: String = "", private val subTitle: String = "",
    private val cancelButtonText: String = "", private val okButtonText: String = "") : BottomSheetDialogFragment() {

    private lateinit var binding: CmnUserConsentDialogBinding

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        //this forces the sheet to appear at max height even on landscape
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = CmnUserConsentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title = title
        binding.subTitle = subTitle

        if (cancelButtonText.isNotEmpty()) {
            binding.cancelButton.text = cancelButtonText
        }

        if (okButtonText.isNotEmpty()) {
            binding.okButton.text = okButtonText
        }

        binding.cancelButton.setOnClickListener {
            callback.onCancelPressed()
        }

        binding.okButton.setOnClickListener {
            callback.onOkPressed()
        }
    }

    interface UserConsentActionListener {
        fun onCancelPressed()
        fun onOkPressed()
    }
}
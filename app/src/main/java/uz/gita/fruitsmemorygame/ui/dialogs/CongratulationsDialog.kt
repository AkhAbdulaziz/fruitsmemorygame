package uz.gita.fruitsmemorygame.ui.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import uz.gita.fruitsmemorygame.R
import uz.gita.fruitsmemorygame.databinding.DialogCongratulationBinding
import uz.gita.fruitsmemorygame.domain.AppRepository

class CongratulationsDialog : DialogFragment(R.layout.dialog_congratulation) {
    private val repository = AppRepository()

    private var _binding: DialogCongratulationBinding? = null
    private val binding get() = _binding!!
    private var okButtonPressedListener: (() -> Unit)? = null
    fun setOkButtonPressedListener(f: () -> Unit) {
        okButtonPressedListener = f
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DialogCongratulationBinding.bind(view)
        isCancelable = false

        // dialog ni background ini transparent qilish uchun
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.timeText.text = "Time: ${repository.currentTime}"
        binding.gameModeText.text = "Game Mode: ${repository.gameMode}"

        binding.okButton.setOnClickListener {
            okButtonPressedListener?.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
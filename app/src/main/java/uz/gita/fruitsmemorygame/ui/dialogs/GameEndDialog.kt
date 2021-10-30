package uz.gita.fruitsmemorygame.ui.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import uz.gita.fruitsmemorygame.R
import uz.gita.fruitsmemorygame.databinding.DialogGameEndBinding

class GameEndDialog : DialogFragment(R.layout.dialog_game_end) {
    private var _binding: DialogGameEndBinding? = null
    private val binding get() = _binding!!

    private var listener: ((Boolean) -> Unit)? = null
    fun setListener(f: (Boolean) -> Unit) {
        listener = f
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DialogGameEndBinding.bind(view)
        isCancelable = false

        // dialog ni background ini transparent qilish uchun
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.yesButton.setOnClickListener {
            listener?.invoke(true)
            dismiss()
        }

        binding.noButton.setOnClickListener {
            listener?.invoke(false)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
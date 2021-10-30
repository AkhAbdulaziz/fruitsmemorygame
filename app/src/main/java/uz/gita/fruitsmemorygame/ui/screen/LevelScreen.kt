package uz.gita.fruitsmemorygame.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fruitsmemorygame.R
import uz.gita.fruitsmemorygame.app.App
import uz.gita.fruitsmemorygame.databinding.ScreenLevelBinding
import uz.gita.fruitsmemorygame.util.scope

@AndroidEntryPoint
class LevelScreen : Fragment(R.layout.screen_level) {
    private val binding by viewBinding(ScreenLevelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        easyButton.setOnClickListener { openGameScreen(3, 4) }
        mediumButton.setOnClickListener { openGameScreen(4, 5) }
        hardButton.setOnClickListener { openGameScreen(5, 6) }

        // Share App
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            val link =
                "Play just now!: https://play.google.com/store/apps/details?id=${App.instance.packageName}"
            intent.putExtra(Intent.EXTRA_TEXT, link)
            requireActivity().startActivity(Intent.createChooser(intent, "Share:"))
        }
        // Our Games sharing
        moreGamesButton.setOnClickListener {
            ourApps()
        }
    }

    private fun ourApps() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse(
                "https://play.google.com/store/apps/developer?id=GITA+Dasturchilar+Akademiyasi"
            )
        startActivity(intent)
    }

    private fun openGameScreen(x: Int, y: Int) {
        findNavController().navigate(LevelScreenDirections.actionLevelScreenToGameScreen(x, y))
    }
}

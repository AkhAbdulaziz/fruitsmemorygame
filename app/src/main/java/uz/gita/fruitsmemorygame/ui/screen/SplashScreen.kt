package uz.gita.fruitsmemorygame.ui.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fruitsmemorygame.R

@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            findNavController().navigate(R.id.action_splashScreen_to_levelScreen)
        }, 3000L)
    }
}
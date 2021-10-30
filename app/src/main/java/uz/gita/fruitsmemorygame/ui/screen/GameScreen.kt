package uz.gita.fruitsmemorygame.ui.screen

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fruitsmemorygame.R
import uz.gita.fruitsmemorygame.data.ImageData
import uz.gita.fruitsmemorygame.databinding.ScreenGameBinding
import uz.gita.fruitsmemorygame.ui.dialogs.CongratulationsDialog
import uz.gita.fruitsmemorygame.ui.dialogs.GameEndDialog
import uz.gita.fruitsmemorygame.ui.viewmodel.GameViewModel
import uz.gita.fruitsmemorygame.util.*
import java.util.concurrent.Executors

@AndroidEntryPoint
class GameScreen : Fragment(R.layout.screen_game) {
    private val binding by viewBinding(ScreenGameBinding::bind)
    private val viewModel: GameViewModel by viewModels()
    private val imageList = ArrayList<ImageView>()
    private var x = 0
    private var y = 0
    private var _height = 0
    private var _width = 0
    private var firstPos = -1
    private var secondPos = -1
    private lateinit var handler: Handler
    private var imageCount = 0
    private var isAnimated = false
    private val args: GameScreenArgs by navArgs()
    private var baseTime = 60
    private var time = 0
    private var isDialogOpened = false
    private var needToCount = true
    private lateinit var mpTimerTickTick: MediaPlayer
    private lateinit var mpTimeEnd: MediaPlayer
    private lateinit var mpAnswerCorrect: MediaPlayer
    private lateinit var mpAnswerWrong: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        time = 0
        loadGame()
    }

    override fun onResume() {
        super.onResume()
        needToCount = true
    }

    private fun timeManager() = binding.scope {
        mpTimerTickTick = MediaPlayer.create(requireContext(), R.raw.timer_progress)
        mpTimeEnd = MediaPlayer.create(requireContext(), R.raw.time_end)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        timeText.setTextColor(resources.getColor(R.color.thirdColor))
        executor.execute {
            while (time < baseTime) {
                if (needToCount) {
                    handler.post {
                        timeText.text = "$time/$baseTime seconds"
                    }
                    time++
                    if (time > baseTime - 10) {
                        handler.post {
                            if (time % 2 == 0) {
                                timeText.setTextColor(resources.getColor(R.color.red))
                            } else {
                                timeText.setTextColor(resources.getColor(R.color.thirdColor))
                            }
                        }
                        mpTimerTickTick.start()
                    } else {
                        timeText.setTextColor(resources.getColor(R.color.thirdColor))
                    }
                    Thread.sleep(1000)
                }
            }
            if (time == baseTime) {
                if (!isDialogOpened) {
                    mpTimeEnd.start()
                    needToCount = false
                    mpTimerTickTick.pause()
                    time = baseTime
                    val gameEndDialog = GameEndDialog()
                    gameEndDialog.setListener {
                        if (it) {
                            restartGame()
                        } else {
                            findNavController().popBackStack()
                        }
                    }
                    gameEndDialog.show(childFragmentManager, "GameEndDialog")
                    isDialogOpened = true
                }
            }
        }
    }

    private fun loadGame() = binding.scope {
        handler = Handler(Looper.getMainLooper())
        y = args.height
        x = args.width
        if (x == 3 && y == 4) {
            viewModel.setGameMode("EASY")
            baseTime = 60
        } else if (x == 4 && y == 5) {
            viewModel.setGameMode("MIDDLE")
            baseTime = 180
        } else {
            viewModel.setGameMode("HARD")
            baseTime = 300
        }
        imageCount = x * y
        main.post {
            _height = main.height / (y + 3)
            _width = main.width / (x + 1)
            container.layoutParams.apply {
                height = y * _height
                width = x * _width
            }
            loadViews()
            viewModel.loadImages(imageCount)
        }
        viewModel.imageLiveData.observe(viewLifecycleOwner, imagesObserver)
    }

    private fun loadViews() {
        timeManager()
        isDialogOpened = false
        for (i in 0 until y) {
            for (j in 0 until x) {
                val image = ImageView(requireContext())
                binding.container.addView(image)

                image.layoutParams.apply {
                    height = _height
                    width = _width
                }
                image.y = i * _height * 1f
                image.x = j * _width * 1f
                /* val lp = RelativeLayout.LayoutParams(
                     RelativeLayout.LayoutParams.WRAP_CONTENT,
                     RelativeLayout.LayoutParams.WRAP_CONTENT
                 )
                 lp.setMargins(1, 1, 1, 1)
                 image.setLayoutParams(lp)*/
                image.setImageResource(R.drawable.image_back)
                image.scaleType = ImageView.ScaleType.FIT_CENTER
//                image.scaleType = ImageView.ScaleType.CENTER_CROP
                imageList.add(image)
            }
        }
    }

    private fun check() {
        mpAnswerCorrect = MediaPlayer.create(requireContext(), R.raw.answer_correct)
        mpAnswerWrong = MediaPlayer.create(requireContext(), R.raw.answer_wrong)
        val firstImageTag = imageList[firstPos].tag as ImageData
        val secondImageTag = imageList[secondPos].tag as ImageData
        if (firstImageTag.amount == secondImageTag.amount) {
            mpAnswerCorrect.start()
            baseTime += 5
            binding.bonusImg.apply {
                alpha = 1f
                visible()
            }
            handler.postDelayed({
                binding.bonusImg.hideImage {
                    scaleX = 1f
                    scaleY = 1f
                }
            }, 1000)
            handler.postDelayed({
                timber("firstPos = $firstPos")
                imageList[firstPos].remove {
                    scaleX = 1f
                    scaleY = 1f
                }
                imageList[secondPos].remove {
                    scaleX = 1f
                    scaleY = 1f
                    imageCount -= 2
                    if (imageCount == 0) finishGame()
                    firstPos = -1
                    secondPos = -1
                    isAnimated = false
                }
            }, 250)
        } else {
            mpAnswerWrong.start()
            handler.postDelayed({
                imageList[firstPos].closeAnimation()
                imageList[secondPos].closeAnimation {
                    firstPos = -1
                    secondPos = -1
                    isAnimated = false
                }
            }, 250)
        }
    }

    private fun finishGame() = binding.scope {
        for (image in imageList) {
            image.visible()
        }
        viewModel.setCurrentTime(time)
        if (!isDialogOpened) {
            needToCount = false
            mpTimerTickTick.pause()
            time = baseTime
            val congratulationsDialog = CongratulationsDialog()

            congratulationsDialog.setOkButtonPressedListener {
                findNavController().popBackStack()
            }
            congratulationsDialog.show(childFragmentManager, "GameEndDialog")
            isDialogOpened = true
        }
    }

    private fun restartGame() = binding.scope {
        time = 0
        container.removeAllViews()
        imageList.clear()
        firstPos = -1
        secondPos = -1
        isAnimated = false
        loadGame()
        needToCount = true
    }

    private val imagesObserver = Observer<List<ImageData>> {
        for (i in imageList.indices) {
            imageList[i].apply {
                tag = it[i]
                setOnClickListener {
                    if (isAnimated) return@setOnClickListener
                    if (firstPos == -1) {
                        firstPos = i
                        if (rotationY == 0f) {
                            this.openAnimation()
                        }
                    } else if (i != firstPos) {
                        isAnimated = true
                        secondPos = i
                        if (rotationY == 0f) this.openAnimation {
                            check()
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        needToCount = false
    }
}


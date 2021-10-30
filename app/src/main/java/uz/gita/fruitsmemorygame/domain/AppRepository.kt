package uz.gita.fruitsmemorygame.domain

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.fruitsmemorygame.R
import uz.gita.fruitsmemorygame.app.App
import uz.gita.fruitsmemorygame.data.ImageData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor() {
    /* companion object {
         private lateinit var instance : AppRepository
         fun getRepository() : AppRepository {
             if (!::instance.isInitialized)
                 instance = AppRepository()
             return instance
         }
     }*/

    private val pref = App.instance.getSharedPreferences("AppPref", Context.MODE_PRIVATE)
    var currentTime
        set(value) = pref.edit().putInt("CURRENT_TIME", value).apply()
        get() = pref.getInt("CURRENT_TIME", 0)

    var gameMode
        set(value) = pref.edit().putString("GAME_MODE", value).apply()
        get() = pref.getString("GAME_MODE", "EASY")

    private val data = ArrayList<ImageData>()

    init {
        loadData()
    }

    private fun loadData() {
        data.add(ImageData(R.drawable.image_1, 1))
        data.add(ImageData(R.drawable.image_2, 2))
        data.add(ImageData(R.drawable.image_3, 3))
        data.add(ImageData(R.drawable.image_4, 4))
        data.add(ImageData(R.drawable.image_5, 5))
        data.add(ImageData(R.drawable.image_6, 6))
        data.add(ImageData(R.drawable.image_7, 7))
        data.add(ImageData(R.drawable.image_8, 8))
        data.add(ImageData(R.drawable.image_9, 9))
        data.add(ImageData(R.drawable.image_10, 10))
        data.add(ImageData(R.drawable.image_11, 11))
        data.add(ImageData(R.drawable.image_12, 12))
        data.add(ImageData(R.drawable.image_13, 13))
        data.add(ImageData(R.drawable.image_14, 14))
        data.add(ImageData(R.drawable.image_15, 15))
    }

    fun getImagesData(count: Int): Flow<List<ImageData>> = flow {
        data.shuffle()
        val resultData = ArrayList<ImageData>()
        resultData.addAll(data.subList(0, count / 2))
        resultData.addAll(data.subList(0, count / 2))
        resultData.shuffle()
        emit(resultData)
    }
}
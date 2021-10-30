package uz.gita.fruitsmemorygame.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.gita.fruitsmemorygame.data.ImageData
import uz.gita.fruitsmemorygame.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
//    private val repository = AppRepository.getRepository()

    private val _imagesLiveData = MutableLiveData<List<ImageData>>()
    val imageLiveData: LiveData<List<ImageData>> get() = _imagesLiveData

    fun loadImages(count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImagesData(count).collect {
                _imagesLiveData.postValue(it)
            }
        }
    }

    fun setCurrentTime(currentTime: Int) {
        repository.currentTime = currentTime
    }

    fun setGameMode(gameMode: String) {
        repository.gameMode = gameMode
    }
}

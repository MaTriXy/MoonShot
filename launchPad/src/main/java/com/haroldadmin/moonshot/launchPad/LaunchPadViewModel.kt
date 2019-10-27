package com.haroldadmin.moonshot.launchPad

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.base.koin
import com.haroldadmin.moonshot.base.safeArgs
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LaunchPadViewModel(
    initState: LaunchPadState,
    private val launchPadUseCase: GetLaunchPadUseCase
) : MoonShotViewModel<LaunchPadState>(initState) {

    init {
        viewModelScope.launch {
            getLaunchPad(initState.siteId)
        }
    }

    suspend fun getLaunchPad(siteId: String) {
        launchPadUseCase
            .getLaunchPad(siteId)
            .collect { launchPadRes ->
                setState { copy(launchPad = launchPadRes) }
            }
    }

    companion object : VectorViewModelFactory<LaunchPadViewModel, LaunchPadState> {
        override fun initialState(handle: SavedStateHandle, owner: ViewModelOwner): LaunchPadState? {
            val safeArgs = owner.safeArgs<LaunchPadFragmentArgs>()
            return LaunchPadState(safeArgs.siteId)
        }

        override fun create(
            initialState: LaunchPadState,
            owner: ViewModelOwner,
            handle: SavedStateHandle
        ): LaunchPadViewModel? = with(owner.koin()) {
            LaunchPadViewModel(initialState, get())
        }
    }
}
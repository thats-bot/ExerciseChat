package com.exercisechat.di

import com.exercisechat.presentation.messages.MessageViewModel
import com.exercisechat.presentation.users.UsersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::UsersViewModel)

    viewModel { parameters -> MessageViewModel(receiverUserId = parameters.get(), get(), get(), get(), get()) }
}

package com.asimgasimzade.mastermind.framework.di

interface DependencyProvider<INSTANCE_TYPE> {
    fun getInstance(): INSTANCE_TYPE
}

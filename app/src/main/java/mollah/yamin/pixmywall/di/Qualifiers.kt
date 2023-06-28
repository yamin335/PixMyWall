package mollah.yamin.pixmywall.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LargeImageLoader

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeneralImageLoader
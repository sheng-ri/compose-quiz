package cn.hellozjf.project.composequiz.di

import android.app.Application
import cn.hellozjf.project.composequiz.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

  @Provides
  fun provideMyApplication(application: Application): MyApplication {
    return application as MyApplication
  }
}
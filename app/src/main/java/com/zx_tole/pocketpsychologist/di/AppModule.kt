package com.zx_tole.pocketpsychologist.di

import android.content.Context
import com.zx_tole.pocketpsychologist.data.repository.MoodDatabase
import com.zx_tole.pocketpsychologist.data.repository.MoodDao
import com.zx_tole.pocketpsychologist.data.repository.MoodRepository
import com.zx_tole.pocketpsychologist.data.repository.MoodRepositoryImpl
import com.zx_tole.pocketpsychologist.voice.VoiceAnalyzer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.room.Room

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoodDatabase(@ApplicationContext context: Context): MoodDatabase {
        return Room.databaseBuilder(
            context,
            MoodDatabase::class.java,
            MoodDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideMoodDao(database: MoodDatabase): MoodDao {
        return database.moodDao()
    }
    
    @Provides
    @Singleton
    fun provideMoodRepository(@ApplicationContext context: Context): MoodRepository {
        return MoodRepositoryImpl(context)
    }
    
    @Provides
    fun provideVoiceAnalyzer(@ApplicationContext context: Context): VoiceAnalyzer {
        return VoiceAnalyzer(context)
    }
}

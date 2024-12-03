package com.example.outdoorsport.di

import android.content.Context
import androidx.room.Room
import com.example.outdoorsport.data.repository.Game1LogsRepositoryImp
import com.example.outdoorsport.data.repository.Game2LogsRepositoryImp
import com.example.outdoorsport.data.repository.Game3LogsRepositoryImp
import com.example.outdoorsport.data.source.local.Game1LogsDao
import com.example.outdoorsport.data.source.local.Game1LogsDatabase
import com.example.outdoorsport.data.source.local.Game2LogsDao
import com.example.outdoorsport.data.source.local.Game2LogsDatabase
import com.example.outdoorsport.data.source.local.Game3LogsDao
import com.example.outdoorsport.data.source.local.Game3LogsDatabase
import com.example.outdoorsport.data.source.local.TeamCodeMsgDAO
import com.example.outdoorsport.data.source.local.TeamCodeMsgDatabase
import com.example.outdoorsport.data.source.local.TeamCodesDao
import com.example.outdoorsport.data.source.local.TeamCodesDatabase
import com.example.outdoorsport.domain.repository.Game1LogsRepository
import com.example.outdoorsport.domain.repository.Game2LogsRepository
import com.example.outdoorsport.domain.repository.Game3LogsRepository
import com.example.outdoorsport.domain.repository.TeamCodeMsgRepository
import com.example.outdoorsport.domain.repository.TeamCodeRepository
import com.example.outdoorsport.usecase.AddGame1DataFirebase
import com.example.outdoorsport.usecase.AddGame1Logs
import com.example.outdoorsport.usecase.AddGame2DataFirebase
import com.example.outdoorsport.usecase.AddGame2Logs
import com.example.outdoorsport.usecase.AddGame3DataFirebase
import com.example.outdoorsport.usecase.AddGame3Logs
import com.example.outdoorsport.usecase.DeleteAllGame1Logs
import com.example.outdoorsport.usecase.DeleteAllGame2Logs
import com.example.outdoorsport.usecase.DeleteAllGame3Logs
import com.example.outdoorsport.usecase.Game1LogsUseCases
import com.example.outdoorsport.usecase.Game2LogsUseCases
import com.example.outdoorsport.usecase.Game3LogsUseCases
import com.example.outdoorsport.usecase.ReadGame1Logs
import com.example.outdoorsport.usecase.ReadGame2Logs
import com.example.outdoorsport.usecase.ReadGame3Logs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesGame1LogsRepository(game1LogsDao: Game1LogsDao): Game1LogsRepository =
        Game1LogsRepositoryImp(game1LogsDao)

    @Provides
    fun provideGame1LogsDao(game1LogsDatabase: Game1LogsDatabase): Game1LogsDao {
        return game1LogsDatabase.game1LogsDao()
    }

    @Provides
    @Singleton
    fun provideGame1LogsDatabase(@ApplicationContext appContext: Context): Game1LogsDatabase {
        return Room.databaseBuilder(
            appContext,
            Game1LogsDatabase::class.java,
            "game1_logs_database"
        ).fallbackToDestructiveMigration().build()
    }


    @Singleton
    @Provides
    fun providesGame2LogsRepository(game2LogsDao: Game2LogsDao): Game2LogsRepository =
        Game2LogsRepositoryImp(game2LogsDao)

    @Provides
    fun provideGame2LogsDao(game2LogsDatabase: Game2LogsDatabase): Game2LogsDao {
        return game2LogsDatabase.game2LogsDao()
    }

    @Provides
    @Singleton
    fun provideGame2LogsDatabase(@ApplicationContext appContext: Context): Game2LogsDatabase {
        return Room.databaseBuilder(
            appContext,
            Game2LogsDatabase::class.java,
            "game2_logs_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesGame3LogsRepository(game3LogsDao: Game3LogsDao): Game3LogsRepository =
        Game3LogsRepositoryImp(game3LogsDao)

    @Provides
    fun provideGame3LogsDao(game3LogsDatabase: Game3LogsDatabase): Game3LogsDao {
        return game3LogsDatabase.game3LogsDao()
    }

    @Provides
    @Singleton
    fun provideGame3LogsDatabase(@ApplicationContext appContext: Context): Game3LogsDatabase {
        return Room.databaseBuilder(
            appContext,
            Game3LogsDatabase::class.java,
            "game3_logs_database"
        )
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }


    @Singleton
    @Provides
    fun providesTeamCodesRepository(teamCodesDao: TeamCodesDao): TeamCodeRepository =
        TeamCodeRepository(teamCodesDao)

    @Provides
    fun provideTeamCodesDao(teamCodesDatabase: TeamCodesDatabase): TeamCodesDao {
        return teamCodesDatabase.teamCodesDao()
    }

    @Provides
    @Singleton
    fun provideTeamCodesDatabase(@ApplicationContext appContext: Context): TeamCodesDatabase {
        return Room.databaseBuilder(
            appContext,
            TeamCodesDatabase::class.java,
            "code_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesTeamCodeMsgRepository(teamCodeMsgDAO: TeamCodeMsgDAO): TeamCodeMsgRepository =
        TeamCodeMsgRepository(teamCodeMsgDAO)

    @Provides
    fun provideTeamCodeMsgDao(teamCodeMsgDatabase: TeamCodeMsgDatabase): TeamCodeMsgDAO {
        return teamCodeMsgDatabase.teamCodeMsgDao()
    }

    @Provides
    @Singleton
    fun provideTeamCodeMsgDatabase(@ApplicationContext appContext: Context): TeamCodeMsgDatabase {
        return Room.databaseBuilder(
            appContext,
            TeamCodeMsgDatabase::class.java,
            "code_msg_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGame1UseCases(
        repo: Game1LogsRepository
    ) = Game1LogsUseCases(
        readGame1Logs = ReadGame1Logs(repo),
        addGame1Logs = AddGame1Logs(repo),
        addGame1DataFirebase = AddGame1DataFirebase(repo),
        deleteAllGame1Logs = DeleteAllGame1Logs(repo)
    )

    @Provides
    fun provideGame2UseCases(
        repo: Game2LogsRepository
    ) = Game2LogsUseCases(
        readGame2Logs = ReadGame2Logs(repo),
        addGame2Logs = AddGame2Logs(repo),
        addGame2DataFirebase = AddGame2DataFirebase(repo),
        deleteAllGame2Logs = DeleteAllGame2Logs(repo)
    )

    @Provides
    fun provideGame3UseCases(
        repo: Game3LogsRepository
    ) = Game3LogsUseCases(
        readGame3Logs = ReadGame3Logs(repo),
        addGame3Logs = AddGame3Logs(repo),
        addGame3DataFirebase = AddGame3DataFirebase(repo),
        deleteAllGame3Logs = DeleteAllGame3Logs(repo)
    )
}
package com.example.roomwithhiltdemo

import android.content.Context
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Entity(tableName = "items")
data class User(@PrimaryKey(autoGenerate = true)
                                   var id: Int? = null)

@Dao
interface MainDao {

    @Query("SELECT count(id) FROM items")
    suspend fun numberOfItemsInDB() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetUser(user: User)
}

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMaindao(): MainDao
}



@Singleton
class MainRepository @Inject constructor(
    private val Maindao: MainDao
){
    // Other functions from Maindao.kt

    suspend fun numberOfItemsInDB() = Maindao.numberOfItemsInDB()

    suspend fun insertdata(user: User) = Maindao.insetUser(user)
}


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "test.db"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideMaindao(db: AppDatabase) =
        db.getMaindao() // The reason we can implement a Dao for the database
}

//class MainViewModel @ViewModelInject constructor(
//    private val repository: MainRepository
//): ViewModel() {
//    suspend fun databaseSize() : Int {
//        return repository.numberOfItemsInDB()
//    }
//
//    suspend fun insertdata(user: User) {
//        repository.insertdata(user)
//    }
//}

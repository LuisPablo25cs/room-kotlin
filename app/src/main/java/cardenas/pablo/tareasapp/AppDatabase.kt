package cardenas.pablo.tareasapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.concurrent.Volatile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase(){
    abstract  fun taskDao(): TaskDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val TAREAS_LPCS = listOf(
            TaskEntity(
                titulo = "Diseño de algoritmo de recomendación",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementación del algoritmo de recomendación",
                completado = true
            ),
            TaskEntity(
                titulo = "Desarrollo de API del microservicio",
                completado = true
            ),
            TaskEntity(
                titulo = "Adaptación de la API para realizar un manejo asincrono de las requests con RabbitMQ",
                completado = true
            ),
            TaskEntity(
                titulo = "Adaptación de la API para servir datos de manera paginizada",
                completado = true
            ),
            TaskEntity(
                titulo = "Integración de la API con visualize PR 104 y 125",
                completado = true
            ),
            TaskEntity(
                titulo = "Sincronización de la UI con iOS + otras cosas PR 120",
                completado = true
            ),
        )
        fun getInstance(
            context: Context
        ): AppDatabase{
            return INSTANCE ?: synchronized(
                lock = this
            ){
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "tasks_db"
                    )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).taskDao()
                                TAREAS_LPCS.forEach { tarea ->
                                    dao.insert(tarea)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
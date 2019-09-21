package resource.estagio.workload.infra;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import resource.estagio.workload.data.local.dao.UserDao;
import resource.estagio.workload.data.local.entity.UserEntity;

@Database(
        entities = {UserEntity.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converter.class})
abstract class AppDatabase extends RoomDatabase {
    abstract UserDao userDao();
}

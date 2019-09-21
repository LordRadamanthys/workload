package resource.estagio.workload.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import resource.estagio.workload.data.local.entity.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<UserEntity> list();
}

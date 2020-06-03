package ch.noseryoung.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface PendenzDao {
    @Query("SELECT * FROM pendenz")
    List<Pendenz> getAll();

    @Insert
    void insert(Pendenz pendenz);

    @Update
    void update(Pendenz pendenz);

    @Delete
    void delete(Pendenz pendenz);

}
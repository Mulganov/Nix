package com.mulganov.test_task.nix.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Element.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1, exportSchema = false)
public abstract class DB extends RoomDatabase {
    public abstract ElementDoa getElementDoa();
}
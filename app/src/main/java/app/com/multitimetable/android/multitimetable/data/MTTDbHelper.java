/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.com.multitimetable.android.multitimetable.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Manages a local database for weather data.
 */
public class MTTDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 4;

    static final String DATABASE_NAME = "multi_time_table.db";

    public MTTDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_SCHEDULE_TABLE = "CREATE TABLE " + MTTContract.ScheduleEntry.TABLE_NAME + " (" +
                MTTContract.ScheduleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MTTContract.ScheduleEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MTTContract.ScheduleEntry.COLUMN_IS_OFF + " INTEGER NOT NULL DEFAULT 0 " +
                " );";

        final String SQL_CREATE_SUBJECT_TABLE = "CREATE TABLE " + MTTContract.SubjectEntry.TABLE_NAME + " (" +
                MTTContract.SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MTTContract.SubjectEntry.COLUMN_SCHEDULE_KEY + " INTEGER NOT NULL, " +
                MTTContract.SubjectEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MTTContract.SubjectEntry.COLUMN_PROFESSOR + " TEXT, " +
                MTTContract.SubjectEntry.COLUMN_COLOR + " TEXT , " +

                " FOREIGN KEY (" + MTTContract.SubjectEntry.COLUMN_SCHEDULE_KEY + ") REFERENCES " +
                MTTContract.ScheduleEntry.TABLE_NAME + " (" + MTTContract.ScheduleEntry._ID + ") " +
                "ON DELETE CASCADE );";
        final String SQL_CREATE_TIME_TABLE = "CREATE TABLE " + MTTContract.TimeEntry.TABLE_NAME + " (" +
                MTTContract.TimeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MTTContract.TimeEntry.COLUMN_SUBJECT_KEY + " INTEGER NOT NULL, " +
                MTTContract.TimeEntry.COLUMN_DAY + " INTEGER NOT NULL, " +
                MTTContract.TimeEntry.COLUMN_START_TIME + " INTEGER NOT NULL, " +
                MTTContract.TimeEntry.COLUMN_END_TIME + " INTEGER NOT NULL, " +
                MTTContract.TimeEntry.COLUMN_PLACE + " TEXT, " +

                " FOREIGN KEY (" + MTTContract.TimeEntry.COLUMN_SUBJECT_KEY + ") REFERENCES " +
                MTTContract.SubjectEntry.TABLE_NAME + " (" + MTTContract.SubjectEntry._ID + ") " +
                "ON DELETE CASCADE );";



        sqLiteDatabase.execSQL(SQL_CREATE_SCHEDULE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SUBJECT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TIME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MTTContract.ScheduleEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MTTContract.SubjectEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MTTContract.TimeEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}

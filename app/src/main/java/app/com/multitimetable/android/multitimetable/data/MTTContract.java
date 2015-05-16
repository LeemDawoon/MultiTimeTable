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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import app.com.multitimetable.android.multitimetable.Utility;

/**
 * Defines table and column names for the weather database.
 */
public class MTTContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "app.com.multitimetable.android.multitimetable";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_SCHEDULE="schedule";
    public static final String PATH_SUBJECT="subject";
    public static final String PATH_TIME="time";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class ScheduleEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCHEDULE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;

        public static final String TABLE_NAME = "schedule";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IS_OFF = "is_off";

        // insert.
        public static Uri buildScheduleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class SubjectEntry implements  BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBJECT).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECT;

        public static final String TABLE_NAME = "subject";

        public static final String COLUMN_SCHEDULE_KEY = "schedule_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROFESSOR = "professor";
        public static final String COLUMN_COLOR = "color";

        // insert.
        public static Uri buildSubjectUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // query.

        public static Uri buildSubjectWithSubjectIdUri(int subjectId) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(subjectId)).build();
        }
        public static Uri buildSubjectWithScheduleIdUri(int scheduleId){
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_SCHEDULE_KEY,Integer.toString(scheduleId)).build();
        }
        public static Uri buildSubjectTimeWithScheduleIdUri(int scheduleId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_TIME).appendQueryParameter(COLUMN_SCHEDULE_KEY,Integer.toString(scheduleId)).build();
        }
        
        

        public static int getScheduleIdFromUri(Uri uri){
            String scheduleIdString = uri.getQueryParameter(COLUMN_SCHEDULE_KEY);
            if (Utility.validate(scheduleIdString))
                return Integer.parseInt(scheduleIdString);
            else
                return 0;
        }

        public static int getSubjectIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

    public static final class TimeEntry implements  BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TIME).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIME;

        public static final String TABLE_NAME = "time";

        public static final String COLUMN_SUBJECT_KEY = "subject_id";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_PLACE = "place";

        // insert.
        public static Uri buildTimeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        
        public static Uri buildTimeWithSubjectId(int subjectId) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_SUBJECT_KEY,Integer.toString(subjectId)).build();
        }

        public static int getSubjectIdFromUri(Uri uri){
            String subjectIdString = uri.getQueryParameter(COLUMN_SUBJECT_KEY);
            if (Utility.validate(subjectIdString))
                return Integer.parseInt(subjectIdString);
            else
                return 0;
        }
    }
}

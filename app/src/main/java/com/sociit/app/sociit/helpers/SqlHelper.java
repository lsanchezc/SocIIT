package com.sociit.app.sociit.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;

import com.sociit.app.sociit.entities.Activity;
import com.sociit.app.sociit.entities.Building;
import com.sociit.app.sociit.entities.Comment;
import com.sociit.app.sociit.entities.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Manuel on 19/04/2016.
 */
public class SqlHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "SqlHelper";
    // Database Version
    private static final int DATABASE_VERSION = 39;
    // Database Name
    private static final String DATABASE_NAME = "SociitDB";
    // Table Names
    private static final String TABLE_ACTIVITY = "activities";
    private static final String TABLE_BUILDING = "buildings";
    private static final String TABLE_COMMENT = "comments";
    private static final String TABLE_USER = "users";
    private static final String TABLE_USER_ACTIVITY = "users_activities";
    // Common column names
    private static final String KEY_ID = "id";
    // ACTIVITIES Table - column names
    private static final String KEY_ACTIVITY_NAME = "name";
    private static final String KEY_ACTIVITY_BUILDING = "building";
    private static final String KEY_ACTIVITY_DATE = "date";
    private static final String KEY_ACTIVITY_DESCRIPTION = "description";
    // BUILDINGS Table - column names
    private static final String KEY_BUILDING_NAME = "name";
    private static final String KEY_BUILDING_ADDRESS = "address";
    // COMMENTS Table - column names
    private static final String KEY_COMMENT_MESSAGE = "message";
    private static final String KEY_COMMENT_USER = "user";
    private static final String KEY_COMMENT_ACTIVITY = "activity";
    // USERS Table - column names
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_USERNAME = "username";
    private static final String KEY_USER_PASSWORD = "password";
    // USERS_ACTIVITIES Table - column names
    private static final String KEY_USER_ACTIVITY_USER = "user";
    private static final String KEY_USER_ACTIVITY_ACTIVITY = "activity";
    // Table Create Statements
    // Activity table create statement
    private static final String CREATE_TABLE_ACTIVITY =
            "CREATE TABLE " + TABLE_ACTIVITY + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_ACTIVITY_NAME + " TEXT," +
                    KEY_ACTIVITY_DATE + " TEXT," +
                    KEY_ACTIVITY_BUILDING + " INTEGER, " +
                    KEY_ACTIVITY_DESCRIPTION + " TEXT, " +
                    "FOREIGN KEY(" + KEY_ACTIVITY_BUILDING + ") REFERENCES buildings(" + KEY_ID + ")" +
                    ")";
    // Building table create statement
    private static final String CREATE_TABLE_BUILDING =
            "CREATE TABLE " + TABLE_BUILDING + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_BUILDING_NAME + " TEXT," +
                    KEY_BUILDING_ADDRESS + " TEXT " +
                    ")";
    // Comment table create statement
    private static final String CREATE_TABLE_COMMENT =
            "CREATE TABLE " + TABLE_COMMENT + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_COMMENT_MESSAGE + " TEXT," +
                    KEY_COMMENT_USER + " INTEGER," +
                    KEY_COMMENT_ACTIVITY + " INTEGER, " +
                    "FOREIGN KEY(" + KEY_COMMENT_USER + ") REFERENCES users(" + KEY_ID + "), " +
                    "FOREIGN KEY(" + KEY_COMMENT_ACTIVITY + ") REFERENCES activities(" + KEY_ID + ") " +
                    ")";
    // User table create statement
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_USER_NAME + " TEXT," +
                    KEY_USER_USERNAME + " TEXT UNIQUE," +
                    KEY_USER_PASSWORD + " TEXT" +
                    ")";
    private static final String CREATE_TABLE_USER_ACTIVITY =
            "CREATE TABLE " + TABLE_USER_ACTIVITY + "(" +
                    KEY_USER_ACTIVITY_USER + " INTEGER," +
                    KEY_USER_ACTIVITY_ACTIVITY + " INTEGER, " +
                    "FOREIGN KEY(" + KEY_USER_ACTIVITY_USER + ") REFERENCES users(" + KEY_ID + "), " +
                    "FOREIGN KEY(" + KEY_USER_ACTIVITY_ACTIVITY + ") REFERENCES activities(" + KEY_ID + ") " +
                    ")";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ACTIVITY);
        db.execSQL(CREATE_TABLE_BUILDING);
        db.execSQL(CREATE_TABLE_COMMENT);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER_ACTIVITY);
        prepopulateDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ACTIVITY);
        // create new tables
        onCreate(db);
    }

    private void prepopulateDB(SQLiteDatabase db) {
        User user1 = new User(0, "mmarti45", "Manuel Martinez", "hello", null);
        this.addUser(user1, db);
        User user2 = new User(0, "lsanchez7", "Lazaro Sanchez", "world", null);
        this.addUser(user2, db);
        User user3 = new User(0, "anicolas", "Alejandro Nicolas", "hello", null);
        this.addUser(user3, db);
        //TODO: Fix latitudes and longitudes and add missing buildings
        Address mtcc_address = new Address(Locale.getDefault());
        mtcc_address.setLatitude(41.835728);
        mtcc_address.setLongitude(-87.625956);
        Building mtcc = new Building(1, mtcc_address, "MTCC", null);
        Address stuart_address = new Address(Locale.getDefault());
        stuart_address.setLatitude(41.838682);
        stuart_address.setLongitude(-87.627353);
        Building stuart = new Building(2, stuart_address, "Stuart Building", null);
        Address hermann_address = new Address(Locale.getDefault());
        hermann_address.setLatitude(41.835685);
        hermann_address.setLongitude(-87.628373);
        Building hermann = new Building(3, hermann_address, "Hermann Hall", null);
        Address crownHall_address = new Address(Locale.getDefault());
        crownHall_address.setLatitude(41.8331938);
        crownHall_address.setLongitude(-87.6294636);
        Building crownHall = new Building(4, crownHall_address, "S. R. Crown Hall", null);
        Address library_address = new Address(Locale.getDefault());
        library_address.setLatitude(41.8340731);
        library_address.setLongitude(-87.6281547);
        Building library = new Building(5, library_address, "Paul V Galvin Library", null);
        Address keating_address = new Address(Locale.getDefault());
        keating_address.setLatitude(41.8383843);
        keating_address.setLongitude(-87.6264233);
        Building keating = new Building(6, keating_address, "Keating Sports Center", null);
        Address vanderCook_address = new Address(Locale.getDefault());
        vanderCook_address.setLatitude(41.8363401);
        vanderCook_address.setLongitude(-87.6284961);
        Building vanderCook = new Building(7, vanderCook_address, "VanderCook College of Music", null);
        Address iitTower_address = new Address(Locale.getDefault());
        iitTower_address.setLatitude(41.8313864);
        iitTower_address.setLongitude(-87.6278956);
        Building iitTower = new Building(8, iitTower_address, "IIT Tower", null);
        Address lifeSciences_address = new Address(Locale.getDefault());
        lifeSciences_address.setLatitude(41.8365884);
        lifeSciences_address.setLongitude(-87.6292395);
        Building lifeSciencies = new Building(9, lifeSciences_address, "Life Sciences Building", null);
        Address perlstein_address = new Address(Locale.getDefault());
        perlstein_address.setLatitude(41.834588);
        perlstein_address.setLongitude(-87.6271206);
        Building perlstein = new Building(10, perlstein_address, "Perlstein Hall", null);
        Address wishnick_address = new Address(Locale.getDefault());
        wishnick_address.setLatitude(41.8348128);
        wishnick_address.setLongitude(-87.6273834);
        Building wishnick = new Building(11, wishnick_address, "Wishnick Hall", null);
        this.addBuilding(mtcc, db);
        this.addBuilding(stuart, db);
        this.addBuilding(hermann, db);
        this.addBuilding(crownHall, db);
        this.addBuilding(library, db);
        this.addBuilding(keating, db);
        this.addBuilding(vanderCook, db);
        this.addBuilding(iitTower, db);
        this.addBuilding(lifeSciencies, db);
        this.addBuilding(perlstein, db);
        this.addBuilding(wishnick, db);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = new Date();
        try {
            date = formatter.parse("Thu Jun 18 20:56:02 EDT 2009");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mtcc = this.getBuildingByName("mtcc", db);
        List<User> barList = new ArrayList<>();
        User bar = this.getUserByUsername("lsanchez7", db);
        barList.add(bar);
        Activity activity = new Activity(0, "Basketball", keating, date, barList, null, "Play basketball");
        this.addActivity(activity, db);
        List<User> fooList = new ArrayList<>();
        User foo = this.getUserByUsername("mmarti45", db);
        fooList.add(foo);
        Activity activityFoo = new Activity(0, "Pool", mtcc, date, fooList, null, "Play pool");
        this.addActivity(activityFoo, db);
    }

    // TODO: Editar las siguientes funciones para que hagan peticiones a la base de datos.
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        addUser(user, db);
        db.close();
    }

    public void addUser(User user, SQLiteDatabase db) {
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_USERNAME, user.getUsername());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        // 3. insert
        db.insert(TABLE_USER, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/values
    }

    public User getUserById(int userId, SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ID + " LIKE \"" + userId + "\"";
        List<User> users = new LinkedList<User>();
        Cursor cursor = db.rawQuery(query, null);
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                users.add(user);
            } while (cursor.moveToNext());
        }
        User returnUser;
        if (users.size() == 0) {
            returnUser = null;
        } else {
            returnUser = users.get(0);
        }
        return returnUser;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = getUserById(userId, db);
        db.close();
        return user;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = getUserByUsername(username, db);
        db.close();
        return user;
    }

    public User getUserByUsername(String username, SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USER_USERNAME + " LIKE \"" + username + "\"";
        List<User> users = new LinkedList<User>();
        Cursor cursor = db.rawQuery(query, null);
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                users.add(user);
            } while (cursor.moveToNext());
        }
        User returnUser;
        if (users.size() == 0) {
            returnUser = null;
        } else {
            returnUser = users.get(0);
        }
        return returnUser;
    }


    public void editUser(User user) {
        return;
    }

    public void deleteUser(User user) {
        return;
    }

    public void addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        addActivity(activity, db);
        db.close();
    }

    public void addActivity(Activity activity, SQLiteDatabase db) {
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_NAME, activity.getName()); // get name
        values.put(KEY_ACTIVITY_BUILDING, activity.getBuilding().getId()); // get building
        values.put(KEY_ACTIVITY_DATE, activity.getDate().toString());
        values.put(KEY_ACTIVITY_DESCRIPTION, activity.getDescription());
        //values.put(KEY_ACTIVITY_USER, activity.getUserList().get(0).getId());
        // 3. insert
        activity.getUserList().toString();
        db.insert(TABLE_ACTIVITY, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/values
        if (activity.getUserList() != null) {
            this.linkUserActivity(activity.getCreator(), this.getActivityByName(activity.getName(), db), db);
        }
    }



    public void linkUserActivity(User user, Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        linkUserActivity(user, activity, db);
        db.close();
    }

    public void linkUserActivity(User user, Activity activity, SQLiteDatabase db) {
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ACTIVITY_USER, user.getId()); // get name
        values.put(KEY_USER_ACTIVITY_ACTIVITY, activity.getId()); // get building
        // 3. insert
        db.insert(TABLE_USER_ACTIVITY, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/values
    }

    public void unlinkUserActivity(User user, Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        unlinkUserActivity(user, activity, db);
        db.close();
    }


    public void unlinkUserActivity(User user, Activity activity, SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_USER_ACTIVITY + " WHERE " + KEY_USER_ACTIVITY_USER + "=\"" + user.getId() + "\" AND " + KEY_USER_ACTIVITY_ACTIVITY + "=\"" + activity.getId() + "\";");
    }

    public Activity getActivityById(int id, SQLiteDatabase db) {
        List<Activity> activities = new LinkedList<Activity>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ACTIVITY + " WHERE " + KEY_ID + " LIKE \"" + id + "\"";
        // 2. get reference to writable DB
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        Activity activity = null;
        if (cursor.moveToFirst()) {
            do {
                activity = new Activity();
                activity.setId(Integer.parseInt(cursor.getString(0)));
                activity.setName(cursor.getString(1));
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                Date activityDate = new Date();
                try {
                    activityDate = formatter.parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int buildingIdColumn = cursor.getColumnIndex(KEY_ACTIVITY_BUILDING);
                int buildingId = cursor.getInt(buildingIdColumn);
                activity.setBuilding(this.getBuildingById(buildingId, db));
                activity.setDescription(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_DESCRIPTION)));
                activity.setDate(activityDate);
                activity.setUserList(this.getActivityUsers(activity, db));
                activities.add(activity);
            } while (cursor.moveToNext());
        }
        Activity returnActivity;
        if (activities.size() == 0) {
            returnActivity = null;
        } else {
            returnActivity = activities.get(0);
        }
        return returnActivity;
    }

    public List<User> getActivityUsers(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<User> users = getActivityUsers(activity, db);
        db.close();
        return users;
    }

    public List<User> getActivityUsers(Activity activity, SQLiteDatabase db) {
        List<User> users = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_USER_ACTIVITY + " WHERE " + KEY_USER_ACTIVITY_ACTIVITY + " LIKE \"" + activity.getId() + "\"";
        Cursor cursor = db.rawQuery(query, null);
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = getUserById(cursor.getInt(cursor.getColumnIndex(KEY_USER_ACTIVITY_USER)));
                users.add(user);
            } while (cursor.moveToNext());
        }
        return users;
    }

    public void leaveActivity(int idUser, int idActivity) {
        //String query = "SELECT * FROM " + TABLE_USER_ACTIVITY + " WHERE " + KEY_USER_ACTIVITY_ACTIVITY + " LIKE \"" + idActivity + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery(query, null);
        //c.moveToFirst();
        db.delete(TABLE_USER_ACTIVITY,
                KEY_USER_ACTIVITY_USER + " = ? AND " + KEY_USER_ACTIVITY_ACTIVITY + " = ?",
                new String[]{"" + idUser, idActivity + ""});
        db.close();
    }

    public Activity getActivityById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Activity returnActivity = this.getActivityById(id, db);
        db.close();
        return returnActivity;
    }

    public Activity getActivityByName(String name, SQLiteDatabase db) {
        List<Activity> activities = new LinkedList<Activity>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ACTIVITY + " WHERE " + KEY_ACTIVITY_NAME + " LIKE \"" + name + "\"";
        // 2. get reference to writable DB
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        Activity activity = null;
        if (cursor.moveToFirst()) {
            do {
                activity = new Activity();
                activity.setId(Integer.parseInt(cursor.getString(0)));
                activity.setName(cursor.getString(1));
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                Date activityDate = new Date();
                try {
                    activityDate = formatter.parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int buildingIdColumn = cursor.getColumnIndex(KEY_ACTIVITY_BUILDING);
                int buildingId = cursor.getInt(buildingIdColumn);
                activity.setBuilding(this.getBuildingById(buildingId, db));
                activity.setDate(activityDate);
                activity.setDescription(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_DESCRIPTION)));
                activities.add(activity);
            } while (cursor.moveToNext());
        }
        Activity returnActivity;
        if (activities.size() == 0) {
            returnActivity = null;
        } else {
            returnActivity = activities.get(0);
        }
        return returnActivity;
    }

    public Activity getActivityByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Activity returnActivity = this.getActivityByName(name, db);
        db.close();
        return returnActivity;
    }

    public List<Activity> getActivitiesByUserId(int userId) {
        List<Activity> activities = new LinkedList<Activity>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_USER_ACTIVITY + " WHERE " + KEY_USER_ACTIVITY_USER + " LIKE \"" + userId + "\"";
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        Activity activity = null;
        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex(KEY_USER_ACTIVITY_ACTIVITY);
                activity = this.getActivityById(Integer.parseInt(cursor.getString(columnIndex)));
                activities.add(activity);
            } while (cursor.moveToNext());
        }
        db.close();
        return activities; // return books
    }

    public List<Activity> getActivitiesByBuildingId(int buildingId, SQLiteDatabase db) {
        List<Activity> activities = new LinkedList<Activity>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ACTIVITY + " WHERE " + KEY_ACTIVITY_BUILDING + " LIKE \"" + buildingId + "\"";
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        Activity activity = null;
        if (cursor.moveToFirst()) {
            do {
                activity = new Activity();
                activity.setId(Integer.parseInt(cursor.getString(0)));
                activity.setName(cursor.getString(1));
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                Date activityDate = new Date();
                try {
                    activityDate = formatter.parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                activity.setBuilding(getBuildingById(buildingId, db));
                activity.setDate(activityDate);
                // Add book to books
                activities.add(activity);
            } while (cursor.moveToNext());
        }
        return activities; // return activities
    }

    public List<Activity> getActivitiesByBuildingId(int buildingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Activity> activities = this.getActivitiesByBuildingId(buildingId, db);
        db.close();
        return activities;
    }

    // NOT WORKING, WHY?
    public long getNumberOfActivities() {
        String query = "SELECT COUNT(*) FROM " + TABLE_ACTIVITY;
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(query);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public List<Activity> getAllActivities() {
        List<Activity> activities = new LinkedList<Activity>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ACTIVITY;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build activity and add it to list
        Activity activity = null;
        if (cursor.moveToFirst()) {
            do {
                activity = new Activity();
                activity.setId(Integer.parseInt(cursor.getString(0)));
                activity.setName(cursor.getString(1));
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                Date activityDate = new Date();
                try {
                    activityDate = formatter.parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int buildingIdColumn = cursor.getColumnIndex(KEY_ACTIVITY_BUILDING);
                int buildingId = cursor.getInt(buildingIdColumn);
                activity.setBuilding(this.getBuildingById(buildingId, db));
                activity.setDate(activityDate);
                activity.setDescription(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_DESCRIPTION)));
                activities.add(activity);
            } while (cursor.moveToNext());
        }
        db.close();
        return activities; // return activities
    }

    public List<Activity> getUserActivities(User user) {
        List<Activity> activityList = new LinkedList<>();
        Activity activity = new Activity();
        activity.setName("TODO");
        activityList.add(activity);
        return activityList;
    }

    public void addBuilding(Building building, SQLiteDatabase db) {
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_BUILDING_NAME, building.getName());
        values.put(KEY_BUILDING_ADDRESS, building.getAddress().getLatitude() + ":" + building.getAddress().getLongitude());
        // 3. insert
        db.insert(TABLE_BUILDING, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/values
    }

    public List<Activity> getBuildingActivities(Building building) {
        List<Activity> activityList = new LinkedList<>();
        Activity activity = new Activity();
        activity.setName("TODO");
        activityList.add(activity);
        return activityList;
    }

    public void editActivity(Activity activity) {
        return;
    }

    public void deleteActivity(Activity activity) {
        return;
    }

    public Building getBuildingByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Building building = this.getBuildingByName(name, db);
        db.close();
        return building;
    }

    public Building getBuildingByName(String name, SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_BUILDING + " WHERE name LIKE \"" + name + "\"";
        List<Building> buildings = new LinkedList<Building>();
        Cursor cursor = db.rawQuery(query, null);
        Building building = null;
        if (cursor.moveToFirst()) {
            do {
                building = new Building();
                building.setId(Integer.parseInt(cursor.getString(0)));
                building.setName(cursor.getString(1));
                Address address = new Address(Locale.getDefault());
                address.setLatitude(Double.parseDouble(cursor.getString(2).split(":")[0]));
                address.setLongitude(Double.parseDouble(cursor.getString(2).split(":")[1]));
                building.setAddress(address);
                buildings.add(building);
            } while (cursor.moveToNext());
        }
        Building returnBuilding;
        if (buildings.size() == 0) {
            returnBuilding = null;
        } else {
            returnBuilding = buildings.get(0);
        }
        return returnBuilding;
    }

    public Building getBuildingById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Building building = this.getBuildingById(id, db);
        db.close();
        return building;
    }

    public Building getBuildingById(int id, SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_BUILDING + " WHERE id LIKE \"" + id + "\"";
        List<Building> buildings = new LinkedList<Building>();
        Cursor cursor = db.rawQuery(query, null);
        Building building = null;
        if (cursor.moveToFirst()) {
            do {
                building = new Building();
                building.setId(Integer.parseInt(cursor.getString(0)));
                building.setName(cursor.getString(1));
                Address address = new Address(Locale.getDefault());
                address.setLatitude(Double.parseDouble(cursor.getString(2).split(":")[0]));
                address.setLongitude(Double.parseDouble(cursor.getString(2).split(":")[1]));
                building.setAddress(address);
                buildings.add(building);
            } while (cursor.moveToNext());
        }
        Building returnBuilding;
        if (buildings.size() == 0) {
            returnBuilding = null;
        } else {
            returnBuilding = buildings.get(0);
        }
        return returnBuilding;
    }

    public List<Building> getAllBuildings(SQLiteDatabase db) {
        List<Building> buildings = new LinkedList<Building>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_BUILDING;
        // 2. get reference to writable DB
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        Building building = null;
        if (cursor.moveToFirst()) {
            do {
                building = new Building();
                building.setId(Integer.parseInt(cursor.getString(0)));
                building.setName(cursor.getString(1));
                Address address = new Address(Locale.getDefault());
                address.setLatitude(Double.parseDouble(cursor.getString(2).split(":")[0]));
                address.setLongitude(Double.parseDouble(cursor.getString(2).split(":")[1]));
                building.setAddress(address);
                building.setActivityList(this.getActivitiesByBuildingId(building.getId(), db));
                buildings.add(building);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return buildings;
    }

    public List<Building> getAllBuildings() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Building> buildings = this.getAllBuildings(db);
        db.close();
        return buildings;
    }

    public void addComment(Comment comment) {
        return;
    }

    public List<Comment> getActivityComments(Activity activity) {
        return null;
    }
}

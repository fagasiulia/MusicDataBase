package model;

public class Constants {

	public static final String DB_NAME = "music.db";
	public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

	public static final String TABLE_ALBUMS = "albums";
	public static final String COLUMN_ALBUM_ID = "_id";
	public static final String COLUMN_ALBUM_NAME = "name";
	public static final String COLUMN_ALBUM_ARTIST = "artist";
	public static final int INDEX_ALBUM_ID = 1;
	public static final int INDEX_ALBUM_NAME = 2;
	public static final int INDEX_ALBUM_ARTIST = 3;

	public static final String TABLE_ARTISTS = "artists";
	public static final String COLUMN_ARTISTS_ID = "_id";
	public static final String COLUMN_ARTISTS_NAME = "name";
	public static final int INDEX_ARTIST_ID = 1;
	public static final int INDEX_ARTIST_NAME = 2;

	public static final String TABLE_SONGS = "songs";
	public static final String COLUMN_SONGS_ID = "_id";
	public static final String COLUMN_SONGS_TRACK = "track";
	public static final String COLUMN_SONGS_TITLE = "title";
	public static final String COLUMN_SONGS_ALBUM = "album";
	public static final int INDEX_SONG_ID = 1;
	public static final int INDEX_SONG_TRACK = 2;
	public static final int INDEX_SONG_TITLE = 3;
	public static final int INDEX_SONG_ALBUM = 4;

	public static final int ORDER_BY_NONE = 1;
	public static final int ORDER_BY_ASC = 2;
	public static final int ORDER_BY_DESC = 3;

	public static final String QUERY_ALBUMS_BY_ARTIST_START = "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME
			+ " FROM " + TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "."
			+ COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + " WHERE " + TABLE_ARTISTS + "."
			+ COLUMN_ARTISTS_NAME + " = \"";

	public static final String QUERY_ALBUMS_BY_ARTIST_SORT = " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME
			+ " COLLATE NOCASE ";

	public static final String QUERY_ARTIST_FOR_SONG_START = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME
			+ ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TRACK + " FROM "
			+ TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = "
			+ TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "."
			+ COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + " WHERE " + TABLE_SONGS + "."
			+ COLUMN_SONGS_TITLE + " = \"";

	public static final String QUERY_ARTIST_FOR_SONG_SORT = " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME
			+ ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

	public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
	public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW
			+ " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME
			+ " AS " + COLUMN_SONGS_ALBUM + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TRACK + ", " + TABLE_SONGS + "."
			+ COLUMN_SONGS_TITLE + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "."
			+ COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + " ." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS
			+ " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID
			+ " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME
			+ "," + TABLE_SONGS + "." + COLUMN_SONGS_TRACK;

	public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTISTS_NAME + ", " + COLUMN_SONGS_ALBUM + ", "
			+ COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONGS_TITLE + " = \"";

	public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTISTS_NAME + ", " + COLUMN_SONGS_ALBUM
			+ ", " + COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONGS_TITLE + "=?";

	public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS + "(" + COLUMN_ARTISTS_NAME
			+ ") VALUES(?)";
	public static final String INSERT_ALBUM = "INSERT INTO " + TABLE_ALBUMS + "(" + COLUMN_ALBUM_NAME + ", "
			+ COLUMN_ALBUM_ARTIST + ") VALUES (? , ?)";
	public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONGS + "(" + COLUMN_SONGS_TRACK + ", "
			+ COLUMN_SONGS_TITLE + ", " + COLUMN_SONGS_ALBUM + ") VALUES(?, ?, ?)";

	public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTISTS_ID + " FROM " + TABLE_ARTISTS + " WHERE "
			+ COLUMN_ARTISTS_NAME + "= ?";
	
	public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " + TABLE_ALBUMS + " WHERE "
			+ COLUMN_ALBUM_NAME + "= ?";
}

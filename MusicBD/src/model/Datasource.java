package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

	private Connection conn;
	private PreparedStatement querySongInfoView;

	private PreparedStatement insertIntoArtists;
	private PreparedStatement insertIntoAlbums;
	private PreparedStatement insertIntoSongs;

	private PreparedStatement queryArtist;
	private PreparedStatement queryAlbum;

	/*
	 * public boolean open() { try { con =
	 * DriverManager.getConnection(CONNECTION_STRING); return true; } catch
	 * (SQLException e) { System.out.println("Couldn't connect to the database");
	 * return false; }
	 * 
	 * }
	 * 
	 */

	public boolean open() {
		try {
			conn = DriverManager.getConnection(Constants.CONNECTION_STRING);
			querySongInfoView = conn.prepareStatement(Constants.QUERY_VIEW_SONG_INFO_PREP);

			insertIntoArtists = conn.prepareStatement(Constants.INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
			insertIntoAlbums = conn.prepareStatement(Constants.INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS);
			insertIntoSongs = conn.prepareStatement(Constants.INSERT_SONG);

			queryArtist = conn.prepareStatement(Constants.QUERY_ARTIST);
			queryAlbum = conn.prepareStatement(Constants.QUERY_ALBUM);

			return true;
		} catch (SQLException e) {
			System.out.println("Couldn't connect to database: " + e.getMessage());
			return false;
		}
	}

	/*
	 * public void close() { try { if (conn != null) { conn.close(); } } catch
	 * (SQLException e) { System.out.println("Couldn't close connection");
	 * 
	 * }
	 * 
	 * }
	 * 
	 */

	public void close() {
		try {
			if (querySongInfoView != null) {
				querySongInfoView.close(); // Order is important !! we close them in reverse order of creation
			}
			if (insertIntoArtists != null) {
				insertIntoArtists.close();
			}
			if (insertIntoAlbums != null) {
				insertIntoAlbums.close();
			}
			if (insertIntoSongs != null) {
				insertIntoSongs.close();
			}
			if (queryArtist != null) {
				queryArtist.close();
			}
			if (queryAlbum != null) {
				queryAlbum.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Couldn't close connection: " + e.getMessage());
		}
	}

//	This method will print the artists table based on the selected order
	public List<Artist> queryArtists(int sortOrder) {

		StringBuilder sb = new StringBuilder("SELECT * FROM ");
		sb.append(Constants.TABLE_ARTISTS);

		if (sortOrder != Constants.ORDER_BY_NONE) {
			sb.append(" ORDER BY ");
			sb.append(Constants.COLUMN_ARTISTS_NAME);
			sb.append(" COLLATE NOCASE ");
			if (sortOrder == Constants.ORDER_BY_ASC)
				sb.append("ASC");
			else
				sb.append("DESC");

		}

		try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(sb.toString());) {

			List<Artist> artists = new ArrayList<>();
			while (results.next()) {
				Artist artist = new Artist();
				artist.setId(results.getInt(Constants.INDEX_ARTIST_ID));
				artist.setName(results.getString(Constants.INDEX_ARTIST_NAME));
				artists.add(artist);
			}
			return artists;

		} catch (SQLException e) {
			System.out.println("Query failed");
			return null;
		}
	}

//	This method will return the albums of the requested artist and will sort them in the selected order
	public List<String> queryAlbumForArtists(String artistName, int sortOrder) {

		StringBuilder sb = new StringBuilder(Constants.QUERY_ALBUMS_BY_ARTIST_START);
		sb.append(artistName);
		sb.append("\"");

		if (sortOrder != Constants.ORDER_BY_NONE) {
			sb.append(Constants.QUERY_ALBUMS_BY_ARTIST_SORT);
			if (sortOrder == Constants.ORDER_BY_ASC) {
				sb.append("ASC");
			} else {
				sb.append("DESC");
			}
		}
		System.out.println("SQL statement = " + sb.toString());

		try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(sb.toString())) {

			// When using ResultSet getter method the index corresponds to the index of the
			// column
			// in the ResultSet not the index of the column in the table
			// Our query only returns the album name so there is only one column and it can
			// start
			// from 1 not 0
			List<String> albums = new ArrayList<>();
			while (results.next()) {
				albums.add(results.getString(1));
			}

			return albums;

		} catch (SQLException e) {
			System.out.println("Query failed: " + e.getMessage());
			return null;
		}

	}

//  This method will return the name of the artists for the specified song
	public List<SongArtist> queryArtistForSong(String songName, int sortOrder) {
		StringBuilder sb = new StringBuilder(Constants.QUERY_ARTIST_FOR_SONG_START);
		sb.append(songName);
		sb.append("\"");

		if (sortOrder != Constants.ORDER_BY_NONE) {
			sb.append(Constants.QUERY_ARTIST_FOR_SONG_SORT);
			if (sortOrder == Constants.ORDER_BY_ASC) {
				sb.append("ASC");
			} else {
				sb.append("DESC");
			}
		}

		System.out.println("SQL statement = " + sb.toString());

		try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(sb.toString())) {

			List<SongArtist> songArtist = new ArrayList<>();
			while (results.next()) {
				SongArtist songA = new SongArtist();
				songA.setArtistName(results.getString(1));
				songA.setAlbumName(results.getString(2));
				songA.setTrack(results.getInt(3));
				songArtist.add(songA);
			}

			return songArtist;

		} catch (SQLException e) {
			System.out.println("Query failed: " + e.getMessage());
			return null;
		}
	}

//	Method for accessing Meta data using ResultsSet. This will return something
//    similar with the .schema
	public void querySongsMetadata() {
		String sql = "SELECT * FROM " + Constants.TABLE_SONGS;

		try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(sql)) {

			ResultSetMetaData meta = results.getMetaData();
			int numColumns = meta.getColumnCount();
			for (int i = 1; i < numColumns; i++) {
				System.out.format("Column %d in the songs table is names %s\n", i, meta.getColumnName(i));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	This will count the columns of the specified table
	public int getCount(String table) {
		String sql = "SELECT COUNT(*) AS count, MIN(_id) AS min_id FROM " + table;

		try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(sql)) {

			int count = results.getInt("count");
			int min = results.getInt("min_id");
			System.out.format("Count = %d, Min = %d ", count, min);
			return count;

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

//	Method for creating a view
	public boolean createViewForSongArtists() {
		try (Statement statement = conn.createStatement()) {

			statement.execute(Constants.CREATE_ARTIST_FOR_SONG_VIEW);
			return true;

		} catch (SQLException e) {
			System.out.println("Create View failed: " + e.getMessage());
			return false;
		}
	}

//  Method for querying info regarding a song	
	public List<SongArtist> querySongInfoView(String title) {

		try {
			querySongInfoView.setString(1, title); // 1 comes for the position of the paceholder we want to replace
													// because it can be more than 1
			ResultSet results = querySongInfoView.executeQuery();

			List<SongArtist> songArtist = new ArrayList<>();
			while (results.next()) {
				SongArtist songA = new SongArtist();
				songA.setArtistName(results.getString(1));
				songA.setAlbumName(results.getString(2));
				songA.setTrack(results.getInt(3));

				songArtist.add(songA);
			}

			return songArtist;

		} catch (SQLException e) {
			System.out.println("Unable to get the song's info from the view ");
			e.printStackTrace();
			return null;
		} finally {
			// We just need to close the PrepareStatement and that will close also the
			// ResultSet
		}
	}

//	Method for inserting a new artist into the artists table
	private int insertArtist(String name) throws SQLException {
		queryArtist.setString(1, name);
		ResultSet results = queryArtist.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		} else {
			// Insert the artist
			insertIntoArtists.setString(1, name);
			int affectedRows = insertIntoArtists.executeUpdate();

			if (affectedRows != 1) {
				throw new SQLException("Couldn't insert artist");
			}

			ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Couldn't get the _id for the artist");
			}
		}
	}

//	Method for inserting a new album into the album table
	private int insertAlbum(String name, int artistId) throws SQLException {
		queryAlbum.setString(1, name);
		ResultSet results = queryAlbum.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		} else {
			// Insert the album
			insertIntoAlbums.setString(1, name);
			insertIntoAlbums.setInt(2, artistId);
			int affectedRows = insertIntoAlbums.executeUpdate();

			if (affectedRows != 1) {
				throw new SQLException("Couldn't insert album");
			}

			ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Couldn't get the _id for the album");
			}
		}
	}

//	Method for inserting a new song into the song table
	public void insertSong(String title, String artist, String album, int track) {
		try {
			conn.setAutoCommit(false);

			int artistId = insertArtist(artist);
			int albumId = insertAlbum(album, artistId);
			insertIntoSongs.setInt(1, track);
			insertIntoSongs.setString(2, title);
			insertIntoSongs.setInt(3, albumId);
			int affectedRows = insertIntoSongs.executeUpdate();

			if (affectedRows == 1) {
				conn.commit();
			} else {
				throw new SQLException("Song insert failed ");
			}

		} catch (Exception e) {
			System.out.println("Insert song excetion " + e.getMessage());

			try {
				System.out.println("Performing rollback");
				conn.rollback();

			} catch (SQLException e2) {
				System.out.println("Oh boy! Things are really bad! " + e2.getMessage());

			}
		} finally {
			try {
				System.out.println("Resetting default commit behaviour!");
				conn.setAutoCommit(true);

			} catch (SQLException e3) {
				System.out.println("Couldn't reset default commit behaviour " + e3.getMessage());
			}
		}
	}

}

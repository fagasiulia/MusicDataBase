import java.util.List;
import java.util.Scanner;

import model.Artist;
import model.Constants;
import model.Datasource;
import model.SongArtist;

public class Main {

	public static void main(String[] args) {

		Datasource datasource = new Datasource();

		if (!datasource.open()) {
			System.out.println("Unable to open");
			return;
		}

		/** This will print the list of the artists **/
//		List<Artist> artists = datasource.queryArtists(Constants.ORDER_BY_ASC);
//		if (artists == null) {
//			System.out.println("No artists");
//		}
//
//		for (Artist artist : artists) {
//			System.out.println("ID = " + artist.getId() + " NAME: " + artist.getName());
//		}

		/** This will print the albums of the requested artist **/
//		List<String> albumsForArtist = datasource.queryAlbumForArtists("Iron Maiden", Constants.ORDER_BY_ASC);
//
//		for (String album : albumsForArtist) {
//			System.out.println(album);
//		}

		/** This will print the artists name based on the requested song **/
//		List<SongArtist> songArtist = datasource.queryArtistForSong("Hearthless", Constants.ORDER_BY_ASC);
//		if (songArtist == null) {
//			System.out.println("Couldn't find the artist for that song");
//			return;
//		}
//
//		// Get schema info
//		// This may differ accross DBs
//		for (SongArtist song : songArtist) {
//			System.out.println("Artist name = " + song.getArtistName() +
//	                " Album name = " + song.getAlbumName() +
//	                " Track = " + song.getTrack());
//		}

		/** This will print the number of songs and the name of each column **/
//		int count = datasource.getCount(Constants.TABLE_SONGS);
//		System.out.println("Number of songs is: " + count);
//		datasource.querySongsMetadata();

		/** This will create a view **/
//		datasource.createViewForSongArtists();

		/** This will print the info regarding the specified song **/
//		Scanner input = new Scanner(System.in);
//		System.out.println("Enter a song title: ");
//		String songTitle = input.nextLine();
//
//		songArtist = datasource.querySongInfoView(songTitle);
//		if (songArtist.isEmpty()) {
//			System.out.println("Couldn't find the artist for the song");
//			return;
//		}
//		for (SongArtist song : songArtist) {
//			System.out.println("FROM VIEW - Artist name = " + song.getArtistName() +
//	                " Album name = " + song.getAlbumName() +
//	                " Track number = " + song.getTrack());
//		}

		datasource.insertSong("Touch of Gray", "Grateful Dead", "In The Dark", 1);
		datasource.close();
	}

}

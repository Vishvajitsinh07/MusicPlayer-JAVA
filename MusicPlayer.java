import java.io.*;
import java.util.*;

class MusicTrack {
    private String title;
    private String artist;
    private String duration; // format: mm:ss

    public MusicTrack(String title, String artist, String duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Artist: " + artist + ", Duration: " + duration;
    }

    // To save to text file format
    public String toFileFormat() {
        return title + ";" + artist + ";" + duration;
    }

    // Create a MusicTrack from a file format string
    public static MusicTrack fromFileFormat(String line) {
        String[] parts = line.split(";");
        return new MusicTrack(parts[0], parts[1], parts[2]);
    }
}

public class MusicPlayer {
    private static final ArrayList<MusicTrack> playlist = new ArrayList<>();
    private static int currentTrackIndex = -1;
    private static boolean isPlaying = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            displayMenu();
            int choice = getValidChoice(sc);
            handleMenuChoice(sc, choice);
        }
    }

    private static void displayMenu() {
        System.out.println("\nMusic Player System");
        System.out.println("1. Add Track");
        System.out.println("2. Play");
        System.out.println("3. Pause");
        System.out.println("4. Next Track");
        System.out.println("5. Previous Track");
        System.out.println("6. Show Playlist");
        System.out.println("7. Remove Track");
        System.out.println("8. Search Track by Title");
        System.out.println("9. Save Playlist to File");
        System.out.println("10. Load Playlist from File");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getValidChoice(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input! Please enter a valid number.");
            sc.next(); // consume the invalid input
        }
        return sc.nextInt();
    }

    private static void handleMenuChoice(Scanner sc, int choice) {
        switch (choice) {
            case 1: addTrack(sc); break;
            case 2: playTrack(); break;
            case 3: pauseTrack(); break;
            case 4: playNextTrack(); break;
            case 5: playPreviousTrack(); break;
            case 6: showPlaylist(); break;
            case 7: removeTrack(sc); break;
            case 8: searchTrackByTitle(sc); break;
            case 9: savePlaylistToFile(); break;
            case 10: loadPlaylistFromFile(); break;
            case 0: exitPlayer(); break;
            default: System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addTrack(Scanner sc) {
        sc.nextLine(); // consume newline character
        System.out.print("Enter track title: ");
        String title = sc.nextLine();
        System.out.print("Enter artist name: ");
        String artist = sc.nextLine();
        System.out.print("Enter track duration (in mm:ss format, e.g., 3:45): ");
        String duration = sc.nextLine();

        MusicTrack track = new MusicTrack(title, artist, duration);
        playlist.add(track);
        System.out.println("Track added to the playlist: " + track);
    }

    private static void playTrack() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        if (!isPlaying) {
            currentTrackIndex = 0;
            isPlaying = true;
            System.out.println("Now playing: " + playlist.get(currentTrackIndex));
        } else {
            System.out.println("Resuming track: " + playlist.get(currentTrackIndex));
        }
    }

    private static void pauseTrack() {
        if (isPlaying) {
            isPlaying = false;
            System.out.println("Track paused: " + playlist.get(currentTrackIndex));
        } else {
            System.out.println("No track is currently playing.");
        }
    }

    private static void playNextTrack() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        if (currentTrackIndex < playlist.size() - 1) {
            currentTrackIndex++;
            isPlaying = true;
            System.out.println("Now playing: " + playlist.get(currentTrackIndex));
        } else {
            System.out.println("End of playlist.");
        }
    }

    private static void playPreviousTrack() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        if (currentTrackIndex > 0) {
            currentTrackIndex--;
            isPlaying = true;
            System.out.println("Now playing: " + playlist.get(currentTrackIndex));
        } else {
            System.out.println("Beginning of playlist.");
        }
    }

    private static void showPlaylist() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        System.out.println("Playlist:");
        for (int i = 0; i < playlist.size(); i++) {
            System.out.println((i + 1) + ". " + playlist.get(i));
        }
    }

    private static void removeTrack(Scanner sc) {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        showPlaylist();
        System.out.print("Enter the track number to remove: ");
        int trackNumber = getValidChoice(sc);
        if (trackNumber < 1 || trackNumber > playlist.size()) {
            System.out.println("Invalid track number.");
            return;
        }
        MusicTrack removedTrack = playlist.remove(trackNumber - 1);
        System.out.println("Track removed: " + removedTrack);
    }

    private static void searchTrackByTitle(Scanner sc) {
        sc.nextLine(); // consume newline character
        System.out.print("Enter track title to search for: ");
        String searchQuery = sc.nextLine().toLowerCase();

        boolean found = false;
        for (MusicTrack track : playlist) {
            if (track.toString().toLowerCase().contains(searchQuery)) {
                System.out.println("Found track: " + track);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tracks found with the title: " + searchQuery);
        }
    }

    private static void xsavePlaylistToFile() {
        if (playlist.isEmpty()) {
            System.out.println("Playlist is empty. Nothing to save.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("playlist.txt"))) {
            for (MusicTrack track : playlist) {
                writer.write(track.toFileFormat());
                writer.newLine();
            }
            System.out.println("Playlist saved to playlist.txt.");
        } catch (IOException e) {
            System.out.println("Error saving playlist: " + e.getMessage());
        }
    }

    private static void loadPlaylistFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("playlist.txt"))) {
            playlist.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                playlist.add(MusicTrack.fromFileFormat(line));
            }
            System.out.println("Playlist loaded from playlist.txt.");
        } catch (IOException e) {
            System.out.println("Error loading playlist: " + e.getMessage());
        }
    }

    private static void exitPlayer() {
        System.out.println("Exiting Music Player System.");
        System.exit(0);
    }
}

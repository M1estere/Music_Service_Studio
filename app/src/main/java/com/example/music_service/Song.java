package com.example.music_service;

public class Song
{
    public String path;

    public String title;
    public String artist;
//    public String cover;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Song(String path)
    {
        path = path;

        title = GetTitleFromPath(path);

        artist = "Maroon 5";
        //cover = SongsInfo.AllCovers[SongsInfo.AllSongs.IndexOf(Path)];
    }

    // convert 'ashes_of_dreams.mp3' to 'Ashes Of Dreams'
    public static String GetTitleFromPath(String title)
    {
        int index = title.indexOf('.');
        if (index >= 0)
            title = title.substring(0, index);

        String result = "";

        char prevChar = ' ';
        for (int i = 0; i < title.length(); i++)
        {
            if (Character.isWhitespace(prevChar)) // first iteration
            {
                prevChar = title.charAt(i);
                result += Character.toUpperCase(title.charAt(i));
                continue;
            }
            if (prevChar == '_')
            {
                result += Character.toUpperCase(title.charAt(i));
                prevChar = title.charAt(i);
                continue;
            }

            if (title.charAt(i) != '_') result += title.charAt(i);
            else result += ' ';

            prevChar = title.charAt(i);
        }

        return result;
    }
}

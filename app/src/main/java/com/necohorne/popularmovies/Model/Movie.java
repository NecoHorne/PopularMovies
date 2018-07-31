package com.necohorne.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Movie implements Parcelable {
    //implements parcelable so that we can easily put the object as a bundle to pass data between activities.

    private String posterPath;
    private boolean adult;
    private String overview;
    private String releaseDate;
    private int[] genreIds;
    public int id;
    private String imdbId;
    private String originalTitle;
    private String originalLanguage;
    private String backdropPath;
    private long popularity;
    private int voteCount;
    private boolean video;
    private long voteAverage;

    public Movie() {
    }

    public Movie(String posterPath, boolean adult, String overview, String releaseDate, int[] genreIds, int id, String imdbId, String originalTitle, String originalLanguage, String backdropPath, long popularity, int voteCount, boolean video, long voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.imdbId = imdbId;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        genreIds = in.createIntArray();
        id = in.readInt();
        imdbId = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        backdropPath = in.readString();
        popularity = in.readLong();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeIntArray(genreIds);
        dest.writeInt(id);
        dest.writeString(imdbId);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(backdropPath);
        dest.writeLong(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeLong(voteAverage);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(long voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "posterPath='" + posterPath + '\'' +
                ", adult=" + adult +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", genreIds=" + Arrays.toString(genreIds) +
                ", id=" + id +
                ", imdbId='" + imdbId + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", popularity=" + popularity +
                ", voteCount=" + voteCount +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                '}';
    }

}

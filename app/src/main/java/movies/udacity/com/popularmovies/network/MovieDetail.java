package movies.udacity.com.popularmovies.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mangesh on 21/2/16.
 */

public class MovieDetail implements Parcelable {

    public Long _id;

    private String poster_path;
    private Boolean adult;
    private String overview;
    private String release_date;
    // private List<Integer> genreIds = new ArrayList<Integer>();
    private Integer id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdrop_path;
    private Double popularity;
    private Integer vote_count;
    private Boolean video;
    private Double vote_average;

    private String movieTrailerOneID = null;

    public String getReview2() {
        return review2;
    }

    public void setReview2(String review2) {
        this.review2 = review2;
    }

    public String getReview1() {
        return review1;
    }

    public void setReview1(String review1) {
        this.review1 = review1;
    }

    public String getMovieTrailerOneID() {
        return movieTrailerOneID;
    }

    public void setMovieTrailerOneID(String movieTrailerOneID) {
        this.movieTrailerOneID = movieTrailerOneID;
    }

    public String getMovieTrailerTwoID() {
        return movieTrailerTwoID;
    }

    public void setMovieTrailerTwoID(String movieTrailerTwoID) {
        this.movieTrailerTwoID = movieTrailerTwoID;
    }

    private String movieTrailerTwoID = null;

    private String review1 = null;

    private String review2 = null;


    public boolean isOfflineData() {
        return isOfflineData != 0;
    }

    public void setOfflineData(int offlineData) {
        isOfflineData = offlineData;
    }

    private int isOfflineData = 0;

    public MovieDetail(int id, String title, String poster_path, Double vote_Average, String release_date, String overview,
                       String review1, String review2, String movieTrailerOneID, String movieTrailerTwoID, int isOfflineData) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.vote_average = vote_Average;
        this.release_date = release_date;
        this.overview = overview;
        this.review1 = review1;
        this.review2 = review2;
        this.movieTrailerOneID = movieTrailerOneID;
        this.movieTrailerTwoID = movieTrailerTwoID;
        this.isOfflineData = isOfflineData;

    }

    /**
     * @return The posterPath
     */
    public String getPosterPath() {
        return poster_path;
    }

    /**
     * @param posterPath The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.poster_path = posterPath;
    }

    /**
     * @return The adult
     */
    public Boolean getAdult() {
        return adult;
    }

    /**
     * @param adult The adult
     */
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    /**
     * @return The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @param overview The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * @return The releaseDate
     */
    public String getReleaseDate() {
        return release_date;
    }

    /**
     * @param releaseDate The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

  /*  *//**
     * @return The genreIds
     *//*
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    *//**
     * @param genreIds The genre_ids
     *//*
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }*/

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * @param originalTitle The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * @return The originalLanguage
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * @param originalLanguage The original_language
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The backdropPath
     */
    public String getBackdropPath() {
        return backdrop_path;
    }

    /**
     * @param backdropPath The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdrop_path = backdropPath;
    }

    /**
     * @return The popularity
     */
    public Double getPopularity() {
        return popularity;
    }

    /**
     * @param popularity The popularity
     */
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    /**
     * @return The voteCount
     */
    public Integer getVoteCount() {
        return vote_count;
    }

    /**
     * @param voteCount The vote_count
     */
    public void setVoteCount(Integer voteCount) {
        this.vote_count = voteCount;
    }

    /**
     * @return The video
     */
    public Boolean getVideo() {
        return video;
    }

    /**
     * @param video The video
     */
    public void setVideo(Boolean video) {
        this.video = video;
    }

    /**
     * @return The voteAverage
     */
    public Double getVoteAverage() {
        return vote_average;
    }

    /**
     * @param voteAverage The vote_average
     */
    public void setVoteAverage(Double voteAverage) {
        this.vote_average = voteAverage;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "adult=" + adult +
                ", posterPath='" + poster_path + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + release_date + '\'' +
                //   ", genreIds=" + genreIds +
                ", id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", title='" + title + '\'' +
                ", backdropPath='" + backdrop_path + '\'' +
                ", popularity=" + popularity +
                ", voteCount=" + vote_count +
                ", video=" + video +
                ", voteAverage=" + vote_average +
                '}';
    }

    public MovieDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.poster_path);
        dest.writeValue(this.adult);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeValue(this.id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
        dest.writeString(this.backdrop_path);
        dest.writeValue(this.popularity);
        dest.writeValue(this.vote_count);
        dest.writeValue(this.video);
        dest.writeValue(this.vote_average);
        dest.writeString(this.movieTrailerOneID);
        dest.writeString(this.movieTrailerTwoID);
        dest.writeString(this.review1);
        dest.writeString(this.review2);
        dest.writeInt(this.isOfflineData);
    }

    protected MovieDetail(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.poster_path = in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overview = in.readString();
        this.release_date = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.title = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_count = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.movieTrailerOneID = in.readString();
        this.movieTrailerTwoID = in.readString();
        this.review1 = in.readString();
        this.review2 = in.readString();
        this.isOfflineData = in.readInt();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}



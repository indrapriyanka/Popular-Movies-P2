package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by priyanka on 5/28/2017.
 */

public class CustomGrid implements Parcelable {
    private String posterName;
    private String title;
    private String overview;
    private String rating;
    private String release_date;
    private int id;
    private String review;

    public CustomGrid(Context c, String posterName, String title, String overview, String rating, String release_date, int id) {
        this.posterName = posterName;
        this.title = title;
        this.overview = overview;
        this.rating = rating;
        this.release_date = release_date;
        this.id = id;
    }
    public CustomGrid(Context c, String review)
    {
        this.review=review;
    }
    public CustomGrid()
    {

    }


    public String getOverview() {

        return overview;
    }

    public void setOverview(String overview) {

        this.overview = overview;
    }

    public String getRating() {

        return rating;
    }

    public void setRating(String rating) {

        this.rating = rating;
    }

    public String getRelease_date() {

        return release_date;
    }

    public void setRelease_date(String release_date) {

        this.release_date = release_date;
    }

    public String getPosterName() {

        return posterName;
    }

    public void setPosterName(String posterName) {

        this.posterName = posterName;
    }

    public String gettitle() {

        return title;
    }

    public void settitle(String title) {

        this.title = title;
    }
    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    private CustomGrid(Parcel in) {
        posterName = in.readString();
        title = in.readString();
        overview = in.readString();
        rating = in.readString();
        release_date = in.readString();
        id = in.readInt();
        review = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterName);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(rating);
        parcel.writeString(release_date);
        parcel.writeInt(id);
        parcel.writeString(review);
    }

    public final Parcelable.Creator<CustomGrid> CREATOR = new Parcelable.Creator<CustomGrid>() {
        @Override
        public CustomGrid createFromParcel(Parcel parcel) {

            return new CustomGrid(parcel);
        }

        @Override
        public CustomGrid[] newArray(int i) {
            return new CustomGrid[i];
        }
    };
}


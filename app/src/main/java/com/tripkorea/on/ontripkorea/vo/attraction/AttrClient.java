package com.tripkorea.on.ontripkorea.vo.attraction;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edward Won on 2018-06-11.
 */

public class AttrClient implements Parcelable {
    @SerializedName("contentid")
    public String contentID;
    @SerializedName("addr")
    public String addr;
    @SerializedName("title")
    public String title;
    @SerializedName("category_num")
    public String categoryNum;
    @SerializedName("summary")
    public String summary;
    @SerializedName("description")
    public String description;
    @SerializedName("contenttypeid")
    public int contentTypeID;
    @SerializedName("areacode")
    public int areacode;
    @SerializedName("cat1")
    public String cat1;
    @SerializedName("cat2")
    public String cat2;
    @SerializedName("cat3")
    public String cat3;
    @SerializedName("firstimg")
    public String firstimage;
    @SerializedName("firstimg2")
    public String firstimage2;
    @SerializedName("mapx")
    public String mapx;
    @SerializedName("mapy")
    public String mapy;
    @SerializedName("popular")
    public int popular;
    @SerializedName("steady")
    public int steady;
    @SerializedName("boardCount")
    public int boardCount;
    @SerializedName("festival_commence")
    public String festival_commence;
    @SerializedName("festival_end")
    public String festival_end;
    @SerializedName("direction")
    public String direction;
    @SerializedName("content_time")
    public String contentTime;
    @SerializedName("day_off")
    public String dayOff;
    @SerializedName("operating_hours")
    public String operatingHours;
    @SerializedName("fee")
    public String fee;
    @SerializedName("tag")
    public String tag;
    @SerializedName("importance")
    public int importance;
    @SerializedName("rating")
    public String rating;
    @SerializedName("distance")
    public double distance;
    @SerializedName("youtubekey")
    public String youtubekey;

    public String youtubetitle;
    public String youtubediscription;

    public int traceCheck;
    public int likeCheck;



    public AttrClient(){}


    protected AttrClient(Parcel in) {
        contentID = in.readString();
        addr = in.readString();
        title = in.readString();
        categoryNum = in.readString();
        summary = in.readString();
        description = in.readString();
        contentTypeID = in.readInt();
        areacode = in.readInt();
        cat1 = in.readString();
        cat2 = in.readString();
        cat3 = in.readString();
        firstimage = in.readString();
        firstimage2 = in.readString();
        mapx = in.readString();
        mapy = in.readString();
        popular = in.readInt();
        steady = in.readInt();
        boardCount = in.readInt();
        festival_commence = in.readString();
        festival_end = in.readString();
        direction = in.readString();
        contentTime = in.readString();
        dayOff = in.readString();
        operatingHours = in.readString();
        fee = in.readString();
        tag = in.readString();
        importance = in.readInt();
        rating = in.readString();
        youtubetitle = in.readString();
        youtubediscription  = in.readString();
        distance = in.readDouble();
        traceCheck = in.readInt();
        likeCheck = in.readInt();
        youtubekey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contentID);
        dest.writeString(addr);
        dest.writeString(title);
        dest.writeString(categoryNum);
        dest.writeString(summary);
        dest.writeString(description);
        dest.writeInt(contentTypeID);
        dest.writeInt(areacode);
        dest.writeString(cat1);
        dest.writeString(cat2);
        dest.writeString(cat3);
        dest.writeString(firstimage);
        dest.writeString(firstimage2);
        dest.writeString(mapx);
        dest.writeString(mapy);
        dest.writeInt(popular);
        dest.writeInt(steady);
        dest.writeInt(boardCount);
        dest.writeString(festival_commence);
        dest.writeString(festival_end);
        dest.writeString(direction);
        dest.writeString(contentTime);
        dest.writeString(dayOff);
        dest.writeString(operatingHours);
        dest.writeString(fee);
        dest.writeString(tag);
        dest.writeInt(importance);
        dest.writeString(rating);
        dest.writeString(youtubetitle);
        dest.writeString(youtubediscription);
        dest.writeDouble(distance);
        dest.writeInt(traceCheck);
        dest.writeInt(likeCheck);
        dest.writeString(youtubekey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttrClient> CREATOR = new Creator<AttrClient>() {
        @Override
        public AttrClient createFromParcel(Parcel in) {
            return new AttrClient(in);
        }

        @Override
        public AttrClient[] newArray(int size) {
            return new AttrClient[size];
        }
    };
}
package de.htwg.mobilecomputing.aid.Library;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LibraryElement implements Parcelable {
    private String _id;
    private String sensor;
    private String location;
    private int timestamp;
    private Bitmap image;
    private String imgUrl;

    /*private String label;
    private Date date;

    public LibraryElement(Image image, String label, Date date) {
        this.image = image;
        this.label = label;
        this.date = date;
    }*/

    protected LibraryElement(Parcel in) {
        //label = in.readString();
    }

    public String getId() {
        return _id;
    }

    public String getSensor() {
        return sensor;
    }

    public String getLocation() {
        return location;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    /*public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private static int count = 0;
    public static ArrayList<LibraryElement> generateElements(int number) {
        ArrayList<LibraryElement> elements = new ArrayList<>();
        for(int i=0; i<number; i++) {
            count++;
            Date date;
            if(count % 2 == 0)
                date = parseDate("2018-12-" + (count % 24) + " 01:00:00");
            else
                date = parseDate("2018-12-" + ((count - 1) % 24) + " 01:00:00");
            elements.add(new LibraryElement(null, "Image " + count, date));
        }
        return elements;
    }

    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(label);
    }

    public static final Creator<LibraryElement> CREATOR = new Creator<LibraryElement>() {
        @Override
        public LibraryElement createFromParcel(Parcel in) {
            return new LibraryElement(in);
        }

        @Override
        public LibraryElement[] newArray(int size) {
            return new LibraryElement[size];
        }
    };
}

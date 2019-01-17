package de.htwg.mobilecomputing.aid.Library;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class LibraryElement implements Parcelable, Comparable<LibraryElement>{
    private final String _id;
    private final String sensor;
    private final String location;
    private final long timestamp;
    private Bitmap image;

    protected LibraryElement(Parcel in) {
        _id = in.readString();
        sensor = in.readString();
        location = in.readString();
        timestamp = in.readLong();
        //image = in.readParcelable(Bitmap.class.getClassLoader());
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

    public String getId() {
        return _id;
    }

    public String getSensor() {
        return sensor;
    }

    public String getLocation() {
        return location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(sensor);
        dest.writeString(location);
        dest.writeLong(timestamp);
        //dest.writeParcelable(image, flags);
    }

    @Override
    public int compareTo(LibraryElement element) {
        long l = timestamp - element.getTimestamp();
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}

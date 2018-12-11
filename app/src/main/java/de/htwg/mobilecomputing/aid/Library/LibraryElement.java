package de.htwg.mobilecomputing.aid.Library;

import android.media.Image;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibraryElement {
    private Image image;
    private String label;
    private Date date;

    public LibraryElement(Image image, String label, Date date) {
        this.image = image;
        this.label = label;
        this.date = date;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getLabel() {
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
                date = parseDate("2018-12-" + (count % 24));
            else
                date = parseDate("2018-12-" + ((count - 1) % 24));
            elements.add(new LibraryElement(null, "Image " + count, date));
        }
        return elements;
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}

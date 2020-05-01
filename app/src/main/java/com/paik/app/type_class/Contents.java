package com.paik.app.type_class;

public class Contents  {
    private String text;
    private String time;
    private String photo;
    private String thumb;
    private String visit;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public String getViewer() {
        return visit;
    }

    public void setViewer(String viewer) {
        this.visit = viewer;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

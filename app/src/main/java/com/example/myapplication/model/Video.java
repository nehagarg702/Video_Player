package com.example.myapplication.model;

public class Video {
    private String title;
    private String description;
    private String url;
    private String thumb;
    private String id;
    private String time;

    public Video(String id,String Title, String Descriptuion, String Thumb,String URL,String TIME )
    {
        this.title=Title;
        this.id=id;
        this.description=Descriptuion;
        this.url=URL;
        this.thumb=Thumb;
        this.time=TIME;

    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getTime(){
        return time;
    }

    public void setTime(String time) { this.time = time; }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url) { this.url = url; }

    public String getUrlToImage(){
        return thumb;
    }

    public void setUrlToImage(String urlToImage) { this.thumb = urlToImage; }

    public String getid(){
        return id;
    }

    public void setid(String publishedAt) { this.id = id; }

}

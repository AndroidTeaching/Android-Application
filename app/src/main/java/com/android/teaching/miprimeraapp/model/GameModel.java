package com.android.teaching.miprimeraapp.model;

public class GameModel {

    private int id;
    private String name;
    private String description;
    private String officialWebsiteUrl;
    private String icon;
    private String background;

    public GameModel() {

    }

    public GameModel(int id, String name, String description, String officialWebsiteUrl,
                     String iconDrawable, String backgroundDrawable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.officialWebsiteUrl = officialWebsiteUrl;
        this.icon = iconDrawable;
        this.background = backgroundDrawable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfficialWebsiteUrl() {
        return officialWebsiteUrl;
    }

    public void setOfficialWebsiteUrl(String officialWebsiteUrl) {
        this.officialWebsiteUrl = officialWebsiteUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}

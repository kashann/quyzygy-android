package com.victor.oprica.quyzygy20.Model;

public class Subject {

    private String Description,Image,MenuId,Name;

    public Subject() {
    }

    public Subject(String description, String image, String menuId, String name) {
        Description = description;
        Image = image;
        MenuId = menuId;
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

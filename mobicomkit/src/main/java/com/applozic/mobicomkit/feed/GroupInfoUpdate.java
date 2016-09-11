package com.applozic.mobicomkit.feed;

import com.applozic.mobicommons.json.JsonMarker;

import java.io.Serializable;

/**
 * Created by sunil on 11/3/16.
 */
public class GroupInfoUpdate extends JsonMarker {

    private Integer groupId;
    private String clientGroupId;
    private String newName;
    private String imageUrl;

    public GroupInfoUpdate(String newName, int groupId) {
        this.newName = newName;
        this.groupId = groupId;
    }

    public GroupInfoUpdate(String newName, String clientGroupId) {
        this.newName = newName;
        this.clientGroupId = clientGroupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getClientGroupId() {
        return clientGroupId;
    }

    public void setClientGroupId(String clientGroupId) {
        this.clientGroupId = clientGroupId;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "GroupInfoUpdate{" +
                "groupId=" + groupId +
                ", clientGroupId='" + clientGroupId + '\'' +
                ", newName='" + newName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

package com.team08storyapp;

import android.graphics.Bitmap;

public class Photo {

	private int photoID;
	private Bitmap picture;

	public Photo() {

	}

	public Photo(Bitmap picture) {
		this.picture = picture;
	}

	public Photo(int photoID, Bitmap picture) {
		super();
		this.photoID = photoID;
		this.picture = picture;
	}

	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}

	public int getPhotoID() {
		return photoID;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public Bitmap getPicture() {
		return picture;
	}

	@Override
	public String toString() {
		return "Photo [photoID=" + photoID + ", picture=" + picture + "]";
	}
}

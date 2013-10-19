package com.team08storyapp;

import java.util.ArrayList;

public class StoryFragment {
	private int storyFragmentId;
	private String storyText;
	private ArrayList<Photo> photos;
	private ArrayList<Choice> choices;
	
	public StoryFragment(int storyFragmentId){
		this.storyFragmentId = storyFragmentId;
	}
	
	public StoryFragment(String storyText){
		this.storyText = storyText;
	}

	public int getStoryFragmentId() {
		return storyFragmentId;
	}

	public void setStoryFragmentId(int storyFragmentId) {
		this.storyFragmentId = storyFragmentId;
	}

	public String getStoryText() {
		return storyText;
	}

	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}

	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}

	public ArrayList<Choice> getChoices() {
		return choices;
	}

	public void setChoices(ArrayList<Choice> choices) {
		this.choices = choices;
	}
}

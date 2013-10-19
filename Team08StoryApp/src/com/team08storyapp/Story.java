package com.team08storyapp;

import java.util.ArrayList;

public class Story {

	private int storyId;
	private String title;
	private String author;
	private ArrayList<StoryFragment> storyFragments;
	private StoryFragment firstStoryFragment;
	
	public Story(String title, String author){
		this.title = title;
		this.author = author;
	}
	
	public Story(int id, String title, String author){
		this.storyId = id;
		this.title = title;
		this.author = author;
	}

	public int getStoryId() {
		return storyId;
	}

	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public ArrayList<StoryFragment> getStoryFragments() {
		return storyFragments;
	}

	public void setStoryFragments(ArrayList<StoryFragment> storyFragments) {
		this.storyFragments = storyFragments;
	}

	public StoryFragment getFirstStoryFragment() {
		return firstStoryFragment;
	}

	public void setFirstStoryFragment(StoryFragment firstStoryFragment) {
		this.firstStoryFragment = firstStoryFragment;
	}
	
	
}

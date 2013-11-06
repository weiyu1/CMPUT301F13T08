/*
AUTHORS
========
Alice Wu, Ana Marcu, Michele Paulichuk, Jarrett Toll, Jiawei Shen.

LICENSE
=======
Copyright  �  2013 Alice Wu, Ana Marcu, Michele Paulichuk, Jarrett Toll, Jiawei Shen,  
Free Software Foundation, Inc., Marky Mark  License GPLv3+: GNU
GPL version 3 or later <http://gnu.org/licenses/gpl.html>.
This program is free software: you can redistribute it and/or modify it under the terms of 
the GNU General Public License as published by the Free Software Foundation, either 
version 3 of the License, or (at your option) any later version. This program is distributed 
in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public 
License for more details. You should have received a copy of the GNU General Public License 
along with this program.  If not, see <http://www.gnu.org/licenses/>.
              
3rd Party Libraries
=============
Retrieved Oct. 27, 2013 - https://github.com/rayzhangcl/ESDemo
-This demo was used to help with JSON and ESHelper which is under the CC0 licenses

Retrieved Oct. 29, 2013  - http://hc.apache.org/downloads.cgi
-This is for the fluent library which is licensed under apache V2

Retrieved Oct. 29, 2013 
- https://code.google.com/p/google-gson/downloads/detail?name=google-gson-2.2.4-release.zip&can=2&q=
-This is for JSON which is licensed under apache V2
 */

package com.team08storyapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MyStoryFragmentActivity is a view class that displays a specific story fragment
 * for stories created by the user. Currently, users are able to add illustrations
 * to these fragments.
 * <p>
 * In future developments, users will be able to add/edit/delete dialogue and chocies from
 * this activity.
 * 
 * @author Michele Paulichuk
 * @author Alice Wu
 * @author Ana Marcu
 * @author Jarrett Toll
 * @author Jiawei Shen
 * @version 1.0 November 8, 2013
 * @since 1.0
 */

/**
 * variable for selection intent
 * variable to store the currently selected image
 * adapter for gallery view
 * gallery object
 * image view for larger display
 */

/**
 * instantiate the interactive gallery
 */
public class MyStoryFragmentActivity extends Activity {

    private int currentStoryFragmentId;
    private int currentStoryFragmentIndex;
    private int currentStoryId;
    private int currentPic = 0;
    private PicAdapter imgAdapt;
    private Gallery picGallery;
    private ListView lv;
    private View headerText;
    private View headerGallery;
    private ImageView picView;
    private Story currentStory;
    private StoryFragment currentStoryFragment;
    private FileHelper fHelper;
    private Uri imageFileUri;
    private PhotoController pc;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	fHelper = new FileHelper(this, 1);

	/* set up background layout */
	setContentView(R.layout.activity_story_list);
	lv = (ListView) findViewById(android.R.id.list);

	/* set up text header */
	headerText = getLayoutInflater().inflate(R.layout.header_text, null);
	TextView textSection = (TextView) headerText
		.findViewById(R.id.headerText);
	textSection.setOnTouchListener(new View.OnTouchListener() {
	    @Override
	    public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	    }
	});
	textSection.setMovementMethod(new ScrollingMovementMethod());

	/* set up gallery header */
	headerGallery = getLayoutInflater().inflate(R.layout.header_gallery,
		null);
	/* set up the picView */
	picView = (ImageView) headerGallery.findViewById(R.id.picture);

	/* get the gallery view */
	picGallery = (Gallery) headerGallery.findViewById(R.id.gallery);

	/* set the click listener for each item in the thumbnail gallery */
	picGallery.setOnItemClickListener(new OnItemClickListener() {
	    /* handle clicks */
	    public void onItemClick(AdapterView<?> parent, View v,
		    int position, long id) {
		/*
		 * set the larger image view to display the chosen bitmap
		 * calling method of adapter class
		 */
		picView.setImageBitmap(imgAdapt.getPic(position));
	    }
	});

	picGallery.setOnItemLongClickListener(new OnItemLongClickListener() {

	    /* handle long clicks */
	    public boolean onItemLongClick(AdapterView<?> parent, View v,
		    int position, long id) {
		currentPic = position;
		Intent pickIntent = new Intent();
		pickIntent.setType("image/*");
		pickIntent.setAction(Intent.ACTION_GET_CONTENT);
		/* we will handle the returned data in onActivityResult */
		startActivityForResult(
			Intent.createChooser(pickIntent, "Select Picture"), 1);
		return true;
	    }
	});

	/*
	 * Get the intent - passed either by Online/OfflineStoriesActivity or by
	 * StoryFragmentActivity
	 */
	Intent storyFragment = getIntent();

	/* Get the story object from the intent */
	currentStory = (Story) storyFragment.getSerializableExtra("story");

	/* Get the story fragment id from the intent - the fragment to display */
	currentStoryFragmentId = storyFragment
		.getIntExtra("storyFragmentId", 0);
	currentStoryId = currentStory.getOfflineStoryId();

	for (int i = 0; i < currentStory.getStoryFragments().size(); i++) {
	    if (currentStory.getStoryFragments().get(i).getStoryFragmentId() == currentStoryFragmentId) {
		currentStoryFragmentIndex = i;
	    }
	}

	/*
	 * The current story fragment object - from the story fragment list, by
	 * id
	 */
	currentStoryFragment = StoryController.readStoryFragment(
		currentStory.getStoryFragments(), currentStoryFragmentId);

	/* Display the current fragment text */
	textSection.setText(currentStoryFragment.getStoryText());

	/* The list of choices from the current fragment */
	ArrayList<Choice> storyFragmentChoices = currentStoryFragment
		.getChoices();

	pc = new PhotoController(this, getApplicationContext(), currentStory,
		currentStoryFragment, currentStoryFragmentIndex, fHelper);

	/*
	 * Populate choices listview with the go to choices from the current
	 * fragment
	 */
	ArrayList<Photo> illustrationList = currentStoryFragment.getPhotos();

	/* create a new adapter */
	imgAdapt = new PicAdapter(this, illustrationList, currentStoryId,
		currentStoryFragmentId);

	/* set the gallery adapter */
	picGallery.setAdapter(imgAdapt);
	fillChoice(storyFragmentChoices);
	lv.setOnItemClickListener(new OnItemClickListener() {

	    /* handle clicks */
	    public void onItemClick(AdapterView<?> parent, View v,
		    int position, long id) {
		Intent nextStoryFragment = new Intent(getApplicationContext(),
			MyStoryFragmentActivity.class);
		nextStoryFragment.putExtra("story", currentStory);
		Choice nextChoice = (Choice) lv.getAdapter().getItem(position);
		int nextStoryFragmentId = nextChoice.getStoryFragmentID();
		nextStoryFragment.putExtra("storyFragmentId",
			nextStoryFragmentId);
		startActivity(nextStoryFragment);

	    }
	});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	/* Inflate the menu; this adds items to the action bar if it is present. */
	getMenuInflater().inflate(R.menu.my_stories, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	/* Handle item selection */
	switch (item.getItemId()) {

	/* when user selects "View annotations" button in action bar */
	case R.id.take_picture:
	    if (pc.currentPosition() >= 4) {
		Toast.makeText(getApplicationContext(),
			"Sorry. Illustration gallery is full now.",
			Toast.LENGTH_LONG).show();
		return true;
	    }
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
	    startActivityForResult(intent, 2);
	    return true;

	    /* when user selects "add annotations" icon in action bar */
	case R.id.choose_picture:
	    if (pc.currentPosition() >= 4) {
		Toast.makeText(getApplicationContext(),
			"Sorry. Illustration gallery is full now.",
			Toast.LENGTH_LONG).show();
		return true;
	    }

	    /* take the user to their chosen image selection app */
	    Intent pickIntent = new Intent();
	    pickIntent.setType("image/*");
	    pickIntent.setAction(Intent.ACTION_GET_CONTENT);
	    startActivityForResult(
		    Intent.createChooser(pickIntent, "Select Picture"), 1);
	    return true;

	case R.id.main_menu:
	    Intent mainIntent = new Intent(getApplicationContext(),
		    MainActivity.class);
	    startActivity(mainIntent);
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    public void fillChoice(ArrayList<Choice> cList) {
	lv.addHeaderView(headerGallery);
	lv.addHeaderView(headerText);
	ChoiceAdapter adapter = new ChoiceAdapter(this, android.R.id.list,
		cList);
	lv.setAdapter(adapter);

    }

    protected void onResume() {
	try {
	    currentStory = fHelper.getOfflineStory(currentStoryId);
	    currentStoryFragment = currentStory.getStoryFragments().get(
		    currentStoryFragmentIndex);

	} catch (Exception e) {
	    e.printStackTrace();
	}
	super.onResume();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	if (resultCode == RESULT_OK) {
	    Uri pickedUri = data.getData();
	    Bitmap pic = pc.savePhoto(pickedUri);
	    if (pic != null) {
		currentPic = pc.currentPosition();
		if (currentPic > 4) {
		    currentPic = (currentPic % 5);
		}
		imgAdapt.addPic(currentPic, pic);
		picGallery.setAdapter(imgAdapt);
		picView.setImageBitmap(pic);
		picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	    }
	    try {
		currentStory = fHelper.getOfflineStory(currentStoryId);
		currentStoryFragment = currentStory.getStoryFragments().get(
			currentStoryFragmentIndex);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	} else {
	    super.onActivityResult(requestCode, resultCode, data);
	}
    }
}

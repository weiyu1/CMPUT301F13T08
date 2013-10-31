package com.team08storyapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
//variable for selection intent
//variable to store the currently selected image
//adapter for gallery view
//gallery object
//image view for larger display

/**
 * instantiate the interactive gallery
 */
public class StoryFragmentActivity extends Activity {

    private Story currentStory;
    private int currentStoryFragmentId;
    private StoryFragment currentStoryFragment;
    private final int PICKER = 1;
    private int currentPic = 0;
    private PicAdapter imgAdapt;
    private Gallery picGallery;
    private ListView lv;
    private View headerText;
    private View headerGallery;
    private ImageView picView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	// set up background layout
	setContentView(R.layout.activity_story_list);
	lv = (ListView) findViewById(android.R.id.list);

	// set up text header
	headerText = getLayoutInflater().inflate(R.layout.header_text, null);
	headerText.setBackgroundColor(0x0099cc);

	TextView textSection = (TextView) headerText
		.findViewById(R.id.headerText);

	textSection.setOnTouchListener(new View.OnTouchListener() {
	    @Override
	    public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	    }
	});
	textSection.setMovementMethod(new ScrollingMovementMethod());
	textSection
		.setText("One hundred and ninety-one floors up, you look over the edge of the roof and the street "
			+ "below is mottled with a shag carpet of people, standing, looking up. The breaking glass is a window "
			+ "right below us. A window blows out the side of the building, and then comes a file cabinet big as a "
			+ "black refrigerator, right below us a six-drawer filing cabinet drops right out of the cliff "
			+ "face of the building, and drops turning slowly, and drops getting smaller, and drops disappearing "
			+ "into the packed crowd. ");

	// set up gallery header
	headerGallery = getLayoutInflater().inflate(R.layout.header_gallery,
		null);
	// set up the picView
	picView = (ImageView) headerGallery.findViewById(R.id.picture);

	// get the gallery view
	picGallery = (Gallery) headerGallery.findViewById(R.id.gallery);

	// create a new adapter
	imgAdapt = new PicAdapter(this);
	// set the gallery adapter
	picGallery.setAdapter(imgAdapt);

	// set the click listener for each item in the thumbnail gallery
	picGallery.setOnItemClickListener(new OnItemClickListener() {
	    // handle clicks
	    public void onItemClick(AdapterView<?> parent, View v,
		    int position, long id) {
		// set the larger image view to display the chosen bitmap
		// calling method of adapter class
		picView.setImageBitmap(imgAdapt.getPic(position));
	    }
	});

	picGallery.setAdapter(imgAdapt);

	// Get the intent - passed either by Online/OfflineStoriesActivity or
	// by StoryFragmentActivity
	Intent storyFragment = getIntent();

	// Get the story object from the
	//intent 
	currentStory = (Story) storyFragment
		.getSerializableExtra("story"); // Get the current story
	// fragment id from the intent - the fragment to display
	currentStoryFragmentId = storyFragment
		.getIntExtra("storyFragmentId", 0);

	// The current story fragment object - get from the current story list
	// fragment, by id
	currentStoryFragment = StoryController.readStoryFragment(
		currentStory.getStoryFragments(), currentStoryFragmentId);
	textSection.setText(currentStoryFragment.getStoryText()); // fragment
	// choices now NULL - not sure why yet!
	ArrayList<Choice> storyFragmentChoices = currentStoryFragment
		.getChoices();
	fillChoice(storyFragmentChoices);
	// fillChoice(choiceList);

    }

    public void fillChoice(ArrayList<Choice> cList) {
	lv.addHeaderView(headerGallery);
	lv.addHeaderView(headerText);
	ChoiceAdapter adapter = new ChoiceAdapter(this, android.R.id.list,
		cList);
	lv.setAdapter(adapter);

    }

    public class PicAdapter extends BaseAdapter {

	// use the default gallery background image
	int defaultItemBackground;

	// gallery context
	private Context galleryContext;

	// array to store bitmaps to display
	private Bitmap[] imageBitmaps;
	// placeholder bitmap for empty spaces in gallery
	Bitmap placeholder;

	// constructor
	public PicAdapter(Context c) {

	    // instantiate context
	    galleryContext = c;

	    // create bitmap array
	    imageBitmaps = new Bitmap[10];
	    // decode the placeholder image
	    placeholder = BitmapFactory.decodeResource(getResources(),
		    R.drawable.ic_launcher);

	    // set placeholder as all thumbnail images in the gallery initially
	    for (int i = 0; i < imageBitmaps.length; i++)
		imageBitmaps[i] = placeholder;

	    // get the styling attributes - use default Andorid system resources
	    TypedArray styleAttrs = galleryContext
		    .obtainStyledAttributes(R.styleable.PicGallery);
	    // get the background resource
	    defaultItemBackground = styleAttrs.getResourceId(
		    R.styleable.PicGallery_android_galleryItemBackground, 0);
	    // recycle attributes
	    styleAttrs.recycle();
	}

	// BaseAdapter methods

	// return number of data items i.e. bitmap images
	public int getCount() {
	    return imageBitmaps.length;
	}

	// return item at specified position
	public Object getItem(int position) {
	    return position;
	}

	// return item ID at specified position
	public long getItemId(int position) {
	    return position;
	}

	// get view specifies layout and display options for each thumbnail in
	// the gallery
	public View getView(int position, View convertView, ViewGroup parent) {

	    // create the view
	    ImageView imageView = new ImageView(galleryContext);
	    // specify the bitmap at this position in the array
	    imageView.setImageBitmap(imageBitmaps[position]);
	    // set layout options
	    imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
	    // scale type within view area
	    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	    // set default gallery item background
	    imageView.setBackgroundResource(defaultItemBackground);
	    // return the view
	    return imageView;
	}

	// custom methods for this app

	// helper method to add a bitmap to the gallery when the user chooses
	// one
	public void addPic(Bitmap newPic) {
	    // set at currently selected index
	    imageBitmaps[currentPic] = newPic;
	}

	// return bitmap at specified position for larger display
	public Bitmap getPic(int posn) {
	    // return bitmap at posn index
	    return imageBitmaps[posn];
	}
    }

}

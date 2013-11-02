package com.team08storyapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileHelper {

    /*
     * a context variable provides access to application-specific resources and
     * classes, as well as up-calls for application-level operations such as
     * launching activities, broadcasting and receiving intents, etc. In this
     * case, it provides the directory of internal storage to store our files.
     */
    private Context fileContext;
    private Gson gson = new Gson();
    private static final int Download = 0;
    private static final int My = 1;
    private String prefix;

    /*
     * Initialize the fileContext with passed context. Since we design to store
     * author's own stories and downloaded stories in different directories in
     * internal storage, we need to differentiate them. (Actually I haven't come
     * up with a better idea than using a third party library called
     * DirectoryPicker when saving files in different directory in internal
     * storage. But I think the "prefix" can do the job as well. So I may stick
     * to it.)
     */
    public FileHelper(Context context, int mode) {
	fileContext = context;
	switch (mode) {
	case Download:
	    prefix = "Download";
	    break;
	case My:
	    prefix = "My";
	    break;
	}
    }

    /*
     * function: addOfflineStory input : Story story output: boolean value
     * 
     * description: Create a file named after the story's Id (since it's
     * unique), and write the story content into the file. If a story is
     * successfully written into the file, true will be returned. Otherwise,
     * function will return false.
     */
    public boolean addOfflineStory(Story story) throws FileNotFoundException,
	    IOException {
	try {
	    String fileName = prefix
		    + Integer.toString(story.getOfflineStoryId());
	    // translate the story context to Json
	    String context = gson.toJson(story);
	    FileOutputStream ops = fileContext.openFileOutput(fileName,
		    Context.MODE_PRIVATE);
	    ops.write(context.getBytes());
	    ops.close();
	    System.out.println(fileName);
	    return true;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    /*
     * function: updateOfflineStory input: Story story output: boolean value
     * 
     * description: Update process is done by deleting the original file and
     * write a new file. So this function deletes the original file named by the
     * story id and creates a new file and writes the updated content into the
     * new file, then saves it. When update is successful, true will be
     * returned, otherwise false will be returned.
     */
    public boolean updateOfflineStory(Story story)
	    throws FileNotFoundException, IOException {
	try {
	    String fileName = prefix
		    + Integer.toString(story.getOfflineStoryId());
	    fileContext.deleteFile(fileName); // delete original file
	    System.out.println("DELETE FILE");
	    System.out.println("FIND? "+getOfflineStory(story.getOfflineStoryId()));
	    addOfflineStory(story); // add new file
	    return true;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    /*
     * function: getOfflineStory input: int storyId output: Story story
     * 
     * description: Retrieve the file by the passed story id, and get the file.
     * Finally return it. If any error occurs, a null object will be returned.
     */
    public Story getOfflineStory(int storyId) throws FileNotFoundException,
	    IOException {
	try {
	    String fileName = prefix + Integer.toString(storyId);
	    InputStream is = fileContext.openFileInput(fileName);

	    if (is != null) {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String temp = "";
		StringBuilder stringBuilder = new StringBuilder();
		while ((temp = br.readLine()) != null) {
		    stringBuilder.append(temp);
		}
		is.close();
		Type storyType = new TypeToken<Story>() {
		}.getType();
		Story story = gson
			.fromJson(stringBuilder.toString(), storyType);
		System.out.println(story.toString());
		return story;
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
     * function: getOfflineStories input: N/A output: ArrayList<Story>
     * storyList;
     * 
     * description: First the function creates a file which contains the
     * directory of all files. Then file.listFiles() function returns a list of
     * files represented by the directory in the file. And the function will get
     * a list of stories in Json and put them into ArrayList<Story> sList. On
     * success, the sList will be returned with a list of stories. Otherwise
     * sList will be null.
     */
    public ArrayList<Story> getOfflineStories() throws FileNotFoundException,
	    IOException {
	File file = fileContext.getFilesDir();
	File[] fileList = file.listFiles();

	ArrayList<File> prefixFileList = new ArrayList<File>();
	for (int i = 0; i < fileList.length; i++) {
	    System.out.println(fileList[i].getName());
	    if (fileList[i].getName().startsWith(prefix)) {
		System.out.println("add " + fileList[i].getName());
		prefixFileList.add(fileList[i]);
	    }
	}

	ArrayList<Story> sList = new ArrayList<Story>();

	for (int i = 0; i < prefixFileList.size(); i++) {
	    // ReadIn process
	    InputStream is = new BufferedInputStream(new FileInputStream(
		    prefixFileList.get(i)));
	    if (is != null) {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String temp = "";
		StringBuilder stringBuilder = new StringBuilder();
		while ((temp = br.readLine()) != null) {
		    stringBuilder.append(temp);
		}
		is.close();
		// Translation process
		Type storyType = new TypeToken<Story>() {
		}.getType();
		Story story = gson
			.fromJson(stringBuilder.toString(), storyType);
		// System.out.println(story.toString());
		// Add process
		sList.add(story);
	    }
	}
	return sList;
    }

    /*
     * function: searchOfflineStories input: String searchText output:
     * ArrayList<Story> storyList;
     * 
     * description: First the function gets all the stories in the current
     * directory. And then compares each stories author and title with
     * searchText to determine if this story should be added into the result
     * storyList.
     */
    public ArrayList<Story> searchOfflineStories(String searchText) {
	searchText = searchText.toLowerCase();

	try {

	    ArrayList<Story> allList = getOfflineStories();
	    ArrayList<Story> resultList = new ArrayList<Story>();

	    for (int i = 0; i < allList.size(); i++) {
		String title = allList.get(i).getTitle().toLowerCase();
		String author = allList.get(i).getAuthor().toLowerCase();
		if (author.contains(searchText) || title.contains(searchText)) {
		    resultList.add(allList.get(i));
		}
	    }
	    return resultList;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Story encodeStory(Story s) throws IOException {
	// OnlineStory onlineStory = new OnlineStory(s.getOfflineStoryId());

	// get all fragments
	ArrayList<StoryFragment> sfList = s.getStoryFragments();

	// for each fragment, get it's photolist and annotation list

	for (int i = 0; i < sfList.size(); i++) {
	    ArrayList<Photo> photos = sfList.get(i).getPhotos();
	    ArrayList<Annotation> annotations = sfList.get(i).getAnnotations();

	    // set picture in each Photo object to empty after encoding.
	    for (int m = 0; m < photos.size(); m++) {
		InputStream is = fileContext.openFileInput(photos.get(m)
			.getPictureName());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = is.read(b)) != -1) {
		    bos.write(b, 0, bytesRead);
		}
		byte[] bytes = bos.toByteArray();

		photos.get(m).setEncodedPicture(
			Base64.encodeToString(bytes, Base64.DEFAULT));
	    }

	    /*
	     * Encode the photo pf annotation object first and clear the photo.
	     * since we don't want Json to handle huge byteArrays.
	     */

	    for (int n = 0; n < annotations.size(); n++) {

		InputStream is = fileContext.openFileInput(annotations.get(n)
			.getPhoto());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = is.read(b)) != -1) {
		    bos.write(b, 0, bytesRead);
		}
		byte[] bytes = bos.toByteArray();
		annotations.get(n).setEncodedAnnotation(
			Base64.encodeToString(bytes, Base64.DEFAULT));
	    }
	    sfList.get(i).setAnnotations(annotations);
	    sfList.get(i).setPhotos(photos);
	}
	return s;

    }

    public Story decodeStory(Story story) {
	// get a story
	ArrayList<StoryFragment> sfList = story.getStoryFragments();

	for (int i = 0; i < sfList.size(); i++) {
	    ArrayList<Photo> photos = sfList.get(i).getPhotos();
	    ArrayList<Annotation> annotations = sfList.get(i).getAnnotations();

	    for (int m = 0; m < photos.size(); m++) {
		byte[] photoByte = Base64.decode(photos.get(m).getEncodedPicture(),
			Base64.DEFAULT);
		Bitmap photoBM = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
		// clear the encoded string to avoid conflicts with encodeStory
		// and save spaces.
		photos.get(m).setEncodedPicture(null);

		String fileName;
		if (photos.get(m).getPictureName().isEmpty()) {
		    fileName = "ImageFragment" + Integer.toString(i + 1)
			    + "Photo" + Integer.toString(m + 1) + ".png";
		} else {
		    fileName = photos.get(m).getPictureName();
		}

		try {
		    FileOutputStream fos = fileContext.openFileOutput(fileName,
			    Context.MODE_PRIVATE);
		    photoBM.compress(CompressFormat.PNG, 90, fos);
		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	    for (int n = 0; n < annotations.size(); n++) {

		byte[] annotationByte = 
			Base64.decode(
				annotations.get(n).getEncodedAnnotation(),
				Base64.DEFAULT);
		Bitmap annotationBM = BitmapFactory.decodeByteArray(annotationByte, 0, annotationByte.length);

		annotations.get(n).setEncodedAnnotation(null);
		
		String fileName;
		if(annotations.get(n).getPhoto().isEmpty()){
		    fileName = "ImageFragment" + Integer.toString(i + 1)
			    + "Annotation" + Integer.toString(n + 1) + ".png";
		}else{
		    fileName = annotations.get(n).getPhoto();
		    
		}
		try {
		    FileOutputStream fos = fileContext.openFileOutput(fileName,
			    Context.MODE_PRIVATE);
		    annotationBM.compress(CompressFormat.PNG, 90, fos);
		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    sfList.get(i).setAnnotations(annotations);
	    sfList.get(i).setPhotos(photos);
	}
	return story;
    }
}

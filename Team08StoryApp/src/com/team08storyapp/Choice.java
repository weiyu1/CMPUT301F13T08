/*
AUTHORS
========
Alice Wu, Ana Marcu, Michele Paulichuk, Jarrett Toll, Jiawei Shen.

LICENSE
=======
Copyright  ���  2013 Alice Wu, Ana Marcu, Michele Paulichuk, Jarrett Toll, Jiawei Shen,  
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

import java.io.Serializable;

/**
 * Choice is a model for the possible choices a Story Fragment may contain. A
 * Choice for a Story Fragment leads the Story to another Story Fragment of the
 * Reader's choice.
 * <p>
 * A Choice has the following properties:
 * <ul>
 * <li>Choice Id to uniquely identify the choice.
 * <li>Text displayed for the choice.
 * <li>Story Fragment Id of the Story Fragment to go to from the choice.
 * </ul>
 * These properties are able to be accessed through the constructor or through
 * public getters/setters.
 * <p>
 * When a user is reading a Story, the user may come across a Story Fragment
 * that contains these Choices. The user must select one of the Choices in order
 * to lead to the next part of the Story. This is a way for the user to decide
 * where the Story may lead.
 * 
 * @see StoryFragment
 * @see Story
 * 
 * @author Michele Paulichuk
 * @author Alice Wu
 * @author Ana Marcu
 * @author Jarrett Toll
 * @author Jiawei Shen
 * @version 1.0 November 8, 2013
 * @since 1.0
 * 
 */
public class Choice implements Serializable {

    private static final long serialVersionUID = 1L;
    private String text;
    private int toGoToStoryFragmentID;
    private int choiceId;

    /**
     * The default constructor for creating an Choice object. It will have none
     * of it's properties set, requiring the class instantiating the object to
     * set it's properties.
     */
    public Choice() {
    }

    /**
     * A constructor for creating a Choice object. This is the only constructor
     * used and is used to help setup the Choices to display for a Story
     * Fragment for when a Story is being read by a user or saving Choices when
     * an Author is writing a Story. The following properties must be known at
     * time of creating a Choice object:
     * <ul>
     * <li>Story Fragment Id
     * <li>Choice Id
     * <li>Text
     * </ul>
     * 
     * @param toGoToStoryFragmentID
     *            Story Fragment the Choice leads to.
     * @param choiceId
     *            Unique identifier of the Choice.
     * @param text
     *            The text to display to the Choice.
     */
    public Choice(int toGoToStoryFragmentID, int choiceId, String text) {
	super();
	this.text = text;
	this.toGoToStoryFragmentID = toGoToStoryFragmentID;
	this.choiceId = choiceId;
    }

    /**
     * This getter method gets the text to display for the Choice.
     * 
     * @return The text to display for the Choice.
     */
    public String getText() {
	return text;
    }

    /**
     * This setter method sets the text to display for the Choice.
     * 
     * @param text
     *            The text to set for the Choice
     */
    public void setText(String text) {
	this.text = text;
    }

    /**
     * This getter method gets the unique identifier for Story Fragment that the
     * Choice leads to.
     * 
     * @return The unique identifier of the Story Fragment the Choice leads to.
     */
    public int getStoryFragmentID() {
	return toGoToStoryFragmentID;
    }

    /**
     * This setter method sets the unique identifier for Story Fragment that the
     * Choice leads to.
     * 
     * @param setStoryFragmentID
     *            The unique identifier of the Story Fragment to set for the
     *            Choice to lead to.
     */
    public void setStoryFragmentID(int toGoToStoryFragmentID) {
	this.toGoToStoryFragmentID = toGoToStoryFragmentID;
    }

    /**
     * This getter method gets the unique identifier used to identify the
     * Choice.
     * 
     * @return The unique identifier for the choice.
     */
    public int getChoiceId() {
	return choiceId;
    }

    /**
     * This setter method sets the unique identifier used to identify the
     * Choice.
     * 
     * @param choiceId
     *            The unique identifier to set for the choice.
     */
    public void setChoiceId(int choiceId) {
	this.choiceId = choiceId;
    }

}

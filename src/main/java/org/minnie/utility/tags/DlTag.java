package org.minnie.utility.tags;

import org.htmlparser.tags.CompositeTag;

public class DlTag extends CompositeTag {

	private static final long serialVersionUID = 3783715531840438812L;

	private static final String[] mIds = new String[] { "DL" };

	public String[] getIds() {
		return mIds;
	}

	public String[] getEnders() {
		return mIds;
	}
	
    public String getId(){
    	return super.getAttribute("id");
    }
    
}

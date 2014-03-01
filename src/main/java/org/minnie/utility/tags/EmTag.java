package org.minnie.utility.tags;

import org.htmlparser.tags.CompositeTag;

public class EmTag extends CompositeTag {
	
	private static final long serialVersionUID = 7961972032979956855L;
	
	private static final String[] mIds = new String[] { "EM" };

	public String[] getIds() {
		return mIds;
	}

	public String[] getEnders() {
		return mIds;
	}
	
}
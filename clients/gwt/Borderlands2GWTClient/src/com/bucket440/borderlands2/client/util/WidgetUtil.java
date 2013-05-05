package com.bucket440.borderlands2.client.util;

import java.util.Iterator;

import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class WidgetUtil {
	public static void maximize(Widget input){
		input.setWidth("100%");
		input.setHeight("100%");
	}
	
	public static void removeAllChildren(HasWidgets input){
		Iterator<Widget> iterator = input.iterator();
		while(iterator.hasNext()){
			input.remove(iterator.next());
		}
	}
}

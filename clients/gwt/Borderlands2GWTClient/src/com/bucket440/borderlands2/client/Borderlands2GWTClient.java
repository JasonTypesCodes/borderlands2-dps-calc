package com.bucket440.borderlands2.client;

import static com.bucket440.borderlands2.client.util.WidgetUtil.*;

import com.bucket440.borderlands2.client.widget.GunInputForm;
import com.bucket440.borderlands2.client.widget.GunResultDisplay;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Borderlands2GWTClient implements EntryPoint {

	GunResultDisplay gunResultDisplay;
	
	public void onModuleLoad() {
		RootPanel.get().add(buildPage());
	}
	
	private Widget buildPage(){
		VerticalPanel layout = new VerticalPanel();
		maximize(layout);
		layout.setSpacing(20);
		layout.add(buildHeader());
		layout.add(buildResultTabs());
		return layout;
	}
	
	private Widget buildHeader(){
		VerticalPanel header = new VerticalPanel();
		maximize(header);
		
		Label title = new Label("Borderlands 2 GWT Client");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		title.getElement().getStyle().setFontSize(36, Unit.PX);
		
		Button addGunButton = new Button("Add Gun");
		
		addGunButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				new GunInputForm().show();
			}
		});
		
		header.add(title);
		header.add(addGunButton);
		
		return header;
	}
	
	private Widget buildResultTabs(){
		gunResultDisplay = new GunResultDisplay();
		maximize(gunResultDisplay);
		return gunResultDisplay;
	}
}

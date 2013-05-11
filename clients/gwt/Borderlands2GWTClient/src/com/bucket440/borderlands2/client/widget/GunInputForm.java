package com.bucket440.borderlands2.client.widget;

import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.bucket440.borderlands2.client.gun.Gun;
import com.bucket440.borderlands2.client.gun.GunEventManager;
import com.bucket440.borderlands2.client.gun.GunType;
import com.bucket440.borderlands2.client.gun.RandomGunFactory;
import com.bucket440.borderlands2.client.rest.CalcServiceClient;
import com.bucket440.borderlands2.client.util.NameMaker;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class GunInputForm extends DialogBox {
	
	private LinkedHashMap<String, ValueBoxBase<?>> myFields;
	private Button calculateButton;
	private Button cancelButton;
	private GunTypeDropDown gunTypeDropDown;
	private GunInputForm me;
	private CalcServiceClient client;
	private DialogBox workingDialog;
	
	private static final String NAME = "Name";
	private static final String DAMAGE = "Damage";
	private static final String DAMAGE_MULTIPLIER = "Damage Multiplier";
	private static final String ACCURACY = "Accuracy";
	private static final String FIRE_RATE = "Fire Rate";
	private static final String RELOAD_SPEED = "Reload Speed";
	private static final String MAGAZINE_SIZE = "Magazine Size";
	private static final String ROUNDS_PER_SHOT = "Rounds Per Shot";
	private static final String ELEMENTAL_DPS = "Elemental DPS";
	private static final String ELEMENTAL_CHANCE = "Elemental Chance";
	
	
	public GunInputForm(){
		super();
		me = this;
		this.client = new CalcServiceClient(buildSuccessHandler(), buildErrorHandler());
		this.setAnimationEnabled(true);
		this.setGlassEnabled(true);
		this.setTitle("New Gun");
		this.add(buildForm());
		cancelButton.addClickHandler(buildCancelHandler());
		calculateButton.addClickHandler(buildSubmitHandler());
		//calculateButton.addClickHandler(buildTestSubmitHandler());
		buildWorkingDialog();
		this.center();
	}
	
	private void buildWorkingDialog(){
		workingDialog = new DialogBox();
		workingDialog.setAnimationEnabled(true);
		workingDialog.setGlassEnabled(true);
		workingDialog.setTitle("Calculating...");
		
		Label calculating = new Label("Calculating... Please Wait...");
		calculating.getElement().getStyle().setFontSize(24, Unit.PX);
		
		workingDialog.add(calculating);
		workingDialog.center();
		workingDialog.hide();
	}
	
	private Widget buildForm(){
		myFields = new LinkedHashMap<String, ValueBoxBase<?>>();
		gunTypeDropDown = new GunTypeDropDown();
		cancelButton = new Button("Cancel");
		calculateButton = new Button("Calculate");
		
		TextBox name = new TextBox();
		name.setValue(NameMaker.nameMaker().makeMeAName());
		myFields.put(NAME, name);
		myFields.put(DAMAGE, new DoubleBox());
		myFields.put(DAMAGE_MULTIPLIER, new IntegerBox());
		myFields.put(ACCURACY, new DoubleBox());
		myFields.put(FIRE_RATE, new DoubleBox());
		myFields.put(RELOAD_SPEED, new DoubleBox());
		myFields.put(MAGAZINE_SIZE, new IntegerBox());
		myFields.put(ROUNDS_PER_SHOT, new IntegerBox());
		myFields.put(ELEMENTAL_DPS, new DoubleBox());
		myFields.put(ELEMENTAL_CHANCE, new DoubleBox());
		
		Grid grid = new Grid(myFields.size() + 2, 2);
		
		grid.setWidget(0, 0, new Label("Type: "));
		grid.setWidget(0, 1, gunTypeDropDown);
		
		int row = 1;
		Iterator<Entry<String, ValueBoxBase<?>>> iterator = myFields.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, ValueBoxBase<?>> entry = iterator.next();
			grid.setWidget(row, 0, new Label(entry.getKey() + ": "));
			grid.setWidget(row, 1, entry.getValue());
			row++;
		}
		
		grid.setWidget(row, 0, cancelButton);
		grid.setWidget(row, 1, calculateButton);
		
		return grid;
	}
	
	public Gun buildNewGunFromForm(){
		String name = valueAsString(NAME);
		GunType type = gunTypeDropDown.getSelectedType();
		Double damage = valueAsDouble(DAMAGE);
		Integer damageMultiplier = valueAsInteger(DAMAGE_MULTIPLIER);
		Double accuracy = valueAsDouble(ACCURACY);
		Double fireRate = valueAsDouble(FIRE_RATE);
		Double reloadTime = valueAsDouble(RELOAD_SPEED);
		Integer magazineSize = valueAsInteger(MAGAZINE_SIZE);
		Integer roundsPerShot = valueAsInteger(ROUNDS_PER_SHOT);
		Double elementalDPS = valueAsDouble(ELEMENTAL_DPS);
		Double elementalChance = valueAsDouble(ELEMENTAL_CHANCE);
		
		return new Gun(name, type, damage, damageMultiplier, accuracy, fireRate, reloadTime, magazineSize, roundsPerShot, elementalDPS, elementalChance);
	}
	
	private boolean inputsAreValid(){
		for(ValueBoxBase<?> valueBox : myFields.values()){
			try {
				valueBox.getValueOrThrow();
			} catch (ParseException e) {
				return false;
			}
		}
		return true;
	}
	
	private Integer valueAsInteger(String key){
		if(myFields.containsKey(key) && myFields.get(key) instanceof IntegerBox){
			return ((IntegerBox)myFields.get(key)).getValue();
		}
		return null;
	}
	
	private Double valueAsDouble(String key){
		if(myFields.containsKey(key) && myFields.get(key) instanceof DoubleBox){
			return ((DoubleBox)myFields.get(key)).getValue();
		}
		return null;
	}
	
	private String valueAsString(String key){
		if(myFields.containsKey(key) && myFields.get(key) instanceof TextBox){
			return ((TextBox)myFields.get(key)).getValue();
		}
		return null;
	}
	
	private ClickHandler buildSubmitHandler(){
		return new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(inputsAreValid()){
					workingDialog.show();
					Gun newGun = me.buildNewGunFromForm();
					client.getCalculationsForGun(newGun);					
				} else {
					Window.alert("All fields must be numeric!");
				}
			}
		};
	}
	
	@SuppressWarnings("unused")
	private ClickHandler buildTestSubmitHandler(){
		return new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				workingDialog.hide();
				GunEventManager.getManagerInstance().publishGunAddedEvent(RandomGunFactory.randomGun());
			}
		};
	}
	
	private ClickHandler buildCancelHandler(){
		return new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				me.clear();
				me.hide();
				me = null;
			}
			
		};
	}
	
	private CalcServiceClient.OnSuccess buildSuccessHandler(){
		return new CalcServiceClient.OnSuccess() {
			@Override
			public void onSuccess(Gun result) {
				workingDialog.hide();
				GunEventManager.getManagerInstance().publishGunAddedEvent(result);
				me.hide();
				me.clear();
				me = null;
			}
		};
	}
	
	private CalcServiceClient.OnError buildErrorHandler(){
		return new CalcServiceClient.OnError() {
			@Override
			public void onError(Gun input, String errorMessage) {
				workingDialog.hide();
				Window.alert("Error! " + errorMessage);
			}
		};
	}
}

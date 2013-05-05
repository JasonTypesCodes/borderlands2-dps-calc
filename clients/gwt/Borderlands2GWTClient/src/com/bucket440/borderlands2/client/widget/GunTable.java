package com.bucket440.borderlands2.client.widget;

import static com.bucket440.borderlands2.client.util.WidgetUtil.maximize;

import java.util.Comparator;

import com.bucket440.borderlands2.client.gun.Gun;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class GunTable extends VerticalPanel{
	
	ListDataProvider<Gun> myGuns = new ListDataProvider<Gun>();
	ListHandler<Gun> sortHandler;
	CellTable<Gun> innerTable;
	Label defaultMessage;
	private boolean includeReload = true;
	private boolean includeAccuracy = true;
	private boolean includeElemental = true;
	
	public GunTable(){
		super();
		
		maximize(this);
		
		this.add(buildTop());
		rebuildResults();
	}
	
	public void rebuildResults(){
		this.add(buildResults());		
	}
	
	public void addGun(Gun newGun) {
		boolean doReload = false;
		if(!hasGuns()){
			doReload = true;
		}
		myGuns.getList().add(newGun);
		if(doReload){
			rebuildResults();
		}
		innerTable.setVisibleRange(0, myGuns.getList().size());
	}
	
	public void removeGun(Gun gun){
		myGuns.getList().remove(gun);
		rebuildResults();
	}
	
	public boolean hasGuns(){
		return myGuns.getList().size() > 0;
	}
	
	private Widget buildTop(){
		VerticalPanel top = new VerticalPanel();
		
		HorizontalPanel checkBoxes = new HorizontalPanel();
		CheckBox reloadBox = new CheckBox("Include Reload Time?");
		CheckBox accuracyBox = new CheckBox("Include Accuracy?");
		CheckBox elementalBox = new CheckBox("Include Elemental?");
		
		reloadBox.setValue(includeReload, false);
		accuracyBox.setValue(includeAccuracy, false);
		elementalBox.setValue(includeElemental, false);
		
		reloadBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				includeReload = ((CheckBox) event.getSource()).getValue();
				rebuildResults();
			}
		});
		
		accuracyBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				includeAccuracy = ((CheckBox) event.getSource()).getValue();
				rebuildResults();
			}
		});
		
		elementalBox.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				includeElemental = ((CheckBox) event.getSource()).getValue();
				rebuildResults();
			}
		});
		
		checkBoxes.add(reloadBox);
		checkBoxes.add(accuracyBox);
		checkBoxes.add(elementalBox);
		
		top.add(checkBoxes);
		
		return top;
	}
	
	private Widget buildResults(){
		clearResults();
		if(this.hasGuns()){
			Widget table = buildInnerTable();
			innerTable.setVisibleRange(0, myGuns.getList().size());
			return table;
		} else {
			defaultMessage = new Label("No guns right now!  Select 'Add Gun' above to input some!");
			return defaultMessage;
		}
	}
	
	private void clearResults(){
		if(innerTable != null && innerTable.isAttached()){
			innerTable.removeFromParent();
		}
		if(defaultMessage != null && defaultMessage.isAttached()){
			defaultMessage.removeFromParent();
		}
	}
	
	private Widget buildInnerTable(){
		
		innerTable = new CellTable<Gun>();
		
		TextColumn<Gun> nameColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return gun.name;
			}
		};
		
		nameColumn.setSortable(true);
		
		innerTable.addColumn(nameColumn, "Name");
		
		TextColumn<Gun> dpsColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				if(gun.hasResults()){
					return String.valueOf(gun.calculationResults.getDPS(includeReload, includeAccuracy, includeElemental));
				} else {
					return "N/A";
				}
			}
		};
		
		dpsColumn.setSortable(true);
		
		innerTable.addColumn(dpsColumn, "DPS");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.damage);
			}
			
		}, "Damage");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.damageMultiplier);
			}
			
		}, "Damage Multiplier");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.accuracy);
			}
			
		}, "Accuracy");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.fireRate);
			}
			
		}, "Fire Rate");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.reloadTime);
			}
			
		}, "Reload Time");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.magazineSize);
			}
			
		}, "Magazine Size");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.roundsPerShot);
			}
			
		}, "Rounds Per Shot");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.elementalDPS);
			}
			
		}, "Elemental DPS");
		
		innerTable.addColumn(new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.elementalChance);
			}
			
		}, "Elemental Chance");
		
		myGuns.addDataDisplay(innerTable);
		
		sortHandler = new ListHandler<Gun>(myGuns.getList());
		sortHandler.setComparator(nameColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		
		sortHandler.setComparator(dpsColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.calculationResults.getDPS(includeReload, includeAccuracy, includeElemental).compareTo(o2.calculationResults.getDPS(includeReload, includeAccuracy, includeElemental));
			}
		});
		
		innerTable.getColumnSortList().push(nameColumn);
		innerTable.getColumnSortList().push(dpsColumn);
		
		return innerTable;
	}
}

package com.bucket440.borderlands2.client.widget;

import static com.bucket440.borderlands2.client.util.WidgetUtil.maximize;

import java.util.Comparator;

import com.bucket440.borderlands2.client.gun.Gun;
import com.bucket440.borderlands2.client.gun.GunEventManager;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
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
		this.add(buildResults());
	}
	
	public void rebuildResults(){
		clearResults();
		this.add(buildResults());
		forceTableSort();
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
		refresh();
		forceTableSort();
	}
	
	public void forceTableSort(){
		if(innerTable != null){
			ColumnSortEvent.fire(innerTable, innerTable.getColumnSortList());			
		}
	}
	
	public void refresh(){
		if(this.hasGuns()){
			innerTable.setVisibleRange(0, myGuns.getList().size());
		}
		forceTableSort();
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
		
		/*
		 * I *hate* building out the table this way.  It is just so verbose.  Once I figure out a better way to do it, I will...
		 * If you are reading this comment and you know of a better way... please let me know what it is.
		 * -Jason
		 */
		
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
		
		TextColumn<Gun> damageColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.damage);
			}
		};
		
		damageColumn.setSortable(true);
		
		innerTable.addColumn(damageColumn, "Damage");
		
		TextColumn<Gun> damageMultiColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.damageMultiplier);
			}
		};
		
		damageMultiColumn.setSortable(true);
		
		innerTable.addColumn(damageMultiColumn, "Damage Multiplier");
		
		TextColumn<Gun> accuracyColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.accuracy);
			}
		};
		
		accuracyColumn.setSortable(true);
		
		innerTable.addColumn(accuracyColumn, "Accuracy");
		
		TextColumn<Gun> fireRateColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.fireRate);
			}
		};
		
		fireRateColumn.setSortable(true);
		
		innerTable.addColumn(fireRateColumn, "Fire Rate");
		
		TextColumn<Gun> reloadTimeColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.reloadTime);
			}
		};
		
		reloadTimeColumn.setSortable(true);
		
		innerTable.addColumn(reloadTimeColumn, "Reload Time");
		
		TextColumn<Gun> magazineSizeColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.magazineSize);
			}
		};
		
		magazineSizeColumn.setSortable(true);
		
		innerTable.addColumn(magazineSizeColumn, "Magazine Size");
		
		TextColumn<Gun> roundsPerShotColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.roundsPerShot);
			}
		};
		
		roundsPerShotColumn.setSortable(true);
		
		innerTable.addColumn(roundsPerShotColumn, "Rounds Per Shot");
		
		TextColumn<Gun> elementalDPSColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.elementalDPS);
			}
		};
		
		elementalDPSColumn.setSortable(true);
		
		innerTable.addColumn(elementalDPSColumn, "Elemental DPS");
		
		TextColumn<Gun> elementalChanceColumn = new TextColumn<Gun>(){
			@Override
			public String getValue(Gun gun) {
				return String.valueOf(gun.elementalChance);
			}
		};
		
		elementalChanceColumn.setSortable(true);
		
		innerTable.addColumn(elementalChanceColumn, "Elemental Chance");
		
		innerTable.addColumn(getEditButtonColumn(), "Edit");
		innerTable.addColumn(getDeleteButtonColumn(), "Delete");
		
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
		
		sortHandler.setComparator(damageColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.damage.compareTo(o2.damage);
			}
		});
		
		sortHandler.setComparator(damageMultiColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.damageMultiplier.compareTo(o2.damageMultiplier);
			}
		});
		
		sortHandler.setComparator(accuracyColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.accuracy.compareTo(o2.accuracy);
			}
		});
		
		sortHandler.setComparator(fireRateColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.fireRate.compareTo(o2.fireRate);
			}
		});
		
		sortHandler.setComparator(reloadTimeColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.reloadTime.compareTo(o2.reloadTime);
			}
		});
		
		sortHandler.setComparator(magazineSizeColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.magazineSize.compareTo(o2.magazineSize);
			}
		});
		
		sortHandler.setComparator(roundsPerShotColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.roundsPerShot.compareTo(o2.roundsPerShot);
			}
		});
		
		sortHandler.setComparator(elementalDPSColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.elementalDPS.compareTo(o2.elementalDPS);
			}
		});
		
		sortHandler.setComparator(elementalChanceColumn, new Comparator<Gun>(){
			@Override
			public int compare(Gun o1, Gun o2) {
				return o1.elementalChance.compareTo(o2.elementalChance);
			}
		});
		
		dpsColumn.setDefaultSortAscending(false);
		innerTable.getColumnSortList().push(dpsColumn);
		
		innerTable.addColumnSortHandler(sortHandler);
		
		return innerTable;
	}
	
	private Column <Gun, String> getDeleteButtonColumn(){
		ButtonCell deleteCell = new ButtonCell();
		Column<Gun, String> deleteColumn = new Column<Gun, String>(deleteCell){
			@Override
			public String getValue(Gun object) {
				return "Delete";
			}
			
		};
		
		deleteColumn.setFieldUpdater(new FieldUpdater<Gun, String>(){
			@Override
			public void update(int index, Gun object, String value) {
				if(Window.confirm("Are you sure you want to delete " + object.name + "?")){
					GunEventManager.getManagerInstance().publishGunRemovedEvent(object);
				}
			}
		});
		
		return deleteColumn;
	}
	
	private Column <Gun, String> getEditButtonColumn(){
		ButtonCell editCell = new ButtonCell();
		Column<Gun, String> editColumn = new Column<Gun, String>(editCell){
			@Override
			public String getValue(Gun object) {
				return "Edit";
			}
			
		};
		
		editColumn.setFieldUpdater(new FieldUpdater<Gun, String>(){
			@Override
			public void update(int index, Gun object, String value) {
				final Gun finalGun = object;
				GunEventManager.getManagerInstance().publishGunRemovedEvent(finalGun);
				GunInputForm myForm = new GunInputForm(finalGun);
				myForm.setOnCancel(new GunInputForm.OnCancel() {
					@Override
					public void onCancel() {
						GunEventManager.getManagerInstance().publishGunAddedEvent(finalGun);
					}
				});
				myForm.show();
			}
		});
		
		return editColumn;
	}
}

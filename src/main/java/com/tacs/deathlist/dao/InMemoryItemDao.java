package com.tacs.deathlist.dao;

import java.util.HashMap;
import java.util.Map;

import com.tacs.deathlist.domain.Item;

public class InMemoryItemDao implements ItemDao {

	static private Map<String,Item> items = new HashMap<>();	
	
	@Override
	public void createItem(String itemName, Item item) {
		items.put(itemName, item);
	}

	@Override
	public void deteleItem(String itemName) {
		items.remove(itemName);
	}

	@Override
	public void voteItem(String itemName) {
		Item item = items.remove(itemName);
		item.recibirVoto();
		items.put(itemName, item);
	}

}

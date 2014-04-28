package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Item;

public interface ItemDao {

	void createItem(String itemName, Item item);

	void deteleItem(String itemName);
	
	void voteItem(String itemName);
}

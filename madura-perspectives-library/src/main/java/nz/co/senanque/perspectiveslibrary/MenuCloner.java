/*******************************************************************************
 * Copyright (c)2014 Prometheus Consulting
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nz.co.senanque.perspectiveslibrary;

import java.util.List;

import nz.co.senanque.vaadin.CommandExt;
import nz.co.senanque.vaadin.MaduraSessionManager;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;

/**
 * Helper class that recursively clones one menu into another
 * resulting in a merged menu.
 * 
 * @author Roger Parkinson
 *
 */
public class MenuCloner {
	
	public static void merge(MenuBar target, MenuBar source, List<MenuBar.MenuItem> added)
	{
		if (source != null)
		{
			List<MenuBar.MenuItem> targetItems = target.getItems();
			for (MenuBar.MenuItem sourceItem:source.getItems())
			{
				MenuBar.MenuItem targetItem = findItem(sourceItem.getText(),targetItems);
				if (targetItem == null)
				{
					targetItem = target.addItem(sourceItem.getText(), sourceItem.getCommand());
					fixMenuItem(targetItem,sourceItem);
					added.add(targetItem);
				}
				merge(targetItem,sourceItem,added);
			}
		}
	}
	
	private static MenuBar.MenuItem findItem(String name, List<MenuBar.MenuItem> items)
	{
		if (items != null)
		{
			for (MenuBar.MenuItem item: items)
			{
				if (item.getText().equals(name))
				{
					return item;
				}
			}
		}
		return null;
	}
	
	private static void merge(MenuBar.MenuItem target, MenuBar.MenuItem source, List<MenuBar.MenuItem> added)
	{
		if (source.hasChildren())
		{
			List<MenuBar.MenuItem> targetItems = target.getChildren();
			for (MenuBar.MenuItem sourceItem:source.getChildren())
			{
				if (sourceItem.isSeparator())
				{
					target.addSeparator();
				}
				else
				{
					MenuBar.MenuItem targetItem = findItem(sourceItem.getText(),targetItems);
					if (targetItem == null)
					{
						targetItem = target.addItem(sourceItem.getText(), sourceItem.getCommand());
						fixMenuItem(targetItem,sourceItem);
						added.add(targetItem);
					}
					merge(targetItem,sourceItem,added);
				}
			}
		}
	}
	private static void fixMenuItem(MenuBar.MenuItem targetItem, MenuBar.MenuItem sourceItem)
	{
		Command command = sourceItem.getCommand();
		if (command != null && command instanceof CommandExt)
		{
			CommandExt commandExt = (CommandExt)command;
			MaduraSessionManager maduraSessionManager = commandExt.getMaduraSessionManager();
			maduraSessionManager.register(targetItem);
			maduraSessionManager.bind(targetItem);
		}
	}

	public static void clean(MenuBar target, List<MenuBar.MenuItem> added) {
		if (target != null)
		{
			List<MenuBar.MenuItem> items = target.getItems();
			for (MenuBar.MenuItem r:added)
			{
				for (MenuBar.MenuItem targetItem:items)
				{
					if (r.getId()==targetItem.getId())
					{
						items.remove(r);
						break;
					}
				}
			}
			for (MenuBar.MenuItem targetItem:items)
			{
				clean(targetItem,added);
			}
		}
		added.clear();
	}
	private static void clean(MenuBar.MenuItem target, List<MenuBar.MenuItem> added)
	{
		if (target.hasChildren())
		{
			List<MenuBar.MenuItem> items = target.getChildren();
			for (MenuBar.MenuItem r:added)
			{
				for (MenuBar.MenuItem targetItem:items)
				{
					if (r.getId()==targetItem.getId())
					{
						items.remove(r);
						break;
					}
				}
			}
			for (MenuBar.MenuItem targetItem:items)
			{
				clean(targetItem,added);
			}
		}
	}
}
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * @author Roger Parkinson
 *
 */
public class App {
	
	private static Logger logger = LoggerFactory.getLogger(App.class);
	private MenuBar m_menuBar;
	private ComponentContainer m_componentContainer;
	
	public App()
	{
	}

	public MenuBar getMenuBar() {
		return m_menuBar;
	}

	public void setMenuBar(MenuBar menuBar) {
		m_menuBar = menuBar;
	}

	public ComponentContainer getComponentContainer() {
		return m_componentContainer;
	}

	public void setComponentContainer(ComponentContainer componentContainer) {
		m_componentContainer = componentContainer;
	}

	public List<MenuItem> getMenuPainters() {
		return null;
	}
	
	


}

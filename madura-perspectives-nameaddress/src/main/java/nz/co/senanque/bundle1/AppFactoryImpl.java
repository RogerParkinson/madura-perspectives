/*******************************************************************************
 * Copyright (c)2011 Prometheus Consulting
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
package nz.co.senanque.bundle1;

import nz.co.senanque.perspectiveslibrary.App;
import nz.co.senanque.perspectiveslibrary.AppFactory;
import nz.co.senanque.perspectiveslibrary.Blackboard;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Creates an app for the Name and Address bundle.
 * 
 * @author Roger Parkinson
 *
 */
public class AppFactoryImpl implements AppFactory, MessageSourceAware {
	
	
	private MessageSource m_messageSource;

	/* (non-Javadoc)
	 * @see nz.co.senanque.bundle1.AppFactory#createApp()
	 */
	@Override
	public App createApp(Blackboard blackboard)
	{
		App ret = new App();
		final MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(m_messageSource);
		final Layout layout = new Layout(messageSourceAccessor);
		layout.setBlackboard(blackboard);
		ret.setComponentContainer(layout);

		MenuBar menuBar = new MenuBar();
		final MenuBar.MenuItem file = menuBar.addItem(messageSourceAccessor.getMessage("file"), null);
		file.addItem(messageSourceAccessor.getMessage("close"), new Command(){

			private static final long serialVersionUID = -1L;

			public void menuSelected(MenuItem selectedItem) {
				layout.close();
				
			}});
		final MenuBar.MenuItem save = menuBar.addItem(messageSourceAccessor.getMessage("edit"), null);
		save.addItem(messageSourceAccessor.getMessage("save"), new Command(){

			private static final long serialVersionUID = -1L;

			public void menuSelected(MenuItem selectedItem) {
				layout.close();
				
			}});
		
		ret.setMenuBar(menuBar);
		return ret;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		m_messageSource = messageSource;
		
	}
}

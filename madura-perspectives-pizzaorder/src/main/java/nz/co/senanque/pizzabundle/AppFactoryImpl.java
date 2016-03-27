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
package nz.co.senanque.pizzabundle;

import nz.co.senanque.perspectiveslibrary.App;
import nz.co.senanque.perspectiveslibrary.AppFactory;
import nz.co.senanque.perspectiveslibrary.Blackboard;
import nz.co.senanque.pizzaorder.instances.Pizza;
import nz.co.senanque.vaadin.CommandExt;
import nz.co.senanque.vaadin.MaduraFieldGroup;
import nz.co.senanque.vaadin.MaduraSessionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;

/**
 * @author Roger Parkinson
 *
 */
public class AppFactoryImpl implements AppFactory, BeanFactoryAware, MessageSourceAware {
	
	protected transient Logger log = LoggerFactory.getLogger(this.getClass());

	private MessageSource m_messageSource;
	private MaduraSessionManager m_maduraSessionManager;
	private BeanFactory m_beanFactory;

	/* (non-Javadoc)
	 * @see nz.co.senanque.bundle1.AppFactory#createApp()
	 */
	public App createApp(Blackboard blackboard)
	{
		// Explicitly fetch this bean to ensure it is not instantiated until the session has started.
		m_maduraSessionManager = m_beanFactory.getBean("maduraSessionManager",MaduraSessionManager.class);
		App ret = new App();
		MaduraFieldGroup fieldGroup = getMaduraSessionManager().createMaduraFieldGroup();
		final Layout layout = new Layout(m_maduraSessionManager,fieldGroup);
		layout.setBlackboard(blackboard);
		ret.setComponentContainer(layout);
		Pizza pizza = new Pizza();

		m_maduraSessionManager.getValidationSession().bind(pizza);
		MenuBar menuBar = new MenuBar();
		final MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(m_messageSource);
		final MenuBar.MenuItem edit = menuBar.addItem(messageSourceAccessor.getMessage("menu.edit", "Edit"), null);

		CommandExt command = fieldGroup.createMenuItemCommand(new ClickListener(){

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show(messageSourceAccessor.getMessage("message.clicked.cancel"),
						messageSourceAccessor.getMessage("message.noop"),
						Notification.Type.HUMANIZED_MESSAGE);
				
			}});
		MenuItem menuItemSave = edit.addItem("menu.cancel", command);
        fieldGroup.bind(menuItemSave);

		command = fieldGroup.createMenuItemCommandSubmit(new ClickListener(){

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show(messageSourceAccessor.getMessage("message.clicked.submit"),
						messageSourceAccessor.getMessage("message.noop"),
						Notification.Type.HUMANIZED_MESSAGE);
				
			}});
		MenuItem menuItemCancel = edit.addItem("menu.save", command);
        fieldGroup.bind(menuItemCancel);

		ret.setMenuBar(menuBar);

		layout.load(pizza);

		return ret;
	}

	public void setMessageSource(MessageSource messageSource) {
		m_messageSource = messageSource;
	}

	public MaduraSessionManager getMaduraSessionManager() {
		return m_maduraSessionManager;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		m_beanFactory = beanFactory;
	}

}

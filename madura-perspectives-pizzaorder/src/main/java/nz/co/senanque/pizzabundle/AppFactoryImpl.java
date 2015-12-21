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
import nz.co.senanque.pizzaorder.instances.ItemType;
import nz.co.senanque.pizzaorder.instances.OrderItem;
import nz.co.senanque.pizzaorder.instances.Pizza;
import nz.co.senanque.vaadin.CommandExt;
import nz.co.senanque.vaadin.MaduraSessionManager;
import nz.co.senanque.vaadin.MenuItemPainter;
import nz.co.senanque.vaadin.SubmitButtonPainter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * @author Roger Parkinson
 *
 */
public class AppFactoryImpl implements AppFactory, MessageSourceAware, BeanFactoryAware {
	
	protected transient Logger log = LoggerFactory.getLogger(this.getClass());

	private MessageSourceAccessor m_messageSourceAccessor;
	private MaduraSessionManager m_maduraSessionManager;
	private BeanFactory m_beanFactory;

	/* (non-Javadoc)
	 * @see nz.co.senanque.bundle1.AppFactory#createApp()
	 */
	public App createApp(Blackboard blackboard)
	{
		// Explicitly fetch this bean to ensure it is not instantiated until the session has started.
		m_maduraSessionManager = m_beanFactory.getBean("maduraSessionManager",MaduraSessionManager.class);
//		log.debug("found maduraSessionManager {} {}",m_maduraSessionManager.toString(),System.identityHashCode(m_maduraSessionManager));
//		log.debug("found permissionManager {} {}",m_maduraSessionManager.getPermissionManager().toString(),System.identityHashCode(m_maduraSessionManager.getPermissionManager()));
		App ret = new App();
		final Layout layout = new Layout(m_maduraSessionManager);
		layout.setBlackboard(blackboard);
		ret.setComponentContainer(layout);
		Pizza orderItem = new Pizza();
		orderItem.setItemType(ItemType.PIZZA);
		m_maduraSessionManager.getValidationSession().bind(orderItem);
		layout.setItemDataSource(new BeanItem<OrderItem>(orderItem));
		MenuBar menuBar = new MenuBar();
		final MenuBar.MenuItem edit = menuBar.addItem(m_messageSourceAccessor.getMessage("edit", "Edit"), null);
		MenuItem menuItem = edit.addItem(m_messageSourceAccessor.getMessage("save", "Save"), new CommandExt(){

			private static final long serialVersionUID = 1L;
			MenuItemPainter m_menuItemPainter = new SubmitButtonPainter(m_maduraSessionManager);
			
			public void menuSelected(MenuItem selectedItem) {
				layout.save();
			}

			public MenuItemPainter getPainter() {
				return m_menuItemPainter;
			}

			public MaduraSessionManager getMaduraSessionManager() {
				return m_maduraSessionManager;
			}});
		m_maduraSessionManager.register(menuItem);
		m_maduraSessionManager.bind(menuItem, layout.getProperties());
		ret.setMenuBar(menuBar);
		return ret;
	}

	public void setMessageSource(MessageSource messageSource) {
		m_messageSourceAccessor = new MessageSourceAccessor(messageSource);
	}

	public MaduraSessionManager getMaduraSessionManager() {
		return m_maduraSessionManager;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		m_beanFactory = beanFactory;
	}

}

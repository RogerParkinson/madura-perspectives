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
package nz.co.senanque.bmi;

import java.util.Locale;

import nz.co.senanque.perspectiveslibrary.Blackboard;
import nz.co.senanque.pizzaorder.instances.Customer;
import nz.co.senanque.vaadin.MaduraFieldGroup;
import nz.co.senanque.vaadin.MaduraSessionManager;
import nz.co.senanque.vaadin.directed.OneFieldWindowFactory;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Roger Parkinson
 *
 */
public class Layout extends VerticalLayout {

	private VerticalLayout mainLayout;
	private static final long serialVersionUID = -1L;
	private Blackboard m_blackboard;
	private MaduraFieldGroup m_maduraFieldGroup;
    private final MaduraSessionManager m_maduraSessionManager;
    private OneFieldWindowFactory m_oneFieldWindowFactory;
	private VerticalLayout m_panel;
	private Customer m_customer;

    public MaduraSessionManager getMaduraSessionManager() {
		return m_maduraSessionManager;
	}
	public Layout(MaduraSessionManager maduraSessionManager, OneFieldWindowFactory oneFieldWindowFactory) {
		m_maduraSessionManager = maduraSessionManager;
		m_oneFieldWindowFactory = oneFieldWindowFactory;
		m_maduraFieldGroup = m_maduraSessionManager.createMaduraFieldGroup();
		buildMainLayout();
		addComponent(mainLayout);
	}
	public Blackboard getBlackboard() {
		return m_blackboard;
	}
	public void setBlackboard(Blackboard blackboard) {
		m_blackboard = blackboard;
	}
	public void load(final Customer customer) {
		m_panel.removeAllComponents();
		getMaduraSessionManager().getValidationSession().bind(customer);
		m_customer = customer;
	}
	public void activated() {
		// this is fired when the bundle activates.
	}
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// m_name
		Locale locale = LocaleContextHolder.getLocale();
		MessageSource messageSource = getMaduraSessionManager().getMessageSource();
		String s = messageSource.getMessage("bmi.button", null, locale);
		Button bmiButton = m_maduraFieldGroup.createButton("bmi.button", new ClickListener(){

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				m_oneFieldWindowFactory.createWindow(m_customer, "bmi",ValoTheme.BUTTON_PRIMARY);
			}});
		mainLayout.addComponent(bmiButton);
		m_panel = new VerticalLayout();
		mainLayout.addComponent(m_panel);
		return mainLayout;
	}
}

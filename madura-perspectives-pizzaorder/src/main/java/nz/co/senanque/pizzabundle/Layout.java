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

import nz.co.senanque.perspectiveslibrary.Blackboard;
import nz.co.senanque.perspectiveslibrary.BlackboardListener;
import nz.co.senanque.pizzaorder.instances.Pizza;
import nz.co.senanque.vaadin.MaduraFieldGroup;
import nz.co.senanque.vaadin.MaduraSessionManager;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Roger Parkinson
 *
 */
public class Layout extends VerticalLayout {

	private VerticalLayout mainLayout;
	private MaduraFieldGroup m_fieldGroup;
	private TextField textUserName;
	private static final long serialVersionUID = -1L;
	private Blackboard m_blackboard;
	private String[] fieldList = new String[]{"base","topping","size","amount","testing","description"};
    private final MaduraSessionManager m_maduraSessionManager;
	private VerticalLayout m_panel;

    public MaduraSessionManager getMaduraSessionManager() {
		return m_maduraSessionManager;
	}
	public Layout(MaduraSessionManager maduraSessionManager,MaduraFieldGroup fieldGroup) {
		m_maduraSessionManager = maduraSessionManager;
		m_fieldGroup = fieldGroup;
		buildMainLayout();
		addComponent(mainLayout);
	}
	public Blackboard getBlackboard() {
		return m_blackboard;
	}
	public void setBlackboard(Blackboard blackboard) {
		m_blackboard = blackboard;
		m_blackboard.add(new BlackboardListener(){

			public void valueChanged(Object value) {
				textUserName.setValue(value.toString());
			}

			public String getName() {
				return "userName";
			}});
	}
	public void load(final Pizza pizza) {
		m_panel.removeAllComponents();
		getMaduraSessionManager().getValidationSession().bind(pizza);
    	BeanItem<Pizza> beanItem = new BeanItem<Pizza>(pizza);
    	m_fieldGroup.buildAndBind(m_panel, fieldList, beanItem);  	
	}
	public void setItemDataSource(BeanItem<Pizza> beanItem) {
		m_fieldGroup.setItemDataSource(beanItem);
	}
	public void save() {
		@SuppressWarnings("unchecked")
		BeanItem<Pizza> beanItem = (BeanItem<Pizza>)m_fieldGroup.getItemDataSource();
		m_blackboard.publish("orderItem", beanItem.getBean());
	}
	@AutoGenerated
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
		
		// textField_1
		textUserName = new TextField();
		textUserName.setCaption("User Name");
		textUserName.setImmediate(false);
		textUserName.setWidth("-1px");
		textUserName.setHeight("-1px");
		mainLayout.addComponent(textUserName);
		
		m_panel = new VerticalLayout();
		mainLayout.addComponent(m_panel);
		
		return mainLayout;
	}
	public MaduraFieldGroup getFieldGroup() {
		return m_fieldGroup;
	}
}

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

import nz.co.senanque.perspectiveslibrary.App;
import nz.co.senanque.perspectiveslibrary.AppFactory;
import nz.co.senanque.perspectiveslibrary.Blackboard;
import nz.co.senanque.pizzaorder.instances.Customer;
import nz.co.senanque.vaadin.MaduraSessionManager;
import nz.co.senanque.vaadin.directed.OneFieldWindowFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

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
		Locale locale = LocaleContextHolder.getLocale();
		m_maduraSessionManager = m_beanFactory.getBean("maduraSessionManager",MaduraSessionManager.class);
		OneFieldWindowFactory oneFieldWindowFactory = m_beanFactory.getBean(OneFieldWindowFactory.class);
		App ret = new App();
		final Layout layout = new Layout(m_maduraSessionManager,oneFieldWindowFactory);
		layout.setBlackboard(blackboard);
		ret.setComponentContainer(layout);
		Customer customer = new Customer();

		m_maduraSessionManager.getValidationSession().bind(customer);
		layout.load(customer);

		return ret;
	}

	public MaduraSessionManager getMaduraSessionManager() {
		return m_maduraSessionManager;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		m_beanFactory = beanFactory;
	}
	public void setMessageSource(MessageSource messageSource) {
		m_messageSource = messageSource;
	}

}

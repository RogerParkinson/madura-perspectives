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

import java.io.Serializable;

import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.Resource;

/**
 * Encapsulates a sub-application that can be plugged into the perspectives manager.
 * The UI part of the application is held in the ComponentContainer.
 * The MenuBar will be merged with the menu in the main application (and de-merged
 * when we switch away from this application)
 * 
 * @author Roger Parkinson
 *
 */
public class SubApplicationimpl implements SubApplication, Serializable  {
	
	private static final long serialVersionUID = -1367277688477759273L;
	private String m_caption;
	private String m_description;
	private Resource m_icon;
	private MessageSource m_messageSource;
	private AppFactory m_appFactory;
	private String m_version;
	private BundleVersion m_bundleVersion;

    public void setMessageSource(MessageSource messageSource)
    {
    	m_messageSource = messageSource;
    }
	/* (non-Javadoc)
	 * @see nz.co.maduraperspectivemanager.SubApplication#getCaption()
	 */
	public String getCaption() {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(m_messageSource);
		return (m_caption==null)?null:messageSourceAccessor.getMessage("project.name",null,m_caption);
	}

	/* (non-Javadoc)
	 * @see nz.co.maduraperspectivemanager.SubApplication#getTooltip()
	 */
	public String getDescription() {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(m_messageSource);
		return (m_description==null)?null:messageSourceAccessor.getMessage(m_description,null,m_description);
	}

	public void setCaption(String caption) {
		m_caption = caption;
	}

	public void setDescription(String description) {
		m_description = description;
	}

	public Resource getIcon() {
		return m_icon;
	}
	public void setIcon(Resource icon) {
		m_icon = icon;
	}
	public String getVersion() {
		return m_version;
	}
	public void setVersion(String version) {
		m_version = version;
	}
	public MessageSource getMessageSource() {
		return m_messageSource;
	}
	public AppFactory getAppFactory() {
		return m_appFactory;
	}
	public void setAppFactory(AppFactory appFactory) {
		m_appFactory = appFactory;
	}
	public BundleVersion getBundleVersion() {
		return m_bundleVersion;
	}
	public void setBundleVersion(BundleVersion bundleVersion) {
		m_bundleVersion = bundleVersion;
	}

}
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nz.co.senanque.madura.bundle.BundleListener;
import nz.co.senanque.madura.bundle.BundleRoot;
import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;

/**
 * 
 * This bundle listener registers new bundled applications as they arrive.
 * 
 * @author Roger Parkinson
 * @version $Revision:$
 */
public class BundleListenerImpl implements BundleListener, Serializable
{
	private static final long serialVersionUID = 1L;
	private Logger m_logger = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource m_messageSource;
	private Map<String,Set<BundleVersion>> m_map = new HashMap<String,Set<BundleVersion>>();
	private Comparator<BundleVersion> m_comparator = new Comparator<BundleVersion>(){

		public int compare(BundleVersion o1, BundleVersion o2) {
            if ( o2.getVersion().equals(o1.getVersion()) ) 
                return 0;
			return o2.getVersion().compareTo(o1.getVersion());
		}};
	
	private Map<String,SubApplication> m_subApplications = Collections.synchronizedMap(new HashMap<String,SubApplication>());
	
	private BundleVersion addToMap(BundleVersion bundleVersion) {
		Set<BundleVersion> bundles = m_map.get(bundleVersion.getName());
		if (bundles == null) {
			// No bundles of this name yet to create new name
			bundles = new TreeSet<BundleVersion>(m_comparator);
			bundles.add(bundleVersion);
			m_map.put(bundleVersion.getName(), bundles);
		} else {
			// we do have a bundle of this name so add this version
			// it is a Set so if it is already there nothing happens.
			bundles.add(bundleVersion);
		}
		return getLatestVersion(bundleVersion.getName());
	}
	private BundleVersion removeFromMap(BundleVersion bundleVersion) {
		Set<BundleVersion> bundles = m_map.get(bundleVersion.getName());
		if (bundles == null) {
			// No bundles of this name yet to create new name
			return null;
		} else {
			// we do have a bundle of this name so add this version
			// it is a Set so if it is already there nothing happens.
			bundles.remove(bundleVersion);
		}
		return getLatestVersion(bundleVersion.getName());
	}
	private BundleVersion getLatestVersion(String name) {
		Set<BundleVersion> bundles = m_map.get(name);
		if (bundles == null || bundles.isEmpty()) {
			// No bundles of this name yet return null
			return null;
		} else {
			// we do have bundles of this name so get the latest version
			return bundles.iterator().next(); // gets the first one in the set
		}
	}

    public void add(BundleVersion bv)
    {
    	// ensure we record this bundle and also coerce it to the latest version.
    	// That way if a bundle with an earlier version shows up we don't use it, 
    	// but we keep it in case the later version gets deleted
    	BundleVersion bundleVersion = addToMap(bv);
    	addSubApplication(bundleVersion);
    }

    public void remove(BundleVersion bv)
    {
        m_logger.info("removed SubApplication {}",bv.getId());
        m_subApplications.remove(bv.getName());
        BundleVersion bundleVersion = removeFromMap(bv);
        if (bundleVersion != null) {
        	addSubApplication(bundleVersion);
        }
    }
    
    /**
     * Given a BundleVersion set up the Sub Application.
     * Do nothing if there is an existing Sub Application with a later version.
     * @param bundleVersion
     */
    private void addSubApplication(BundleVersion bundleVersion) {
        String bundleName = bundleVersion.getName();
        try {
        	BundleRoot root = bundleVersion.getRoot();
        	SubApplication subApplication = (SubApplication)root.getApplicationContext().getBean("SubApplication");
        	SubApplication existing = m_subApplications.get(bundleName);
        	if (existing == null || (existing.getVersion().compareToIgnoreCase(subApplication.getVersion()) < 0))
        	{
        		// If this is the latest version of this bundle then set the app message source as the parent message source
        		// and add the sub application to the map.
        		AbstractMessageSource messageSource = (AbstractMessageSource)subApplication.getMessageSource();
        		messageSource.setParentMessageSource(getMessageSource());
        		m_subApplications.put(bundleName, subApplication);
        		subApplication.setBundleVersion(bundleVersion);
        	}
		} catch (BeansException e) {
			// ignore bundles with missing bean, assume they do something else.
		}    	
        m_logger.info("added SubApplication {}",bundleVersion.getId());
    }

	public Collection<SubApplication> getSubApplicationValues() {
		return m_subApplications.values();
	}

	public Map<String, SubApplication> getSubApplications() {
		return m_subApplications;
	}

	public void setSubApplications(Map<String, SubApplication> subApplications) {
		m_subApplications = subApplications;
	}

	public MessageSource getMessageSource() {
		return m_messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		m_messageSource = messageSource;
	}

}

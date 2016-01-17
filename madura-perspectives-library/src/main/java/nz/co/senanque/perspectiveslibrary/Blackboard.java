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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.vaadin.spring.annotation.UIScope;

/**
 * The blackboard facilitiates loose coupling between applications using a publish and subscribe pattern
 * An application publishes a change of value and this is passed to all the registered listeners.
 * Because a new listener may be registered at any time a log of published items is held and this log is
 * played against any newly registered listeners.
 * 
 * The applications need to agree on the name/object, eg they might both refer to something called 'CUSTOMER' whose
 * value is a Customer object.
 * 
 * The log of published items might grow quite large if there are lots of publishes in a session. However the log
 * does not persist past the end of a session and a session only lasts as long as someone is operating it. This is
 * mitigated by having duplicate published items overwrite each other.
 * 
 * There must be no assumptions in the listeners about the order things are published.
 * 
 * @author Roger Parkinson
 *
 */
@Component("blackboard")
@UIScope
public class Blackboard implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Set<BlackboardListener> m_listeners = Collections.synchronizedSet(new HashSet<BlackboardListener>());
	private Map<String,Object> m_publishedItems = Collections.synchronizedMap(new HashMap<String,Object>());
	
	public void add(BlackboardListener listener)
	{
		m_listeners.add(listener);
		for (Map.Entry<String,Object> publishedItem: m_publishedItems.entrySet())
		{
			if (listener.getName().equals(publishedItem.getKey()))
			{
				listener.valueChanged(publishedItem.getValue());
			}
		}
	}
	public void remove(BlackboardListener listener)
	{
		m_listeners.remove(listener);
	}
	public void publish(String name, Object value)
	{
		m_publishedItems.put(name,value);
		for (BlackboardListener listener: m_listeners)
		{
			if (listener.getName().equals(name))
			{
				listener.valueChanged(value);
			}
		}
	}
}

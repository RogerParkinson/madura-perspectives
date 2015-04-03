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
package nz.co.senanque.bundle2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roger Parkinson
 *
 */
public class TestSessionBean {
	
	private String m_field;
	private Logger log = LoggerFactory.getLogger(TestSessionBean.class);

	public TestSessionBean() {
		log.debug("instantiating test session bean");
	}

	protected String getField() {
		return m_field;
	}

	protected void setField(String field) {
		m_field = field;
	}

}

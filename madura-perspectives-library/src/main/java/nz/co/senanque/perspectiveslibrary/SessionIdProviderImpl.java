/**
 * 
 */
package nz.co.senanque.perspectiveslibrary;

import nz.co.senanque.madura.bundle.spring.SessionIdProvider;

import org.springframework.stereotype.Component;

import com.vaadin.server.VaadinService;

/**
 * This implementation returns the Vaadin Session Id, as opposed to the usual Spring Session Id.
 * 
 * @author Roger Parkinson
 *
 */
@Component("sessionIdProvider")
public class SessionIdProviderImpl implements SessionIdProvider {

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.spring.SessionIdProvider#getSessionId()
	 */
	@Override
	public String getSessionId() {
		return VaadinService.getCurrentRequest().getWrappedSession().getId();
	}

}

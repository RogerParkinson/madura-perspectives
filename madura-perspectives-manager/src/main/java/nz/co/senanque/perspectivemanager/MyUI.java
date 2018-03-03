package nz.co.senanque.perspectivemanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import nz.co.senanque.login.PermissionResolverSpringSecurity;
import nz.co.senanque.madura.bundle.BundleExport;
import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.spring.BundledInterfaceRegistrar;
import nz.co.senanque.permissionmanager.PermissionManager;
import nz.co.senanque.permissionmanager.PermissionManagerImpl;
import nz.co.senanque.perspectiveslibrary.App;
import nz.co.senanque.perspectiveslibrary.Blackboard;
import nz.co.senanque.perspectiveslibrary.BundleListenerImpl;
import nz.co.senanque.perspectiveslibrary.MenuCloner;
import nz.co.senanque.perspectiveslibrary.SubApplication;
import nz.co.senanque.vaadin.Hints;
import nz.co.senanque.vaadin.HintsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.context.ContextLoaderListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Title("Madura Perspectives")
@Widgetset("com.vaadin.DefaultWidgetSet")
@SpringUI
public class MyUI extends UI implements MessageSourceAware {

	private static final long serialVersionUID = 1L;
	private static Logger m_logger = LoggerFactory.getLogger(MyUI.class);
	
	@Autowired private PermissionManager m_permissionManager;
	@Autowired private AboutWindow m_aboutWindow;
	@Autowired private BundleListenerImpl m_bundleListenerImpl;
	@Autowired private BundleManager m_bundleManager;
	@Autowired private Blackboard m_blackboard;

	private VerticalLayout mainLayout;
	private HorizontalLayout bodyLayout;
	private Panel panel_2;
	private VerticalLayout ApplicationlBodyLayout;
	private Panel panel_1;
	private VerticalLayout ApplicationIconContainer;
	private HorizontalLayout headingButtonslLayout;
	private Label loggedInAs;
	private MenuBar menuBar;
	private HorizontalLayout headingLayout;
	private Label titleLabel;
	private Embedded embedded_1;
	private List<MenuBar.MenuItem> m_added = new ArrayList<MenuBar.MenuItem>();
	private MessageSource m_messageSource;

    @WebServlet(name = "MyUIServlet", urlPatterns = {"/app/*", "/VAADIN/*"}, asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {

		private static final long serialVersionUID = 1L;
    }

    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
    	// This causes the applicationContext.xml context file to be loaded
    	// per session.
    }

    @Configuration
    @EnableVaadin
    @Import(BundledInterfaceRegistrar.class)
    @ComponentScan(basePackages = {
    		"nz.co.senanque.perspectiveslibrary",
    		"nz.co.senanque.madura.bundle",
    		"nz.co.senanque.vaadin"})
    @PropertySource("classpath:config.properties")
    public static class MyConfiguration {
    	
    	@Autowired MessageSource messageSource;
    	
    	public MyConfiguration() {
    		m_logger.info("MyConfiguration"); // this gets called at application startup, not session startup so this is an app bean.
    	}

    	// needed for @PropertySource
    	@Bean
    	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
    		return new PropertySourcesPlaceholderConfigurer();
    	}
    	@Bean(name="hints")
    	@Scope(value="vaadin-ui", proxyMode = ScopedProxyMode.TARGET_CLASS)
    	@BundleExport
    	public Hints getHints() {
    		return new HintsImpl();
    	}
    	@Bean(name="permissionManager")
    	@Scope(value="vaadin-ui", proxyMode = ScopedProxyMode.TARGET_CLASS)
    	@BundleExport
    	public PermissionManager getPermissionManager() {
    		PermissionManagerImpl ret =  new PermissionManagerImpl();
    		ret.setPermissionResolver(new PermissionResolverSpringSecurity());
    		return ret;
    	}
    	
    	@Bean(name="bundleListener")
    	public BundleListenerImpl getBundleListener() {
    		BundleListenerImpl ret = new BundleListenerImpl();
    		ret.setMessageSource(messageSource);
    		return ret;
    	}
    	
    }
    @Override
    protected void init(VaadinRequest vaadinRequest) { // called at session start
	
    	final MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(m_messageSource);
    	
    	buildMainLayout(messageSourceAccessor);
    	setContent(mainLayout);
    	
		final MenuBar.MenuItem file = createFileMenu(messageSourceAccessor);
		final MenuBar.MenuItem help = createHelpMenu(messageSourceAccessor);
		for (SubApplication subApplication : m_bundleListenerImpl
				.getSubApplicationValues()) {
			setupSubApplication(subApplication, file);
		}
		file.addItem(messageSourceAccessor.getMessage("logout","Logout"), new Command(){

			private static final long serialVersionUID = -1L;

			public void menuSelected(MenuItem selectedItem) {
				for (SubApplication subApplication : m_bundleListenerImpl
						.getSubApplicationValues()) {
					m_bundleManager.releaseBundle(subApplication.getBundleVersion());
				}
		    	VaadinService.getCurrentRequest().getWrappedSession().invalidate();
		    	getUI().close();
		        String contextPath = VaadinService.getCurrentRequest().getContextPath();
		        getUI().getPage().setLocation(contextPath);
			}
			});

    }
	private void setupSubApplication(final SubApplication subApplication, final MenuBar.MenuItem file)
	{
		m_bundleManager.reserveBundle(subApplication.getBundleVersion());
		file.addItem(subApplication.getCaption(), subApplication.getIcon(), new Command() {

			private static final long serialVersionUID = 1L;
			private App m_app=null;
			public void menuSelected(MenuItem selectedItem) {
				Iterator<Component> it = ApplicationlBodyLayout.iterator();
				while (it.hasNext())
				{
					Component component = it.next();
					ApplicationlBodyLayout.removeComponent(component);
					break;
				}
				m_bundleManager.setBundle(subApplication.getBundleVersion());
				if (m_app == null)
				{
					m_app = subApplication.createApp(m_blackboard);
				}
				ApplicationlBodyLayout.addComponent(m_app.getComponentContainer());
				titleLabel.setValue(subApplication.getDescription());
				MenuCloner.clean(menuBar,m_added);
				MenuCloner.merge(menuBar, m_app.getMenuBar(), m_added);
				menuBar.markAsDirty();
			}});
	}

    private MenuBar.MenuItem createHelpMenu(final MessageSourceAccessor messageSourceAccessor) {
    	MenuBar.MenuItem help = menuBar.addItem(messageSourceAccessor.getMessage("help","Help"), null);
		help.addItem(messageSourceAccessor.getMessage("demo.script","GitHub"), new Command() {

			private static final long serialVersionUID = 1L;
			public void menuSelected(MenuItem selectedItem) {
				Page.getCurrent().open(messageSourceAccessor.getMessage("demo.url"), null);
			}});

		help.addItem(messageSourceAccessor.getMessage("about","About"), new Command(){

			private static final long serialVersionUID = -1L;

			public void menuSelected(MenuItem selectedItem) {
				m_aboutWindow.load();
			}});
		return help;
    }
    private MenuBar.MenuItem createFileMenu(final MessageSourceAccessor messageSourceAccessor) {
    	MenuBar.MenuItem file = menuBar.addItem(messageSourceAccessor.getMessage("file","File"), null);
		return file;
    }
	private VerticalLayout buildMainLayout(MessageSourceAccessor messageSourceAccessor) {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("800px");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("800px");
		setHeight("-1px");
		
		// headingLayout
		headingLayout = buildHeadingLayout(messageSourceAccessor);
		mainLayout.addComponent(headingLayout);
		
		// headingButtonslLayout
		headingButtonslLayout = buildHeadingButtonslLayout(messageSourceAccessor);
		mainLayout.addComponent(headingButtonslLayout);
		
		// bodyLayout
		bodyLayout = buildBodyLayout(messageSourceAccessor);
		mainLayout.addComponent(bodyLayout);
		
		return mainLayout;
	}

	private HorizontalLayout buildHeadingLayout(MessageSourceAccessor messageSourceAccessor) {
		// common part: create layout
		headingLayout = new HorizontalLayout();
		headingLayout.setStyleName("heading");
		headingLayout.setImmediate(false);
		headingLayout.setWidth("100.0%");
		headingLayout.setHeight("40px");
		headingLayout.setMargin(false);
		
		// embedded_1
		embedded_1 = new Embedded();
		embedded_1.setImmediate(false);
		embedded_1.setWidth("-1px");
		embedded_1.setHeight("-1px");
		embedded_1.setSource(new com.vaadin.server.ThemeResource("images/logo.png"));
		embedded_1.setType(1);
		embedded_1.setMimeType("image/gif");
		headingLayout.addComponent(embedded_1);
		
		// titleLabel
		titleLabel = new Label();
		titleLabel.setStyleName("heading-words");
		titleLabel.addStyleName("v-textfield-align-right");
		titleLabel.setImmediate(false);
//		titleLabel.setWidth("470px");
		titleLabel.setHeight("-1px");
		titleLabel.setValue(messageSourceAccessor.getMessage("title"));
		headingLayout.addComponent(titleLabel);
		headingLayout.setComponentAlignment(titleLabel, Alignment.MIDDLE_RIGHT);
		
		return headingLayout;
	}

	private HorizontalLayout buildHeadingButtonslLayout(MessageSourceAccessor messageSourceAccessor) {
		// common part: create layout
		headingButtonslLayout = new HorizontalLayout();
		headingButtonslLayout.setStyleName("heading-buttons");
		headingButtonslLayout.setImmediate(false);
		headingButtonslLayout.setWidth("100.0%");
		headingButtonslLayout.setHeight("26px");
		headingButtonslLayout.setMargin(false);
		
		// menuBar_1
		menuBar = new MenuBar();
		menuBar.setImmediate(false);
		menuBar.setWidth("-1px");
		menuBar.setHeight("-1px");
		headingButtonslLayout.addComponent(menuBar);
		
		// loggedInAs
		loggedInAs = new Label();
		loggedInAs.setStyleName("heading-button");
		loggedInAs.addStyleName("v-textfield-align-right");
		loggedInAs.setImmediate(false);
		loggedInAs.setWidth("-1px");
		loggedInAs.setHeight("-1px");
		String loggedInAsString = messageSourceAccessor.getMessage("logged.in.as",new String[]{m_permissionManager.getCurrentUser()});
		loggedInAs.setValue(loggedInAsString);
		headingButtonslLayout.addComponent(loggedInAs);
		headingButtonslLayout.setComponentAlignment(loggedInAs, Alignment.MIDDLE_RIGHT);
		
		return headingButtonslLayout;
	}

	private HorizontalLayout buildBodyLayout(MessageSourceAccessor messageSourceAccessor) {
		// common part: create layout
		bodyLayout = new HorizontalLayout();
		bodyLayout.setImmediate(false);
		bodyLayout.setWidth("100.0%");
		bodyLayout.setHeight("-1px");
		bodyLayout.setMargin(false);
		
//		// panel_1
//		panel_1 = buildPanel_1(messageSourceAccessor);
//		bodyLayout.addComponent(panel_1);
		
		// panel_2
		panel_2 = buildPanel_2(messageSourceAccessor);
		bodyLayout.addComponent(panel_2);
		bodyLayout.setComponentAlignment(panel_2, Alignment.TOP_RIGHT);
		
		return bodyLayout;
	}

	private Panel buildPanel_1(MessageSourceAccessor messageSourceAccessor) {
		// common part: create layout
		panel_1 = new Panel();
		panel_1.setImmediate(false);
		panel_1.setWidth("150px");
		panel_1.setHeight("-1px");
		
		// ApplicationIconContainer
		ApplicationIconContainer = new VerticalLayout();
		ApplicationIconContainer.setImmediate(false);
		ApplicationIconContainer.setWidth("100.0%");
		ApplicationIconContainer.setHeight("100.0%");
		ApplicationIconContainer.setMargin(false);
		panel_1.setContent(ApplicationIconContainer);
		
		return panel_1;
	}

	private Panel buildPanel_2(MessageSourceAccessor messageSourceAccessor) {
		// common part: create layout
		panel_2 = new Panel();
		panel_2.setImmediate(false);
		panel_2.setWidth("650px");
		panel_2.setHeight("-1px");
		
		// ApplicationlBodyLayout
		ApplicationlBodyLayout = new VerticalLayout();
		ApplicationlBodyLayout.setImmediate(false);
		ApplicationlBodyLayout.setWidth("100.0%");
		ApplicationlBodyLayout.setHeight("100.0%");
		ApplicationlBodyLayout.setMargin(false);
		panel_2.setContent(ApplicationlBodyLayout);
		
		return panel_2;
	}
	@Override
	public void setMessageSource(MessageSource messageSource) {
		m_messageSource = messageSource;
	}

}

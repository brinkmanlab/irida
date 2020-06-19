package ca.corefacility.bioinformatics.irida.ria.integration.admin;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import ca.corefacility.bioinformatics.irida.ria.integration.AbstractIridaUIITChromeDriver;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.LoginPage;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.admin.AdminPage;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("it")
@DatabaseSetup("/ca/corefacility/bioinformatics/irida/ria/web/admin/AdminPageView.xml")
public class AdminPageIT extends AbstractIridaUIITChromeDriver {

	@Test
	public void accessPageAsAdmin() {
		LoginPage.loginAsAdmin(driver());
		AdminPage page = AdminPage.initPage(driver());
		assertTrue("Admin button should be displayed", page.adminPanelButtonVisible());
		page.clickAdminButton();
		assertTrue("Admin can navigate to admin panel, admin page title should be present", page.comparePageTitle("Statistics"));
		// Navigate to users page
		page.clickUsersSubMenu();
		page.clickUsersLink();
		assertTrue("Admin can navigate to users page, user page title should be present", page.comparePageTitle("Users"));
		// Navigate to user groups page
		page.clickUsersSubMenu();
		page.clickGroupsLink();
		assertTrue("Admin can navigate to groups page, groups page title should be present", page.comparePageTitle("Groups"));
		// Navigate back to statistics page
		page.clickStatsLink();
		assertTrue("Admin can navigate to stats page, stats page title should be present", page.comparePageTitle("Statistics"));
	}

	@Test
	public void accessPageFailure() {
		LoginPage.loginAsUser(driver());
		AdminPage page = AdminPage.initPage(driver());
		assertFalse("Admin button should not be displayed", page.adminPanelButtonVisible());
		// No admin button, so attempt to go to admin page by modifying the URL
		page.goToAdminPage(driver());
		assertFalse("User cannot navigate to admin panel, admin page title should not be present", page.comparePageTitle("Statistics"));
	}

	@Test
	public void testPageSetUp() {
		LoginPage.loginAsAdmin(driver());
		AdminPage page = AdminPage.initPage(driver());
		page.clickAdminButton();
		// Check that side menu is visible
		assertTrue("Admin side menu should be visible", page.adminSideMenuVisible());
		// Check that all top level links are visible
		assertTrue("Admin stats link should be visible", page.adminStatsLinkVisible());
		// Open sub menu to view other links
		page.clickUsersSubMenu();
		assertTrue("Admin users link should be visible", page.adminUsersLinkVisible());
		assertTrue("Admin groups link should be visible", page.adminGroupsLinkVisible());
		// Check if content has a page title (check if the class name exists only not the actual title)
		assertTrue("Admin page title should be visible", page.adminContentTitleVisible());
	}
}
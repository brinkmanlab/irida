package ca.corefacility.bioinformatics.irida.ria.integration.samples;

import ca.corefacility.bioinformatics.irida.ria.integration.AbstractIridaUIITChromeDriver;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.LoginPage;
import ca.corefacility.bioinformatics.irida.ria.integration.pages.samples.SampleFilesPage;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

/**
 * <p> Integration test to ensure that the Sample Details Page. </p>
 *
 */
@DatabaseSetup("/ca/corefacility/bioinformatics/irida/ria/web/samples/SamplePagesIT.xml")
public class SampleFilesPageIT extends AbstractIridaUIITChromeDriver {
	private final String SAMPLE_LABEL = "sample1";
	private final Long SAMPLE_ID = 1L;
	private SampleFilesPage page;

	private List<Map<String, String>> BREADCRUMBS = ImmutableList.of(
			ImmutableMap.of(
					"href", "/samples",
					"text", "Samples"
			),
			ImmutableMap.of(
					"href", "/samples/" + SAMPLE_ID,
					"text", String.valueOf(SAMPLE_ID)
			),
			ImmutableMap.of(
					"href", "/samples/" + SAMPLE_ID + "/sequenceFiles",
					"text", "Sequence Files"
			)
	);

	@Before
	public void setUpTest() {
		LoginPage.loginAsManager(driver());
		page = new SampleFilesPage(driver());
	}

	@Test
	public void testPageSetup() {
		page.gotoPage(SAMPLE_ID);
		assertTrue("Page Title contains the sample label", page.getPageTitle().contains(SAMPLE_LABEL));
		assertEquals("Displays the correct number of sequence files", 3, page.getSequenceFileCount());
		page.checkBreadCrumbs(BREADCRUMBS);
	}
	
	@Test
	public void testDeleteFile() {
		page.gotoPage(SAMPLE_ID);
		
		page.deleteFirstFile();
		assertTrue("Should display a confirmation message that the file was deleted", page.isDeleteConfirmationMessageDisplayed());
		assertEquals("Displays the correct number of sequence files", 2, page.getSequenceFileCount());
	}

	@Test
	public void testDeletePair(){
		page.gotoPage(SAMPLE_ID);
		
		page.deleteFirstPair();
		assertTrue("Should display a confirmation message that the file was deleted", page.isDeleteConfirmationMessageDisplayed());
		assertEquals("Displays the correct number of sequence files", 1, page.getSequenceFileCount());
	}
}

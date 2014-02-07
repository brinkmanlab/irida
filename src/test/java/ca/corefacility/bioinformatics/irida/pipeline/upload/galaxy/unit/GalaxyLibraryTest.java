package ca.corefacility.bioinformatics.irida.pipeline.upload.galaxy.unit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.LibrariesClient;
import com.github.jmchilton.blend4j.galaxy.beans.Library;
import com.github.jmchilton.blend4j.galaxy.beans.LibraryContent;
import com.github.jmchilton.blend4j.galaxy.beans.LibraryFolder;
import com.github.jmchilton.blend4j.galaxy.beans.LibraryPermissions;
import com.github.jmchilton.blend4j.galaxy.beans.Role;
import com.sun.jersey.api.client.ClientResponse;

import ca.corefacility.bioinformatics.irida.exceptions.galaxy.ChangeLibraryPermissionsException;
import ca.corefacility.bioinformatics.irida.exceptions.galaxy.CreateLibraryException;
import ca.corefacility.bioinformatics.irida.exceptions.galaxy.GalaxyUserNoRoleException;
import ca.corefacility.bioinformatics.irida.model.upload.galaxy.GalaxyAccountEmail;
import ca.corefacility.bioinformatics.irida.model.upload.galaxy.GalaxyObjectName;
import ca.corefacility.bioinformatics.irida.pipeline.upload.galaxy.GalaxyLibraryBuilder;
import ca.corefacility.bioinformatics.irida.pipeline.upload.galaxy.GalaxySearch;

/**
 * Unit tests for GalaxyLibrary.
 * @author Aaron Petkau <aaron.petkau@phac-aspc.gc.ca>
 *
 */
public class GalaxyLibraryTest {
	@Mock
	private GalaxyInstance galaxyInstance;
	@Mock
	private GalaxySearch galaxySearch;
	@Mock
	private LibrariesClient librariesClient;
	@Mock
	private ClientResponse okayResponse;
	@Mock
	private ClientResponse invalidResponse;

	private final static String LIBRARY_ID = "1";
	private final static GalaxyAccountEmail USER_EMAIL = new GalaxyAccountEmail(
			"user@localhost");
	private final static GalaxyAccountEmail ADMIN_EMAIL = new GalaxyAccountEmail(
			"admin@localhost");
	private final static GalaxyAccountEmail INVALID_EMAIL = new GalaxyAccountEmail(
			"invalid@localhost");
	private final static String ROOT_FOLDER_ID = "10";

	private Library testLibrary;
	private GalaxyLibraryBuilder galaxyLibrary;

	/**
	 * Setup objects for library test.
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 * @throws GalaxyUserNoRoleException
	 */
	@Before
	public void setup() throws FileNotFoundException, URISyntaxException,
			GalaxyUserNoRoleException {
		MockitoAnnotations.initMocks(this);

		when(okayResponse.getClientResponseStatus()).thenReturn(
				ClientResponse.Status.OK);
		when(invalidResponse.getClientResponseStatus()).thenReturn(
				ClientResponse.Status.FORBIDDEN);

		setupLibrariesTest();
		setupPermissionsTest();
		setupFoldersTest();

		galaxyLibrary = new GalaxyLibraryBuilder(galaxyInstance, galaxySearch);
	}

	/**
	 * Setup libraries for test.
	 */
	private void setupLibrariesTest() {
		testLibrary = new Library();
		testLibrary.setName("test");
		testLibrary.setId(LIBRARY_ID);

		when(galaxyInstance.getLibrariesClient()).thenReturn(librariesClient);
	}

	/**
	 * Setup folders in library for test.
	 */
	private void setupFoldersTest() {
		LibraryContent rootFolder = new LibraryContent();
		rootFolder.setName("/");
		rootFolder.setId(ROOT_FOLDER_ID);

		when(librariesClient.getRootFolder(LIBRARY_ID)).thenReturn(rootFolder);
	}

	/**
	 * Setup permissions for users.
	 * @throws GalaxyUserNoRoleException
	 */
	private void setupPermissionsTest() throws GalaxyUserNoRoleException {
		Role userRole = new Role();
		userRole.setName(USER_EMAIL.getName());

		Role adminRole = new Role();
		adminRole.setName(ADMIN_EMAIL.getName());

		when(galaxySearch.findUserRoleWithEmail(USER_EMAIL)).thenReturn(
				userRole);
		when(galaxySearch.findUserRoleWithEmail(ADMIN_EMAIL)).thenReturn(
				adminRole);
		when(galaxySearch.findUserRoleWithEmail(INVALID_EMAIL))
				.thenReturn(null);
	}

	/**
	 * Tests creat library folder within a root dir.
	 * @throws CreateLibraryException
	 */
	@Test
	public void testCreateLibraryFolderRoot() throws CreateLibraryException {
		LibraryFolder folder = new LibraryFolder();
		folder.setName("folder_name");
		folder.setId("1");

		when(
				librariesClient.createFolder(eq(LIBRARY_ID),
						any(LibraryFolder.class))).thenReturn(folder);

		LibraryFolder newFolder = galaxyLibrary.createLibraryFolder(
				testLibrary, new GalaxyObjectName("new_folder"));
		assertNotNull(newFolder);
		assertEquals("folder_name", newFolder.getName());
		assertEquals("1", newFolder.getId());
	}

	/**
	 * Tests creat library folder with no root dir.
	 * @throws CreateLibraryException
	 */
	@Test(expected = CreateLibraryException.class)
	public void testCreateLibraryFolderNoRoot() throws CreateLibraryException {
		when(librariesClient.getRootFolder(LIBRARY_ID)).thenReturn(null);

		galaxyLibrary.createLibraryFolder(testLibrary, new GalaxyObjectName(
				"new_folder"));
	}

	/**
	 * Tests create a library folder.
	 * @throws CreateLibraryException
	 */
	@Test
	public void testCreateLibraryFolder() throws CreateLibraryException {
		LibraryFolder folder = new LibraryFolder();
		folder.setName("folder_name");
		folder.setId("1");

		when(
				librariesClient.createFolder(eq(LIBRARY_ID),
						any(LibraryFolder.class))).thenReturn(folder);

		LibraryFolder newFolder = galaxyLibrary.createLibraryFolder(
				testLibrary, folder, new GalaxyObjectName("new_folder"));
		assertNotNull(newFolder);
		assertEquals("folder_name", newFolder.getName());
		assertEquals("1", newFolder.getId());
	}

	/**
	 * Tests change the library owner.
	 * @throws ChangeLibraryPermissionsException
	 * @throws GalaxyUserNoRoleException
	 */
	@Test
	public void testChangeLibraryOwner()
			throws ChangeLibraryPermissionsException, GalaxyUserNoRoleException {
		when(
				librariesClient.setLibraryPermissions(eq(LIBRARY_ID),
						any(LibraryPermissions.class)))
				.thenReturn(okayResponse);

		Library library = galaxyLibrary.changeLibraryOwner(testLibrary,
				USER_EMAIL, ADMIN_EMAIL);
		assertNotNull(library);
		assertEquals(testLibrary.getName(), library.getName());
		assertEquals(testLibrary.getId(), library.getId());
		verify(librariesClient).setLibraryPermissions(eq(LIBRARY_ID),
				any(LibraryPermissions.class));
	}

	/**
	 * Test change library owner to invalid user.
	 * @throws ChangeLibraryPermissionsException
	 * @throws GalaxyUserNoRoleException
	 */
	@Test(expected = GalaxyUserNoRoleException.class)
	public void testChangeLibraryOwnerInvalidUser()
			throws ChangeLibraryPermissionsException, GalaxyUserNoRoleException {
		when(
				librariesClient.setLibraryPermissions(eq(LIBRARY_ID),
						any(LibraryPermissions.class)))
				.thenReturn(okayResponse);

		galaxyLibrary.changeLibraryOwner(testLibrary, INVALID_EMAIL,
				ADMIN_EMAIL);
	}

	/**
	 * Tests change library owner with invalid admin user.
	 * @throws ChangeLibraryPermissionsException
	 * @throws GalaxyUserNoRoleException
	 */
	@Test(expected = GalaxyUserNoRoleException.class)
	public void testChangeLibraryOwnerInvalidAdmin()
			throws ChangeLibraryPermissionsException, GalaxyUserNoRoleException {
		when(
				librariesClient.setLibraryPermissions(eq(LIBRARY_ID),
						any(LibraryPermissions.class)))
				.thenReturn(okayResponse);

		galaxyLibrary
				.changeLibraryOwner(testLibrary, USER_EMAIL, INVALID_EMAIL);
	}

	/**
	 * Tests error changing library owner.
	 * @throws ChangeLibraryPermissionsException
	 * @throws GalaxyUserNoRoleException
	 */
	@Test(expected = ChangeLibraryPermissionsException.class)
	public void testChangeLibraryOwnerInvalidResponse()
			throws ChangeLibraryPermissionsException, GalaxyUserNoRoleException {
		when(
				librariesClient.setLibraryPermissions(eq(LIBRARY_ID),
						any(LibraryPermissions.class))).thenReturn(
				invalidResponse);

		galaxyLibrary.changeLibraryOwner(testLibrary, USER_EMAIL, ADMIN_EMAIL);
	}

	/**
	 * Tests create empty library.
	 * @throws CreateLibraryException
	 */
	@Test
	public void testBuildEmptyLibrary() throws CreateLibraryException {
		when(librariesClient.createLibrary(any(Library.class))).thenReturn(
				testLibrary);

		Library library = galaxyLibrary.buildEmptyLibrary(new GalaxyObjectName(
				"test"));

		assertNotNull(library);
		assertEquals("test", library.getName());
		assertEquals(LIBRARY_ID, library.getId());
	}

	/**
	 * Tests fail to create empty library.
	 * @throws CreateLibraryException
	 */
	@Test(expected = CreateLibraryException.class)
	public void testFailBuildEmptyLibrary() throws CreateLibraryException {
		when(librariesClient.createLibrary(any(Library.class)))
				.thenReturn(null);

		galaxyLibrary.buildEmptyLibrary(new GalaxyObjectName("test"));
	}

	/**
	 * Tests other error creating library.
	 * @throws CreateLibraryException
	 */
	@Test(expected = RuntimeException.class)
	public void testFailBuildEmptyLibraryException()
			throws CreateLibraryException {
		when(librariesClient.createLibrary(any(Library.class))).thenThrow(
				new RuntimeException("error creating library"));

		galaxyLibrary.buildEmptyLibrary(new GalaxyObjectName("test"));
	}
}

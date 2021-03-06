package ca.corefacility.bioinformatics.irida.model.workflow.execution.galaxy;

/**
 * The different types for a dataset collection.
 * 
 * @see "https://wiki.galaxyproject.org/Documents/Presentations/GCC2014?action=AttachFile&do=view&target=Chilton.pdf"
 * 
 *
 */
public enum DatasetCollectionType {

	/**
	 * Describes a list of files within a collection.
	 */
	LIST("list"),

	/**
	 * Describes a set of paired files (sequence read paired-end files).
	 */
	PAIRED("paired"),

	/**
	 * Describes a list of paired end files.
	 */
	LIST_PAIRED("list:paired");

	private String type;

	private DatasetCollectionType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}

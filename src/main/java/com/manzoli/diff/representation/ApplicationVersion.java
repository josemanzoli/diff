package com.manzoli.diff.representation;

/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Immutable class that holds all the version information of the application. 
 */
public class ApplicationVersion {

	/** Implementation Title defined by Maven in the Manifest file  that can be found at pom.xml file */
	private final String implementationTitle = getClass().getPackage().getImplementationTitle();
	
	/** Implementation Version defined by Maven in the Manifest file  that can be found at pom.xml file */
	private final String implementationVersion = getClass().getPackage().getImplementationVersion();
	
	/** Implementation Vendor defined by Maven in the Manifest file that can be found at pom.xml file */
	private final String implementationVendor = getClass().getPackage().getImplementationVendor();
	
	public String getImplementationTitle() {
		return implementationTitle;
	}
	public String getImplementationVersion() {
		return implementationVersion;
	}
	public String getImplementationVendor() {
		return implementationVendor;
	}
	
	@Override
	public String toString() {
		return "ApplicationVersion [implementationTitle=" + implementationTitle + ", implementationVersion="
				+ implementationVersion + ", implementationVendor=" + implementationVendor + "]";
	}
}

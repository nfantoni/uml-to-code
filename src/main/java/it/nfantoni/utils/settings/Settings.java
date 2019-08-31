package it.nfantoni.utils.settings;

import org.w3c.dom.Element;

public class Settings {

	private String defaultPackage;
	private OUTPUT_TYPE outputType;
	private String outputPath;
	private Boolean useSurrogatesId;

	public enum OUTPUT_TYPE{
		DAO,
		HYBERNATE,
		BRUTE_FORCE
	}

	public String getDefaultPackage() {
		return defaultPackage;
	}

	public void setDefaultPackage(String defaultPackage) {
		this.defaultPackage = defaultPackage;
	}

	public OUTPUT_TYPE getOutputType() {
		return outputType;
	}

	public void setOutputType(OUTPUT_TYPE outputType) {
		this.outputType = outputType;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public Boolean getUseSurrogatesId() {
		return useSurrogatesId;
	}

	public void setUseSurrogatesId(Boolean useSurrogatesId) {
		this.useSurrogatesId = useSurrogatesId;
	}
	
	public Settings(Element element) throws Exception {
		this.defaultPackage = element.getElementsByTagName("defaultPackage").item(0).getTextContent();
		this.outputPath = element.getElementsByTagName("outputPath").item(0).getTextContent();
		this.useSurrogatesId = Boolean.parseBoolean(element.getElementsByTagName("useSurrogatesId").item(0).getTextContent());
		switch (element.getElementsByTagName("outputType").item(0).getTextContent()) {
			case "DAO":
				this.outputType= OUTPUT_TYPE.DAO;
				break; 
			case "HYBERNATE":
				this.outputType= OUTPUT_TYPE.HYBERNATE;
				break;
			case "BRUTE_FORCE":
				this.outputType= OUTPUT_TYPE.BRUTE_FORCE;
				break; 
			default:
				throw new Exception("Output type not recognize");

		}
		
			
	}
}
package com.pitika.atolye.pomoto;

public class Dependency {

	private String groupId;
	private String artifactId;
	private String version;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String toTAG() {
		StringBuilder sb = new StringBuilder();

		return sb.append("<dependency>").append("\n")
				.append("\t").append("<groupId>").append(this.groupId).append("</groupId>").append("\n")
				.append("\t").append("<artifactId>").append(this.artifactId).append("</artifactId>").append("\n")
				.append("\t").append("<version>").append(this.version).append("</version>").append("\n")
				.append("</dependency>")
				.toString();
	}

}

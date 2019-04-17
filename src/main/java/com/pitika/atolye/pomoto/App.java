package com.pitika.atolye.pomoto;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class App {

	// https://search.maven.org/solrsearch/select?q=1:"checksum"
	private final static String URL = "https://search.maven.org/solrsearch/select";

	private JSONObject findMavenArtifact(String checksum) {

		Client client = Client.create();
		WebResource webResource = client.resource(URL).queryParam("q", "1:\"" + checksum + "\"");
		ClientResponse response = webResource.get(ClientResponse.class);

		String entity = response.getEntity(String.class);

		return new JSONObject(entity).getJSONObject("response").getJSONArray("docs").getJSONObject(0);

	}

	private Dependency getMavenDependency(File file) {
		Dependency dependency = new Dependency();
		String checksum = this.toHex(this.checksum(file));
		JSONObject response = findMavenArtifact(checksum);

		dependency.setGroupId(response.getString("g"));
		dependency.setArtifactId(response.getString("a"));
		dependency.setVersion(response.getString("v"));

		return dependency;
	}

	private String toHex(byte[] bytes) {
		return DatatypeConverter.printHexBinary(bytes);
	}

	private byte[] checksum(File input) {
		try (InputStream in = new FileInputStream(input)) {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			byte[] block = new byte[4096];
			int length;
			while ((length = in.read(block)) > 0) {
				digest.update(block, 0, length);
			}
			return digest.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				} else {
					return pathname.getName().toLowerCase().endsWith(".jar");
				}

			}
		})) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				Dependency dependency = getMavenDependency(fileEntry);
				System.out.println(dependency.toTAG());
			}
		}
	}

	public static void main(String[] args) {
		String folderPath = "C:\\Users\\TCSDURMAZ\\.m2\\repository";
		App app = new App();

		app.listFilesForFolder(new File(folderPath));

	}
}

package com.pitika.atolye.pomoto;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class App {

	//https://search.maven.org/solrsearch/select?q=1:"checksum"
	private final static String URL = "https://search.maven.org/solrsearch/select";

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
				System.out.println(fileEntry.getName() + " - " + this.toHex(this.checksum(fileEntry)));
			}
		}
	}

	public static void main(String[] args) {
		String folderPath = "/Users/semudu/.m2/repository/xpp3/xpp3_min/1.1.4c";
		App app = new App();

		app.listFilesForFolder(new File(folderPath));
		
		Dependency dep = new Dependency();
		dep.setGroupId("com.pitika");
		dep.setArtifactId("pomoto");
		dep.setVersion("1.0");
		
		System.out.println(dep.toTAG());
		

	}
}

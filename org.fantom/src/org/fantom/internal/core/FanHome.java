package org.fantom.internal.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.fantom.FantomVM;
import org.osgi.framework.Bundle;

public class FanHome {

	private final File location;

	public FanHome(File location) {
		this.location = location;
	}

	@SuppressWarnings("unchecked")
	public void init(Bundle bundle, String path) {
		makeFiles(bundle.findEntries(path, null, true));
	}

	private void makeFiles(Enumeration<URL> e) {
		while (e.hasMoreElements()) {
			URL url = e.nextElement();
			if (Platform.inDevelopmentMode() && url.getPath().contains("/CVS/"))
				continue;
			try {
				makeFile(url);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				IStatus s = new Status(IStatus.ERROR, FantomVM.PLUGIN_ID, ioe
						.getMessage());
				FantomVM.getDefault().getLog().log(s);
			}
		}
	}

	private void makeFile(URL url) throws IOException {
		if (FantomVM.DEBUG) {
			System.out.println("makefile: " + url);
		}
		String fileName = url.getFile();
		if (fileName.endsWith("/")) {
			new File(location, fileName).mkdirs();
		} else {
			FileOutputStream os = new FileOutputStream(new File(location,
					fileName));
			copyStream(url.openStream(), os);
		}
	}

	private void copyStream(InputStream is, OutputStream os) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		try {
			byte[] buf = new byte[4096];
			int n;
			while ((n = bis.read(buf)) >= 0)
				bos.write(buf, 0, n);
		} finally {
			bis.close();
			bos.close();
		}
	}

	public String getAbsolutePath() {
		return location.getAbsolutePath();
	}

}

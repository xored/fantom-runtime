package org.fantom.internal.sys;

import fan.sys.DateTime;
import fan.sys.File;
import fan.sys.IOErr;
import fan.sys.LocalFile;
import fan.sys.OutStream;
import fan.sys.Err.Val;

public class BundleFile extends LocalFile {

	public BundleFile(java.io.File javaFile) {
		super(javaFile);
	}

	private static Val readOnlyError() {
		throw IOErr.make("BundleFile is readonly").val;
	}

	@Override
	public File create() {
		throw readOnlyError();
	}

	@Override
	public void delete() {
		throw readOnlyError();
	}

	@Override
	public File deleteOnExit() {
		throw readOnlyError();
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public void modified(DateTime time) {
		throw readOnlyError();
	}

	@Override
	public File moveTo(File to) {
		throw readOnlyError();
	}

	@Override
	public OutStream out(boolean append, Long bufSize) {
		throw readOnlyError();
	}

}

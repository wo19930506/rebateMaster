package com.edm.modules.utils.encoder;

public interface Password {

	public String encode(String rawPwd);

	public String encode(String rawPwd, String salt);

	public boolean isValid(String encPwd, String rawPwd);

	public boolean isValid(String encPwd, String rawPwd, String salt);
}

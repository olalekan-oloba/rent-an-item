package com.omegalambdang.rentanitem.feature.notification.impl.email;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Nullable
public class Email implements Serializable {
	
	
	private static final long serialVersionUID = 6481794982612826257L;
	private @Nullable String from;
	private @Nullable String fromEmail;
	private @Nullable String to;
	private @Nullable String subject;
	private @Nullable String templateName;
	private Map<String,String> templateTokens = new HashMap<String,String>();
	private @Nullable String body;
	private @Nullable FileAttachement attachement;

	@Data
	@AllArgsConstructor
	public static class FileAttachement {
		private String fileName;
		private InputStream inputStream;
	}
}

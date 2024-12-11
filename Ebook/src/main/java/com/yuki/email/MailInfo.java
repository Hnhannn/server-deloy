package com.yuki.email;

import lombok.Data;

@Data
public class MailInfo {
	String from;
	String to;
	String[] cc;
	String[] bcc;
	String subject;
	String body;
	String[] attachments;

	public MailInfo(String to, String subject, String body) {
		this.from = "Yuki Ebook <poly@fpt.edu.vn>";
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
}

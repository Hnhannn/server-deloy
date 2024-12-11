package com.yuki.utils;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {

	private final HttpSession session;

	public SessionService(HttpSession session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) session.getAttribute(name);
	}

	public void set(String name, Object value) {
		session.setAttribute(name, value);
	}

	// Modified by Vinh
	public void remove(String user) {
		session.removeAttribute(user);
	}
	// Modified by Vinh
}
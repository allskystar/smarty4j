package com.ruixus.smarty4j.util;

public class SimpleCharBuffer {
	private char[] buf;
	private int off;
	
	public SimpleCharBuffer(int initSize) {
		buf = new char[initSize];
	}
	
	public void append(char c) {
		ensureCapacityInternal(off + 1);
		buf[off++] = c;
	}
	
	public void append(String str) {
		int len = str.length();
		ensureCapacityInternal(off + len);
		str.getChars(0, len, buf, off);
		off += len;
	}
	
	public void append(char[] str, int offset, int len) {
		ensureCapacityInternal(off + len);
		System.arraycopy(str, offset, buf, off, len);
		off += len;
	}

	public void appendString(String str) {
		int len = str.length();
		ensureCapacityInternal(off + len * 2);
		buf[off++] = '"';
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			switch (c) {
			case '"':
			case '\\':
			case '/':
				buf[off++] = '\\';
				buf[off++] = c;
				break;
			case '\b':
				buf[off++] = '\\';
				buf[off++] = 'b';
				break;
			case '\f':
				buf[off++] = '\\';
				buf[off++] = 'f';
				break;
			case '\n':
				buf[off++] = '\\';
				buf[off++] = 'n';
				break;
			case '\r':
				buf[off++] = '\\';
				buf[off++] = 'r';
				break;
			case '\t':
				buf[off++] = '\\';
				buf[off++] = 't';
				break;
			default:
				buf[off++] = c;
			}
		}
		buf[off++] = '"';
	}

	public int length() {
		return off;
	}

	public void setCharAt(int off, char c) {
		buf[off] = c;
	}

    public void setLength(int off) {
		this.off = off;
	}
	
	public String toString() {
		return new String(buf, 0, off);
	}

	private void ensureCapacityInternal(int size) {
		int len = buf.length;
		if (size > len) {
			char[] newBuf = new char[Math.max(size * 2 - len, len * 2)];
			System.arraycopy(buf, 0, newBuf, 0, off);
			buf = newBuf;
		}
	}
}

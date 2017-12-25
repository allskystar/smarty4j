package com.ruixus.smarty4j.util;

import java.lang.Integer;
import java.lang.Math;
import java.lang.String;
import java.lang.System;

public class SimpleCharBuffer {
	public static final String NAME = SimpleCharBuffer.class.getName().replace('.', '/');

	private char[] buf;
	private int off;

	public SimpleCharBuffer(int initSize) {
		buf = new char[initSize];
	}

	private static final int[] intTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999,
			Integer.MAX_VALUE };

	private static int stringSize(int x) {
		for (int i = 0;; i++)
			if (x <= intTable[i])
				return i + 1;
	}

	private static final long[] longTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999,
			9999999999L, 99999999999L, 999999999999L, 9999999999999L, 99999999999999L, 999999999999999L,
			9999999999999999L, 99999999999999999L, 999999999999999999L, Long.MAX_VALUE };

	private static int stringSize(long x) {
		for (int i = 0;; i++)
			if (x <= longTable[i])
				return i + 1;
	}

	private static final char[] digitOnes = new char[100];
	private static final char[] digitTens = new char[100];
	private static final int[] escCodes = new int[128];
	
	private static final char[] hexChars = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	static {
		for (int i = 0; i < 100; i++) {
			digitOnes[i] = (char) ('0' + i % 10);
			digitTens[i] = (char) ('0' + i / 10);
		}
		for (int i = 0; i < 128; i++) {
			switch (i) {
			case '\\':
				escCodes[i] = '\\';
				break;
			case '"':
				escCodes[i] = '"';
				break;
			case '\b':
				escCodes[i] = 'b';
				break;
			case '\f':
				escCodes[i] = 'f';
				break;
			case '\n':
				escCodes[i] = 'n';
				break;
			case '\r':
				escCodes[i] = 'r';
				break;
			case '\t':
				escCodes[i] = 't';
				break;
			default:
				if (i < 32 || i == 127) {
					escCodes[i] = 'u';
				}
			}
		}
	}

	private static void getChars(int i, int index, char[] buf) {
		char sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		while (i >= 100) {
			int r = i % 100;
			i = i / 100;
			buf[--index] = digitOnes[r];
			buf[--index] = digitTens[r];
		}

		buf[--index] = digitOnes[i];
		if (i >= 10) {
			buf[--index] = digitTens[i];
		}
		if (sign != 0) {
			buf[--index] = sign;
		}
	}

	private static void getChars(long l, int index, char[] buf) {
		char sign = 0;

		if (l < 0) {
			sign = '-';
			l = -l;
		}

		while (l > Integer.MAX_VALUE) {
			int r = (int) (l % 100);
			l = l / 100;
			buf[--index] = digitOnes[r];
			buf[--index] = digitTens[r];
		}

		int i = (int) l;
		while (i >= 100) {
			int r = i % 100;
			i = i / 100;
			buf[--index] = digitOnes[r];
			buf[--index] = digitTens[r];
		}

		buf[--index] = digitOnes[i];
		if (i >= 10) {
			buf[--index] = digitTens[i];
		}
		if (sign != 0) {
			buf[--index] = sign;
		}
	}

	public void append(int i) {
		int capacity = off + ((i < 0) ? stringSize(-i) + 1 : stringSize(i));
		ensureCapacityInternal(capacity);
		off = capacity;
		getChars(i, off, buf);
	}

	public void append(long l) {
		int capacity = off + ((l < 0) ? stringSize(-l) + 1 : stringSize(l));
		ensureCapacityInternal(capacity);
		off = capacity;
		getChars(l, off, buf);
	}

	public void append(boolean b) {
		if (b) {
			ensureCapacityInternal(off + 4);
			buf[off++] = 't';
			buf[off++] = 'r';
			buf[off++] = 'u';
			buf[off++] = 'e';
		} else {
			ensureCapacityInternal(off + 5);
			buf[off++] = 'f';
			buf[off++] = 'a';
			buf[off++] = 'l';
			buf[off++] = 's';
			buf[off++] = 'e';
		}
	}

	public void append(char c) {
		ensureCapacityInternal(off + 1);
		buf[off++] = c;
	}

	public void append(float f) {
		append(Float.toString(f));
	}

	public void append(double d) {
		append(Double.toString(d));
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

	public void appendNull() {
		ensureCapacityInternal(off + 4);
		buf[off++] = 'n';
		buf[off++] = 'u';
		buf[off++] = 'l';
		buf[off++] = 'l';
	}
	
	public void appendString(char c) {
		ensureCapacityInternal(off + 8);
		buf[off++] = '"';
		buf[off] = c;
		if (c < 128) {
			int code = escCodes[c];
			if (code != 0) {
				buf[off++] = '\\';
				buf[off] = (char) code;
				if (code == 'u') {
					buf[++off] = '0';
					buf[++off] = '0';
					buf[++off] = hexChars[(c >> 4)];
					buf[++off] = hexChars[c & 0xF];
				}
			}
		}
		off++;
		buf[off++] = '"';
	}

	public void appendString(String str) {
		final int len = str.length();
		int index = off;
		int i = index + len * 5;
		final int end = i + len;
		ensureCapacityInternal(index + len * 6 + 2);
		char[] pbuf = buf;
		pbuf[index++] = '"';
		str.getChars(0, len, pbuf, i);
		for (; i < end; index++) {
			char c = pbuf[i++];
			pbuf[index] = c;
			if (c < 128) {
				int code = escCodes[c];
				if (code != 0) {
					pbuf[index++] = '\\';
					pbuf[index] = (char) code;
					if (code == 'u') {
						pbuf[++index] = '0';
						pbuf[++index] = '0';
						pbuf[++index] = hexChars[(c >> 4)];
						pbuf[++index] = hexChars[c & 0xF];
					}
				}
			}
		}
		pbuf[index++] = '"';
		off = index;
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

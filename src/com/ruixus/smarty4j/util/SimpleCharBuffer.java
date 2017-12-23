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

	private static final char[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', };

	private static final char[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1',
			'1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3',
			'3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5',
			'5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7',
			'7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9',
			'9', };

	private static final int[] EscapeChar = new int[128];

	static {
		for (int i = 0; i < 128; i++) {
			switch (i) {
			case '\\':
				EscapeChar[i] = '\\';
				break;
			case '"':
				EscapeChar[i] = '"';
				break;
			case '\b':
				EscapeChar[i] = 'b';
				break;
			case '\f':
				EscapeChar[i] = 'f';
				break;
			case '\n':
				EscapeChar[i] = 'n';
				break;
			case '\r':
				EscapeChar[i] = 'r';
				break;
			case '\t':
				EscapeChar[i] = 't';
				break;
			default:
				if (i < 32) {
					EscapeChar[i] = -1;
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
			buf[--index] = DigitOnes[r];
			buf[--index] = DigitTens[r];
		}

		buf[--index] = DigitOnes[i];
		if (i >= 10) {
			buf[--index] = DigitTens[i];
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
			buf[--index] = DigitOnes[r];
			buf[--index] = DigitTens[r];
		}

		int i = (int) l;
		while (i >= 100) {
			int r = i % 100;
			i = i / 100;
			buf[--index] = DigitOnes[r];
			buf[--index] = DigitTens[r];
		}

		buf[--index] = DigitOnes[i];
		if (i >= 10) {
			buf[--index] = DigitTens[i];
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
		int index = off;
		if (b) {
			ensureCapacityInternal(index + 4);
			buf[index++] = 't';
			buf[index++] = 'r';
			buf[index++] = 'u';
			buf[index++] = 'e';
		} else {
			ensureCapacityInternal(off + 5);
			buf[index++] = 'f';
			buf[index++] = 'a';
			buf[index++] = 'l';
			buf[index++] = 's';
			buf[index++] = 'e';
		}
		off = index;
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

	public void appendString(char c) {
		ensureCapacityInternal(off + 8);
		buf[off++] = '"';
		if (c < 128) {
			int code = EscapeChar[c];
			if (code > 0) {
				buf[off++] = '\\';
				c = (char) code;
			}
		}
		buf[off++] = c;
		buf[off++] = '"';
	}

	public void appendString(String str) {
		int len = str.length();
		int index = off;
		int i = index + len * 5;
		final int end = i + len;
		ensureCapacityInternal(index + len * 6 + 2);
		buf[index++] = '"';
		str.getChars(0, len, buf, i);
		for (; i < end; i++) {
			char c = buf[i];
			if (c < 128) {
				int code = EscapeChar[c];
				if (code > 0) {
					buf[index++] = '\\';
					c = (char) code;
				}
			}
			buf[index++] = c;
		}
		buf[index++] = '"';
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

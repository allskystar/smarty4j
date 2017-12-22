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
		off += (i < 0) ? stringSize(-i) + 1 : stringSize(i);
		ensureCapacityInternal(off);
		getChars(i, off, buf);
	}

	public void append(long l) {
		off += (l < 0) ? stringSize(-l) + 1 : stringSize(l);
		ensureCapacityInternal(off);
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

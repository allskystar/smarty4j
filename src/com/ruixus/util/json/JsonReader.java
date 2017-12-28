package com.ruixus.util.json;

import java.io.IOException;
import java.io.Reader;

import com.ruixus.util.SimpleCharBuffer;

public class JsonReader extends Reader {

	/** 文本输入流数据来源 */
	private Reader in;

	/** 文本缓冲区 */
	private char cb[] = new char[8192];

	/** 文本缓冲区全部的数据量 */
	private int nChars;

	/** 文本缓冲区有效的数据起始位置 */
	private int nextChar;

	/**
	 * 建立缓冲文本输入对象。
	 * 
	 * @param in
	 *          文本输入对象
	 */
	public JsonReader(Reader in) {
		this.in = in;
	}

	public void unread() {
		nextChar--;
	}

	public String readString() throws IOException {
		SimpleCharBuffer cb = new SimpleCharBuffer(128);
		if (read() != '"') {
			//TODO 异常
			throw new NullPointerException();
		}
		while (true) {
			int ch = read();
			if (ch == -1) {
				//TODO 异常
				throw new NullPointerException();
			}
			if (ch == '"') {
				return cb.toString();
			}
			if (ch == '\\') {
				//TODO 转义，回头再处理
				throw new NullPointerException();
			}
		}
	}

	@Override
	public int read() throws IOException {
		while (true) {
			if (nextChar >= nChars) {
				fill();
				if (nextChar >= nChars) {
					return -1;
				}
			}
			int ch = cb[nextChar++];
			if (!Character.isWhitespace(ch)) {
				return ch;
			}
		}
	}

	@Override
	public boolean ready() throws IOException {
		if (nextChar >= nChars) {
			fill();
		}
		return nextChar < nChars;
	}

	@Override
	public long skip(long n) throws IOException {
		long r = n;
		while (true) {
			if (nChars - nextChar >= r) {
				nextChar += r;
				break;
			} else {
				r -= nChars - nextChar;
				nextChar = nChars;
				fill();
				if (nextChar >= nChars) {
					break;
				}
			}
		}
		return n - r;
	}

	@Override
	public void close() {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
			in = null;
		}
	}

	@Override
	public int read(char cbuf[], int off, int len) throws IOException {
		int r = len;
		while (r > 0) {
			if (nextChar >= nChars) {
				fill();
				if (nextChar >= nChars) {
					return -1;
				}
			}
			int n = Math.min(r, nChars - nextChar);
			System.arraycopy(cb, nextChar, cbuf, off, n);
			nextChar += n;
			off += n;
			r -= n;
		}
		return len - r;
	}

	/**
	 * 向文本缓冲区内填充数据。
	 * 
	 * @throws IOException
	 *           数据读取产生异常
	 */
	private void fill() throws IOException {
		int len = nChars - nextChar + 1;
		if (nextChar > 0) {
			System.arraycopy(cb, nextChar - 1, cb, 0, len + 1);
		}
		int n;
		do {
			n = in.read(cb, len, cb.length - len);
		} while (n == 0);
		if (n > 0) {
			nextChar = 1;
			nChars = len + n;
		}
	}
}

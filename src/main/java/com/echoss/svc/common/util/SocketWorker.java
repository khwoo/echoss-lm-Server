/*
 * Copyright (c) 2008, TA Networks
 * All rights reserved.
 * 
 * 프로그램 기능 : 
 * 프로그램 ID : SocketWorker
 * 작성자 : TA Networks
 * 작성일 : 2008. 3. 2
 */
package com.echoss.svc.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SocketWorker {
	private Socket m_socket;

    private InputStream m_in;

    private OutputStream m_out;

    /**
     * 생성자.
     * @param socket
     * @throws IOException
     */
    public SocketWorker(Socket socket) throws IOException {
        m_socket = socket;
        m_in = new BufferedInputStream(m_socket.getInputStream());
        m_out = new BufferedOutputStream(m_socket.getOutputStream());
    }

    /**
     * 소켓으로 데이터를 송신한다.
     * @param message
     * @param offset
     * @param length
     * @throws IOException
     */
    public synchronized void write(byte[] message, int offset, int length)
            throws IOException {
        m_out.write(message, offset, length);
    }

    /**
     * 소켓으로 데이터를 송신한다.
     * @param message
     * @throws IOException
     */
    public synchronized void write(byte[] message) throws IOException {
        write(message, 0, message.length);
    }

    /**
     * 소켓으로부터 인자로 넘어온 수만큼 데이터를 읽어들인다.
     * @param readLength
     * @return
     * @throws IOException 
     * @throws IOException
     */
    public synchronized byte [] read(int readLength) throws IOException  {
        ensureOpen();
        
        int totalReadLength = 0;
        int currReadLength = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);

        while ((totalReadLength < readLength)) {
            byte[] temp = new byte[readLength - totalReadLength];
            currReadLength = m_in.read(temp, 0, readLength - totalReadLength);
            if (currReadLength <= 0) {
                break;
            }
            baos.write(temp, 0, currReadLength);
            totalReadLength += currReadLength;
        }

        if (currReadLength == -1)
            return null;
        
        return baos.toByteArray();
    }
    
    /**
     * OutputStream을 flush한다.
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        m_out.flush();
    }

    /**
     * In/OutStream을 close한다.
     */
    public void clear() {
        if (m_in != null) {
            try {
                m_in.close();
            }
            catch (IOException e) {
            }
            finally {
                m_in = null;
            }
        }

        if (m_out != null) {
            try {
                m_out.close();
            }
            catch (Exception e) {
            }
            finally {
                m_out = null;
            }
        }
    }

    /**
     * 소켓을 닫는다.
     */
    public void close() {
        clear();
        if (m_socket != null) {
            try {
                m_socket.close();
            }
            catch (IOException e) {
            }
        }
    }
    /**
     * InputStream을 체크한다.
     */
    private void ensureOpen() throws IOException {
        if (m_in == null)
            throw new IOException("InputStream이 Null입니다.");
    }

    /**
     * Timeout을 설정한다.
     * @param timeout
     * @throws SocketException
     */
    public void setSoTimeout(int timeout) throws SocketException {
        m_socket.setSoTimeout(timeout);
    }
}

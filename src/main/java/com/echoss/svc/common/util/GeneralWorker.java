/*
 * Copyright (c) 2008, TA Networks
 * All rights reserved.
 * 
 * 프로그램 기능 : Socket 핸들링 클래스
 * 프로그램 ID : GeneralWorker
 * 작성자 : TA Networks
 * 작성일 : 2008. 3. 2
 */
package com.echoss.svc.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Socket 핸들링 클래스
 * @author TA Networks
 *
 */
public class GeneralWorker extends SocketWorker {

	/**
     * 수신정보부의 데이타는 업무데이타에서 제외되며,
     * 길이정보에 설정된 길이값은 수신정보부의 데이타를 제외한 길이이다.
     * 이 경우는 송신시 수신정보부를 생성하여야 하며, 수신시는 수신정보부를 제거하여야한다.
     */
    public static final short HEADER_EXCLUDE = 1;
    /**
     * 수신정보부의 데이타는 업무데이타에서 제외되며,
     * 길이정보에 설정된 길이값은 수신정보부의 데이타를 포함한 길이이다.
     * 이 경우는 송신시 수신정보부를 생성하여야 하며, 수신시는 수신정보부를 제거하여야한다.
     */
    public static final short HEADER_EXCLUDE_REMAIN = 2;
    /**
     * 수신정보부의 데이타는 업무데이타에 포함되며,
     * 길이정보에 설정된 길이값은 수신정보부의 데이터를 포함한 길이이다.
     * 그러므로 수신정보부의 길이를 뺀 나머지 데이타을 수신하여야한다. 
     */
    public static final short HEADER_INCLUDE_ALTOGETHER = 3;
    /**
     * 수신정보부의 데이타는 업무데이타에 포함되며,
     * 길이정보에 설정된 길이값은 수신정보부의 데이타를 제외한 길이이다.
     * 그러므로 수신정보부의 길이만큼 나머지 데이타를 수신하여야한다.
     */
    public static final short HEADER_INCLUDE_REMAIN = 4;

    private int m_headerSize = 8;
    private int m_lengthOffset = 0;
    private int m_lengthSize = 8;
    private short m_receiveRule = HEADER_EXCLUDE;
    
    /**
     * 생성자
     * @param socket socket 클래스
     * @throws IOException
     */
    public GeneralWorker(Socket socket) throws IOException {
        super(socket);
    }
    
    /**
     * 생성자
     * @param socket socket 클래스
     * @param timeout 타임아웃 시간
     * @throws IOException
     */
    public GeneralWorker(Socket socket, int timeout) throws IOException {
        this(socket);
    	setSoTimeout(timeout);
    }
    
    /**
     * 데이터 송신
     * @param message 송신한 메세지
     * @throws Exception
     */
    public void sendMessage(byte [] message) throws Exception {
        if (message == null) {
            throw new Exception("송신할테이터가 NULL입니다.");
        }
        ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream(512);

        int reminderLength = 0;

        // 수신받은 형태를 확인하여 Length를 제외시킨경우에는 여기서 Length를 설정하여 보내야 한다.
        if (m_receiveRule == HEADER_EXCLUDE) {
            byte[] postBuffer = leftPad("", m_headerSize, ' ').getBytes();
            messageBuffer.write(postBuffer);
            reminderLength = message.length;
        }
        else if (m_receiveRule == HEADER_EXCLUDE_REMAIN) {
            byte[] postBuffer = leftPad("", m_headerSize, ' ').getBytes();
            messageBuffer.write(postBuffer);
            reminderLength = message.length + m_headerSize;
        }
        else {
            if (message.length < m_headerSize) {
                throw new Exception("송신데이타의 길이가 수신정보부데이타의 길이보다 작습니다. 송신데이타길이[" + message.length + "], 수신정보부데이타길이["+m_headerSize+"], 송신요청 데이타["+new String(message)+"]");
            }

            if (m_receiveRule == HEADER_INCLUDE_ALTOGETHER) {
                // 수신된 모든 데이터가 실제데이터로 인식한다. 
                // 데이터에 정의되는 길이는 먼저수신한 데이터를 포함한 길이이다.
                reminderLength = message.length;
            }
            else if (m_receiveRule == HEADER_INCLUDE_REMAIN) {
                // 수신된 모든 데이터가 실제데이터로 인식한다. 
                // 데이터에 정의되는 길이는 먼저수신한 데이터를 제외한 길이이다.
                reminderLength = message.length - m_headerSize;
            }
        }
        
        messageBuffer.write(message);
        byte [] buffer = messageBuffer.toByteArray();
        String sendDataLength = leftPad(reminderLength, m_lengthSize, '0');
        System.arraycopy(sendDataLength.getBytes(), 0, buffer, m_lengthOffset, m_lengthSize);

        write(buffer);
        flush();
    }

    private String leftPad(int src, int size, char fillChar) {
    	return leftPad(Integer.toString(src), size, fillChar);
    }

    private String leftPad(String src, int size, char fillChar) {
    	
    	byte[] resultBytes = new byte[size];
    	String resultString = "";

    	int srcSize = src.getBytes().length;
    	
    	if(srcSize < size) {
    		for(int i = 0 ; i < size - srcSize ; i++) {
    			resultBytes[i] = (byte) fillChar;
    		}
    		if(srcSize > 0) {
    			System.arraycopy(src.getBytes(), 0, resultBytes, size - srcSize, srcSize);
    		}
    		resultString = new String(resultBytes);
    	}
    	else {
    		resultString = src;
    	}

    	return resultString;
    }

    /**
     * 데이타를 수신한다.
     * @return 수신한 메시지
     * @throws IOException
     * @throws MessageException
     */
    public byte [] readMessage() throws Exception {
        ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream(512);
        
        // 수신정보부 데이타를 수신한다. (메시지의 길이를 구하기 위함임.)
        byte [] buffer = readByLength(m_headerSize);
        if (buffer == null) {
            return null;
        }
        
        // 전문 전체길이를 구해온다.
        int length;
        try {
            length = Integer.parseInt(new String(buffer, m_lengthOffset, m_lengthSize));
        }
        catch (Exception e) {
            throw new Exception(
                "수신정보부에서 수신데이타길이값를 구해오던중 에러가 발생하였습니다. 수신데이타길이값 : [" + new String(buffer, m_lengthOffset, m_lengthSize) + "]", e);
        }

        int reminderLength = 0;
        
        // 수신형태별로 수신해야할 데이타길이를 계산한다.
        if (m_receiveRule == HEADER_EXCLUDE) {
            // 길이부에 순수 데이터의 길이만을 설정한 경우이다.
            // 실제 데이터는 길이부에 정의된 값만큼을 실제데이터로 인식한다. 
            reminderLength = length;
        }
        else if (m_receiveRule == HEADER_EXCLUDE_REMAIN) {
        	reminderLength = length - m_headerSize;
        }
        else {
            // 수신정보부 데이타를 저장한다.
            messageBuffer.write(buffer);
            
            if (m_receiveRule == HEADER_INCLUDE_ALTOGETHER) {
                // 수신된 모든 데이터가 실제데이터로 인식한다. 
                // 단 먼저수신된 데이터에 정의된 길이는 먼저수신한 데이터를 포함한 길이이다.
                reminderLength = length - m_headerSize;
            }
            else if (m_receiveRule == HEADER_INCLUDE_REMAIN) {
                // 수신된 모든 데이터가 실제데이터로 인식한다. 
                // 단 먼저수신된 데이터에 정의된 길이는 먼저수신한 데이터를 제외한 길이이다.
                reminderLength = length;
            }
        }

        // 추가전문을 수신해야되는지 확인한다.
        if (reminderLength <= 0) {
            reminderLength = 0;
        }

        buffer = readByLength(reminderLength);
        if (buffer == null) {
            return null;
        }
        messageBuffer.write(buffer);

        return messageBuffer.toByteArray();
    }
    
    /**
     * 길이만큼 읽어들인다.
     * @param readLength 읽어들일 길이
     * @return 읽어들인 메세지
     * @throws Exception
     */
    private byte [] readByLength(int readLength) throws Exception {
        byte[] readData = read(readLength);
        if (readData == null) {
            return null;
        }
        
        if (readLength != readData.length) {
            throw new Exception("수신정보부의 수신요청길이와 수신된 데이터길이가 같지 않습니다. 수신요청길이[" + readLength + "], 수신된 데이타길이["+readData.length+"], 수신된 데이타["+new String(readData)+"]");
        }
        
        return readData;
    }
    
    /**
     * 수신정보부의 데이타길이의 크기를 설정한다.
     * @param lengthSize 데이타길이의 크기
     */
    public void setLengthSize(int lengthSize) {
        m_lengthSize = lengthSize;
    }
    
    /**
     * 수신정보부의 데이타길이의 위치값을 설정한다.
     * @param lengthOffset 데이타길이의 위치값
     */
    public void setLengthOffset(int lengthOffset) {
        m_lengthOffset = lengthOffset;
    }
    
    /**
     * 수신정보부의 길이를 설정한다.
     * @param headerSize 수신정보부의 길이
     */
    public void setHeaderSize(int headerSize) {
        m_headerSize = headerSize;
        setLengthSize(headerSize);
    }
    
    /**
     * 송/수신형태를 설정한다.
     * @param receiveRule 형태 - 기본값은 HEADER_EXCLUDE
     */
    public void setRule(short receiveRule) {
        m_receiveRule = receiveRule;
    }
}


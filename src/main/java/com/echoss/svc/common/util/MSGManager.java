package com.echoss.svc.common.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MSGManager {

	private String service;
	private String type;
	private String ip;
	private int port;

	public MSGManager(String service, String type, String ip, int port) {
		this.service = service;
		this.type = type;
		this.ip = ip;
		this.port = port;
	}

	/**
	 * 문자 즉시발송
	 * @param unique 고유번호
	 * @param subject 제목
	 * @param mobile 수신휴대폰번호
	 * @param callback 발실번호
	 * @param msg 발신메세지
	 * @throws Exception 
	 */
	public void send(String unique, String mobile, String callback, String msg) throws Exception {
		send(unique, "", mobile, callback, msg, null);
	}

	/**
	 * 문자 즉시발송
	 * @param unique 고유번호
	 * @param subject 제목
	 * @param mobile 수신휴대폰번호
	 * @param callback 발실번호
	 * @param msg 발신메세지
	 * @throws Exception 
	 */
	public void send(String unique, String subject, String mobile, String callback, String msg) throws Exception {
		send(unique, subject, mobile, callback, msg, null);
	}

	/**
	 * 문자 예약발송
	 * @param unique 고유번호
	 * @param subject 제목
	 * @param mobile 수신휴대폰번호
	 * @param callback 발실번호
	 * @param msg 발신메세지
	 * @param sendResvTm 발송예약시간
	 * @throws Exception 
	 */
	public void send(String unique, String subject, String mobile, String callback, String msg, String sendResvTm) throws Exception {

		if(unique == null || unique.trim().equals("")) {
			throw new Exception("unique id is empty");
		}

		if(mobile == null || mobile.trim().equals("")) {
			throw new Exception("mobile number is empty");
		}

		if(callback == null || callback.trim().equals("")) {
			throw new Exception("callback number is empty");
		}

		if(msg == null || msg.trim().equals("")) {
			throw new Exception("send message is empty");
		}

		if(sendResvTm != null && !sendResvTm.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				sdf.parse(sendResvTm);
			} catch (ParseException e) {
				throw new Exception("invalid send reserve time");
			}
		}

		JSONObject rootJson = new JSONObject();
		rootJson.put("service", this.service);
		rootJson.put("cmd", "send");

		JSONObject dataJson = new JSONObject();
		dataJson.put("type", this.type);
		dataJson.put("unique", unique);

		if(subject != null && !subject.trim().equals("")) {
			dataJson.put("subject", subject.trim());
		}

		dataJson.put("mobile", mobile);
		dataJson.put("callback", callback);
		dataJson.put("msg", msg);

		if(sendResvTm != null && !sendResvTm.trim().equals("")) {
			dataJson.put("time", sendResvTm);
		}

		rootJson.put("data", dataJson);

		byte[] resBytes = communicate(rootJson.toString().getBytes());
		if(resBytes == null) {
			throw new Exception("result message is null");
		}

		// 결과메시지를 파싱한다.
		String resCd = "";
		String resMsg = "";

		JSONObject resRootJson = JSONObject.fromObject(new String(resBytes));

		resCd = resRootJson.getString("resCd");
		if(!resCd.equals("00")) {
			// 오류결과를 수신하였습니다.
			resMsg = resRootJson.getString("resMsg");
			throw new Exception(resMsg);
		}
	}

	/**
	 * 문자 즉시발송(대량)
	 * @param uniques 고유번호 목록
	 * @param mobile 수신휴대폰번호 목록
	 * @param callback 발신번호
	 * @param msg 발신메세지
	 * @throws Exception 
	 */
	public void send(String[] uniques, String[] mobiles, String callback, String msg) throws Exception {
		send(uniques, "", mobiles, callback, msg, null);
	}

	/**
	 * 문자 예약발송(대량)
	 * @param uniques 고유번호 목록
	 * @param mobiles 수신휴대폰번호 목록
	 * @param callback 발신번호
	 * @param msg 발신메세지
	 * @param sendResvTm 발송예약시간
	 * @throws Exception
	 */
	public void send(String[] uniques, String[] mobiles, String callback, String msg, String sendResvTm) throws Exception {
		send(uniques, "", mobiles, callback, msg, sendResvTm);
	}

	/**
	 * 문자 즉시발송(대량)
	 * @param uniques 고유번호 목록
	 * @param subject 제목
	 * @param mobile 수신휴대폰번호 목록
	 * @param callback 발실번호
	 * @param msg 발신메세지
	 * @throws Exception 
	 */
	public void send(String[] uniques, String subject, String[] mobiles, String callback, String msg) throws Exception {
		send(uniques, subject, mobiles, callback, msg, null);
	}

	/**
	 * 문자 예약발송(대량)
	 * @param unique 고유번호
	 * @param subject 제목
	 * @param mobile 수신휴대폰번호 목록
	 * @param callback 발실번호
	 * @param msg 발신메세지
	 * @param sendResvTm 발송예약시간
	 * @throws Exception 
	 */
	public void send(String[] uniques, String subject, String[] mobiles, String callback, String msg, String sendResvTm) throws Exception {

		if(uniques == null || uniques.length <= 0) {
			throw new Exception("unique id is empty");
		}

		if(mobiles == null || mobiles.length <= 0) {
			throw new Exception("mobile number is empty");
		}

		if(callback == null || callback.trim().equals("")) {
			throw new Exception("callback number is empty");
		}

		if(msg == null || msg.trim().equals("")) {
			throw new Exception("send message is empty");
		}

		if(sendResvTm != null && !sendResvTm.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				sdf.parse(sendResvTm);
			} catch (ParseException e) {
				throw new Exception("invalid send reserve time");
			}
		}

		JSONObject rootJson = new JSONObject();
		rootJson.put("service", this.service);
		rootJson.put("cmd", "multipleSend");

		JSONObject dataJson = new JSONObject();
		dataJson.put("type", this.type);

		if(subject != null && !subject.trim().equals("")) {
			dataJson.put("subject", subject.trim());
		}

		dataJson.put("callback", callback);
		dataJson.put("msg", msg);

		if(sendResvTm != null && !sendResvTm.trim().equals("")) {
			dataJson.put("time", sendResvTm);
		}

		JSONArray jsonArray = new JSONArray();

		for(int i = 0 ; i < mobiles.length ; i++) {

			String unique = uniques[i];
			String mobile = mobiles[i];

			if(unique == null || unique.trim().equals("")) {
				throw new Exception("mobile number is empty");
			}

			if(mobile == null || mobile.trim().equals("")) {
				throw new Exception("mobile number is empty");
			}

			JSONObject itemJson = new JSONObject();
			itemJson.put("unique", unique);
			itemJson.put("mobile", mobile);

			jsonArray.add(itemJson);
		}

		dataJson.put("list", jsonArray);

		rootJson.put("data", dataJson);

		byte[] resBytes = communicate(rootJson.toString().getBytes());
		if(resBytes == null) {
			throw new Exception("result message is null");
		}

		// 결과메시지를 파싱한다.
		String resCd = "";
		String resMsg = "";

		JSONObject resRootJson = JSONObject.fromObject(new String(resBytes));

		resCd = resRootJson.getString("resCd");
		if(!resCd.equals("00")) {
			// 오류결과를 수신하였습니다.
			resMsg = resRootJson.getString("resMsg");
			throw new Exception(resMsg);
		}
	}

	/**
	 * 발송결과 조회
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public byte[] getResult(String start, String end) throws Exception {

		if(start == null || start.trim().equals("")) {
			throw new Exception("start sequence is empty");
		}

		if(end == null || end.trim().equals("")) {
			throw new Exception("end sequence is empty");
		}

		JSONObject rootJson = new JSONObject();
		rootJson.put("service", this.service);
		rootJson.put("cmd", "result");

		JSONObject dataJson = new JSONObject();
		dataJson.put("type", this.type);
		dataJson.put("start", start);
		dataJson.put("end", end);

		rootJson.put("data", dataJson);

		byte[] resBytes = communicate(rootJson.toString().getBytes());
		if(resBytes == null) {
			throw new Exception("result message is null");
		}

		return resBytes;
	}

	/**
	 * 서버와의 통신을 처리한다.
	 * @param reqBytes 요청메세지
	 * @return 결과메세지
	 * @throws Exception
	 */
	private byte[] communicate(byte[] reqBytes) throws Exception {

		byte[] resBytes = null;

		GeneralWorker worker = null;
		try {
			Socket s = new Socket();
			s.connect(new InetSocketAddress(this.ip, this.port), 3000);
			worker = new GeneralWorker(s);
			worker.setHeaderSize(8);
			worker.setSoTimeout(30 * 1000);

			worker.sendMessage(reqBytes);

			resBytes = worker.readMessage();
		} catch (Exception e) {
			throw e;
		} finally {
			if (worker != null) {
				worker.close();
			}
		}

		return resBytes;
	}
}

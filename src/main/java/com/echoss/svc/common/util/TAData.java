package com.echoss.svc.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * <b>FileName</b>    : TAData.java
 * <b>Project</b>     : Stamp-Book
 * <b>프로그램 설명</b>
 * 	LinkedHashMap을 상속받아 새로운 Map 기능 정의
 * <b>작성자</b>      : wblee
 * <b>작성일</b>      : 2012. 11. 7.
 * <b>변경이력</b>
 *                    이름     : 일자          : 근거자료   : 변경내용
 *                   ------------------------------------------------------
 *                    wblee : 2012. 11. 7. :            : 신규 개발.
 * </PRE>
 */
public class TAData {
	private static final Logger logger = LoggerFactory.getLogger(TAData.class);
	
	private Map<String, Object> map = new LinkedHashMap<String, Object>();

	/** TAData Name */
	private String name;

	/**
	 * TAData의 Name 설정
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * TAData의 Name 반환
	 * @return String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * TAData의 Key에 해당하는 값 반환시 초기값 설정
	 * <PRE>
	 * <b>true</b>
	 *      - getString()  : ""
	 *      - getInt()     : 0
	 *      - getLong()    : 0
	 *      - getDouble()  : 0
	 *      - getFloat()   : 0
	 *      - getBoolean() : false
	 *      - getObject()  : null
	 * 
	 * <b>false</b>
	 *      - getString()  : null
	 *      - getInt()     : Exception
	 *      - getLong()    : Exception
	 *      - getDouble()  : Exception
	 *      - getFloat()   : Exception
	 *      - getBoolean() : Exception
	 *      - getObject()  : null
	 * </PRE>
	 */
	private boolean nullToInitialize;

	/**
	 * TAData의 nullToInitialize 반환
	 * @return String
	 */
    public boolean isNullToInitialize()
    {
        return nullToInitialize;
    }

    /**
	 * TAData의 nullToInitialize 설정
	 * @param nullToInitialize
	 */
    public void setNullToInitialize(boolean nullToInitialize)
    {
        this.nullToInitialize = nullToInitialize;
    }

	public TAData() {
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에 시간 설정정보 추가여부
	 * 
	 *     - SYS_DT : 일자정보 (형식 'yyyyMMdd') 
	 *     - SYS_TM : 시간정보 (형식 'HHmmss') 
	 *     - SYS_DTTM : 일시정보 (형식 'yyyyMMddHHmmss')
	 * </PRE>
	 * @param isDate
	 */
	public TAData(boolean isDate) {
		super();

		if(isDate) {
			Date d = new Date();
			put("SYS_DT", new SimpleDateFormat("yyyyMMdd").format(d));
			put("SYS_TM", new SimpleDateFormat("HHmmss").format(d));
			put("SYS_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(d));
		}

		nullToInitialize = true;
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	map정보 TAData에 맵핑
	 * </PRE>
	 * @param map
	 */
	public TAData(Map<String, Object> map) {
		this(map, false);
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	map정보 TAData에 맵핑, 시간 설정정보 추가여부
	 * 
	 *  - SYS_DT : 일자정보 (형식 'yyyyMMdd') 
	 *  - SYS_TM : 시간정보 (형식 'HHmmss') 
	 *  - SYS_DTTM : 일시정보 (형식 'yyyyMMddHHmmss')
	 * </PRE>
	 * @param map
	 * @param isDate
	 */
	public TAData(Map<String, Object> map, boolean isDate) {
		super();
		if(map != null) putAll(map);

		if(isDate) {
			Date d = new Date();
			put("SYS_DT", new SimpleDateFormat("yyyyMMdd").format(d));
			put("SYS_TM", new SimpleDateFormat("HHmmss").format(d));
			put("SYS_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(d));
		}

		nullToInitialize = true;
	}

	private void put(String key, Object value) {
		this.map.put(key, value);
	}

	public void putAll(Map<String, Object> map) {
		this.map.putAll(map);
	}

	public void putAll(LinkedHashMap<String, String> map) {
		this.map.putAll(map);
	}

	public boolean containsKey(String key) {
		return this.map.containsKey(key);
	}

	public Set<String> keySet() {
		return this.map.keySet();
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	String Value를 TAData에 Set
	 * </PRE>
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		put(key, value);
	}
	
	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	int Value를 TAData에 Set
	 * </PRE>
	 * @param key
	 * @param value
	 */
	public void set(String key, int value) {
		put(key, Integer.valueOf(value));
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	long Value를 TAData에 Set
	 * </PRE>
	 * @param key
	 * @param value
	 */
	public void set(String key, long value) {
		put(key, Long.valueOf(value));
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	float Value를 TAData에 Set
	 * </PRE>
	 * @param key
	 * @param value
	 */
	public void set(String key, float value) {
		put(key, Float.valueOf(value));
	}
	
	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	boolean Value를 TAData에 Set
	 * </PRE>
	 * @param key
	 * @param value
	 */
	public void set(String key, boolean value) {
		put(key, Boolean.valueOf(value));
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	Object Value를 TAData에 Set
	 * </PRE>
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		put(key, value);
	}

	public Object get(String key) {
		return this.map.get(key);
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에서 String Type으로 Value 반환
	 * </PRE>
	 * @param key
	 * @return String
	 */
	public String getString(String key) {
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}
		
		String value = null;

		Object obj = null;
		obj = get(key);
		if(obj == null) {
			if(isNullToInitialize()) {
            	return "";
            }
			return null;
		}
		else if(obj instanceof String) {
			value = (String) obj;
		}
		else if(obj instanceof Number) {
			Number n = (Number) obj;
			value = n.toString();
		}
		
		return value;
	}
	
	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에서 int Type으로 Value 반환
	 * </PRE>
	 * @param key
	 * @return int
	 */
	public int getInt(String key) {
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}
		
		int retVal = 0;
		
		Object obj = null;
		obj = get(key);
		if(obj == null) {
            if(isNullToInitialize()) {
            	return 0;
            }
            
            throw new RuntimeException("TAData : value is not Integer object. value is NULL.");
		}
		else if(obj instanceof String) {
			retVal = Integer.parseInt((String) obj);
		}
		else if(obj instanceof Integer) {
			Integer value = null;
			try {
				value = (Integer) get(key);
				retVal = value.intValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Integer object [" + value + "]");
			}
		}
		else if(obj instanceof Number) {
			Number value = null;
			try {
				value = (Number) get(key);
				retVal = value.intValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Number object [" + value + "]");
			}
		}

		return retVal;
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에서 long Type으로 Value 반환
	 * </PRE>
	 * @param key
	 * @return long
	 */
	public long getLong(String key) {
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}
		
		long retVal = 0;
		
		Object obj = null;
		obj = get(key);
		if(obj == null) {
            if(isNullToInitialize()) {
            	return 0l;
            }
            
            throw new RuntimeException("TAData : value is not Long object. value is NULL.");
		}
		else if(obj instanceof String) {
			retVal = Long.parseLong((String) obj);
		}
		else if(obj instanceof Long) {
			Long value = null;
			try {
				value = (Long) get(key);
				retVal = value.longValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Long object [" + value + "]");
			}
		}
		else if(obj instanceof Number) {
			Number value = null;
			try {
				value = (Number) get(key);
				retVal = value.longValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Number object [" + value + "]");
			}
		}
		
		return retVal;
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에서 double Type으로 Value 반환
	 * </PRE>
	 * @param key
	 * @return double
	 */
	public double getDouble(String key) {
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}
		
		double retVal = 0;
		
		Object obj = null;
		obj = get(key);
		if(obj == null) {
            if(isNullToInitialize()) {
            	return 0d;
            }
            
            throw new RuntimeException("TAData : value is not Long object. value is NULL.");
		}
		else if(obj instanceof String) {
			retVal = Double.parseDouble((String) obj);
		}
		else if(obj instanceof Double) {
			Double value = null;
			try {
				value = (Double) get(key);
				retVal = value.doubleValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Long object [" + value + "]");
			}
		}
		else if(obj instanceof Number) {
			Number value = null;
			try {
				value = (Number) get(key);
				retVal = value.doubleValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Number object [" + value + "]");
			}
		}
		
		return retVal;
	}
	
	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에서 float Type으로 Value 반환
	 * </PRE>
	 * @param key
	 * @return float
	 */
	public float getFloat(String key) {
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}
		
		float retVal = 0;
		
		Object obj = null;
		obj = get(key);
		if(obj == null) {
            if(isNullToInitialize()) {
            	return 0f;
            }
            
            throw new RuntimeException("TAData : value is not Float object. value is NULL.");
		}
		else if(obj instanceof String) {
			retVal = Integer.parseInt((String) obj);
		}
		else if(obj instanceof Float) {
			Float value = null;
			try {
				value = (Float) get(key);
				retVal = value.floatValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Float object [" + value + "]");
			}
		}
		else if(obj instanceof Number) {
			Number value = null;
			try {
				value = (Number) get(key);
				retVal = value.floatValue();
			} catch(Exception e) {
				throw new RuntimeException("TAData : value is not Number object [" + value + "]");
			}
		}

		return retVal;
	}

	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에서 boolean Type으로 Value 반환
	 * </PRE>
	 * @param key
	 * @return boolean
	 */
	public boolean getBoolean(String key) {
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}
		
		Object obj = get(key);
        if(obj == null)
        {
            if(isNullToInitialize()) {
            	return false;
            }
            
            throw new RuntimeException("TAData : value is not Boolean object. value is NULL.");
        }
        else {
            if(obj instanceof Boolean) {
                return ((Boolean)obj).booleanValue();
            }
            else if(obj instanceof String) {
                try {
                    return Boolean.valueOf(obj.toString()).booleanValue();
                } catch(Exception e) {
                	throw new RuntimeException("TAData : value is not Boolean object [" + obj + "]");
                }
            }
        }
		
        return false;
	}
	
	/**
	 * <PRE>
	 * <b>프로그램설명</b>
	 * 	TAData에서 Object Type으로 Value 반환
	 * </PRE>
	 * @param key
	 * @return Object
	 */
	public Object getObject(String key) {
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}
		
		return get(key);
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(String key) {
		
		if(key == null) {
			throw new RuntimeException("TAData : getString parameter is empty!!");
		}

		Object obj = get(key);

		List<String> list = null;
		if(obj instanceof String) {
			list = new ArrayList<String>();
			list.add((String) obj);
		}
		else {
			list = (List<String>) obj;
		}
		
		return list;
	}

	/* (non-Javadoc)
	 * @see java.util.HashMap#isEmpty()
	 */
	public boolean isEmpty() {
		boolean rs = this.map.isEmpty();
		if(rs) return rs;
		
		if(this.map.size() > 0) return false;
		return true;
	}

	public Map<String, Object> toMap() {
		return this.map;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#toString()
	 */
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		String nameStr = "";
		if(this.name != null) nameStr = " - " + this.name;
		
		sb.append("\n");
		sb.append(allocateCenter(makeRepeatString("-", 70), "[TAData" + nameStr + "]")).append("\n");
		sb.append(allocateCenter(makeRepeatString(" ", 25), "KEY") + "|" + allocateCenter(makeRepeatString(" ", 44), "VALUE")).append("\n");
		sb.append(makeRepeatString("-", 70)).append("\n");

		for(Iterator<String> i = this.map.keySet().iterator() ; i.hasNext() ;) {
			String key = i.next();
			Object value = get(key);
			
			sb.append(allocateLeft(makeRepeatString(" ", 25), key));
			sb.append("|");

			String valueStr = "";
			if(value != null) {
				valueStr = value.toString();
				byte[] tmpBytes = new byte[44];
				System.arraycopy(valueStr.getBytes(), 0, tmpBytes, 0, valueStr.getBytes().length < 44 ? valueStr.getBytes().length : 44);
			}
			sb.append(allocateLeft(makeRepeatString(" ", 44), valueStr));
			sb.append("\n");
		}
		
		sb.append(makeRepeatString("-", 70));
		
		return sb.toString();
	}
	
	/**
	 * <b>프로그램 설명</b>
	 * 	해당 str로 size만큼 String을 채운다.
	 * @param str
	 * @param size
	 * @return String
	 */
	private String makeRepeatString(String str, int size) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < size ; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

    /**
     * <b>프로그램 설명</b>
     *  해당 line에 text를 가운데에 삽입한다.
     * @param line
     * @param text
     * @return String
     */
    private String allocateCenter(String line, String text)
    {
        StringBuffer sb = new StringBuffer();
        if(line == null || line.length() == 0)
            return "";
        sb.append(line);
        if(text == null || text.length() == 0)
            return sb.toString();
        if(text.length() > line.length())
        {
            String temp = text;
            text = (new StringBuilder(String.valueOf(temp.substring(0, line.length() - 2)))).append("..").toString();
        }
        int start = line.length() / 2 - text.length() / 2;
        int end = start + text.length();
        sb.replace(start, end, text);
        return sb.toString();
    }

    /**
     * <b>프로그램 설명</b>
     * 	해당 line에 text를 왼쪽에 삽입한다.
     * @param line
     * @param text
     * @return
     */
    private String allocateLeft(String line, String text)
    {
        StringBuffer sb = new StringBuffer();
        if(line == null || line.length() == 0)
            return "";
        sb.append(line);
        if(text == null || text.length() == 0)
            return sb.toString();
        if(text.length() > line.length())
        {
            String temp = text;
            text = (new StringBuilder(String.valueOf(temp.substring(0, line.length() - 2)))).append("..").toString();
        }
        int start = 0;
        int end = start + text.length();
        sb.replace(start, end, text);
        return sb.toString();
    }
    
    // 파라미터 Value가 String 일 경우 
	public static TAData getParameterMap(HttpServletRequest request) {
		
		// 파라미터 이름 
		Enumeration<String> paramNames = request.getParameterNames(); 

		// 저장할 맵 
		TAData paramMap = new TAData(); 

		// 맵에 저장 
		while(paramNames.hasMoreElements()) { 
			String name = paramNames.nextElement().toString(); 
			String value = request.getParameter(name); 
			
			paramMap.set(name, value); 
		} 

		// 결과반환 
		return paramMap; 
	}
	
	// 파라미터 Value가 String[] 일 경우
	public static TAData getParameterMapArray(HttpServletRequest request) {
		
		// 파라미터 이름 
		Enumeration<String> paramNames = request.getParameterNames(); 

		// 저장할 맵 
		TAData paramMap = new TAData(); 

		// 맵에 저장 
		while(paramNames.hasMoreElements()) { 
			String name = paramNames.nextElement().toString(); 
			String[] value = request.getParameterValues(name); 
			paramMap.set(name, value); 
		} 

		// 결과반환 
		return paramMap; 
	}
	
	public static TAData jsonToTAData(JSONObject json) throws JSONException {
		TAData retMap = new TAData();

	    if(json != null) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}

	public static TAData toMap(JSONObject object) throws JSONException {
		TAData map = new TAData();

	    @SuppressWarnings("unchecked")
		Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        
	        Object value = object.get(key);
	        
	        if(value != null){
		        if(value instanceof JSONArray) {
		            value = toList((JSONArray) value);
		        }
		        else if(value instanceof JSONObject) {
		            value = toMap((JSONObject) value);
		        }
	        }
	        
	        map.set(key, value);
	    }
	    return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
}

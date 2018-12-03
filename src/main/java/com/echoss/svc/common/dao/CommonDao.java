package com.echoss.svc.common.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.echoss.svc.common.util.TAData;

/**
 * <pre>
 * <b>Title   </b>: 12cm Common SQL Method
 * </pre>
 * <pre>
 * <b>Company </b>: 12cm
 * <b>Since   </b>: 2017. 9. 6.
 * <b>Version </b>: 1.0
 * 
 * <b>Desc  </b>: 
 * 
 * <b>== Modification Information ==</b>
 * 
 * VER      AUTHOR      NOTE                    DATE
 * ------	---------	---------------------   --------------------
 * 1.0		wblee       Initial generation      2017. 9. 6.
 * </pre>
 * 
 */
@Repository("commonDao")
public class CommonDao extends SqlSessionDaoSupport {

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @return TAData
	 */
	public TAData select(String id) {
		return recreationResult(getSqlSession().selectOne(id));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param params Parameter Data
	 * @return TAData
	 */
	public TAData select(String id, TAData params) {
		return recreationResult(getSqlSession().selectOne(id, params.toMap()));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return TAData
	 */
	public TAData select(String id, String param) {
		return recreationResult(getSqlSession().selectOne(id, param));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return TAData
	 */
	public TAData select(String id, int param) {
		return recreationResult(getSqlSession().selectOne(id, param));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return TAData
	 */
	public TAData select(String id, long param) {
		return recreationResult(getSqlSession().selectOne(id, param));
	}

	/**
	 * Recreation Result (Map -> TAData)
	 * @param map Query Result
	 * @return TAData
	 */
	private TAData recreationResult(Map<String, Object> map) {
		if(map != null) {
			return new TAData(map);
		}
		else {
			return null;
		}
	}
	
	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @return List
	 */
	public List<TAData> selectList(String id) {
		return recreationList(getSqlSession().selectList(id));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param params Parameter Data
	 * @return List
	 */
	public List<TAData> selectList(String id, TAData params) {
		return recreationList(getSqlSession().selectList(id, params.toMap()));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return List
	 */
	public List<TAData> selectList(String id, String param) {
		return recreationList(getSqlSession().selectList(id, param));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return List
	 */
	public List<TAData> selectList(String id, int param) {
		return recreationList(getSqlSession().selectList(id, param));
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return List
	 */
	public List<TAData> selectList(String id, long param) {
		return recreationList(getSqlSession().selectList(id, param));
	}

	/**
	 * Recreation Result (List<Map<String, Object>> -> List<TAData>)
	 * @param results Query Result List
	 * @return TAData
	 */
	private List<TAData> recreationList(List<Map<String, Object>> results) {
		List<TAData> outputs = null;
		if(results != null) {
			outputs = new ArrayList<TAData>();

			for(Map<String, Object> row : results) {
				TAData newRow = new TAData(row);
				outputs.add(newRow);
			}
		}
		return outputs;
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @return int
	 */
	public int selectInt(String id) {
		return getSqlSession().selectOne(id);
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param params Parameter Data
	 * @return int
	 */
	public int selectInt(String id, TAData params) {
		return getSqlSession().selectOne(id, params.toMap());
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return int
	 */
	public int selectInt(String id, String param) {
		return getSqlSession().selectOne(id, param);
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return int
	 */
	public int selectInt(String id, int param) {
		return getSqlSession().selectOne(id, param);
	}

	/**
	 * DB Select Query
	 * @param id Mapper Query ID
	 * @param param Parameter Data
	 * @return int
	 */
	public int selectInt(String id, long param) {
		return getSqlSession().selectOne(id, param);
	}

	/**
	 * DB Insert Query
	 * @param id Mapper Query ID
	 * @param params Parameter Data
	 */
	public void insert(String id, TAData params) {
		getSqlSession().insert(id, params.toMap());
	}

	/**
	 * DB Update Query
	 * @param id Mapper Query ID
	 * @param params Parameter Data
	 */
	public void update(String id, TAData params) {
		getSqlSession().update(id, params.toMap());
	}

	/**
	 * DB Delete Query
	 * @param id Mapper Query ID
	 * @param params Parameter Data
	 */
	public void delete(String id, TAData params) {
		getSqlSession().update(id, params.toMap());
	}
}

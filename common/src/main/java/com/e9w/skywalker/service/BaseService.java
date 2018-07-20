package com.e9w.skywalker.service;

import com.e9w.skywalker.lang.PageList;
import com.e9w.skywalker.lang.Parameter;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fc on 2016-11-29.
 */
public class BaseService {

	protected static Map<String, Object> record2map(Record record) {
		if (record == null) {
			return null;
		}
		Map<String, Object> m = record.getColumns();
		return m;
	}

    protected static Parameter record2param(Record record) {
		if (record == null) {
			return null;
		}
		Map<String, Object> m = record.getColumns();
		return new Parameter(m);
	}

	protected static List<Map<String, Object>> record2list(List<Record> list) {
		List<Map<String, Object>> result = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				result.add(record2map(r));
			}
		}
		return result;
	}

	protected static <T> Map<T, Map<String, Object>> list2map(List<Record> list, String keyField) {
		Map<T, Map<String, Object>> result = new LinkedHashMap<>();
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				Map<String, Object> m = record2map(r);
				T key = (T) m.get(keyField);
				if (key == null) {
					continue;
				}
				result.put(key, m);
			}
		}
		return result;
	}

	protected static PageList<Map<String, Object>> page2PageList(Page<Record> page) {
		PageList<Map<String, Object>> result = new PageList<>(
				record2list(page.getList()), page.getTotalRow(),
				page.getTotalPage(), page.getPageNumber(), page.getPageSize());
		return result;
	}
}

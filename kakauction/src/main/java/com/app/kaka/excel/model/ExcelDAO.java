package com.app.kaka.excel.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.kaka.car.model.CarVO;
import com.app.kaka.member.model.MemberVO;

public interface ExcelDAO {
	//출력시
	public List<CarVO> excelCarList();
	
	//입력시
	public void excelAdd(Map<String, Object> map);
	public void excelAddCar(Map<String, Object> map);
}

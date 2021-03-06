package com.app.kaka.map.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.kaka.map.model.MapService;
import com.app.kaka.map.model.MapVO;

@Controller
@RequestMapping("/map")
public class MapController {
	
	private static final Logger logger = LoggerFactory.getLogger(MapController.class);
	
	@Autowired
	private MapService mapService;
	
	@RequestMapping("/mapConti.do")
	public String showMap(){
		logger.info("지도 보여주기");
		
		return "map/mapConti";
	}
	
	@RequestMapping("/defaultMap.do")
	public String defaultMap(){
		logger.info("지도 보여주기");
		
		return "map/defaultMap";
	}
}

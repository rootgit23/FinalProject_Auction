package com.im.home.wholesale;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
@RequestMapping("/wholesale/*")
public class WholeSaleController {
	
	@GetMapping("realtime")
	@ResponseBody
	public ModelAndView realtime() throws Exception { //실시간 정보 출력 
		
		ModelAndView mv = new ModelAndView();
		WebClient webClient = WebClient.builder()
			    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
				 .baseUrl("https://at.agromarket.kr/openApi/price/real.do")
				 .build();
		
		Mono<String> res = webClient.get()
				.uri("?serviceKey=9596499878664F83A1D560AE3808376E&apiType=json&pageNo=1&whsalCd=110001")
				.retrieve()
				.bodyToMono(String.class);
				
		String r = res.block();
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	JSONParser parser = new JSONParser();
	Map<String, Object> data = objectMapper.readValue(r, new TypeReference<Map<String, Object>>() {});
	
		JSONObject jobj = new JSONObject(data);
		Object jobj2 = jobj.get("data");
		String data2 = objectMapper.writeValueAsString(jobj2);
		JSONArray temp = (JSONArray)parser.parse(data2);

		List<WholeSaleVO>  wholeSaleVOs = new ArrayList<>();
		for(int i =0; i<temp.size(); i++) {
		
			JSONObject jsonObj = (JSONObject)temp.get(i);
			
			log.info("array => {}", jsonObj);
			
			if(temp.size()!=0) {
				WholeSaleVO wholeSaleVO = new WholeSaleVO();
				wholeSaleVO.setSaleDate(jsonObj.get("saleDate").toString());
				wholeSaleVO.setWhsalCd(jsonObj.get("whsalCd").toString());
				wholeSaleVO.setWhsalName(jsonObj.get("whsalName").toString());
				wholeSaleVO.setCmpCd(jsonObj.get("cmpCd").toString());
				wholeSaleVO.setCmpName(jsonObj.get("cmpName").toString());
				wholeSaleVO.setLarge(jsonObj.get("large").toString());
				wholeSaleVO.setMid(jsonObj.get("mid").toString());
				wholeSaleVO.setMidName(jsonObj.get("midName").toString());
				wholeSaleVO.setSmall(jsonObj.get("small").toString());
				wholeSaleVO.setSmallName(jsonObj.get("smallName").toString());
				wholeSaleVO.setSanCd(jsonObj.get("sanCd").toString());
				wholeSaleVO.setCost(jsonObj.get("cost").toString());
				wholeSaleVO.setQty(jsonObj.get("qty").toString());
				wholeSaleVO.setStd(jsonObj.get("std").toString());
				wholeSaleVO.setSbidtime(jsonObj.get("sbidtime").toString());
				wholeSaleVOs.add(i, wholeSaleVO);
			}

		}
		
		mv.addObject("vo", wholeSaleVOs);
		mv.setViewName("wholesale/realtime");
		
		return mv;
	}
	
	@GetMapping("test")
	@ResponseBody
	public ModelAndView wholeSale() throws Exception{
		
		ModelAndView mv = new ModelAndView();
		
		WebClient webClient = WebClient.builder()
			    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
				 .baseUrl("https://at.agromarket.kr/openApi/price/sale.do")
				 .build();
		
		Mono<String> res = webClient.get()
				.uri("?serviceKey=9596499878664F83A1D560AE3808376E&apiType=json&pageNo=1&whsalCd=110001&saleDate=20221116")
				.retrieve()
				.bodyToMono(String.class);
				
		String r = res.block();
		
	//ObjectMapper = JSON 형식을 사용할 때, 응답들을 직렬화하고 요청들을 역직렬화(deserialization)
	ObjectMapper objectMapper = new ObjectMapper();
	
	//JSONParser = JSON 형식의 문자열을 객체로 변환
	JSONParser parser = new JSONParser();
	
	//JSON → Java Object
	//JSON 파일을 Java 객체로 역직렬화  하기 위해서는 ObjectMapper의 readValue() 메서드를 이용
	//map 타입으로 받은 이유는? JSONObject(Map map)에서 Map을 변수로 받기 때문.
	Map<String, Object> data = objectMapper.readValue(r, new TypeReference<Map<String, Object>>() {});
	
		//JSONObject(Map map)
		//JSONObject으로 변환하여 .get("data");으로 키 값을 꺼내올 수 있음.
		JSONObject jobj = new JSONObject(data);

		Object jobj2 = jobj.get("data");
		String data2 = objectMapper.writeValueAsString(jobj2);
		
		//parser.parse(String type); 스프링타입만 담을 수 있기 때문에 - java Object -> String 변환 
		JSONArray temp = (JSONArray)parser.parse(data2);

		//jsonArray를 java List에 담기
		List<WholeSaleVO>  wholeSaleVOs = new ArrayList<>();
		for(int i =0; i<temp.size(); i++) {
		
			JSONObject jsonObj = (JSONObject)temp.get(i);
			
			log.info("array => {}", jsonObj);
			log.info("largeName => {}", jsonObj.get("largeName"));
			
			if(temp.size()!=0) {
				WholeSaleVO wholeSaleVO = new WholeSaleVO();
				wholeSaleVO.setSmall(jsonObj.get("small").toString());
				wholeSaleVO.setLargeName(jsonObj.get("largeName").toString());
				//wholeSaleVO list에 초기화되어 새로운 set값이 설정된 wholeSaleVO 추가
				//add(int index, WholeSaleVO element)
				wholeSaleVOs.add(i, wholeSaleVO);
			}

		}
		
		mv.addObject("vo", wholeSaleVOs);
		mv.setViewName("wholesale/test");
		return mv;
	
	}

}
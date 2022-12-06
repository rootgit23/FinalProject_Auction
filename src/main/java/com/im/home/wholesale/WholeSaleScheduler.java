package com.im.home.wholesale;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class WholeSaleScheduler { //일자별 상세 리스트 출력 용 DB삽입 스케쥴러
	
	
	@Autowired
	private  WholeSaleMapper wholeSaleMapper;
	
	//@Scheduled(cron = "10 0 0 * * 1-5") //월-금 정각 10초에 실행
	public void cron(MustParamVO mustParamVO) throws Exception {
		
	
		ModelAndView mv = new ModelAndView();
		WebClient webClient = WebClient.builder()
			    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
				 .baseUrl("https://at.agromarket.kr/openApi/price/sale.do")
				 .build();
		
		Mono<String> res = webClient.get()
				.uri("?serviceKey=9596499878664F83A1D560AE3808376E&apiType=json&pageNo=1&whsalCd="+mustParamVO.getWhsalCd()+"&saleDate="+mustParamVO.getSaleDate()+
						"&cmpCd="+mustParamVO.getCmpCd()+"&largeCd="+mustParamVO.getLargeCd()+"&midCd="+mustParamVO.getMidCd()+"&smallCd="+mustParamVO.getSmallCd())
				.retrieve()
				.bodyToMono(String.class);
				
		String r = res.block();
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	JSONParser parser = new JSONParser();
	Map<String, Object> data = objectMapper.readValue(r, new TypeReference<Map<String, Object>>() {});
	
		JSONObject jobj = new JSONObject(data);
		String count = jobj.get("totCnt").toString(); //데이터총개수 - 이걸로 페이징을 해볼까
		//총 개수로 파라미터 페이지 총 개수를 설정해놓고,
		//rn으로 페이지 블락처리하고, rn이 1000을 넘으면 파라미터 page 넘어가게 처리
		Object jobj2 = jobj.get("data");
		String data2 = objectMapper.writeValueAsString(jobj2);
		JSONArray temp = (JSONArray)parser.parse(data2);

		List<WholeSaleVO>  wholeSaleVOs = new ArrayList<>();

		for(int i =0; i<temp.size(); i++) {
		
			JSONObject jsonObj = (JSONObject)temp.get(i);
		
				log.info("array => {}", jsonObj);
				if(temp.size()!=0) {
					WholeSaleVO wholeSaleVO = new WholeSaleVO();
					wholeSaleVO.setRn(jsonObj.get("rn").toString());
					wholeSaleVO.setSaleDate(jsonObj.get("saleDate").toString());
					wholeSaleVO.setWhsalCd(jsonObj.get("whsalCd").toString());
					wholeSaleVO.setWhsalName(jsonObj.get("whsalName").toString());
					wholeSaleVO.setCmpCd(jsonObj.get("cmpCd").toString());
					wholeSaleVO.setCmpName(jsonObj.get("cmpName").toString());
					wholeSaleVO.setLarge(jsonObj.get("large").toString());
					wholeSaleVO.setLargeName(jsonObj.get("largeName").toString());
					wholeSaleVO.setMid(jsonObj.get("mid").toString());
					wholeSaleVO.setMidName(jsonObj.get("midName").toString());
					wholeSaleVO.setSmall(jsonObj.get("small").toString());
					wholeSaleVO.setSmallName(jsonObj.get("smallName").toString());
					wholeSaleVO.setTotQty(jsonObj.get("totQty").toString());
					wholeSaleVO.setTotAmt(jsonObj.get("totAmt").toString());
					wholeSaleVO.setMinAmt(jsonObj.get("minAmt").toString());
					wholeSaleVO.setMaxAmt(jsonObj.get("maxAmt").toString());
					wholeSaleVO.setAvgAmt(jsonObj.get("avgAmt").toString());
					wholeSaleVOs.add(i, wholeSaleVO);
					
					//log.info("Result {} ", wholeSaleMapper.setAdd(wholeSaleVO));
				}
			
				}
			
			
		}
		
	
		
	}

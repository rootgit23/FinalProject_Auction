package com.im.home.wholesale;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.im.home.util.Pager;

@Mapper
public interface WholeSaleMapper {
	
	public int setAdd(WholeSaleVO wholeSaleVO) throws Exception;
	
	public int setAddReal(WholeSaleVO wholeSaleVO) throws Exception;

	public List<WholeSaleVO> getList(Pager pager) throws Exception;

	public void deleteList(String delDay)throws Exception;
	
	public void deleteReal(String whsalCd)throws Exception;
	
	public Long getListCount(Pager pager)throws Exception;
	
	public List<WholeSaleVO> getWhsalMain(String saleDate)throws Exception;
	
	public List<WholeSaleVO> getMidMain(String saleDate)throws Exception;
	
	public List<WholeSaleVO> getRtime(String whsalCd)throws Exception;
	
	public WholeSaleVO getTotQty(String saleDate)throws Exception;
	
	public WholeSaleVO getTotAmt(String saleDate)throws Exception;
	
	public List<WholeSaleVO> getMidSale(Pager pager)throws Exception;
	
}

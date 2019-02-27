package com.internousdev.magenda.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.magenda.dao.CartInfoDAO;
import com.internousdev.magenda.dao.PurchaseHistoryInfoDAO;
import com.internousdev.magenda.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementCompleteAction extends ActionSupport implements SessionAware{

	private String id;
	private Map<String,Object>session;

	public String execute() throws SQLException{

		if(!session.containsKey("mCategoryDTOList")){
			return "timeout";
		}

		String result = SUCCESS;

		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		String loginId = String.valueOf(session.get("loginId"));
		List<CartInfoDTO>purchaseHistoryInfoList= cartInfoDAO.getCartInfoDTOList(loginId);

		PurchaseHistoryInfoDAO purchaseHistoryInfoDAO = new PurchaseHistoryInfoDAO();
		int count = 0;

		for(CartInfoDTO dto : purchaseHistoryInfoList){
			count += purchaseHistoryInfoDAO.regist(loginId, dto.getProductId(), dto.getProductCount(), Integer.parseInt(id), dto.getSubtotal());
		}

		if(count>0){
			count=cartInfoDAO.deleteAll(String.valueOf(session.get("loginId")));
			if(count>0){
				List<CartInfoDTO>cartInfoDTOList=new ArrayList<CartInfoDTO>();
				cartInfoDTOList=cartInfoDAO.getCartInfoDTOList(String.valueOf(session.get("loginId")));
				session.put("cartInfoDTOList", cartInfoDTOList);
				int totalPrice=Integer.parseInt(String.valueOf(cartInfoDAO.getTotalPrice(String.valueOf(session.get("loginId")))));
				session.put("totalPrice",totalPrice);
				session.remove("purchaseHistoryInfoDTOList");
			}
		}
		return result;
}
	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id=id;
	}

	public Map<String,Object>getSession(){
		return session;
	}

	public void setSession(Map<String,Object>session){
		this.session=session;
	}

}

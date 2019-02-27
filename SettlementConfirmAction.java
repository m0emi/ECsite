package com.internousdev.magenda.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.magenda.dao.DestinationInfoDAO;
import com.internousdev.magenda.dto.DestinationInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementConfirmAction extends ActionSupport implements SessionAware {

	private Map<String, Object>session;

	public String execute(){

		if(!session.containsKey("mCategoryDTOList")){
			return "timeout";
		}

		String result=ERROR;

		if(session.containsKey("loginId")){
			DestinationInfoDAO destinationInfoDAO=new DestinationInfoDAO();
			List<DestinationInfoDTO> destinationInfoDTOList=new ArrayList<>();
			try{
				destinationInfoDTOList=destinationInfoDAO.getDestinationInfo(String.valueOf(session.get("loginId")));
				Iterator<DestinationInfoDTO> iterator=destinationInfoDTOList.iterator();
				if(!(iterator.hasNext())){
					destinationInfoDTOList=null;
				}
				session.put("destinationInfoDTOList",destinationInfoDTOList);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}

		if(session.get("logined").equals(0)){
			session.remove("loginIdErrorMessageList");
			session.remove("passwordErrorMessageList");
			session.put("cartFlg", 1);
			result=ERROR;
		}else{
			result=SUCCESS;
		}
		return result;
	}

	public Map<String,Object>getSession(){
		return session;
	}

	public void setSession(Map<String, Object>session){
		this.session=session;
	}

}

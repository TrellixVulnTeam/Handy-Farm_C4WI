package com.handyfarm.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.handyfarm.dao.HandyFarmDAO;

public class HandyFarmRoboInsertCommand implements HandyFarmCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String robo_serial = request.getParameter("robo_serial");
		String robo_img = request.getParameter("robo_img");
		String robo_nickname = request.getParameter("robo_nickname");
		String cultivar_number = request.getParameter("cultivar_number");
		String gh_id = request.getParameter("gh_id");
		String phone_number = request.getParameter("phone_number");
		
		HandyFarmDAO dao = new HandyFarmDAO();
		dao.roboinsert(robo_serial, robo_img, robo_nickname, cultivar_number, gh_id, phone_number);
	}
}
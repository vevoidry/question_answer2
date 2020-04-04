package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Message;

public interface MessageService {

	Message save(Message message);
	
	List<Message> findAllBySend_user_idReceive_user_idExchange(Integer send_user_id, Integer receive_user_id);

	List<Message> findFiveSend(Integer me_id);
	
	List<Message> findFiveReceive(Integer me_id);
}

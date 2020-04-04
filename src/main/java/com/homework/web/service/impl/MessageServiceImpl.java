package com.homework.web.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Message;
import com.homework.web.repository.MessageRepository;
import com.homework.web.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Override
	public Message save(Message message) {
		message.setCreate_time(new Date());
		message.setIs_read(false);
		return messageRepository.save(message);
	}

	@Override
	public List<Message> findAllBySend_user_idReceive_user_idExchange(Integer send_user_id, Integer receive_user_id) {
		return messageRepository.findAllBySend_user_idReceive_user_idExchange(send_user_id, receive_user_id);
	}

	@Override
	public List<Message> findFiveSend(Integer me_id) {
		return messageRepository.findFiveSend(me_id);
	}

	@Override
	public List<Message> findFiveReceive(Integer me_id) {
		return messageRepository.findFiveReceive(me_id);
	}

}

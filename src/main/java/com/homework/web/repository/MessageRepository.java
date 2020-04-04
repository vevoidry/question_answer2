package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Message;
import com.homework.web.pojo.Question;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	@Query(value = "select * from message where (send_user_id=:send_user_id and receive_user_id=:receive_user_id) or (send_user_id=:receive_user_id and receive_user_id=:send_user_id)", nativeQuery = true)
	List<Message> findAllBySend_user_idReceive_user_idExchange(Integer send_user_id, Integer receive_user_id);
	
	@Query(value = "select * from message where send_user_id=:me_id  group by receive_user_id order by id desc limit 5", nativeQuery = true)
	List<Message> findFiveSend(Integer me_id);
	
	@Query(value = "select * from message where receive_user_id=:me_id  group by send_user_id order by id desc limit 5", nativeQuery = true)
	List<Message> findFiveReceive(Integer me_id);
}

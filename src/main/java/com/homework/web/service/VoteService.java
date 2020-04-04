package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Vote;

public interface VoteService {

	Vote save(Vote vote);

	List<Vote> findByAnswer_id(Integer answer_id);

	Vote findByAnswer_idUser_id(Integer answer_id, Integer user_id);

	void deleteById(Integer id);

	void deleteByAnswer_id(Integer answer_id);

}

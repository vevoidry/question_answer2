package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homework.web.pojo.Vote;
import com.homework.web.repository.VoteRepository;
import com.homework.web.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {
	@Autowired
	private VoteRepository voteRepository;

	@Override
	public Vote save(Vote vote) {
		return voteRepository.save(vote);
	}

	@Override
	public List<Vote> findByAnswer_id(Integer answer_id) {
		return voteRepository.findByAnswer_id(answer_id);
	}

	@Override
	public Vote findByAnswer_idUser_id(Integer answer_id, Integer user_id) {
		return voteRepository.findByAnswer_idUser_id(answer_id, user_id);
	}

	@Transactional
	@Override
	public void deleteById(Integer id) {
		voteRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void deleteByAnswer_id(Integer answer_id) {
		voteRepository.deleteByAnswer_id(answer_id);
	}
}

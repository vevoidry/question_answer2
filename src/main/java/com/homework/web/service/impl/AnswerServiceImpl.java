package com.homework.web.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homework.util.Sensitive_wordUtil;
import com.homework.web.pojo.Answer;
import com.homework.web.pojo.Question;
import com.homework.web.pojo.Vote;
import com.homework.web.repository.AnswerRepository;
import com.homework.web.service.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService {
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private Sensitive_wordUtil sensitive_wordUtil;
	@Autowired
	private VoteServiceImpl voteServiceImpl;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;

	@Override
	public Answer save(Answer answer) {
		answer.setCreate_time(new Date());
		answer.setVote_count(0);
		Answer answer2 = answerRepository.save(answer);
		if (answer2 != null) {
			// 敏感词过滤
			answer2.setContent(sensitive_wordUtil.check(answer2.getContent()));
		}
		return answer2;
	}

	@Override
	public List<Answer> findAllByPage(Integer question_id, Integer size, Integer page) {
		List<Answer> answerList = answerRepository.findAllByPage(question_id, (page - 1) * size, size);
		if (answerList != null) {
			// 敏感词过滤
			Iterator<Answer> iterator = answerList.iterator();
			while (iterator.hasNext()) {
				Answer next = iterator.next();
				next.setContent(sensitive_wordUtil.check(next.getContent()));
			}
		}
		return answerList;
	}

	@Override
	public Integer findTheCountByQuestion_id(Integer question_id) {
		return answerRepository.findTheCountByQuestion_id(question_id);
	}

	@Override
	public Answer findById(Integer id) {
		Answer answer = answerRepository.findById(id).get();
		if (answer != null) {
			// 敏感词过滤
			answer.setContent(sensitive_wordUtil.check(answer.getContent()));
		}
		return answer;
	}

	@Override
	public Answer update(Answer answer) {
		Answer answer2 = answerRepository.save(answer);
		if (answer2 != null) {
			// 敏感词过滤
			answer2.setContent(sensitive_wordUtil.check(answer2.getContent()));
		}
		return answer2;
	}

	@Override
	public Answer findByUser_idQuestion_id(Integer user_id, Integer question_id) {
		Answer answer = answerRepository.findByUser_idQuestion_id(user_id, question_id);
		if (answer != null) {
			// 敏感词过滤
			answer.setContent(sensitive_wordUtil.check(answer.getContent()));
		}
		return answer;
	}

	@Override
	public List<Answer> findByUser_id(Integer user_id) {
		List<Answer> answerList = answerRepository.findByUser_id(user_id);
		if (answerList != null) {
			// 敏感词过滤
			Iterator<Answer> answerListIterator = answerList.iterator();
			while (answerListIterator.hasNext()) {
				Answer answer = answerListIterator.next();
				answer.setContent(sensitive_wordUtil.check(answer.getContent()));
			}
		}
		return answerList;
	}

	@Transactional
	@Override
	public void deleteByQuestion_id(Integer question_id) {
		// 获取该回答的所有点赞
		List<Answer> answerList = answerRepository.findAllByQuestion_id(question_id);
		if (answerList != null) {
			Iterator<Answer> iterator = answerList.iterator();
			while (iterator.hasNext()) {
				Answer next = iterator.next();
				// 删除相关的点赞
				voteServiceImpl.deleteByAnswer_id(next.getId());
			}
		}
		// 删除回答
		answerRepository.deleteByQuestion_id(question_id);
	}

	@Transactional
	@Override
	public void deleteById(Integer id) {
		// 获取该回答的所有点赞
		Answer answer = answerRepository.findById(id).get();
		// 删除相关的点赞
		voteServiceImpl.deleteByAnswer_id(answer.getId());
		// 若该回答所在问题的置顶回答为该回答，则将其置顶数据设置为null
		Question question = questionServiceImpl.findById(answer.getQuestion_id());
		if (question.getAnswer_id() == answer.getId()) {
			question.setAnswer_id(null);
			questionServiceImpl.update(question);
		}
		// 删除回答
		answerRepository.deleteById(id);
	}
}

package com.homework.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homework.util.Sensitive_wordUtil;
import com.homework.web.pojo.Answer;
import com.homework.web.pojo.Category;
import com.homework.web.pojo.Question;
import com.homework.web.pojo.User;
import com.homework.web.repository.QuestionRepository;
import com.homework.web.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private Sensitive_wordUtil sensitive_wordUtil;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private AnswerServiceImpl answerServiceImpl;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;
//	@Autowired
//	private TagServiceImpl tagServiceImpl;
//	@Autowired
//	private Question_TagServiceImpl question_TagServiceImpl;
	@Autowired
	private CollectionServiceImpl collectionServiceImpl;
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@Override
	public Question save(Question question) {
		question.setCreate_time(new Date());
		question.setOn_off(true);
		question.setAnswer_id(null);
		Question question2 = questionRepository.save(question);
		if (question2 != null) {
			// 敏感词过滤
			question2.setTitle(sensitive_wordUtil.check(question2.getTitle()));
			question2.setContent(sensitive_wordUtil.check(question2.getContent()));
		}
		return question2;
	}

	@Override
	public List<Question> findAllByPage(Integer size, Integer page) {
		List<Question> questionList = questionRepository.findAllByPage((page - 1) * size, size);
		if (questionList != null) {
			// 敏感词过滤
			Iterator<Question> iterator = questionList.iterator();
			while (iterator.hasNext()) {
				Question next = iterator.next();
				next.setTitle(sensitive_wordUtil.check(next.getTitle()));
				next.setContent(sensitive_wordUtil.check(next.getContent()));
			}
		}
		return questionList;
	}

	@Override
	public Integer findTheCount() {
		return questionRepository.findTheCount();
	}

	@Override
	public Question findById(Integer id) {
		Question question = questionRepository.findById(id).get();
		if (question != null) {
			// 敏感词过滤
			question.setTitle(sensitive_wordUtil.check(question.getTitle()));
			question.setContent(sensitive_wordUtil.check(question.getContent()));
		}
		return question;
	}

	@Override
	public Question update(Question question) {
		Question question2 = questionRepository.save(question);
		if (question2 != null) {
			// 敏感词过滤
			question2.setTitle(sensitive_wordUtil.check(question2.getTitle()));
			question2.setContent(sensitive_wordUtil.check(question2.getContent()));
		}
		return question2;
	}

	@Override
	public Integer findTheCountBySearch(String data) {
		return questionRepository.findTheCountBySearch(data);
	}

	@Override
	public List<Question> findAllBySearch(String data, Integer size, Integer page) {
		return questionRepository.findAllBySearch(data, (page - 1) * size, size);
	}

	@Override
	public List<Question> findByRecommend(Authentication authentication) {
		// 若用户未登录，则返回随机五个question的数据
		if (authentication == null) {
			return questionRepository.findAllByLastLimit5();
		}
		// 若用户已登录，则获取用户最近的回答的数据
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		List<Answer> answerList = answerServiceImpl.findByUser_id(me.getId());
		// 若无回答，则返回随机数据
		if (answerList.size() == 0) {
			return questionRepository.findAllByLastLimit5();
		}
		// 若有回答，则获取最近回答的分类，根据分类信息返回至多5条推荐提问
		Iterator<Answer> iterator = answerList.iterator();
		while (iterator.hasNext()) {
			Answer answer = iterator.next();
			Integer question_id = answer.getQuestion_id();
			Question question = questionServiceImpl.findById(question_id);
			// 判断该问题分类是否为0
			if (question.getCategory_id() == 0) {
				return questionRepository.findAllByLastLimit5();
			}
			Category category = categoryServiceImpl.findById(question.getCategory_id());
			List<Question> questionList = questionServiceImpl.findAllByCategory_idPage(category.getId(), 5, 1);
			return questionList;
		}
		// 若运行到此处仍未成功返回推荐数据，则返回随机数据
		return questionRepository.findAllByLastLimit5();
	}

	@Override
	public List<Question> findAllByLastLimit5() {
		return questionRepository.findAllByLastLimit5();
	}

	@Override
	public List<Question> findAllByLastLimit(Integer limit) {
		return questionRepository.findAllByLastLimit(limit);
	}

	@Override
	public List<Question> findAllByUser_id(Integer user_id) {
		return questionRepository.findAllByUser_id(user_id);
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		// 删除提问
		questionRepository.deleteById(id);
		// 删除该提问相关的tag关联
//		question_TagServiceImpl.deleteByQuestion_id(id);
		// 删除该提问下的所有回答
		answerServiceImpl.deleteByQuestion_id(id);
		// 删除该提问下的收藏
		collectionServiceImpl.deleteByQuestion_id(id);
	}

	@Override
	public List<Question> findAllByCategory_idPage(Integer category_id, Integer size, Integer page) {
		return questionRepository.findAllByCategory_idPage(category_id, (page - 1) * size, size);
	}

	@Override
	public Integer findTheCountByCategory_id(Integer category_id) {
		return questionRepository.findTheCountByCategory_id(category_id);
	}

	@Override
	public void updateAllByCategory_id(Integer category_id) {
		questionRepository.updateAllByCategory_id(category_id);
	}
}

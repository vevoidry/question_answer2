package com.homework.web.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Share;
import com.homework.web.repository.ShareRepository;
import com.homework.web.service.ShareService;

@Service
public class ShareServiceImpl implements ShareService {
	@Autowired
	private ShareRepository shareRepository;

	@Override
	public Share save(Share share) {
		share.setCreate_time(new Date());
		return shareRepository.save(share);
	}

	@Override
	public List<Share> findAllByUser_id(Integer user_id) {
		return shareRepository.findAllByUser_id(user_id);
	}

	@Override
	public void deleteById(Integer id) {
		shareRepository.deleteById(id);
	}

}

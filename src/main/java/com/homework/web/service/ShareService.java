package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Share;

public interface ShareService {
	Share save(Share share);

	List<Share> findAllByUser_id(Integer user_id);

	void deleteById(Integer id);
}

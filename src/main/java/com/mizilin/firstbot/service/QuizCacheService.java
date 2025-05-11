package com.mizilin.firstbot.service;

import com.mizilin.firstbot.entity.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class QuizCacheService {

    private final RedisTemplate<String, Quiz> redisTemplate;
    private static final String CACHE_PREFIX = "quiz:";

    @Autowired
    public QuizCacheService(RedisTemplate<String, Quiz> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheQuiz(Long quizId, Quiz quiz) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + quizId, quiz);
    }

    public Quiz getCachedQuiz(Long quizId) {
        return redisTemplate.opsForValue().get(CACHE_PREFIX + quizId);
    }

    public void deleteCachedQuiz(Long quizId) {
        redisTemplate.delete(CACHE_PREFIX + quizId);
    }
}


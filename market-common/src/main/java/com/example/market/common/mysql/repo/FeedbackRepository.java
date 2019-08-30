package com.example.market.common.mysql.repo;

import com.example.market.common.mysql.entity.Feedback;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
}

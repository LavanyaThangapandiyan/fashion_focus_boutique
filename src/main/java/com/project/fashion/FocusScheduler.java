package com.project.fashion;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.fashion.dao.UserDao;

@Component
@ConditionalOnProperty(value = "email", havingValue = "true", matchIfMissing = false)
public class FocusScheduler {
	Logger logger = LoggerFactory.getLogger(UserDao.class);
	   // 1000 * 60 * 2 = 2 mins
		@Scheduled(fixedDelayString = "12000")
		public void notifyMail() {
			LocalDateTime todayDateTime = LocalDateTime.now();
		    logger.info("notifymail :" + todayDateTime);
		}
	
		@Scheduled(cron = "0 */1 * * * *")
		public void notifyMailCron() {
			LocalDateTime todayDateTime = LocalDateTime.now();
		   logger.info("notifymailcron :" + todayDateTime);
		}	
}

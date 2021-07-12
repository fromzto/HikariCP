/*
 * Copyright (C) 2013, 2014 Brett Wooldridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.zaxxer.hikari.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.util.UtilityElf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.zaxxer.hikari.pool.TestElf.newHikariConfig;

/**
 * @author Martin Stříž (striz@raynet.cz)
 */
public class HouseKeeperCleanupTest
{

   private ScheduledThreadPoolExecutor executor;

   @BeforeEach
   public void before() throws Exception
   {
      ThreadFactory threadFactory = new UtilityElf.DefaultThreadFactory("global housekeeper", true);

      executor = new ScheduledThreadPoolExecutor(1, threadFactory, new ThreadPoolExecutor.DiscardPolicy());
      executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
      executor.setRemoveOnCancelPolicy(true);
   }

   @AfterEach
   public void after() throws Exception
   {
      executor.shutdown();
      executor.awaitTermination(5, TimeUnit.SECONDS);
   }

   @Test
   public void testHouseKeeperCleanupWithCustomExecutor() throws Exception
   {
      HikariConfig config = newHikariConfig();
      config.setMinimumIdle(0);
      config.setMaximumPoolSize(10);
      config.setInitializationFailTimeout(Long.MAX_VALUE);
      config.setConnectionTimeout(2500);
      config.setDataSourceClassName("com.zaxxer.hikari.mocks.StubDataSource");
      config.setScheduledExecutor(executor);

      Assertions.assertNotNull(executor);
      Assertions.assertNotNull(executor.getQueue());

      HikariConfig config2 = newHikariConfig();
      config.copyStateTo(config2);

      try (
         final HikariDataSource ds1 = new HikariDataSource(config);
         final HikariDataSource ds2 = new HikariDataSource(config2)
      ) {
         Assertions.assertEquals(2, executor.getQueue().size(), "Scheduled tasks count not as expected, ");
      }

      Assertions.assertEquals(0, executor.getQueue().size(), "Scheduled tasks count not as expected, ");
   }
}

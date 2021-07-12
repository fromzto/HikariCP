package com.zaxxer.hikari.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static com.zaxxer.hikari.pool.TestElf.newHikariConfig;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestSealedConfig
{
   @Test
   public void testSealed1()
   {
      HikariConfig config = newHikariConfig();
      config.setDataSourceClassName("com.zaxxer.hikari.mocks.StubDataSource");

      assertThrows(IllegalStateException.class, () -> {
         try (HikariDataSource ds = new HikariDataSource(config)) {
            ds.setDataSourceClassName("com.zaxxer.hikari.mocks.StubDataSource");
            Assertions.fail("Exception should have been thrown");
         }
      });
   }

   @Test
   public void testSealed2()
   {
      HikariDataSource ds = new HikariDataSource();
      ds.setDataSourceClassName("com.zaxxer.hikari.mocks.StubDataSource");

      assertThrows(IllegalStateException.class, () -> {
         try (HikariDataSource ignored = ds) {
            try (Connection ignored1 = ds.getConnection()) {
               ds.setDataSourceClassName("com.zaxxer.hikari.mocks.StubDataSource");
               Assertions.fail("Exception should have been thrown");
            }
         }
      });
   }

   @Test
   public void testSealed3()
   {
      HikariDataSource ds = new HikariDataSource();
      ds.setDataSourceClassName("com.zaxxer.hikari.mocks.StubDataSource");

      assertThrows(IllegalStateException.class, () -> {
         try (HikariDataSource ignored = ds) {
            try (Connection ignored1 = ds.getConnection()) {
               ds.setAutoCommit(false);
               Assertions.fail("Exception should have been thrown");
            }
         }
      });
   }

   @Test
   public void testSealedAccessibleMethods()
   {
      HikariConfig config = newHikariConfig();
      config.setDataSourceClassName("com.zaxxer.hikari.mocks.StubDataSource");

      try (HikariDataSource ds = new HikariDataSource(config)) {
         ds.setConnectionTimeout(5000);
         ds.setValidationTimeout(5000);
         ds.setIdleTimeout(30000);
         ds.setLeakDetectionThreshold(60000);
         ds.setMaxLifetime(1800000);
         ds.setMinimumIdle(5);
         ds.setMaximumPoolSize(8);
         ds.setPassword("password");
         ds.setUsername("username");
      }
   }
}

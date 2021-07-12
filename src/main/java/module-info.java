module com.zaxxer.hikari
{
   requires java.sql;
   requires java.management;
   requires java.naming;
   requires org.slf4j;
   requires static javassist;
   requires static com.codahale.metrics;
   requires static com.codahale.metrics.health;
   requires static org.hibernate.orm.core;
   requires static simpleclient;
   requires static micrometer.core;

   exports com.zaxxer.hikari;
   exports com.zaxxer.hikari.hibernate;
   exports com.zaxxer.hikari.metrics;
   exports com.zaxxer.hikari.metrics.dropwizard;
   exports com.zaxxer.hikari.metrics.micrometer;
   exports com.zaxxer.hikari.metrics.prometheus;
   exports com.zaxxer.hikari.pool;
   exports com.zaxxer.hikari.util;
}

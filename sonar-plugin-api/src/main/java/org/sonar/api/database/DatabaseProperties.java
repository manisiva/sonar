/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.api.database;

public interface DatabaseProperties {

  int MAX_TEXT_SIZE = 16777215;

  String PROP_URL = "sonar.jdbc.url";
  String PROP_URL_DEFAULT_VALUE = "jdbc:h2:tcp://localhost/sonar";
  String PROP_DRIVER = "sonar.jdbc.driverClassName";
  String PROP_DRIVER_DEFAULT_VALUE = "org.h2.Driver";
  String PROP_DRIVER_DEPRECATED = "sonar.jdbc.driver";
  String PROP_USER = "sonar.jdbc.username";
  String PROP_USER_DEPRECATED = "sonar.jdbc.user";
  String PROP_USER_DEFAULT_VALUE = "sonar";
  String PROP_PASSWORD = "sonar.jdbc.password";
  String PROP_PASSWORD_DEFAULT_VALUE = "sonar";
  String PROP_HIBERNATE_HBM2DLL = "sonar.jdbc.hibernate.hbm2ddl";
  String PROP_HIBERNATE_GENERATE_STATISTICS = "sonar.jdbc.hibernate.generate_statistics";
  String PROP_DIALECT = "sonar.jdbc.dialect";
  String PROP_HIBERNATE_DEFAULT_SCHEMA = "sonar.hibernate.default_schema";
  String PROP_EMBEDDED_DATA_DIR = "sonar.embeddedDatabase.dataDir";

  /**
   * @since 3.2
   */
  String PROP_EMBEDDED_PORT = "sonar.embeddedDatabase.port";

  /**
   * @since 3.2
   */
  String PROP_EMBEDDED_PORT_DEFAULT_VALUE = "9092";
}
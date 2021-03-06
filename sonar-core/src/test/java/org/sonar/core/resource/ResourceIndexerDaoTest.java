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
package org.sonar.core.resource;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Qualifiers;
import org.sonar.core.persistence.AbstractDaoTestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.number.OrderingComparisons.greaterThan;
import static org.junit.Assert.assertThat;

public class ResourceIndexerDaoTest extends AbstractDaoTestCase {

  private static ResourceIndexerDao dao;

  @Before
  public void createDao() {
    dao = new ResourceIndexerDao(getMyBatis());
  }

  @Test
  public void shouldIndexResource() {
    setupData("shouldIndexResource");

    dao.indexResource(10, "ZipUtils", "FIL", 8);

    checkTables("shouldIndexResource", new String[]{"id"}, "resource_index");
  }

  @Test
  public void shouldIndexProjects() {
    setupData("shouldIndexProjects");

    dao.indexProjects();

    checkTables("shouldIndexProjects", new String[]{"id"}, "resource_index");
  }

  @Test
  public void shouldIndexMultiModulesProject() {
    setupData("shouldIndexMultiModulesProject");

    dao.indexProject(1);

    checkTables("shouldIndexMultiModulesProject", new String[]{"id"}, "resource_index");
  }

  @Test
  public void shouldReindexProjectAfterRenaming() {
    setupData("shouldReindexProjectAfterRenaming");

    dao.indexProject(1);

    checkTables("shouldReindexProjectAfterRenaming", new String[]{"id"}, "resource_index");
  }

  @Test
  public void shouldNotIndexPackages() throws SQLException {
    setupData("shouldNotIndexPackages");

    dao.indexProject(1);

    Connection connection = getConnection().getConnection();
    ResultSet rs = null;
    try {
      // project
      rs = connection.createStatement().executeQuery("select count(resource_id) from resource_index where resource_id=1");
      rs.next();
      assertThat(rs.getInt(1), greaterThan(0));

      // directory
      rs = connection.createStatement().executeQuery("select count(resource_id) from resource_index where resource_id=2");
      rs.next();
      assertThat(rs.getInt(1), Is.is(0));

      // file
      rs = connection.createStatement().executeQuery("select count(resource_id) from resource_index where resource_id=3");
      rs.next();
      assertThat(rs.getInt(1), greaterThan(0));
    } finally {
      if (null != rs) {
        rs.close();
      }
      connection.close();
    }
  }

  @Test
  public void shouldNotIndexWhenTooShortName() {
    setupData("empty");

    dao.indexResource(10, "AB", Qualifiers.FILE, 3);

    checkTables("empty", new String[]{"id"}, "resource_index");
  }

  @Test
  public void shouldReindexResource() {
    setupData("shouldReindexResource");

    dao.indexResource(1, "New Struts", Qualifiers.PROJECT, 1);

    checkTables("shouldReindexResource", new String[]{"id"}, "resource_index");
  }

  @Test
  public void shouldNotReindexUnchangedResource() {
    setupData("shouldNotReindexUnchangedResource");

    dao.indexResource(1, "Struts", Qualifiers.PROJECT, 1);

    checkTables("shouldNotReindexUnchangedResource", new String[]{"id"}, "resource_index");
  }
}

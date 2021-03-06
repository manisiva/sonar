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
package org.sonar.server.platform;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.CoreProperties;
import org.sonar.api.config.Settings;
import org.sonar.api.platform.Server;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public final class ServerImpl extends Server {

  private String id;
  private String version;
  private final Date startedAt;
  private Settings settings;
  private final String manifest;

  public ServerImpl(Settings settings) {
    this(settings, "/META-INF/maven/org.codehaus.sonar/sonar-plugin-api/pom.properties");
  }

  @VisibleForTesting
  ServerImpl(Settings settings, String manifest) {
    this.settings = settings;
    this.startedAt = new Date();
    this.manifest = manifest;
  }

  public void start() {
    try {
      id = new SimpleDateFormat("yyyyMMddHHmmss").format(startedAt);
      version = loadVersionFromManifest(manifest);
      if (StringUtils.isBlank(version)) {
        throw new ServerStartException("Unknown Sonar version");
      }

    } catch (IOException e) {
      throw new ServerStartException("Can not load metadata", e);
    }
  }

  @Override
  public String getPermanentServerId() {
    return settings.getString(CoreProperties.PERMANENT_SERVER_ID);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public Date getStartedAt() {
    return startedAt;
  }

  private String loadVersionFromManifest(String pomFilename) throws IOException {
    InputStream pomFileStream = getClass().getResourceAsStream(pomFilename);
    try {
      return readVersion(pomFileStream);

    } finally {
      IOUtils.closeQuietly(pomFileStream);
    }
  }

  protected static String readVersion(InputStream pomFileStream) throws IOException {
    String result = null;
    if (pomFileStream != null) {
      Properties pomProp = new Properties();
      pomProp.load(pomFileStream);
      result = pomProp.getProperty("version");
    }
    return StringUtils.defaultIfEmpty(result, "");
  }

  @Override
  public String getURL() {
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ServerImpl other = (ServerImpl) o;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}

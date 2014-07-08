/*
 * Fortify Plugin for SonarQube
 * Copyright (C) 2014 Vivien HENRIET and SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.fortify.fvdl.element;

public class SourceLocation {
  private final String path;
  private final Integer line;

  public SourceLocation(String path, Integer line) {
    this.path = path;
    this.line = line;
  }

  public String getPath() {
    return this.path;
  }

  public Integer getLine() {
    return this.line;
  }

  @Override
  public String toString() {
    return "[SourceLocation path=" + this.path + ", line=" + this.line + "]";
  }
}
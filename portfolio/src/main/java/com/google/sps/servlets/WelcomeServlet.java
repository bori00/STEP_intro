// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List; 
import java.util.ArrayList;

/** Servlet that returns a random welcome text. */
@WebServlet("/welcome-data")
public class WelcomeServlet extends HttpServlet {
  private List<String> welcomeTexts;

  @Override
  public void init() {
      welcomeTexts = new ArrayList();
      welcomeTexts.add("Welcome");
      welcomeTexts.add("Isten hozott!");
      welcomeTexts.add("Bine ai venit!");
      welcomeTexts.add("Willkommen!");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println(getRandomWelcomeText());
  }

  private String getRandomWelcomeText() {
    int index = (int) (Math.random() * welcomeTexts.size());
    return welcomeTexts.get(index);
  }
}

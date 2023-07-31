package com.swave.urnr.user.service;

import com.swave.urnr.util.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserInProjectService {
    HttpResponse dropProject(HttpServletRequest request, Long projectId);
}

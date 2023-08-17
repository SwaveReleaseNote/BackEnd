package com.swave.urnr.user.service;

import com.swave.urnr.project.exception.NotAuthorizedException;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;

import javax.servlet.http.HttpServletRequest;

public interface UserInProjectService {
    HttpResponse dropProject(HttpServletRequest request, Long projectId);

    HttpResponse subscribeProject(HttpServletRequest request, Long projectId) throws NotAuthorizedException;

    UserRole getRole(HttpServletRequest request, Long projectId);
}

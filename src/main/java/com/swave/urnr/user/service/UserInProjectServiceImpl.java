package com.swave.urnr.user.service;

import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.util.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInProjectServiceImpl implements UserInProjectService{

    private final UserInProjectRepository userInProjectRepository;

    @Override
    @Transactional
    public HttpResponse dropProject(HttpServletRequest request, Long projectId) {

        Long userId = (Long)request.getAttribute("id");

        int drop = userInProjectRepository.dropProject(userId,projectId);
        userInProjectRepository.flush();
        return HttpResponse.builder()
                .message("User drop project")
                .description(drop +" complete")
                .build();
    }
}

package com.swave.urnr.user.service;

import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.http.HttpResponse;
import com.swave.urnr.util.type.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.swave.urnr.util.type.UserRole.None;
import static com.swave.urnr.util.type.UserRole.Subscriber;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInProjectServiceImpl implements UserInProjectService{

    private final UserInProjectRepository userInProjectRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;


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

    @Override
    public HttpResponse subscribeProject(HttpServletRequest request, Long projectId) {

        System.out.println(projectId);
        User user = userRepository.findById((Long)request.getAttribute("id")).orElse(null);

        Project project = projectRepository.findById(projectId).orElse(null);

        UserInProject userInProject = UserInProject.builder()
                .role(Subscriber)
                .user(user)
                .project(project)
                .build();

        userInProjectRepository.save(userInProject);
        userInProjectRepository.flush();

        return HttpResponse.builder()
                .message("User subscribe project")
                .description(userInProject.getId() +" complete")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserRole getRole(HttpServletRequest request, Long projectId) {
        UserInProject userInProject = userInProjectRepository.findByUser_IdAndProject_Id((Long)request.getAttribute("id"),projectId);

        UserRole role;
        if(userInProject==null){
            role = None;
        }else{
            role = userInProject.getRole();
        }

        return role;
    }
}

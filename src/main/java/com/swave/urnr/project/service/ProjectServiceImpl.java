package com.swave.urnr.project.service;


import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.ProjectListResponseDTO;
import com.swave.urnr.project.responsedto.ProjectContentResponseDto;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.type.UserRole;
import lombok.RequiredArgsConstructor;
import com.swave.urnr.util.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;

//0710 확인 CR
@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final UserInProjectRepository userInProjectRepository;

    private final UserRepository userRepository;

    private final ReleaseNoteRepository releaseNoteRepository;

    @Override
    public HttpResponse createProject(HttpServletRequest request, ProjectCreateRequestDTO projectCreateRequestDto) {
        //빌더로 프로젝트생성
        Project project = Project.builder()
                .name(projectCreateRequestDto.getProjectName())
                .description(projectCreateRequestDto.getDescription())
                .createDate(new Date())
                .build();

        log.info(request.toString());
        log.info(project.getDescription().toString());

        //유저리스트 받아서 설정
        project.setUserInProjectList(new ArrayList<>());
        log.info(projectCreateRequestDto.getUserId().toString());
        //projectRequestDto.getUserId()
        User user = userRepository.findById((Long)request.getAttribute("id")).orElse(null);
        //UserInProject.setUserInProject(new ArrayList<>());
        //builder를 이용한 userInProject 생성

        //유저인 프로젝트 생성
        //todo:워너비는 유저인 프로젝트를 크리에이트 하는 것이 아닌 프로젝트 생성시 자동으로 생성되게 하는 것이 아닌지?
        UserInProject userInProject = com.swave.urnr.user.domain.UserInProject.builder()
                .role(UserRole.Manager)
                .user(user)
                .project(project)
                .build();

        //리스트형식에서 삽입을 위한 list생성 빈칸생성은 아닌듯
        //유저의 프로젝트 리스트를 가져와서 추가해서 넣어줌 기존에 유저가 가입된 프로젝트 리스트
        ArrayList<UserInProject> list = new ArrayList<>(user.getUserInProjectList());

        //현재 프로젝트 추가
        list.add(userInProject);
        user.setUserInProjectList(list);

        //리스트를 새로 만들어서 프로젝트도 추가해줌
        list = new ArrayList<>();
        list.add(userInProject);

        //프로젝트에 인원추가?
        project.setUserInProjectList(list);
        //저장하고
        Project saveProject = projectRepository.save(project);

        UserInProject saveUserInProject = userInProjectRepository.save(userInProject);

        if(projectCreateRequestDto.getUsers().size()>=1) {
            for (Long userId : projectCreateRequestDto.getUsers()) {
                //유저인 프로젝트 생성
                UserInProject userInProject1 = new UserInProject();
                userInProject1.setRole(UserRole.Developer);
                userInProject1.setProject(project);

                User user1 = userRepository.findById(userId).get();
                //User user1 = userRepository.findById(userInProject1.getId()).get();
                userInProject1.setUser(user1);
                userInProjectRepository.save(userInProject1);

                //유저생성
                List<UserInProject> userInProjectList = user.getUserInProjectList();
                userInProjectList.add(userInProject);
                user.setUserInProjectList(userInProjectList);
                userRepository.flush();

                userInProjectRepository.flush();

                project.getUserInProjectList().add(userInProject);
            }
        }
        //저장하고
        User saveUser = userRepository.save(user);
        //flush로 반영한다.
        userRepository.flush();
        projectRepository.flush();
        userInProjectRepository.flush();


        //return project.toString();
        return HttpResponse.builder()
                .message("Project Created")
                .description("Project Id "+ project.getId()+" created")
                .build();
    }

    //requestGetAttribute
    //서블렛
    /*@Override
    public String updateUsers(ProjectRequestDto projectRequestDto) {
        //프로젝트를 불러와서
        //리스트 받아서
        //유저인리스트 와
        //유저에 매핑

        //모든객체 싹다 호출
        //Project project = projectRepository.findById(projectRequestDto.getId()).orElse(null);
        //List<UserInProject> teamMembers = new ArrayList<>(project.getUserInProjectList());


        for(Long userId : projectRequestDto.getUsers() )
        {
            UserInProject userInProject = new UserInProject();
            userInProject.setRole(UserRole.Developer);
            userInProject.setProject(project);

            User user = userRepository.findById(userId).get();
            //User user1 = userRepository.findById(userInProject1.getId()).get();
            userInProject.setUser(user);
            userInProjectRepository.save(userInProject);
            List<UserInProject> userInProjectList = user.getUserInProjectList();
            userInProjectList.add(userInProject);
            user.setUserInProjectList(userInProjectList);
            userRepository.flush();

            project.getUserInProjectList().add(userInProject);
        }
        projectRepository.flush();
        return null;
    }*/

    //최신 릴리즈노트 이름 가져오기
    @Override
    public List<ProjectListResponseDTO> loadProjectList(HttpServletRequest request) {
        List<ProjectListResponseDTO> projectList = new ArrayList<>();
        List<UserInProject> userInProjectList = userInProjectRepository.findByUser_Id((Long)request.getAttribute("id"));

        for(UserInProject userInProject: userInProjectList){
            Project project = projectRepository.findById(userInProject.getProject().getId())
                    .orElseThrow(NoSuchFieldError::new);
            List<ReleaseNote> releaseNoteList = releaseNoteRepository.findByProject_Id(project.getId());
            int count = userInProjectRepository.countMember(project.getId());
            String version = releaseNoteRepository.latestReleseNote((Long)request.getAttribute("id"),project.getId());
            log.info(version);
            System.out.println(version+(Long)request.getAttribute("id")+project.getId());
            projectList.add(new ProjectListResponseDTO(project.getId(),userInProject.getRole(),project.getName(),project.getDescription(),project.getCreateDate(),count,version));
        }
        /*
        List<ProjectRequestDto> loadAll = new ArrayList<>();
        for (Project project : projectRequestDtos){
            System.out.println(project.getName());
            loadAll.add(new ProjectRequestDto(project.getId(),project.getName(),project.getDescription(),project.getCreateDate()));
        }*/
        return projectList;
    }

    @Override
    public ProjectContentResponseDto loadProject(Long projectId) {
        ProjectContentResponseDto getproject = new ProjectContentResponseDto();
        Project project = projectRepository.findById(projectId).get();
        getproject.setId(project.getId());
        getproject.setName(project.getName());
        getproject.setDescription(project.getDescription());
        getproject.setCreateDate(project.getCreateDate());
        /*
        List<ProjectRequestDto> loadAll = new ArrayList<>();
        for (Project project : projectRequestDtos){
            System.out.println(project.getName());
            loadAll.add(new ProjectRequestDto(project.getId(),project.getName(),project.getDescription(),project.getCreateDate()));
        }*/
        return getproject;
    }

    @Override
    @Transactional
    public ProjectUpdateRequestDTO updateProject(ProjectUpdateRequestDTO projectUpdateRequestDto) {

        Project project = projectRepository.findById(projectUpdateRequestDto.getId()).orElseThrow(null);

        project.setName(projectUpdateRequestDto.getName());
        project.setDescription(projectUpdateRequestDto.getDescription());
        //project.setUserInProjectList(projectUpdateRequestDto.getUsers());
        //기존에있는 리스트를 가져와서
        //프로젝트에 있는 유저리스트를 불러와서 삭제
        //querydsl 사용하기


        for (Long deleteUserId : projectUpdateRequestDto.getDeleteUsers()){
            int delete = userInProjectRepository.deleteUser(projectUpdateRequestDto.getId(),deleteUserId);
            userInProjectRepository.flush();
        }

        for (Long userId : projectUpdateRequestDto.getUpdateUsers()){
            UserInProject userInProject = new UserInProject();
            userInProject.setRole(UserRole.Developer);
            userInProject.setProject(project);

            User user = userRepository.findById(userId).get();
            userInProject.setUser(user);
            userInProjectRepository.save(userInProject);

            List<UserInProject> userInProjectList = user.getUserInProjectList();
            userInProjectList.add(userInProject);
            user.setUserInProjectList(userInProjectList);
            userRepository.flush();
            userInProjectRepository.flush();
            project.getUserInProjectList().add(userInProject);
        }
        projectRepository.flush();
        userInProjectRepository.flush();

        return projectUpdateRequestDto;
    }

    @Override
    public HttpResponse deleteProject(Long projectId) {
        List<UserInProject> userInProjectList = userInProjectRepository.findByProject_Id(projectId);
        for(UserInProject userInProject:userInProjectList) {
            userInProjectRepository.delete(userInProject);
        }
        projectRepository.deleteById(projectId);
        return HttpResponse.builder()
                .message("Project Created")
                .description("Project Id "+ projectId +" deleted")
                .build();
    }
    //삭제 소프트들리트를 믿어보자


}

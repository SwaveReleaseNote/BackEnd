package com.swave.releasenotesharesystem.Project.service;

import com.swave.releasenotesharesystem.Project.domain.Project;
import com.swave.releasenotesharesystem.Project.repository.ProjectRepository;
import com.swave.releasenotesharesystem.Project.requestDto.ProjectRequestDto;
import com.swave.releasenotesharesystem.Project.responseDto.loadAllProjectResponseDto;
import com.swave.releasenotesharesystem.Project.responseDto.loadOneProjectResponseDto;
import com.swave.releasenotesharesystem.User.domain.User;
import com.swave.releasenotesharesystem.User.domain.UserInProject;
import com.swave.releasenotesharesystem.User.repository.UserInProjectRepository;
import com.swave.releasenotesharesystem.User.repository.UserRepository;
import com.swave.releasenotesharesystem.Util.type.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;

//0710 확인 CR
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserInProjectRepository userInProjectRepository;

    @Autowired
    private UserRepository userRepository;



    @Override
    public String createProject(ProjectRequestDto projectRequestDto) {
        //빌더로 프로젝트생성
        //todo : 빌더를 잘 만지면 한줄로 가능하다.
        Project project = Project.builder()
                .name(projectRequestDto.getName())
                .description(projectRequestDto.getDescription())
                .createDate(new Date())
                .build();

        log.info(project.getDescription().toString());

        //유저리스트 받아서 설정
        project.setUserInProjectList(new ArrayList<>());
        log.info(projectRequestDto.getUserId().toString());
        User user = userRepository.findById(projectRequestDto.getUserId()).orElse(null);
        //UserInProject.setUserInProject(new ArrayList<>());
        //builder를 이용한 userInProject 생성

        //유저인 프로젝트 생성
        //todo:워너비는 유저인 프로젝트를 크리에이트 하는 것이 아닌 프로젝트 생성시 자동으로 생성되게 하는 것이 아닌지?
        UserInProject userInProject = UserInProject.builder()
                .role(UserRole.Manager)
                .user(user)
                .project(project)
                .build();

        //리스트형식에서 삽입을 위한 list생성 빈칸생성은 아닌듯
        //유저의 프로젝트 리스트를 가져와서 추가해서 넣어줌 기존에 유저가 가입된 프로젝트 리스트
        ArrayList<UserInProject> list = new ArrayList<>(user.getUserInProjectList());

        //현재 프로젝트 추가
        //todo : 따로 만든 이유가?
        list.add(userInProject);
        user.setUserInProjectList(list);

        //리스트를 새로 만들어서 프로젝트도 추가해줌
        list = new ArrayList<>();
        list.add(userInProject);

        //프로젝트에 인원추가?
        project.setUserInProjectList(list);
        //저장하고
        Project saveProject = projectRepository.save(project);

        for(Long userId : projectRequestDto.getUsers())
            {
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
        //저장하고
        User saveUser = userRepository.save(user);
        UserInProject saveUserInProject = userInProjectRepository.save(userInProject);
        //flush로 반영한다.
        userRepository.flush();
        projectRepository.flush();
        userInProjectRepository.flush();


        return project.toString();
    }

    @Override
    public String updateUsers(ProjectRequestDto projectRequestDto) {
        //프로젝트를 불러와서
        //리스트 받아서
        //유저인리스트 와
        //유저에 매핑

        //모든객체 싹다 호출
        Project project = projectRepository.findById(projectRequestDto.getId()).orElse(null);
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
    }

    @Override
    public List<loadAllProjectResponseDto> loadProjectList(Long userId) {
        //여기 DTO 왜 못쓰냐 쓰쥬?
        List<loadAllProjectResponseDto> projectList = new ArrayList<>();
        List<UserInProject> userInProjectList = userInProjectRepository.findByUser_Id(userId);
        for(UserInProject userInProject: userInProjectList){
            Project project = projectRepository.findById(userInProject.getProject().getId())
                    .orElseThrow(NoSuchFieldError::new);
            projectList.add(new loadAllProjectResponseDto(project.getId(),userInProject.getRole(),project.getName(),project.getDescription(),project.getCreateDate()));
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
    public loadOneProjectResponseDto loadProject(Long projectId) {
        loadOneProjectResponseDto getproject = new loadOneProjectResponseDto();
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


}

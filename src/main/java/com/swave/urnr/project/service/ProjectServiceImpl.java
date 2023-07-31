package com.swave.urnr.project.service;


import com.swave.urnr.project.domain.Project;
import com.swave.urnr.project.repository.ProjectRepository;
import com.swave.urnr.project.requestdto.ProjectCreateRequestDTO;
import com.swave.urnr.project.requestdto.ProjectKeywordRequestContentDTO;
import com.swave.urnr.project.requestdto.ProjectUpdateRequestDTO;
import com.swave.urnr.project.responsedto.*;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.domain.UserInProject;
import com.swave.urnr.user.repository.UserInProjectRepository;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.responsedto.UserMemberInfoResponseDTO;
import com.swave.urnr.util.type.UserRole;
import lombok.RequiredArgsConstructor;
import com.swave.urnr.util.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

;import static com.swave.urnr.project.domain.Project.makeProjectSearchListResponseDTOList;
import static com.swave.urnr.util.type.UserRole.Manager;

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
                .userInProjectList(new ArrayList<>())
                .build();

        log.info(request.toString());
        log.info(project.getDescription().toString());

        //유저리스트 받아서 설정
        //project.setUserInProjectList(new ArrayList<>()); //빌더에 넣어보기
        //log.info(projectCreateRequestDto.getUserId().toString());
        //projectRequestDto.getUserId()
        User user = userRepository.findById((Long)request.getAttribute("id")).orElse(null);
        //user대신에 명확한 변수명 사용 manager?
        
        //UserInProject.setUserInProject(new ArrayList<>());
        //builder를 이용한 userInProject 생성

        //유저인 프로젝트 생성
        //todo:워너비는 유저인 프로젝트를 크리에이트 하는 것이 아닌 프로젝트 생성시 자동으로 생성되게 하는 것이 아닌지?
        UserInProject userInProject = UserInProject.builder()
                .role(Manager)
                .user(user)
                .project(project)
                .build();

        //리스트형식에서 삽입을 위한 list생성 빈칸생성은 아닌듯
        //유저의 프로젝트 리스트를 가져와서 추가해서 넣어줌 기존에 유저가 가입된 프로젝트 리스트
        ArrayList<UserInProject> list = new ArrayList<>(user.getUserInProjectList());
        //리스트는 리스트로

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
            //List<ReleaseNote> releaseNoteList = releaseNoteRepository.findByProject_Id(project.getId());
            int count = userInProjectRepository.countMember(project.getId());
            String version = releaseNoteRepository.latestReleseNote(project.getId());
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
    public ProjectContentResponseDTO loadProject(Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        ProjectContentResponseDTO getproject = ProjectContentResponseDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createDate(project.getCreateDate())
                .build();
        /*

        getproject.setId(project.getId());
        getproject.setName(project.getName());
        getproject.setDescription(project.getDescription());
        getproject.setCreateDate(project.getCreateDate());*/
        /*
        List<ProjectRequestDto> loadAll = new ArrayList<>();
        for (Project project : projectRequestDtos){
            System.out.println(project.getName());
            loadAll.add(new ProjectRequestDto(project.getId(),project.getName(),project.getDescription(),project.getCreateDate()));
        }*/
        return getproject;
    }

    @Override
    public ProjectManagementContentResponseDTO loadManagementProject(HttpServletRequest request,Long projectId) {
        //유저가 해당 프로젝트 멤버인지 확인 UserInList확인
        //유저가 관리자인지 확인 UserInList확인
        Project project = projectRepository.findById(projectId).get();
        User user = userRepository.findById((Long)request.getAttribute("id")).orElse(null);
        List<UserMemberInfoResponseDTO> getMembers = userInProjectRepository.getMembers(projectId);
        log.info(getMembers.toString());
        System.out.println(getMembers);

        //널에러 즉 팀원이 없을 때 해결하기
        //log.info(getMembers.get(0).getUser_Name());
        //유저인프로젝트 유저 조인 유저인프로젝트에나온 프로젝트 아이디로 유저 셀렉트트
        ProjectManagementContentResponseDTO projectManagementContentResponseDTO = ProjectManagementContentResponseDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createDate(project.getCreateDate())
                .managerId((Long)request.getAttribute("id"))
                .managerName(user.getUsername())
                .managerDepartment(user.getDepartment())
                .teamMembers(getMembers)
                .build();

        return projectManagementContentResponseDTO;
    }

    @Override
    public ProjectManagementContentResponseDTO loadManagementProjectJPA(HttpServletRequest request, Long projectId){
        Project project = projectRepository.findById(projectId).get();
        User user = userRepository.findById((Long)request.getAttribute("id")).orElse(null);
        List<UserMemberInfoResponseDTO> getMembers  = new ArrayList<>();

        for(UserInProject userInProject:project.getUserInProjectList()){
            UserMemberInfoResponseDTO userMemberInfoResponseDTO = new UserMemberInfoResponseDTO();
            userMemberInfoResponseDTO.setUserId(userInProject.getId());
            userMemberInfoResponseDTO.setUsername(userInProject.getUser().getUsername());
            userMemberInfoResponseDTO.setUserDepartment(userInProject.getUser().getDepartment());
            getMembers.add(userMemberInfoResponseDTO);
            //getMembers.add(userInProject.getId(),userInProject.getUser().getUsername(),userInProject.getUser().getDepartment());
        }
        ProjectManagementContentResponseDTO projectManagementContentResponseDTO = ProjectManagementContentResponseDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createDate(project.getCreateDate())
                .managerId((Long)request.getAttribute("id"))
                .managerName(user.getUsername())
                .managerDepartment(user.getDepartment())
                .teamMembers(getMembers)
                .build();
        return projectManagementContentResponseDTO;
    }


    //관리자 변경하기 의외로 쉽게 될수도?
    @Override
    @Transactional
    public ProjectUpdateRequestDTO updateProject(Long projectId, ProjectUpdateRequestDTO projectUpdateRequestDto) {

        System.out.println(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(null);

        project.setName(projectUpdateRequestDto.getName());
        project.setDescription(projectUpdateRequestDto.getDescription());
        
        //dto에 유저 추가하면 되는데 관리자를 개발자리스트나 구독자에선 빼야함

        //project.setUserInProjectList(projectUpdateRequestDto.getUsers());
        //기존에있는 리스트를 가져와서
        //프로젝트에 있는 유저리스트를 불러와서 삭제
        //querydsl 사용하기


        for (Long deleteUserId : projectUpdateRequestDto.getDeleteUsers()){
            int delete = userInProjectRepository.deleteUser(deleteUserId,projectId);
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
                .message("Project Deleted")
                .description("Project Id "+ projectId +" deleted")
                .build();

    }

    @Override
    public ProjectSearchResultListResponseDTO searchProject(ProjectKeywordRequestContentDTO projectKeywordRequestContentDTO) throws UnsupportedEncodingException {
        String keyword = projectKeywordRequestContentDTO.getKeyword();
        ProjectSearchResultListResponseDTO projectSearchResultListResponseDTO = new ProjectSearchResultListResponseDTO();

        //제목 검색
        List<ProjectSearchListResponseDTO> projectSearchByNameListResponseDTOList = projectRepository.searchProjectByName(keyword);
        projectSearchResultListResponseDTO.setTitleSearch(makeProjectSearchListResponseDTOList(projectSearchByNameListResponseDTOList));

        //descriprion 검색
        List<ProjectSearchListResponseDTO> projectSearchByDescriptionListResponseDTOList = projectRepository.searchProjectByDescription(keyword);
        projectSearchResultListResponseDTO.setDescriptionSearch(makeProjectSearchListResponseDTOList(projectSearchByDescriptionListResponseDTOList));


        //Manager 검색
        List<ProjectSearchListResponseDTO> projectSearchByManagerListResponseDTOList = projectRepository.searchProjectByManager(keyword);
        projectSearchResultListResponseDTO.setManagerSearch(makeProjectSearchListResponseDTOList(projectSearchByManagerListResponseDTOList));


        //Developer
        List<ProjectSearchListResponseDTO> projectSearchByDeveloperListResponseDTOList = projectRepository.searchProjectByDeveloper(keyword);
        projectSearchResultListResponseDTO.setDeveloperSearch(makeProjectSearchListResponseDTOList(projectSearchByDeveloperListResponseDTOList));

        return projectSearchResultListResponseDTO;
    }

    @Override
    public List<ProjectUserCheckDTO> checkUser(Long projectId) {
        List<ProjectUserCheckDTO> projectUserCheckList = new ArrayList<>();
        List<UserMemberInfoResponseDTO> getMemberLists = userInProjectRepository.getLoginMembers(projectId);
        log.info(String.valueOf(getMemberLists.get(0)));
        for (UserMemberInfoResponseDTO getMember : getMemberLists) {
            User user = userRepository.getReferenceById(getMember.getUserId());
            ProjectUserCheckDTO projectUserCheck = ProjectUserCheckDTO.builder()
                    .memberId(user.getId())
                    .memberName(user.getUsername())
                    .isOnline(user.isOnline())
                    .build();
            projectUserCheckList.add(projectUserCheck);

        }
        return projectUserCheckList;
    }


}



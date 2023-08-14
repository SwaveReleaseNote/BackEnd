package com.swave.urnr.opensearch.service;

import com.swave.urnr.opensearch.domain.ProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserInProjectOpenSearch;
import com.swave.urnr.opensearch.domain.UserOpenSearch;
import com.swave.urnr.opensearch.repository.OpenSearchProjectRepository;
import com.swave.urnr.project.responsedto.ProjectSearchListResponseDTO;
import com.swave.urnr.project.responsedto.ProjectSearchResultListResponseDTO;
import com.swave.urnr.util.type.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.swave.urnr.project.domain.Project.makeProjectSearchListResponseDTOList;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchServiceImpl implements OpenSearchService{
    private final OpenSearchProjectRepository openSearchProjectRepository;

    private final ElasticsearchOperations elasticsearchOperations;
    /*@Override
    public List<ProjectOpenSearch> searchProjectByDescription(String description){
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .should(QueryBuilders.matchQuery("description", description))
                                .should(QueryBuilders.matchQuery("description.nori", description))
                                .should(QueryBuilders.matchQuery("description.ngram", description)))
                .withMinScore(0.3F)
                .build();

        SearchHits<ProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, ProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }*/

    @Override
    public List<ProjectOpenSearch> searchProjectByProjectName(String name) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("is_deleted", false))
                        .should(QueryBuilders.matchQuery("project_name", name))
                        .should(QueryBuilders.matchQuery("project_name.nori", name))
                        .should(QueryBuilders.matchQuery("project_name.ngram", name)))
                .withMinScore(0.3F)
                .build();

        SearchHits<ProjectOpenSearch> test =  elasticsearchOperations.search(searchQuery, ProjectOpenSearch.class);

        return new ArrayList<>(test.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    private List<ProjectOpenSearch> searchProjectByProjectId(Long projectId) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .should(QueryBuilders.matchQuery("project_id", projectId)))
                .withMinScore(0.3F)
                .build();

        SearchHits<ProjectOpenSearch> test =  elasticsearchOperations.search(searchQuery, ProjectOpenSearch.class);

        return new ArrayList<>(test.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    @Override
    public List<UserInProjectOpenSearch> searchUserInProjectByRole(Long roleId) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("role", roleId)))
                .build();

        SearchHits<UserInProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserInProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    @Override
    public List<UserInProjectOpenSearch> searchUserInProjectByProjectId(Long projectId) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("project_id", projectId)))
                .build();

        SearchHits<UserInProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserInProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    private List<UserInProjectOpenSearch> searchUserInProjectByUserId(Long userId) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("user_id", userId)))
                .build();

        SearchHits<UserInProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserInProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    private List<UserInProjectOpenSearch> searchUserInProjectByUserIdforManager(Long userId) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("user_id", userId))
                                .must(QueryBuilders.matchQuery("role", 2)))
                .build();

        SearchHits<UserInProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserInProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    private List<UserInProjectOpenSearch> searchUserInProjectByUserIdforDeveloper(Long userId) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("user_id", userId))
                                .must(QueryBuilders.matchQuery("role", 1)))
                .build();

        SearchHits<UserInProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserInProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }
    //프로잭트 아이디로 검색하기 그걸 받아서 유저 검색하기 그래서 유저추

    @Override
    public List<UserOpenSearch> searchUserById(Long userId) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("user_id", userId)))
                .build();

        SearchHits<UserOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }

    @Override
    public List<UserOpenSearch> searchUserByName(String name) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .must(QueryBuilders.matchQuery("username", name)))
                .build();

        SearchHits<UserOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, UserOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());
    }



    @Override
    public List<ProjectOpenSearch> searchProjectByDescription(String keyword) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("is_deleted", false))
                                .should(QueryBuilders.matchQuery("description", keyword))
                                .should(QueryBuilders.matchQuery("description.nori", keyword))
                                .should(QueryBuilders.matchQuery("description.ngram", keyword)))
                .withMinScore(0.3F)
                .build();

        SearchHits<ProjectOpenSearch> searchResult =  elasticsearchOperations.search(searchQuery, ProjectOpenSearch.class);

        return new ArrayList<>(searchResult.stream().map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList());

    }



    @Override
    public ProjectSearchResultListResponseDTO searchProject(String keyword) {
        ProjectSearchResultListResponseDTO projectSearchResultListResponseDTO = new ProjectSearchResultListResponseDTO();

        UserRole role = null;
        List<ProjectSearchListResponseDTO> projectSearchByNameListResponseDTOList =  new ArrayList<>();
        List<ProjectOpenSearch> projectOpenNameSearchList = searchProjectByProjectName(keyword);

        //제목 검색
        ProjectSearchListResponseDTO projectSearchListResponseDTO = new ProjectSearchListResponseDTO();
            for(ProjectOpenSearch projectOpenSearch : projectOpenNameSearchList){
                List<UserInProjectOpenSearch> userInProjectOpenSearchList = searchUserInProjectByProjectId(projectOpenSearch.getId());

                for(UserInProjectOpenSearch userInProjectOpenSearch : userInProjectOpenSearchList){
                    List<UserOpenSearch> userOpenSearcheList = searchUserById(userInProjectOpenSearch.getUser_id());

                    for(UserOpenSearch userOpenSearch : userOpenSearcheList ){
                        projectSearchListResponseDTO.setId(projectOpenSearch.getId());
                        projectSearchListResponseDTO.setName(projectOpenSearch.getProjectName());
                        projectSearchListResponseDTO.setDescription(projectOpenSearch.getDescription());
                        projectSearchListResponseDTO.setCreateDate(projectOpenSearch.getCreateDate());
                        projectSearchListResponseDTO.setUserId(userOpenSearch.getId());
                        projectSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                        projectSearchListResponseDTO.setUserName(userOpenSearch.getUsername());
                        if(userInProjectOpenSearch.getRole()==0){
                            role = UserRole.Subscriber;
                        }else if(userInProjectOpenSearch.getRole()==1){
                            role = UserRole.Developer;
                        }else if(userInProjectOpenSearch.getRole()==2){
                            role = UserRole.Manager;
                        }
                        projectSearchListResponseDTO.setUserRole(role);

                        projectSearchByNameListResponseDTOList.add(projectSearchListResponseDTO);

                    }

                }

            }
            //디스크립션 검색
        List<ProjectOpenSearch> projectOpenDescriptionSearchList = searchProjectByDescription(keyword);


        List<ProjectSearchListResponseDTO> projectSearchByDescriptionListResponseDTOList =  new ArrayList<>();

        ProjectSearchListResponseDTO projectDescriptionSearchListResponseDTO = new ProjectSearchListResponseDTO();
        for(ProjectOpenSearch projectOpenSearch : projectOpenDescriptionSearchList){
            List<UserInProjectOpenSearch> userInProjectOpenSearchList = searchUserInProjectByProjectId(projectOpenSearch.getId());

            for(UserInProjectOpenSearch userInProjectOpenSearch : userInProjectOpenSearchList){
                List<UserOpenSearch> userOpenSearcheList = searchUserById(userInProjectOpenSearch.getUser_id());

                for(UserOpenSearch userOpenSearch : userOpenSearcheList ){
                    projectDescriptionSearchListResponseDTO.setId(projectOpenSearch.getId());
                    projectDescriptionSearchListResponseDTO.setName(projectOpenSearch.getProjectName());
                    projectDescriptionSearchListResponseDTO.setDescription(projectOpenSearch.getDescription());
                    projectDescriptionSearchListResponseDTO.setCreateDate(projectOpenSearch.getCreateDate());
                    projectDescriptionSearchListResponseDTO.setUserId(userOpenSearch.getId());
                    projectDescriptionSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                    projectDescriptionSearchListResponseDTO.setUserName(userOpenSearch.getUsername());
                    if(userInProjectOpenSearch.getRole()==0){
                        role = UserRole.Subscriber;
                    }else if(userInProjectOpenSearch.getRole()==1){
                        role = UserRole.Developer;
                    }else if(userInProjectOpenSearch.getRole()==2){
                        role = UserRole.Manager;
                    }
                    projectDescriptionSearchListResponseDTO.setUserRole(role);

                    projectSearchByDescriptionListResponseDTOList.add(projectDescriptionSearchListResponseDTO);

                }

            }

        }
        //매니저 이름 검색 유인아
        //이름으로 유저검색 searchUsername
        //유저가 들어간 프로젝트 아이디 유인프
        //프로젝트 아이디로 프로젝트 프로젝트 아이디로 검색
        //유인프로 유저들 가져오기
        //프로젝트에 들어간 유저검색
        List<UserOpenSearch> projectOpenManagerNameSearchList = searchUserByName(keyword);


        List<ProjectSearchListResponseDTO> projectSearchByManagerListResponseDTOList =  new ArrayList<>();

        ProjectSearchListResponseDTO projectManagerSearchListResponseDTO = new ProjectSearchListResponseDTO();
        for(UserOpenSearch userOpenManagerSearch : projectOpenManagerNameSearchList){

            List<UserInProjectOpenSearch> userInProjectOpenSearchList = searchUserInProjectByUserIdforManager(userOpenManagerSearch.getId());

            for(UserInProjectOpenSearch userInProjectOpenSearch : userInProjectOpenSearchList) {

                List<ProjectOpenSearch> projectOpenSearch = searchProjectByProjectId(userInProjectOpenSearch.getProject_id());

                for (ProjectOpenSearch projectOpenProjectSearch : projectOpenSearch) {
                    List<UserInProjectOpenSearch> userInProjectOpenSearchList2 = searchUserInProjectByProjectId(projectOpenProjectSearch.getId());

                    for (UserInProjectOpenSearch userInProjectOpenSearch2 : userInProjectOpenSearchList2) {
                        List<UserOpenSearch> userOpenSearcheList = searchUserById(userInProjectOpenSearch2.getUser_id());

                        for (UserOpenSearch userOpenSearch : userOpenSearcheList) {
                            projectManagerSearchListResponseDTO.setId(projectOpenProjectSearch.getId());
                            projectManagerSearchListResponseDTO.setName(projectOpenProjectSearch.getProjectName());
                            projectManagerSearchListResponseDTO.setDescription(projectOpenProjectSearch.getDescription());
                            projectManagerSearchListResponseDTO.setCreateDate(projectOpenProjectSearch.getCreateDate());
                            projectManagerSearchListResponseDTO.setUserId(userOpenSearch.getId());
                            projectManagerSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                            projectManagerSearchListResponseDTO.setUserName(userOpenSearch.getUsername());
                            if (userInProjectOpenSearch2.getRole() == 0) {
                                role = UserRole.Subscriber;
                            } else if (userInProjectOpenSearch2.getRole() == 1) {
                                role = UserRole.Developer;
                            } else if (userInProjectOpenSearch2.getRole() == 2) {
                                role = UserRole.Manager;
                            }
                            projectManagerSearchListResponseDTO.setUserRole(role);

                            projectSearchByManagerListResponseDTOList.add(projectManagerSearchListResponseDTO);

                        }

                    }
                }
            }

        }
        //개발자 이름 검색
        List<UserOpenSearch> projectOpenDeveloperNameSearchList = searchUserByName(keyword);


        List<ProjectSearchListResponseDTO> projectSearchByDeveloperListResponseDTOList =  new ArrayList<>();

        ProjectSearchListResponseDTO projectDeveloperSearchListResponseDTO = new ProjectSearchListResponseDTO();
        for(UserOpenSearch userOpenManagerSearch : projectOpenDeveloperNameSearchList){

            List<UserInProjectOpenSearch> userInProjectOpenSearchList = searchUserInProjectByUserIdforDeveloper(userOpenManagerSearch.getId());

            for(UserInProjectOpenSearch userInProjectOpenSearch : userInProjectOpenSearchList) {

                List<ProjectOpenSearch> projectOpenSearch = searchProjectByProjectId(userInProjectOpenSearch.getProject_id());

                for (ProjectOpenSearch projectOpenProjectSearch : projectOpenSearch) {
                    List<UserInProjectOpenSearch> userInProjectOpenSearchList2 = searchUserInProjectByProjectId(projectOpenProjectSearch.getId());

                    for (UserInProjectOpenSearch userInProjectOpenSearch2 : userInProjectOpenSearchList2) {
                        List<UserOpenSearch> userOpenSearcheList = searchUserById(userInProjectOpenSearch2.getUser_id());

                        for (UserOpenSearch userOpenSearch : userOpenSearcheList) {
                            projectDeveloperSearchListResponseDTO.setId(projectOpenProjectSearch.getId());
                            projectDeveloperSearchListResponseDTO.setName(projectOpenProjectSearch.getProjectName());
                            projectDeveloperSearchListResponseDTO.setDescription(projectOpenProjectSearch.getDescription());
                            projectDeveloperSearchListResponseDTO.setCreateDate(projectOpenProjectSearch.getCreateDate());
                            projectDeveloperSearchListResponseDTO.setUserId(userOpenSearch.getId());
                            projectDeveloperSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                            projectDeveloperSearchListResponseDTO.setUserName(userOpenSearch.getUsername());
                            if (userInProjectOpenSearch2.getRole() == 0) {
                                role = UserRole.Subscriber;
                            } else if (userInProjectOpenSearch2.getRole() == 1) {
                                role = UserRole.Developer;
                            } else if (userInProjectOpenSearch2.getRole() == 2) {
                                role = UserRole.Manager;
                            }
                            projectDeveloperSearchListResponseDTO.setUserRole(role);

                            projectSearchByDeveloperListResponseDTOList.add(projectDeveloperSearchListResponseDTO);

                        }

                    }
                }
            }

        }

        projectSearchResultListResponseDTO.setTitleSearch(makeProjectSearchListResponseDTOList(projectSearchByNameListResponseDTOList));
        projectSearchResultListResponseDTO.setDescriptionSearch(makeProjectSearchListResponseDTOList(projectSearchByDescriptionListResponseDTOList));
        projectSearchResultListResponseDTO.setManagerSearch(makeProjectSearchListResponseDTOList(projectSearchByManagerListResponseDTOList));
        projectSearchResultListResponseDTO.setDeveloperSearch(makeProjectSearchListResponseDTOList(projectSearchByDeveloperListResponseDTOList));


        return projectSearchResultListResponseDTO;
    }




}

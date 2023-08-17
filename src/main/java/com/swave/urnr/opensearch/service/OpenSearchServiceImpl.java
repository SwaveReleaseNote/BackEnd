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
import java.util.stream.Collectors;

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
                                .must(QueryBuilders.matchQuery("username", name))
                                .must(QueryBuilders.matchQuery("username.nori", name))
                                .must(QueryBuilders.matchQuery("username.ngram", name)))

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

        List<ProjectOpenSearch> projectOpenNameSearchList = searchProjectByProjectName(keyword);

        List<ProjectSearchListResponseDTO> projectSearchByNameListResponseDTOList = projectOpenNameSearchList.stream()
                .flatMap(projectOpenSearch -> searchUserInProjectByProjectId(projectOpenSearch.getId()).stream()
                        .flatMap(userInProjectOpenSearch -> searchUserById(userInProjectOpenSearch.getUser_id()).stream()
                                .map(userOpenSearch -> {
                                    ProjectSearchListResponseDTO projectSearchListResponseDTO = new ProjectSearchListResponseDTO();
                                    projectSearchListResponseDTO.setId(projectOpenSearch.getId());
                                    projectSearchListResponseDTO.setName(projectOpenSearch.getProjectName());
                                    projectSearchListResponseDTO.setDescription(projectOpenSearch.getDescription());
                                    projectSearchListResponseDTO.setCreateDate(projectOpenSearch.getCreateDate());
                                    projectSearchListResponseDTO.setUserId(userOpenSearch.getId());
                                    projectSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                                    projectSearchListResponseDTO.setUserName(userOpenSearch.getUsername());

                                    UserRole role = switch (userInProjectOpenSearch.getRole().intValue()) {
                                        case 0 -> UserRole.Subscriber;
                                        case 1 -> UserRole.Developer;
                                        case 2 -> UserRole.Manager;
                                        default -> null; // Handle other cases if needed
                                    };
                                    projectSearchListResponseDTO.setUserRole(role);

                                    return projectSearchListResponseDTO;
                                })
                        )
                )
                .collect(Collectors.toList());

        //디스크립션 검색
        List<ProjectOpenSearch> projectOpenDescriptionSearchList = searchProjectByDescription(keyword);

        List<ProjectSearchListResponseDTO> projectSearchByDescriptionListResponseDTOList = projectOpenDescriptionSearchList.stream()
                .flatMap(projectOpenSearch -> searchUserInProjectByProjectId(projectOpenSearch.getId()).stream()
                        .flatMap(userInProjectOpenSearch -> searchUserById(userInProjectOpenSearch.getUser_id()).stream()
                                .map(userOpenSearch -> {
                                    ProjectSearchListResponseDTO projectDescriptionSearchListResponseDTO = new ProjectSearchListResponseDTO();
                                    projectDescriptionSearchListResponseDTO.setId(projectOpenSearch.getId());
                                    projectDescriptionSearchListResponseDTO.setName(projectOpenSearch.getProjectName());
                                    projectDescriptionSearchListResponseDTO.setDescription(projectOpenSearch.getDescription());
                                    projectDescriptionSearchListResponseDTO.setCreateDate(projectOpenSearch.getCreateDate());
                                    projectDescriptionSearchListResponseDTO.setUserId(userOpenSearch.getId());
                                    projectDescriptionSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                                    projectDescriptionSearchListResponseDTO.setUserName(userOpenSearch.getUsername());

                                    UserRole role = switch (userInProjectOpenSearch.getRole().intValue()) {
                                        case 0 -> UserRole.Subscriber;
                                        case 1 -> UserRole.Developer;
                                        case 2 -> UserRole.Manager;
                                        default -> null; // Handle other cases if needed
                                    };
                                    projectDescriptionSearchListResponseDTO.setUserRole(role);

                                    return projectDescriptionSearchListResponseDTO;
                                })
                        )
                )
                .collect(Collectors.toList());

        //매니저 이름 검색 유인아
        //이름으로 유저검색 searchUsername
        //유저가 들어간 프로젝트 아이디 유인프
        //프로젝트 아이디로 프로젝트 프로젝트 아이디로 검색
        //유인프로 유저들 가져오기
        //프로젝트에 들어간 유저검색
        List<UserOpenSearch> projectOpenManagerNameSearchList = searchUserByName(keyword);

        List<ProjectSearchListResponseDTO> projectSearchByManagerListResponseDTOList = projectOpenManagerNameSearchList.stream()
                .flatMap(userOpenManagerSearch -> searchUserInProjectByUserIdforManager(userOpenManagerSearch.getId()).stream())
                .flatMap(userInProjectOpenSearch -> searchProjectByProjectId(userInProjectOpenSearch.getProject_id()).stream()
                        .flatMap(projectOpenProjectSearch -> searchUserInProjectByProjectId(projectOpenProjectSearch.getId()).stream()
                                .flatMap(userInProjectOpenSearch2 -> searchUserById(userInProjectOpenSearch2.getUser_id()).stream()
                                        .map(userOpenSearch -> {
                                            ProjectSearchListResponseDTO projectManagerSearchListResponseDTO = new ProjectSearchListResponseDTO();
                                            projectManagerSearchListResponseDTO.setId(projectOpenProjectSearch.getId());
                                            projectManagerSearchListResponseDTO.setName(projectOpenProjectSearch.getProjectName());
                                            projectManagerSearchListResponseDTO.setDescription(projectOpenProjectSearch.getDescription());
                                            projectManagerSearchListResponseDTO.setCreateDate(projectOpenProjectSearch.getCreateDate());
                                            projectManagerSearchListResponseDTO.setUserId(userOpenSearch.getId());
                                            projectManagerSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                                            projectManagerSearchListResponseDTO.setUserName(userOpenSearch.getUsername());

                                            UserRole role = switch (userInProjectOpenSearch2.getRole().intValue()) {
                                                case 0 -> UserRole.Subscriber;
                                                case 1 -> UserRole.Developer;
                                                case 2 -> UserRole.Manager;
                                                default -> null; // Handle other cases if needed
                                            };
                                            projectManagerSearchListResponseDTO.setUserRole(role);

                                            return projectManagerSearchListResponseDTO;
                                        })
                                )
                        )
                )
                .collect(Collectors.toList());


        //개발자 이름 검색

        List<UserOpenSearch> projectOpenDeveloperNameSearchList = searchUserByName(keyword);

        List<ProjectSearchListResponseDTO> projectSearchByDeveloperListResponseDTOList = projectOpenDeveloperNameSearchList.stream()
                .flatMap(userOpenManagerSearch -> searchUserInProjectByUserIdforDeveloper(userOpenManagerSearch.getId()).stream())
                .flatMap(userInProjectOpenSearch -> searchProjectByProjectId(userInProjectOpenSearch.getProject_id()).stream()
                        .flatMap(projectOpenProjectSearch -> searchUserInProjectByProjectId(projectOpenProjectSearch.getId()).stream()
                                .flatMap(userInProjectOpenSearch2 -> searchUserById(userInProjectOpenSearch2.getUser_id()).stream()
                                        .map(userOpenSearch -> {
                                            ProjectSearchListResponseDTO projectManagerSearchListResponseDTO = new ProjectSearchListResponseDTO();
                                            projectManagerSearchListResponseDTO.setId(projectOpenProjectSearch.getId());
                                            projectManagerSearchListResponseDTO.setName(projectOpenProjectSearch.getProjectName());
                                            projectManagerSearchListResponseDTO.setDescription(projectOpenProjectSearch.getDescription());
                                            projectManagerSearchListResponseDTO.setCreateDate(projectOpenProjectSearch.getCreateDate());
                                            projectManagerSearchListResponseDTO.setUserId(userOpenSearch.getId());
                                            projectManagerSearchListResponseDTO.setUserDepartment(userOpenSearch.getDepartment());
                                            projectManagerSearchListResponseDTO.setUserName(userOpenSearch.getUsername());

                                            UserRole role = switch (userInProjectOpenSearch2.getRole().intValue()) {
                                                case 0 -> UserRole.Subscriber;
                                                case 1 -> UserRole.Developer;
                                                case 2 -> UserRole.Manager;
                                                default -> null; // Handle other cases if needed
                                            };
                                            projectManagerSearchListResponseDTO.setUserRole(role);

                                            return projectManagerSearchListResponseDTO;
                                        })
                                )
                        )
                )
                .collect(Collectors.toList());

        projectSearchResultListResponseDTO.setTitleSearch(makeProjectSearchListResponseDTOList(projectSearchByNameListResponseDTOList));
        projectSearchResultListResponseDTO.setDescriptionSearch(makeProjectSearchListResponseDTOList(projectSearchByDescriptionListResponseDTOList));
        projectSearchResultListResponseDTO.setManagerSearch(makeProjectSearchListResponseDTOList(projectSearchByManagerListResponseDTOList));
        projectSearchResultListResponseDTO.setDeveloperSearch(makeProjectSearchListResponseDTOList(projectSearchByDeveloperListResponseDTOList));


        return projectSearchResultListResponseDTO;
    }




}

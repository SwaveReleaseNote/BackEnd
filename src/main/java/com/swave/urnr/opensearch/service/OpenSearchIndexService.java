package com.swave.urnr.opensearch.service;

import java.io.IOException;

public interface OpenSearchIndexService {
    public String createProjectIndex() throws IOException;

    public String createUserInProjectIndex() throws IOException;

    public String createUserIndex() throws IOException;

}

package com.omegalambdang.rentanitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
public abstract class AbstractTest {
    @Value("${api.basepath-api}")
    protected String baseApiPath="";
    @Autowired
    protected MockMvc mockMvc;
}

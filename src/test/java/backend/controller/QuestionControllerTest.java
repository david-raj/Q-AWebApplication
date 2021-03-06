package backend.controller;

import backend.model.qa.QuestionModel;
import backend.model.user.UserModel;
import backend.repository.qa.QuestionRepository;
import backend.repository.user.UserRepository;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

import static backend.controller.constants.ForumPostConstants.FORMAT;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
public class QuestionControllerTest {

    private MediaType mediaType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private final QuestionModel question =
            new QuestionModel("Title 1", "Unit test message", new HashSet<>(List.of("Programming", "Java")),
                    new DateTime().toString(FORMAT));
    private final QuestionModel question2 =
            new QuestionModel("Title 2", "Unit test message 2", new HashSet<>(List.of("Programming", "C++")),
                    new DateTime().toString(FORMAT));
    private final UserModel user = new UserModel("","","","","","","");

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        question.setUserQuestion(user);
        question2.setUserQuestion(user);
        userRepository.save(user);
        questionRepository.save(question);
        questionRepository.save(question2);
    }

    @Test
    public void questionNotFound_thenReturnEmptyString() throws Exception {
        mockMvc.perform(get("/questions/" + 0).content("")
                .contentType(mediaType))
                .andExpect(status().isOk());
    }

    @Test
    public void ifQuestionFound_thenReturnJson() throws Exception {
        mockMvc.perform(get("/questions/" + question.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.id", is(question.getId()
                        .intValue())))
                .andExpect(jsonPath("$.questionTitle", is(question.getQuestionTitle())))
                .andExpect(jsonPath("$.questionCategories[0]", is(question.getQuestionCategories()
                        .toArray()[0])))
                .andExpect(jsonPath("$.questionCategories[1]", is(question.getQuestionCategories()
                        .toArray()[1])))
                .andExpect(jsonPath("$.message", is(question.getMessage())))
                .andExpect(jsonPath("$.postedDate", is(question.getPostedDate())))
                .andExpect(jsonPath("$.updatedTime", is(question.getUpdatedTime())))
                .andExpect(jsonPath("$.userId", is(question.getUserId().intValue())));
    }

    @Test
    public void getAllQuestionsRequested_thenReturnAllQuestionInDatabase() throws Exception {
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(question2.getId()
                        .intValue())))
                .andExpect(jsonPath("$[0].questionTitle", is(question2.getQuestionTitle())))
                .andExpect(jsonPath("$[0].questionCategories[0]", is(question2.getQuestionCategories()
                        .toArray()[0])))
                .andExpect(jsonPath("$[0].questionCategories[1]", is(question2.getQuestionCategories()
                        .toArray()[1])))
                .andExpect(jsonPath("$[0].message", is(question2.getMessage())))
                .andExpect(jsonPath("$[0].postedDate", is(question2.getPostedDate())))
                .andExpect(jsonPath("$[0].updatedTime", is(question2.getUpdatedTime())))
                .andExpect(jsonPath("$[0].userId", is(question2.getUserId().intValue())))
                .andExpect(jsonPath("$[1].id", is(question.getId()
                        .intValue())))
                .andExpect(jsonPath("$[1].questionTitle", is(question.getQuestionTitle())))
                .andExpect(jsonPath("$[1].questionCategories[0]", is(question.getQuestionCategories()
                        .toArray()[0])))
                .andExpect(jsonPath("$[1].questionCategories[1]", is(question.getQuestionCategories()
                        .toArray()[1])))
                .andExpect(jsonPath("$[1].message", is(question.getMessage())))
                .andExpect(jsonPath("$[1].postedDate", is(question.getPostedDate())))
                .andExpect(jsonPath("$[1].updatedTime", is(question.getUpdatedTime())))
                .andExpect(jsonPath("$[1].userId", is(question.getUserId().intValue())));
    }

    @After
    public void tearDown() {
        questionRepository.deleteAll();
    }
}

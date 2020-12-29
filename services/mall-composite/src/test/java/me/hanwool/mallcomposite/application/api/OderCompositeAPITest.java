package me.hanwool.mallcomposite.application.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
//import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //
//@WebFluxTest
//@WebFluxTest(OderCompositeAPIImpl.class)
//@AutoConfigureWebTestClient
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
//@AutoConfigureRestDocs
class OderCompositeAPITest {

    // test !!!!!
    private static final int ORDER_ID_OK = 1;
    private static final int ORDER_ID_NOT_FOUND = 2;
    ////////////

//    @Autowired
//    private ApplicationContext context;

//    private WebTestClient client;
//
//    @BeforeEach
//    void setup(ApplicationContext applicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.client = WebTestClient.bindToApplicationContext(applicationContext)
//                .configureClient()
//                .filter(documentationConfiguration(restDocumentation))
//                .build();
//    };
//    @BeforeEach
//    void setup(RestDocumentationContextProvider restDocumentation) {
//        this.client = WebTestClient.bindToApplicationContext(this.context)
//                .configureClient()
//                .filter(documentationConfiguration(restDocumentation))
//                .build();
//    };

 /*   @Test
    @DisplayName("주문 정보 확인")
    void getOrderById() throws Exception {
        client.get()
                .uri("/open-api/order/" + ORDER_ID_OK)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
//                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo(ORDER_ID_OK)
                .consumeWith(document("sample"));
    }*/

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    @DisplayName("주문 정보 확인")
    void getOrderById() throws Exception {
        this.mockMvc.perform(get("/open-api/order/" + ORDER_ID_OK)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(ORDER_ID_OK))
                .andDo(document("get-order-api"));
    }

//    @Test
//    public void getOrderNotFound() {
//
//        client.get()
//                .uri("/open-api/order/" + ORDER_ID_NOT_FOUND)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectHeader().contentType(APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.path").isEqualTo("/open-api/order/" + ORDER_ID_NOT_FOUND)
//                .jsonPath("$.message").isEqualTo("NOT FOUND: " + ORDER_ID_NOT_FOUND);
//    }
}
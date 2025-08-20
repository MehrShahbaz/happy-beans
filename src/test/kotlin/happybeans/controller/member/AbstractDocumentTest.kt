package happybeans.controller.member

import happybeans.config.argumentResolver.LoginMemberArgumentResolver
import happybeans.config.interceptor.MemberInterceptor
import happybeans.model.User
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
abstract class AbstractDocumentTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var loginMemberArgumentResolver: LoginMemberArgumentResolver

    @MockitoBean
    private lateinit var memberInterceptor: MemberInterceptor

    lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        testUser = User(
            id = 1L,
            firstName = "Test User",
            email = "test@example.com",
            password = "password",
            lastName = "Test User"
        )

        whenever(loginMemberArgumentResolver.supportsParameter(any())).thenReturn(true)
        whenever(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
            .thenReturn(testUser)
        whenever(memberInterceptor.preHandle(any(), any(), any())).thenReturn(true)
    }
}


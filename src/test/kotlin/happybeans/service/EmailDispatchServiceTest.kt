package happybeans.service

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import kotlin.test.assertEquals

class EmailDispatchServiceTest {
    private val mailSender = mockk<JavaMailSender>(relaxed = true)
    private val service = EmailDispatchService(mailSender)

    @Test
    fun `should send welcome email with correct subject and body`() {
        val to = "owner@restaurant.com"
        val password = "test123"

        val slot = slot<SimpleMailMessage>()
        every { mailSender.send(capture(slot)) } just Runs

        service.sendRestaurantOwnerWelcomeEmail(to, password)

        val sentMessage = slot.captured
        assertEquals(to, sentMessage.to?.get(0))
        assertEquals("Welcome to Happy Beans", sentMessage.subject)
        assertEquals(
            "Welcome to happy beans, login credentials password:$password. Note: Use the same email",
            sentMessage.text,
        )

        verify(exactly = 1) { mailSender.send(any<SimpleMailMessage>()) }
    }
}

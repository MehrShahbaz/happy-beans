package happybeans.service

import happybeans.model.Order
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailDispatchService(
    private val mailSender: JavaMailSender,
) {
    fun sendRestaurantOwnerWelcomeEmail(
        to: String,
        password: String,
        subject: String = RESTAURANT_OWNER_EMAIL_SUBJECT,
    ) {
        val html =
            """
            <div style="font-family: Arial, sans-serif; padding:20px; color:#333;">
                <h2 style="color:#2d6a4f;">Welcome to Happy Beans!</h2>
                <p>We’re excited to have you on board as a restaurant partner 🎉</p>
                <p><strong>Login Email:</strong> $to</p>
                <p><strong>Temporary Password:</strong> $password</p>
                <p style="margin-top:20px;">Please log in and update your password as soon as possible.</p>
                <hr style="margin:20px 0; border:0; border-top:1px solid #ddd;"/>
                <p style="font-size:12px; color:#888;">Happy Beans Team</p>
            </div>
            """.trimIndent()
        sendHtmlMessage(to, subject, html)
    }

    fun sendJoinRequestRejectEmail(to: String) {
        val html =
            """
            <div style="font-family: Arial, sans-serif; padding:20px; color:#333;">
                <h2 style="color:#d00000;">Join Request Update</h2>
                <p>We appreciate your interest in joining Happy Beans, but unfortunately your request was not approved at this time.</p>
                <p>Feel free to apply again in the future.</p>
                <hr style="margin:20px 0; border:0; border-top:1px solid #ddd;"/>
                <p style="font-size:12px; color:#888;">Happy Beans Team</p>
            </div>
            """.trimIndent()
        sendHtmlMessage(to, REJECT_INVITE_SUBJECT, html)
    }

    fun sendOrderConfirmationEmail(order: Order) {
        val itemsHtml = buildOrderItemsTable(order)
        val html =
            """
            <div style="font-family: Arial, sans-serif; padding:20px; color:#333;">
                <h2 style="color:#2d6a4f;">Order Confirmation</h2>
                <p>Hi,</p>
                <p>Good news 🎉 Your order <strong>#${order.id}</strong> has been confirmed.</p>
                $itemsHtml
                <p style="margin-top:20px;">We’ll notify you once it’s on the way.</p>
                <hr style="margin:20px 0; border:0; border-top:1px solid #ddd;"/>
                <p style="font-size:12px; color:#888;">Thank you for choosing Happy Beans!</p>
            </div>
            """.trimIndent()
        sendHtmlMessage(order.userEmail, CONFIRM_ORDER_SUBJECT, html)
    }

    fun sendOrderFailEmail(order: Order) {
        val itemsHtml = buildOrderItemsTable(order)
        val html =
            """
            <div style="font-family: Arial, sans-serif; padding:20px; color:#333;">
                <h2 style="color:#d00000;">Order Failed</h2>
                <p>Hi,</p>
                <p>We’re sorry 😔 Your order <strong>#${order.id}</strong> could not be processed.</p>
                $itemsHtml
                <p style="margin-top:20px;">Please try again or contact our support team for assistance.</p>
                <hr style="margin:20px 0; border:0; border-top:1px solid #ddd;"/>
                <p style="font-size:12px; color:#888;">Happy Beans Support</p>
            </div>
            """.trimIndent()
        sendHtmlMessage(order.userEmail, FAILED_ORDER_SUBJECT, html)
    }

    private fun buildOrderItemsTable(order: Order): String {
        val formatedTotal = "%.2f".format(order.totalAmount)
        val rows =
            order.orderProducts.joinToString("") { product ->
                """
            <tr>
                <td style="border:1px solid #ddd; padding:8px;">${product.dishOptionName}</td>
                <td style="border:1px solid #ddd; padding:8px; text-align:center;">${product.quantity}</td>
                <td style="border:1px solid #ddd; padding:8px; text-align:right;">$${"%.2f".format(product.price)}</td>
                <td style="border:1px solid #ddd; padding:8px; text-align:right;">$${"%.2f".format(product.price * product.quantity)}</td>
            </tr>
            """
            }

        return """
            <h3 style="margin-top:20px;">Order Details</h3>
            <table style="border-collapse:collapse; width:100%; font-size:14px;">
                <thead style="background-color:#f2f2f2;">
                <tr>
                    <th style="border:1px solid #ddd; padding:8px; text-align:left;">Item</th>
                    <th style="border:1px solid #ddd; padding:8px; text-align:center;">Qty</th>
                    <th style="border:1px solid #ddd; padding:8px; text-align:right;">Price</th>
                    <th style="border:1px solid #ddd; padding:8px; text-align:right;">Total</th>
                </tr>
            </thead>
            <tbody>
                $rows
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="3" style="border:1px solid #ddd; padding:8px; text-align:right;"><strong>Grand Total:</strong></td>
                    <td style="border:1px solid #ddd; padding:8px; text-align:right;"><strong>$$formatedTotal</strong></td>
                </tr>
            </tfoot>
            </table>
            """.trimIndent()
    }

    private fun sendHtmlMessage(
        to: String,
        subject: String,
        html: String,
    ) {
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(html, true) // true = HTML
        mailSender.send(mimeMessage)
    }

    companion object {
        private const val RESTAURANT_OWNER_EMAIL_SUBJECT = "Welcome to Happy Beans"
        private const val REJECT_INVITE_SUBJECT = "Your Happy Beans Join Request"
        private const val CONFIRM_ORDER_SUBJECT = "Your order has been confirmed 🎉"
        private const val FAILED_ORDER_SUBJECT = "Your order could not be processed"
    }
}

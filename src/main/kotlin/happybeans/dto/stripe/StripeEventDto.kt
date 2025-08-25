package happybeans.dto.stripe

data class StripeEventDto(
    val id: String,
    val `object`: String,
    val api_version: String?,
    val created: Long,
    val data: EventData,
    val livemode: Boolean,
    val pending_webhooks: Int,
    val request: Request?,
    val type: String,
) {
    data class EventData(
        val `object`: PaymentIntentDto,
    )

    data class Request(
        val id: String?,
        val idempotency_key: String?,
    )

    data class PaymentIntentDto(
        val id: String,
        val `object`: String,
        val amount: Long,
        val amount_capturable: Long,
        val amount_details: AmountDetails?,
        val amount_received: Long,
        val application: String?,
        val application_fee_amount: Long?,
        val automatic_payment_methods: Any?,
        val canceled_at: Long?,
        val cancellation_reason: String?,
        val capture_method: String?,
        val client_secret: String?,
        val confirmation_method: String?,
        val created: Long,
        val currency: String,
        val customer: String?,
        val description: String?,
        val excluded_payment_method_types: Any?,
        val invoice: String?,
        val last_payment_error: Any?,
        val latest_charge: String?,
        val livemode: Boolean,
        val metadata: Map<String, String> = emptyMap(),
        val next_action: Any?,
        val on_behalf_of: String?,
        val payment_method: String?,
        val payment_method_configuration_details: Any?,
        val payment_method_options: PaymentMethodOptions?,
        val payment_method_types: List<String>?,
        val processing: Any?,
        val receipt_email: String?,
        val review: String?,
        val setup_future_usage: String?,
        val shipping: Shipping?,
        val source: String?,
        val statement_descriptor: String?,
        val statement_descriptor_suffix: String?,
        val status: String,
        val transfer_data: Any?,
        val transfer_group: String?,
    ) {
        data class AmountDetails(
            val tip: Map<String, Any> = emptyMap(),
        )

        data class PaymentMethodOptions(
            val card: Card?,
        ) {
            data class Card(
                val installments: Any?,
                val mandate_options: Any?,
                val network: String?,
                val request_three_d_secure: String?,
            )
        }

        data class Shipping(
            val address: Address,
            val carrier: String?,
            val name: String?,
            val phone: String?,
            val tracking_number: String?,
        ) {
            data class Address(
                val city: String?,
                val country: String?,
                val line1: String?,
                val line2: String?,
                val postal_code: String?,
                val state: String?,
            )
        }
    }
}

package happybeans.config

import happybeans.config.argumentResolver.LoginMemberArgumentResolver
import happybeans.config.argumentResolver.RestaurantOwnerArgumentResolver
import happybeans.config.interceptor.AdminInterceptor
import happybeans.config.interceptor.MemberInterceptor
import happybeans.config.interceptor.RestaurantOwnerInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val adminInterceptor: AdminInterceptor,
    private val memberInterceptor: MemberInterceptor,
    private val restaurantOwnerInterceptor: RestaurantOwnerInterceptor,
    private val loginMemberArgumentResolver: LoginMemberArgumentResolver,
    private val restaurantOwnerArgumentResolver: RestaurantOwnerArgumentResolver,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(memberInterceptor)
            .addPathPatterns("/api/member/cart/**")
        registry.addInterceptor(adminInterceptor)
            .addPathPatterns("/api/admin/restaurant-owner/**", "/api/admin/join-request/**")
        registry.addInterceptor(restaurantOwnerInterceptor)
            .addPathPatterns("")
        super.addInterceptors(registry)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        val additionalResolvers =
            listOf(
                loginMemberArgumentResolver,
                restaurantOwnerArgumentResolver,
            )
        resolvers.addAll(additionalResolvers)
        super.addArgumentResolvers(resolvers)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders(HttpHeaders.LOCATION)
    }
}

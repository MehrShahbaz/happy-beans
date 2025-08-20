package happybeans.config

import happybeans.config.argumentResolver.LoginMemberArgumentResolver
import happybeans.config.argumentResolver.RestaurantOwnerArgumentResolver
import happybeans.config.interceptor.AdminInterceptor
import happybeans.config.interceptor.MemberInterceptor
import happybeans.config.interceptor.RestaurantOwnerInterceptor
import happybeans.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val adminInterceptor: AdminInterceptor,
    private val authInterceptor: MemberInterceptor,
    private val userRepository: UserRepository,
    private val restaurantOwnerInterceptor: RestaurantOwnerInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/reviews/**")
        registry.addInterceptor(adminInterceptor)
            .addPathPatterns("")
        registry.addInterceptor(restaurantOwnerInterceptor)
            .addPathPatterns("/api/restaurant-owner/restaurants/**")
        super.addInterceptors(registry)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        val additionalResolvers =
            listOf(
                LoginMemberArgumentResolver(userRepository),
                RestaurantOwnerArgumentResolver(userRepository),
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

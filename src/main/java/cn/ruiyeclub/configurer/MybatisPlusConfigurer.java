package cn.ruiyeclub.configurer;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * mybatis-plus配置
 * @author Ray。
 */
@Configuration
@MapperScan("cn.ruiyeclub.manage.mapper")
@EnableTransactionManagement
public class MybatisPlusConfigurer {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}

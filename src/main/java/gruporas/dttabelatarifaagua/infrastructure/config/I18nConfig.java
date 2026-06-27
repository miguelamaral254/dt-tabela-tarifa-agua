package gruporas.dttabelatarifaagua.infrastructure.config;

import gruporas.dttabelatarifaagua.shared.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class I18nConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.ENGLISH);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader("Accept-Language");

                if (StringUtils.isBlank(headerLang)) {
                    return Locale.ENGLISH;
                }

                Locale locale = Locale.forLanguageTag(headerLang);
                if (locale.getLanguage().equalsIgnoreCase("pt")) {
                    return new Locale("pt");
                }

                return Locale.ENGLISH;
            }
        };
    }
}

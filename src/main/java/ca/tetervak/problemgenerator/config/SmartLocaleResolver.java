package ca.tetervak.problemgenerator.config;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SmartLocaleResolver extends CookieLocaleResolver {

    private List<Locale> supportedLocales = Collections.singletonList(Locale.ENGLISH);

    public SmartLocaleResolver(
            @NonNull String cookieName
    ) {
        super(cookieName);
    }

    public void setSupportedLocales(
            @NonNull List<Locale> supportedLocales
    ) {
        this.supportedLocales = supportedLocales;
    }

    @Override
    @NonNull
    public Locale resolveLocale(@NonNull HttpServletRequest request) {
        // 1. Resolve the user's intended locale (Cookie or Browser)
        Locale resolved = super.resolveLocale(request);

        String lang = resolved.getLanguage();
        
        // 2. Validate against the list (match by language)
        boolean isSupported = supportedLocales.stream()
                .anyMatch(locale -> locale.getLanguage().equals(lang));

        if (isSupported) {
            return resolved;
        }

        // 3. Fallback: Return the first supported locale (default)
        if (!supportedLocales.isEmpty()) {
            return supportedLocales.getFirst();
        }
        
        return Locale.ENGLISH;
    }
}

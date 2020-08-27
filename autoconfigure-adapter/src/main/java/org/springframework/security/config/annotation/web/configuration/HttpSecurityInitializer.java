package org.springframework.security.config.annotation.web.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link ApplicationContextInitializer} adapter for {@link HttpSecurityConfiguration}.
 */
public class HttpSecurityInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
	private static final String BEAN_NAME_PREFIX = "org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration.";
	static final String HTTPSECURITY_BEAN_NAME = BEAN_NAME_PREFIX + "httpSecurity";

	private final Consumer<HttpSecurity> httpSecurityDsl;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	public HttpSecurityInitializer(Consumer<HttpSecurity> httpSecurityDsl, AuthenticationManager authenticationManager,
								   UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.httpSecurityDsl = httpSecurityDsl;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(GenericApplicationContext context) {
		if (passwordEncoder != null) {
			context.registerBean(PasswordEncoder.class, () -> passwordEncoder);
		}

		HttpSecurityConfiguration configuration = new HttpSecurityConfiguration();
		configuration.setApplicationContext(context);
		if (authenticationManager != null) {
			configuration.setAuthenticationManager(authenticationManager);
		}

		Supplier<HttpSecurity> httpSecuritySupplier = () -> {
			configuration.setObjectPostProcessor(context.getBean(ObjectPostProcessor.class));

			HttpSecurity httpSecurity;
			try {
				httpSecurity = configuration.httpSecurity();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}

			if (userDetailsService != null) {
				try {
					httpSecurity.userDetailsService(userDetailsService);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			this.httpSecurityDsl.accept(httpSecurity);

			return httpSecurity;
		};

		// todo expose configuration '@Bean's
		context.registerBean(
				HTTPSECURITY_BEAN_NAME,
				HttpSecurity.class,
				httpSecuritySupplier,
				bd -> {
					bd.setScope("prototype");
					bd.setAutowireCandidate(true);
				}
		);
	}
}

// @formatter:off
package com.everhomes.locale;

public interface LocaleStringProvider {
    LocaleString find(String scope, String code, String locale);
    LocaleString findByText(String scope, String text, String locale);
}

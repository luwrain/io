
package org.luwrain.app.gpt;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
    String errNoYandexFolderId();
    String errNoYandexApiKey();
        String filePrefix();
    String inputPrefix();
    String optionsAreaName();
    String yandexApiKeyEdit();
        String yandexFolderIdEdit();

}

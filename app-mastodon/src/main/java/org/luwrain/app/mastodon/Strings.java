
package org.luwrain.app.mastodon;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    static final String NAME = "luwrain.mastodon";

    String appName();
    
    String wizardIntro();
    String wizardName();
    String wizardMail();
    String wizardPassword();
    String wizardContinue();
    }

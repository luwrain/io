
package org.luwrain.app.telegram;

import org.luwrain.core.annotations.*;
import java.util.Date;

@ResourceStrings(langs = {"en", "ru"})
public interface Strings
{
    String appName();
    String actionMainChats();
    String actionContacts();
    String actionSearchChats();
    String actionJoin();

    String actionCloseChat();
    String actionDeleteMessage();
    String actionNewContact();

    String chatsAreaName();
    String chatPropsAreaName();
    String contactsAreaName();

    String authPhoneNumberIntro();
    String authPhoneNumberInput();
    String authCodeIntro();
    String authContinue();
    String authAddProxy();

    String actionNewChannel();
    String newChannelPopupName();
    String newChannelTitlePopupPrefix();
        String newChannelDescrPopupPrefix();
    String channelCreated(String title);

    String actionComposeText();
    String actionEditMessageText();
    String composeTextAreaName();
    String composedTextEmpty();

    String actionDeleteChat();
    String chatDeletingPopupName();
    String chatDeletingPopupText(String title);

    String actionChatStat();
    String chatStatAreaName();

    String actionPrivacySettings();



    String actionSetChatPhoto();
    String actionChatMembers();

            String actionSendAudio();
    String composeAudioTitle();
    String composeAudioAuthor();
    String composeAudioFile();
    String composeAudioComment();

        String actionSendPhoto();
    String composePhotoFile();
    String composePhotoComment();

    String actionSetUserName();
    String userNamePopupName();
    String userNamePopupPrefix();

    String actionSetChatDescr();
    String setChatDescrAreaName();

    String addProxyPopupName();
    String addProxyPopupPrefix();
}

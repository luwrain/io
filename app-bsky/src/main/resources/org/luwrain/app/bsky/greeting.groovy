wizard "BlueSky", 'greeting', {

frame 'greeting', {
text getStrings().greetingIntro()
input 'handle', getStrings().greetingHandle()
password 'passwd', getStrings().greetingPasswd()
button getStrings().wizardContinue(), { values ->

}
button getStrings().greetingSignUp(), {values ->
show "sign-up"
}
button getStrings().greetingSkip(), {

}
}

frame 'sign-up', {
text getStrings().signUpIntro()
input 'mail', getStrings().greetingMail()
input 'handle', getStrings().greetingHandle()
password 'passwd', getStrings().greetingPasswd()
button 'Sign Up', {values ->
if (value.get(0).trim().isEmpty())
{
error getStrings().signUpMailCannotBeEmpty()
return
}
if (value.get(1).trim().isEmpty())
{
error getStrings().signUpHandleCannotBeEmpty()
return
}
if (value.get(2).trim().isEmpty())
{
error getStrings().signUpPasswdCannotBeEmpty()
return
}
signUp values.getText(0), values.getText(1), values.getText(2)
}
}

}


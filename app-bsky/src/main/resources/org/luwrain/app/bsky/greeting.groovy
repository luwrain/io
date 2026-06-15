wizard "BlueSky", 'greeting', {

frame 'greeting', {
text getStrings().wizardIntro()
input 'handle', getStrings().wizardHandle()
input 'app-password', getStrings().wizardAppPassword()
button getStrings().wizardContinue(), { values ->
//save values.getText(0), values.getText(1)
}
button "Sign Up", {values ->
show 'sign-up'
}
button getStrings().wizardSkip(), {
//skip()
}
}

frame 'sign-up', {
text "Need mail, handle and password"
input 'mail', 'Mail:'
input 'handle', "Handled:"
input 'passwd', "Password:"
button 'Sign Up', {values ->
signUp values.getText(0), values.getText(1), values.getText(2)
}
}

}


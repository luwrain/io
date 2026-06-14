wizard getStrings().wizardTitle(), 'greeting', {

frame 'greeting', {
text getStrings().wizardIntro()
input 'handle', getStrings().wizardHandle()
input 'app-password', getStrings().wizardAppPassword()
button getStrings().wizardContinue(), { values ->
save values.getText(0), values.getText(1)
}
button getStrings().wizardSkip(), { skip() }
}

}

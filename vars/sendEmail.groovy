def call () {
    startTime = new Date(currentBuild.startTimeInMillis).format('dd.MM.yyyy HH:mm')
    emailext attachLog: true, 
    compressLog: true,
    body: """
        Build Start Date&Time: ${startTime}
        Build Status: ${currentBuild.result}
        Launched by ${BUILD_USER_ID}
        ${NODE}-${JOB_BASE_NAME}-${BUILD_NUMBER}""",
            from: 'prime_news@ucscards.ru',
            subject: "Build Status ${currentBuild.result}, ${NODE}-${JOB_BASE_NAME}-${BUILD_NUMBER}",
            to: "${env.MAIL_RECIPIENTS_DEV}";
}


def runUnitTests() {
    sh "mvn clean install"
}


def buildMedia() {
        sh "mvn clean install -DskipTests=true"
}

def publishRCResults() {
    junit testResults: '**/target/surefire-reports/*.xml', testDataPublishers: [[$class: 'StabilityTestDataPublisher']]
    checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/checkstyle-result.xml', unHealthy: ''
    junit '**/target/surefire-reports/*.xml'
    step( [ $class: 'JacocoPublisher' ] )
    if ((env.BRANCH_NAME == 'master') && (currentBuild.currentResult != 'SUCCESS') ) {
       slackSend "Build unstable - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
    }
    if (env.BRANCH_NAME ==~ /^PR-\d+$/) {
        //step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: emailextrecipients([[$class: 'FailingTestSuspectsRecipientProvider']])])
        /*if (currentBuild.currentResult != 'SUCCESS' ) { // Other values: SUCCESS, UNSTABLE, FAILURE
            setGitHubPullRequestStatus (context:'CI', message:'IT unstable', state:'FAILURE')
        } else {
           setGitHubPullRequestStatus (context:'CI', message:'IT passed', state:'SUCCESS')
        }*/
    }

}

node("cxs-slave-master") {

   echo sh(returnStdout: true, script: 'env')

    configFileProvider(
        [configFile(fileId: '37cb206e-6498-4d8a-9b3d-379cd0ccd99b',  targetLocation: 'settings.xml')]) {
	sh 'mkdir -p ~/.m2 && sed -i "s|@LOCAL_REPO_PATH@|$WORKSPACE/M2_REPO|g" $WORKSPACE/settings.xml && cp $WORKSPACE/settings.xml -f ~/.m2/settings.xml'
    }

   stage ('Checkout') {
    checkout scm
   }

   stage ("Build") {
    buildMedia()
   }

   stage ("Build") {
    runUnitTests()
   }
//   stage("PublishResults") {
//    publishRCResults()
//   }
}


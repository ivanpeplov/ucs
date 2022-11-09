import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
import jenkins.model.CauseOfInterruption.UserInterruption

def call(message)
{
    currentBuild.displayName = "#${env.BUILD_NUMBER} $message"
    echo message
    currentBuild.result = 'ABORTED'
    throw new FlowInterruptedException(Result.ABORTED, new UserInterruption(env.BUILD_USER_ID))
}
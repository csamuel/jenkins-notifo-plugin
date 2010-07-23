package hudson.plugins.notifo;

import com.google.common.base.Splitter;

import hudson.Extension;
import hudson.Launcher;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.User;

import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

public class NotifoNotifier extends Notifier {

    public final String serviceUser;
    public final String apiToken;
    public final String userNames;
    public final boolean notifyOnSuccess;
    private transient Notifo notifo;

    @DataBoundConstructor
    public NotifoNotifier(String serviceUser, String apiToken, String userNames, boolean notifyOnSuccess) {
        this.serviceUser = serviceUser;
        this.apiToken = apiToken;
        this.userNames = userNames;
        this.notifyOnSuccess = notifyOnSuccess;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    private void initializeNotifo()
            throws IOException {
        if (notifo == null) {
            notifo = new Notifo(this.serviceUser,
                     this.apiToken,
                     Splitter.on(',').omitEmptyStrings().trimResults().split(this.userNames));
        }
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        if (!build.getResult().toString().equals(Result.SUCCESS.toString()) || notifyOnSuccess) {
            initializeNotifo();
            String message = build.getProject().getName() + ": " + build.getResult().toString() + "\n";
            if (!build.getCulprits().isEmpty()) {
                for (User user : build.getCulprits()) {
                    message = message + "Possible Culprit: " + user.getDisplayName();
                }
            }
            notifo.post(message, listener);
        }
        return true;
    }

    @Extension
    public static final class DescriptorImpl
            extends BuildStepDescriptor<Publisher> {
        /*
         * (non-Javadoc)
         *
         * @see hudson.tasks.BuildStepDescriptor#isApplicable(java.lang.Class)
         */

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        /*
         * (non-Javadoc)
         *
         * @see hudson.model.Descriptor#getDisplayName()
         */
        @Override
        public String getDisplayName() {
            return "Notifo";
        }
    }
}

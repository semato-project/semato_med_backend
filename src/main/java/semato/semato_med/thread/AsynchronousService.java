package semato.semato_med.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AsynchronousService {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private MailerThread mailerThread;

    @PostConstruct
    public void executeAsynchronously() {
        taskExecutor.execute(mailerThread);
    }
}
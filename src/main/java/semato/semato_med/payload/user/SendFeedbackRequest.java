package semato.semato_med.payload.user;

import lombok.Getter;

@Getter
public class SendFeedbackRequest {

    private String subject;

    private String content;

}

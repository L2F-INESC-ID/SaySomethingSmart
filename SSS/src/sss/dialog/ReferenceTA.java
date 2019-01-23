package sss.dialog;

/**
 * @author Vania
 * @date 05/07/2016
 */
public class ReferenceTA {

    private long dialogId;
    private String trigger;
    private String answer;

    public ReferenceTA() {
    }

    public ReferenceTA(long dialogId, String trigger, String answer) {
        this.dialogId = dialogId;
        this.trigger = trigger;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "ReferenceTA{" +
                "dialogId=" + dialogId +
                ", trigger='" + trigger + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public long getDialogId() {
        return dialogId;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

package sss.dialog.evaluator;

import sss.dialog.Conversation;
import sss.dialog.QA;

import java.util.List;

public interface Evaluator {
    public void score(String userQuestion, List<QA> qas, Conversation conversation);

}

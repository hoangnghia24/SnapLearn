package hcmute.edu.vn.snaplearn.models;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
    private String topicId;
    private String topicName;
    private List<Flashcard> flashcardList;

    public Topic() {
    }

    public Topic(String topicId, String topicName, List<Flashcard> flashcardList) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.flashcardList = flashcardList;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<Flashcard> getFlashcardList() {
        return flashcardList;
    }

    public void setFlashcardList(List<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }
}

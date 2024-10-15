package keycontacts.model.lesson;

/**
 * Represents a makeup lesson which happens if a student misses a lesson.
*/
public class MakeupLesson extends Lesson {

    public MakeupLesson(Day lessonDay, Time startTime, Time endTime) {
        super(lessonDay, startTime, endTime);
    }
}

package keycontacts.model;

import static java.util.Objects.requireNonNull;
import static keycontacts.model.lesson.Lesson.lessonsClash;

import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import keycontacts.commons.util.ToStringBuilder;
import keycontacts.model.lesson.ClashResult;
import keycontacts.model.lesson.Lesson;
import keycontacts.model.lesson.MakeupLesson;
import keycontacts.model.student.Student;
import keycontacts.model.student.UniqueStudentList;

/**
 * Wraps all data at the student directory level.
 * Duplicates are not allowed (by .isSameStudent comparison)
 */
public class StudentDirectory implements ReadOnlyStudentDirectory {

    private final UniqueStudentList students;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        students = new UniqueStudentList();
    }

    public StudentDirectory() {}

    /**
     * Creates a StudentDirectory using the Students in the {@code toBeCopied}
     */
    public StudentDirectory(ReadOnlyStudentDirectory toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    /**
     * Resets the existing data of this {@code StudentDirectory} with {@code newData}.
     */
    public void resetData(ReadOnlyStudentDirectory newData) {
        requireNonNull(newData);

        setStudents(newData.getStudentList());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in the student directory.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the student directory.
     * The student must not already exist in the student directory.
     */
    public void addStudent(Student p) {
        students.add(p);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the student directory.
     * The student identity of {@code editedStudent} must not be the same as another
     * existing student in the student directory.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        students.setStudent(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code StudentDirectory}.
     * {@code key} must exist in the student directory.
     */
    public void removeStudent(Student key) {
        students.remove(key);
    }

    /**
     * Checks for any clashing lessons in the existing student directory.
     * @param lessonToCheck Lesson to check for clashes with.
     * @return A {@code ClashResult} object containing the results of the clash check.
     */
    public ClashResult checkClashingLesson(Lesson lessonToCheck) {
        ObservableList<Student> students = this.getStudentList();
        for (Student student : students) {
            Lesson clashLesson = findClashingLesson(student, lessonToCheck);
            if (clashLesson != null) {
                return new ClashResult(true, clashLesson);
            }
        }
        return new ClashResult(false, null); // No clashes found
    }

    private Lesson findClashingLesson(Student student, Lesson lessonToCheck) {
        Lesson regularLesson = student.getRegularLesson();
        Set<MakeupLesson> makeupLessons = student.getMakeupLessons();

        // Check for clashes with the provided lesson against the current student's lessons
        if (lessonsClash(lessonToCheck, regularLesson)) {
            return regularLesson; // Clash with the student's regular lesson
        }

        for (MakeupLesson ml : makeupLessons) {
            if (lessonsClash(lessonToCheck, ml)) {
                return ml; // Clash with the student's makeup lesson
            }
        }

        return null; // No clashes found
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("students", students)
                .toString();
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentDirectory)) {
            return false;
        }

        StudentDirectory otherAddressBook = (StudentDirectory) other;
        return students.equals(otherAddressBook.students);
    }

    @Override
    public int hashCode() {
        return students.hashCode();
    }
}

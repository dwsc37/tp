package keycontacts.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GradeLevelTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new GradeLevel(null));
    }

    @Test
    public void constructor_invalidGradeLevel_throwsIllegalArgumentException() {
        String invalidGradeLevel = ""; //empty string
        assertThrows(IllegalArgumentException.class, () -> new GradeLevel(invalidGradeLevel));
    }

    @Test
    public void isValidGradeLevel() {
        // valid grade levels
        assertTrue(GradeLevel.isValidGradeLevel("ABRSM 1"));
        assertTrue(GradeLevel.isValidGradeLevel("Trinity 1"));
        assertTrue(GradeLevel.isValidGradeLevel("RSL 2"));
        assertTrue(GradeLevel.isValidGradeLevel("LCM 3"));
        assertTrue(GradeLevel.isValidGradeLevel("AMEB 4"));

        // invalid grade levels
        // special character
        assertFalse(GradeLevel.isValidGradeLevel("ABRSM 1!"));
        // no space
        assertFalse(GradeLevel.isValidGradeLevel("Trinity1"));
    }

    @Test
    public void equals() {
        GradeLevel gradeLevel = new GradeLevel("ABRSM 1");

        // same values -> returns true
        assertTrue(gradeLevel.equals(new GradeLevel("ABRSM 1")));

        // same object -> returns true
        assertTrue(gradeLevel.equals(gradeLevel));

        // null -> returns false
        assertFalse(gradeLevel.equals(null));

        // different types -> returns false
        assertFalse(gradeLevel.equals(5.0f));

        // different values -> returns false
        assertFalse(gradeLevel.equals(new GradeLevel("Trinity 1")));
    }
}

package tests;

import org.junit.jupiter.api.*;

import ru.arsmagna.SubField;

import static org.junit.jupiter.api.Assertions.*;

class SubFieldTest
{
    @BeforeEach
    void setUp()
    {
    }

    @AfterEach
    void tearDown()
    {
    }

    @Test
    void SubField_clone_1()
    {
        SubField first = new SubField('a', "Some text");
        first.userData = "User data";
        SubField second = first.clone();
        assertEquals(first.code, second.code);
        assertEquals(first.value, second.value);
        assertNull(second.userData);
    }

    @Test
    void SubField_compare_1()
    {
        SubField first = new SubField('a', "Some text");
        SubField second = new SubField('b', "Other text");
        assertTrue(SubField.compare(first, second) < 0);
    }

    @Test
    void SubField_compareCodes_1()
    {
        assertTrue(SubField.compareCodes('a', 'b') < 0);
        assertTrue(SubField.compareCodes('a', 'A') == 0);
    }

    @Test
    void SubField_normalize_1()
    {
        assertEquals('a', SubField.normalize('a'));
        assertEquals('a', SubField.normalize('A'));
    }

    @Test
    void SubField_toString_1()
    {
        SubField subField = new SubField('a', "Some text");
        assertEquals("^aSome text", subField.toString());
    }
}
with open("app/src/test/java/com/nivar/app/utils/LocationUtilsTest.kt", "r") as f:
    content = f.read()

content = content.replace('''        assertEquals(12.1, anonymized.latitude, 0.0)
        assertEquals(-77.45, anonymized.longitude, 0.0)
import android.Manifest''', '''        assertEquals(12.1, anonymized.latitude, 0.0)
        assertEquals(-77.45, anonymized.longitude, 0.0)
    }
}
import android.Manifest''')

# Also wait, they are two classes named LocationUtilsTest in the same file.
# Let's clean the entire file by deleting everything after the first class and just keeping the first one if we can, or just fixing the brackets.

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NoteServiceTest {
    private val noteService = NoteService<String>()

    @Before
    fun clearBefore() {
        noteService.clear()
    }

    @Test
    fun addTest() {
        val noteId = noteService.add("title", "Text", 0, 0)
        assertTrue(noteId.isNotBlank())
    }

    @Test
    fun createCommentTest() {
        val noteId = noteService.add("title", "Text", 0, 0)
        val commentId = noteService.createComment(noteId, "message")
        assertTrue(commentId?.isNotBlank() ?: false)
    }

    @Test
    fun deleteTest() {
        val noteId = noteService.add("title", "Text", 0, 0)
        val deleted = noteService.delete(noteId)
        assertEquals(1, deleted)

    }

    @Test
    fun deleteCommentTest() {
        val noteId = noteService.add("title", "Text", 0, 0)
        val commentId = noteService.createComment(noteId, "Text")
        val deleted = commentId?.let { noteService.deleteComment(commentId) } ?: 0
        assertEquals(1, deleted)
    }

    @Test
    fun restoreCommentTest() {
        val noteId = noteService.add("title", "Text", 0, 0)
        val commentId = noteService.createComment(noteId, "Text")
        commentId?.let { noteService.deleteComment(commentId) } ?: 0
        val restored = commentId?.let { noteService.restoreComment(commentId) } ?: 0
        assertEquals(1, restored)
    }

    @Test
    fun edit() {
        val noteId = noteService.add("Title", "Text", 0, 0)
        noteService.edit(noteId, "New Title", "New Text", 1, 1)
        val editedNote = noteService.get(listOf(noteId), 1).firstOrNull()
        assertEquals("New Title", editedNote?.title)
        assertEquals("New Text", editedNote?.text)
        assertEquals(1, editedNote?.privacy)
        assertEquals(1, editedNote?.commentPrivacy)
    }


    @Test
    fun editCommentTest() {
        val noteId = noteService.add("Title", "Text", 0, 0)
        val commentId = noteService.createComment(noteId, "Comment")
        assertEquals(1, noteService.editComment(commentId!!, "Updated Comment"))
        val updatedComment = noteService.getComments(noteId).firstOrNull { it.cid == commentId }
        assertEquals("Updated Comment", updatedComment?.message)
    }

    @Test
    fun get() {
        val noteId = noteService.add("Title", "Text", 0, 0)
        val retrievedNotes = noteService.get(listOf(noteId), 1)
        assertEquals(1, retrievedNotes.size)
        assertEquals(noteId, retrievedNotes[0].nid)
    }

    @Test
    fun getComments() {
        val noteId = noteService.add("Title", "Text", 0, 0)
        noteService.createComment(noteId, "Comment")
        val comments = noteService.getComments(noteId)
        assertEquals(1, comments.size)
    }
}
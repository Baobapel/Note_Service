
data class Note<T>(
    val nid: String,
    var title: String,
    var text: String,
    var privacy: Int,
    var commentPrivacy: Int,
    var deleted: Boolean = false,
    val comment: Comment<T> = Comment("", "", "", false)
)

data class Comment<T>(
    val cid: String,
    val noteId: String,
    var message: String,
    var deleted: Boolean = false
)

class NoteService<T> {
    private var notes = mutableListOf<Note<T>>()
    private var comments = mutableListOf<Comment<T>>()

    fun add(title: String, text: String, privacy: Int, commentPrivacy: Int): String {
        val nid = generateUniqueId()
        val newNote = Note<T>(nid, title, text, privacy, commentPrivacy)
        notes.add(newNote)
        return nid
    }

    fun createComment(noteId: String, message: String): String? {
        val note = notes.find { it.nid == noteId } ?: return null
        val cid = generateUniqueId()
        val newComment = Comment<T>(cid, noteId = noteId, message = message)
        comments.add(newComment)
        return cid
    }

    fun delete(noteId: String): Int {
        val note = notes.find { it.nid == noteId } ?: return 0
        return if (!note.deleted) {
            notes.remove(note)
            note.deleted = true
            // Пометим все комментарии к этой заметке как удаленные
            comments.filter { it.noteId == noteId }.forEach { it.deleted = true }
            1
        } else {
            0
        }
    }

    fun deleteComment(commentId: String): Int {
        val comment = comments.find { it.cid == commentId } ?: return 0
        comment.deleted = true
        return 1
    }

    fun restoreComment(commentId: String): Int {
        val comment = comments.find { it.cid == commentId } ?: return 0
        comment.deleted = false
        return 1
    }

    fun edit(noteId: String, title: String, text: String, privacy: Int, commentPrivacy: Int) {
        val note = notes.find { it.nid == noteId }
        note?.let {
            it.title = title
            it.text = text
            it.privacy = privacy
            it.commentPrivacy = commentPrivacy
        }
    }

    fun editComment(commentId: String, message: String): Int {
        val comment = comments.find { commentId == it.cid } ?: return 0
        if (comment.deleted) {
            println("Нельзя редактировать удаленный комментарий")
            return 0
        }
        comment.message = message
        return if (message.length >= 2) {
            1
        } else {
            println("Сообщение должно быть не короче 2 символов")
            0
        }
    }

        fun get(noteIds: List<String>, count: Int): List<Note<T>> {
            val noteList = notes.filter { it.nid in noteIds }
            return noteList.take(count)
        }

        fun getComments(noteId: String): List<Comment<T>> {
            return comments.filter { it.noteId == noteId && !it.deleted }
        }

        fun clear() {
            notes = mutableListOf<Note<T>>()
            comments = mutableListOf<Comment<T>>()
        }

    private fun generateUniqueId(): String {
        return java.util.UUID.randomUUID().toString()
    }

    }



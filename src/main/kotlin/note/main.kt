package note

data class Note(
    var nId: Int,
    var title: String = "title",
    var text: String = "text",
    var privacy: Int = 0,
    var commentPrivacy: Int = 0,
    var privacyView: String = "privacy",
    var privacyComment: String = "privacy",
    val comments: MutableList<Comment> = emptyList<Comment>() as MutableList<Comment>
) {
    override fun toString(): String {
        val str = "$title \n $text"
        return str
    }
}

data class Comment(
    var ownerId: Int,
    var replyTo: Int,
    var message: String,
    var guid: String
)

interface ServiceForGeneric<N> {
    fun add(elem: N): N
    fun delete(elem: N, deleteId: Int): Boolean
    fun edit(elem: N): Boolean
    fun get(): String
    fun restore(elem: N, deleteId: Int): Boolean
}

class CantCommentThisNoteException : RuntimeException()

object NoteService : ServiceForGeneric<Note> {

    private var notes = mutableListOf<Note>()

    override fun add(elem: Note): Note {
        //  val nid = elem.hashCode()
        notes.add(elem.copy(nId = 0))
        return notes.last()
    }

    @Throws(CantCommentThisNoteException::class)
    fun createComment(elem: Note): Int {
        for ((index, oldNote) in notes.withIndex()) {
            if (elem.nId == oldNote.nId) {
                notes[index] = elem.copy(oldNote.nId)
                return oldNote.nId
            }
        }
        throw CantCommentThisNoteException()
    }

    private var deletedNotes = mutableListOf<Note>()

    override fun delete(elem: Note, deleteId: Int): Boolean {
        if (deleteId == elem.nId) {
            //      val nid = elem.hashCode()
            deletedNotes.add(elem.copy(nId = 0))
            notes.removeAt(index = 0)
            return true
        }
        return false
    }

    override fun edit(elem: Note): Boolean {
        for ((index, oldNote) in notes.withIndex()) {
            if (elem.nId == oldNote.nId) {
                notes[index] = elem.copy(oldNote.nId)
                return true
            }
        }
        return false
    }

    override fun get(): String {
        return notes.toString()
    }

    fun getById(nId: Int): String {
        var str = notes[nId]
        return str.toString()
    }

    override fun restore(elem: Note, deleteId: Int): Boolean {
        for ((index) in notes.withIndex()) {
            if ((deleteId == elem.nId)) {
                notes.add(elem.copy(index))
                deletedNotes.removeAt(index)
                return true
            }
        }
        return false
    }
}

class CommentService : ServiceForGeneric<Comment> {
    override fun add(elem: Comment): Comment {
        TODO("Not yet implemented")
    }

    override fun delete(elem: Comment, deleteId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun edit(elem: Comment): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(): String {
        TODO("Not yet implemented")
    }

    override fun restore(elem: Comment, deleteId: Int): Boolean {
        TODO("Not yet implemented")
    }
}

fun main() {
    NoteService.add(Note(0))
    println(NoteService.get())
    NoteService.edit(Note(nId = 0, title = "title2", text = "text2"))
    println(NoteService.get())
    println(NoteService.getById(0))
}


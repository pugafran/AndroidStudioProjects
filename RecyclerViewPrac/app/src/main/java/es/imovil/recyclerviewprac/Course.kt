package es.imovil.recyclerviewprac

class Course {


    //Crear una nueva clase de datos (Course) para albergar información relacionada con una asignatura: nombre y profesor. Serán dos cadenas de texto.

    var name: String = ""
    var teacher: String = ""

    constructor(name: String, teacher: String) {
        this.name = name
        this.teacher = teacher
    }

    constructor() {
        this.name = ""
        this.teacher = ""
    }

    override fun toString(): String {
        return "Course(name='$name', teacher='$teacher')"
    }

    companion object {
        val courses = arrayOf(
            Course("Matemáticas", "Juan"),
            Course("Lengua", "María"),
            Course("Inglés", "John"),
            Course("Física", "Alberto"),
            Course("Química", "Ana"),
            Course("Biología", "Luis"),
            Course("Historia", "Carlos"),
            Course("Geografía", "Elena"),
            Course("Filosofía", "Sofía"),
            Course("Educación Física", "Pedro")
        )
    }


}
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
        fun createCourseList(asignaturas: Array<String>, profesores: Array<String>): List<Course> {
            val courses = mutableListOf<Course>()
            if (asignaturas.size == profesores.size) {
                // Añadimos a la lista un objeto Course tomando un
                // elemento el array de asignaturas y otro de profesores
                // Sugerencia: usar la función zip
                for ((asignatura, profesor) in asignaturas.zip(profesores)) {
                    courses.add(Course(asignatura, profesor))
                }
            }
            return courses.toList()
        }
    }


}
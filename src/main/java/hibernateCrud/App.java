package hibernateCrud;

public class App {

	public static void main(String[] args) {

		InterfaceStudents dao = new StudentDao();

		Student student = new Student("gdu", "csn", "testo@01.com");

		// save student
		dao.saveStudent(student);

		// update student
		student.setEmail("newemail@0.com");
		dao.updateStudent(student);

		// get student by id
		Student getStudent = dao.getStudentById(1);
		System.out.println(getStudent);

		// delete student
		dao.deleteStudent(1);

	}

}

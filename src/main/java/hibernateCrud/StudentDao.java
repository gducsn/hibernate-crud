package hibernateCrud;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StudentDao implements InterfaceStudents {

	@Override
	public void saveStudent(Student student) {
		Transaction transaction = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(student);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateStudent(Student student) {
		Transaction transaction = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(student);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Student getStudentById(long id) {
		Transaction transaction = null;
		Student student = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			student = session.get(Student.class, (int) id);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return student;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Student> getAllStudents() {
		Transaction transaction = null;
		List<Student> students = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			students = session.createQuery("from Student").list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return students;
	}

	@Override
	public void deleteStudent(long id1) {
		Transaction transaction = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Student stdStudent = session.load(Student.class, (int) id1);
			session.remove(stdStudent);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
}
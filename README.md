![anim-opt](https://user-images.githubusercontent.com/94108883/176179501-6ce4223b-b38e-478a-8632-aa8baf2aef86.gif)

# Hibernate - crud

Gestione dell’operazioni CRUD (Create, Read, Update, Delete) utilizzando 
maven, hibernate, notazioni JPA e mysql.

Struttura:

- pom.xml
- hibernate.cfg.xml
- Student.java
- HibernateUtils.java
- StudentDao.java / InterfaceStudent.java
- App.java

---

pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.exe</groupId>
	<artifactId>Student_01</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<properties>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
	</properties>

	<dependencies>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.28</version>
		</dependency>

		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>5.6.5.Final</version>
		</dependency>

	</dependencies>
</project>
```

Il file pom.xml descrive il nostro progetto, le sue dipendenze e tutte le 
configurazioni più utili per ogni evenienza.

La parte iniziale del pom definisce alcuni degli aspetti principali del 
nostro progetto. Ad esempio il ‘gropupID’ identica in modo univoco il 
nostro progetto tra tutti i progetti. Un ID gruppo dovrebbe seguire le 
regole del nome come per i pacchetti di Java. Questo significa che 
dovrebbe seguire il nome di dominio invertito (com.example).

Abbiamo anche ‘artifactId’ che definisce il nome del nostro file finale e 
‘version’ in cui definiamo la versione del nostro progetto.

Inoltre abbiamo la possibilità di decidere quale versione del compilatore 
usare nel nostro progetto, in questo caso la versione 1.8.

Il file pom.xml contiene principalmente la definizione delle nostre 
dipendenze, cioè tutte quelle librerie esterne che ci permettono di far 
funzionare il nostro progetto.

In questo caso sono installate due: mysql e hibernate.

In entrambe sono definiti alcuni aspetti, come il groupID, artifact e la 
versione. Ogni aspetto è importate.

---

hibernate.cfg.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE hibernate-configuration PUBLIC
    "-//Hibernate/Hibernate Configuration DTD//EN"
    "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

<hibernate-configuration>

	<session-factory>
		<property 
name="dialect">org.hibernate.dialect.MySQL5Dialect</property>
		<property 
name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
		<property 
name="connection.url">jdbc:mysql://localhost:3306/hibernate</property>
		<property name="connection.username">root</property>
		<property name="connection.password">root</property>
		<property name="show_sql">true</property>
		<property name="format_sql">true</property>
		<property name="hbm2ddl.auto">create-drop</property>
		<mapping class="hibernateCrud.Student"></mapping>

	</session-factory>

</hibernate-configuration>
```

Il file hibernate.cfg.xml ci permette di configurare ogni parametro per la
corretta connessione al database.

Per poterci connettere con hibernate al nostro database c’è bisogno prima 
di tutto di definire il dialect, il quale specifica che tipo di database 
stiamo usando e che tipo di query dovrà fare hibernate.

Successivamente c’è bisogno di selezione il giusto driver il quale cambia 
in base alla versione di mysql in uso.

Fatto questo dobbiamo definire l’url del database, username e password.

Possiamo anche decidere se far comparire in consolle la query fatta da 
hibernate al database con la proprietà 
‘[show_sql](https://mkyong.com/hibernate/hibernate-display-generated-sql-to-console-show_sql-format_sql-and-use_sql_comments/)’ 
attiva. E’ possibile anche formattare la query con una seconda proprietà 
‘format_sql’ attiva.

Per questa applicazione abbiamo dichiarato, tramite la proprietà 
“hbm2ddl.auto”, in che modo viene gestito lo schema durante il ciclo di 
vita del SessionFactory.

Sono varie le proprietà:

- create: crea lo schema, distruggendo i dati precedenti.
- validate: valida e non cambia nulla nel DB.
- update: aggiorna lo schema nel DB.
- create-drop: svuota lo schema quando il SessionFactory è chiuso 
esplicitamente, di solito quando l’applicazione viene chiusa.

Nel nostro caso abbiamo utilizzando il valore ‘create-drop’ così quando la 
sessione viene esplicitamente chiusa lo schema viene svuotato. 

Infine dobbiamo mappare il nostro database collegandolo alla classe 
specifica per questo compito.

---

Student.java 

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "students", schema = "hibernate_crud")

public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "first_name")
	private String firstname;

	@Column(name = "last_name")
	private String lastname;

	@Column(name = "email")
	private String email;

	public Student() {
	}

	public Student(String firstname, String lastname, String email) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
	}
}

// get/set non inseriti
```

Per dichiarare una classe come entità si usa l’apposita annotation subito 
prima della dichiarazione della classe: “@Entity”.

Con una semplice notazione abbiamo definito la nostra classe come 
un’entità persistente.

Ora dobbiamo collegare la tabella del nostro database. Lo facciamo 
utilizzando la notazione “@Table” scrivendo il nome della tabella che 
vogliamo collegare, in questo caso student.

Inoltre abbiamo definito lo schema a cui fare riferimento con l’apposita 
proprietà.

Ora possiamo mappare le colonne del nostro database utilizzando la 
notazione “@Column”. Quello che scriviamo nell’attributo ‘name’ risulta 
essere case sensitive, quindi “FirstName” è decisamente diverso da 
“firstname”.

Il nome della colonna nel table del database deve essere uguale a quello 
scritto nella notazione.

E’ importante definire l’id come chiave principale del nostro database. 
Infatti, non basterà utilizzare la notazione “@Id” per definire 
automaticamente la chiave principale del database. Per farlo dobbiamo 
utilizzare la notazione “@GeneratedValue” la quale ci mette a disposizione 
4 strategie per definire il tipo per la generazione della chiave 
principale:

- AUTO : hibernate decide autonomamente il tipo in base al dialect in 
utilizzo.
- IDENTITY : hibernate aumenterà automaticamente la colonna ID per noi. 
Dal punto di vista del database è molto efficace ma Hibernate richiede un 
valore di chiave primaria per ciascuna entità gestita e pertanto deve 
eseguire immediatamente l'istruzione di INSERT. Ciò gli impedisce di 
utilizzare diverse tecniche di ottimizzazione.
- SEQUENCE : è il tipo di generazione raccomandata nella documentazione di 
hibernate. C’è bisogno di un ulteriore configurazione.
- TABLE : tipo di generazione usata meno. Questo tipo mantiene una tabella 
separata con i valori della chiave primaria.

---

HibernateUtils.java

```java
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {

	private static SessionFactory sessionFactory = 
buildSessionFactory();

	private static SessionFactory buildSessionFactory() {

		try {
			if (sessionFactory == null) {
				StandardServiceRegistry standardRegistry = 
new StandardServiceRegistryBuilder().configure().build();

				Metadata metadata = new 
MetadataSources(standardRegistry).getMetadataBuilder().build();

				sessionFactory = 
metadata.getSessionFactoryBuilder().build();
			}
			return sessionFactory;
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	};

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	};

}
```

C’è bisogno di poter utilizzare il file di configurazione per creare una 
nuova sessione.

È necessario creare uno StandardServiceRegistry, creare un oggetto 
Metadata e utilizzarlo per creare un'istanza di SessionFactory.

Utilizziamo StandardServiceRegistryBuilder per creare un'istanza di 
StandardServiceRegistry, successivamente carichiamo una configurazione da 
un file di risorse, quindi il nostro hibernate.cfg.xml e infine invochiamo 
il metodo build() per ottenere un'istanza di StandardServiceRegistry.

In questo modo l'implementazione è semplificata e ci consente di 
modificare la configurazione senza modificare il codice sorgente. 
Hibernate carica automaticamente il file di configurazione dal percorso di 
classe quando chiama il metodo configure su 
StandardServiceRegistryBuilder.

Dopo aver creato un'istanza di un ServiceRegistry configurato, è 
necessario creare una rappresentazione dei metadati del modello di 
dominio.

Per prima cosa utilizziamo ServiceRegistry creato nel passaggio precedente 
per creare un'istanza di un nuovo oggetto MetadataSources.

Una volta istanziato un nuovo oggetto possiamo utilizzarlo per istanziare 
una nuova sessione.

In breve:

Abbiamo creato un nuovo servizio al quale colleghiamo il nostro file di 
configurazione.

Una volta creato dobbiamo inserirlo nell’oggetto metadata. Questo oggetto 
ci permetterà di istanziare una nuova sessione e avere l’effettiva 
connessione.

Infine possiamo creare un nuovo metodo che ci permette di ritornare la 
sessione quando ne abbiamo bisogno:

```java
public static SessionFactory getSessionFactory() {
		return sessionFactory;
	};
```

---

StudentDao.java / InterfaceStudent.java

```java
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StudentDao implements InterfaceStudents {

	@Override
	public void saveStudent(Student student) {
		Transaction transaction = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
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
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
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
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
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
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			students = session.createQuery("from 
Student").list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return students;
	}

	@Override
	public void deleteStudent(long id1) {
		Transaction transaction = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Student stdStudent = session.load(Student.class, 
(int) id1);
			session.remove(stdStudent);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
}
```

L’interfaccia è una classe che contiene solo metodi astratti, cioè metodi 
dichiarati ma non implementati.

L’implementazione di questi metodi avviene nella classe StudentDao.java e 
quindi la notazione @Override.

La notazione 
@[SuppressWarnings](https://docs.oracle.com/javase/8/docs/api/java/lang/SuppressWarnings.html)("unchecked") 
indica al compilatore che il programmatore ritiene che il codice sia 
sicuro e non causerà eccezioni impreviste. 

Analizziamo ogni metodo. Prime le cose in comune tra loro.

Ogni connessione è gestita dal blocco try/catch. In questo caso si dice 
‘try with resources’. Questo vuol dire che il blocco try gestisce una 
risorsa, la nostra connessione. Quando non abbiamo più bisogno della 
nostra connessione il blocco la chiude automaticamente. 

Tutti i metodi sono gestiti in questo modo.

---

Il primo riguarda l’inserimento dell’utente nel database:

```java
@Override
	public void saveStudent(Student student) {
		Transaction transaction = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(student);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
```

Il metodo prende come argomento uno student di tipo Student, cioè 
l’oggetto che vogliamo inserire nel database. L’interfaccia ‘Transaction’ 
rappresenta un’unità di lavoro ed è associata alla sessione. Ha molti 
metodi utili, come .beginTransaction() che ci permette di iniziare una 
nuova transazione e restituire il proprio oggetto.

Per iniziare un’effettiva ‘collaborazione’ tra il nostro database e le 
nostre intenzioni c’è bisogno quindi di avere un oggetto Session e uno 
Transaction.

[Session](https://docs.jboss.org/hibernate/orm/3.5/javadocs/org/hibernate/Session.html) 
è la principale interfaccia tra Java e Hibernate. 

Citando la documentazione:

<<The main function of the Session is to offer create, read and delete 
operations for instances of mapped entity classes. Instances may exist in 
one of three states:

- transient: never persistent, not associated with any Session
- persistent: associated with a unique Session
- detached: previously persistent, not associated with any Session

Transient instances may be made persistent by calling save(), persist() or 
saveOrUpdate(). Persistent instances may be made transient by calling 
delete(). Any instance returned by a get() or load() method is persistent. 
Detached instances may be made persistent by calling update(), 
saveOrUpdate(), lock() or replicate(). The state of a transient or 
detached instance may also be made persistent as a new persistent instance 
by calling merge().

save() and persist() result in an SQL INSERT, delete() in an SQL DELETE 
and update() or merge() in an SQL UPDATE. Changes to persistent instances 
are detected at flush time and also result in an SQL UPDATE. 
saveOrUpdate() and replicate() result in either an INSERT or an UPDATE>>

L’istanza della nostra entità può essere in tre stati:

- transient: non persistente e mai associata ad alcuna sessione
- persistent: associata ad una sessione univoca
- detached: prima persistente, adesso non associata ad alcuna Session

Ogni istanza ritornata dal metodo .get() o .load() è persistente. Istanze 
non persistenti potrebbero diventarle utilizzando il metodo .update(), 
saveOrUpdate(), lock() o replicate().

Lo stato di un'istanza transitoria o distaccata può anche essere reso 
persistente come una nuova istanza persistente chiamando merge().

Inoltre i metodi save(), persist() risultano nel SQL come INSERT, delete() 
come SQL DELETE e update() o merge() come SQL UPDATE. saveOrUpdate() and 
replicate() risulta come INSERT o UPDATE.

Quindi

```java
session.save(student);
```

Abbiamo l’istanza di Student che non è persistente ma transiente, cioè mai 
associata ad alcuna Session. Vogliamo renderla persistente e passarla al 
database, lo facciamo tramite il metodo .save(student), il quale prima 
assegna alla nostra istanza un identificativo univoco e poi rende la 
nostra istanza persistente.

Infine, dobbiamo chiudere la nostra transazione:

```java
transaction.commit();
```

In realtà il metodo commit() ha due funzioni importanti. Prima di chiudere 
la sessione avviene lo svuotamento, cioè il processo di sincronizzazione 
dell'archivio persistente con lo stato persistente mantenuto in memoria. 
Infine avviene la chiusura dell’unità di lavoro.

---

Il secondo metodo ci serve per aggiornare il nostro utente.

```java
@Override
	public void updateStudent(Student student) {
		Transaction transaction = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(student);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
```

La struttura è come il precedente, cambia solo il metodo della sessione 
che in questo caso risulta essere .saveOrUpdate(). 
Il metodo risulta essere come una query UPDATE in sql.

---

Prendere lo studente con id associato dal database.

```java
@Override
	public Student getStudentById(long id) {
		Transaction transaction = null;
		Student student = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			student = session.get(Student.class, (int) id);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return student;
	}
```

Ricordiamo che ogni istanza ritornata dal metodo .get() o .load() è 
persistente.

Il metodo 
.[get](https://docs.jboss.org/hibernate/orm/3.5/javadocs/org/hibernate/Session.html)() 
ritorna lo studente che cerchiamo e richiede due argomenti:

La classe associata all’istanza e i un suo identificativo, in questo caso 
l’id.

Il metodo .getStudentById() ha come argomento l’id di tipo 
[long](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html#:~:text=long%3A%20The%20long%20data%20type,value%20of%20264%2D1.) 
(’`Thread.currentThread().getId()`return a long’, quindi 64bit). 
Quello che avviene nel metodo .get() è il cast dell’argomento ‘id’, in 
modo che potremmo passare anche direttamente un numero per recuperare la 
nostra istanza persistente.

---

Ritornare tutti gli studenti.

```java
@Override
	@SuppressWarnings("unchecked")
	public List<Student> getAllStudents() {
		Transaction transaction = null;
		List<Student> students = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			students = session.createQuery("from 
Student").list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return students;
	}
```

Creiamo al di fuori del blocco try due istanze, una con l’interfaccia 
Transaction e l’altra la lista di tipo Student. Entrambe con valore null. 
Le gestiamo nel blocco try.

Ritorniamo le istanze persistenti dal database assegnandole alla lista 
students con il metodo  createQuery() passando come argomento la query che 
vogliamo fare. Il metodo viene eseguito se chiamato il metodo 
[.list()](https://docs.jboss.org/hibernate/orm/3.2/api/org/hibernate/Query.html).

```java
@SuppressWarnings("unchecked")
```

Abbiamo bisogno di questa notazione perché con il metodo .list() associato 
al metodo createQuery(), hibernate ritorna una lista raw, cioè una lista 
che potrebbe contenere qualsiasi cosa. Noi diciamo al compilatore che è 
sicuro e quindi non deve inalzare errori.

---

Cancellare studente.

```java
@Override
	public void deleteStudent(long id1) {
		Transaction transaction = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Student stdStudent = session.load(Student.class, 
(int) id1);
			session.remove(stdStudent);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
```

 

Utilizziamo il metodo load perché siamo sicuri di avere l’istanza 
desiderata nel database. Passiamo due argomenti, la classe equivalente 
dell’istanza voluta e l’id per identificarlo.

Infine, una volta assegnato lo studente che vogliamo eliminare in un 
oggetto di tipo student possiamo usare il metodo .remove() per eliminare 
l’istanza persiste dal database.

[Differenza .load() e 
.get().](https://stackoverflow.com/questions/5370482/whats-the-advantage-of-load-vs-get-in-hibernate)

---

Interfaccia della classe Dao.

```java
public interface InterfaceStudents {

	void saveStudent(Student student);

	void updateStudent(Student student);

	Student getStudentById(long id);

	List<Student> getAllStudents();

	void deleteStudent(long id1);

}
```

---

App.java

```java
public class App {

	public static void main(String[] args) {

		InterfaceStudents dao = new StudentDao();

		Student student = new Student("gdu", "csn", 
"testo@01.com");

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
```

La classe App ci permette di avviare l’applicazione e di utilizzare i 
metodi. Istanziamo la nostra interfaccia così da avere i metodi a 
disposizione. Creiamo una nuova istanza del nostro Student.

Possiamo salvare il nostro student con il metodo .saveStudent().

Possiamo aggiornarlo successivamente, recuperarlo e infine eliminarlo.

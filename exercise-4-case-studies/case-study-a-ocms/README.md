# Case Study A - Online Course Management System (OCMS)

Spring Boot REST API (`backend`) with a React front end (`frontend`).

## Modules

| Module | Backend | React page |
|--------|---------|------------|
| Student - register, update details, enroll, track progress, completed courses | `UserService`, `EnrollmentService` | Register, Profile, My learning |
| Course - create and manage courses, videos / PDFs / quizzes, list all courses | `CourseService`, `CourseController` | Courses, My courses (instructor) |
| Instructor - register, manage own courses, see enrolled students and their performance | `CourseService.students()` | My courses, Students of a course |
| Enrollment - student to course link, date, status (ongoing / completed), history | `Enrollment` entity, `EnrollmentService` | My learning |
| Notification - new course, course update, reminders, new enrollment | `NotificationService`, `ReminderScheduler` | Notifications (badge in the menu) |
| Authentication and roles - sign up / login, role based access, secured API | `SecurityConfig`, `JwtService`, `JwtAuthFilter` | Login, Register |

## How it behaves

- Two roles: `STUDENT` and `INSTRUCTOR`, chosen when registering. Login returns a JWT that the React app sends in the `Authorization` header.
- Only an instructor can create a course, and only the instructor who owns it can change it, add content or see its students (`@PreAuthorize` plus an owner check). Only students can enroll.
- A student ticks content items as done; progress is `done / total`. When every item is done the enrollment becomes `COMPLETED`; adding new content to the course puts it back to `ONGOING`.
- Notifications: all students are told about a new course, enrolled students about updates and new content, the instructor about a new enrollment.
- Reminders: a scheduled job tells every student with pending content. The schedule is `ocms.reminder.cron` in `application.properties` (default 9:00 every day).

## Run

Backend (port 8080, needs JDK 17+ and Maven):

```
cd backend
mvn spring-boot:run
```

Front end (port 5173, needs Node 20.19+ or 22.12+), in another terminal:

```
cd frontend
npm install
npm run dev
```

Open http://localhost:5173 and register an instructor and a student (use two browsers or log out between them).

The data is kept in `backend/data` (H2 database file). To use MySQL change the four `spring.datasource` / `ddl` lines in `application.properties` and add the MySQL driver to `pom.xml`.

## API

| Method and path | Who |
|-----------------|-----|
| `POST /api/auth/register`, `POST /api/auth/login` | everyone |
| `GET /api/users/me`, `PUT /api/users/me` | logged in |
| `GET /api/courses`, `GET /api/courses/{id}` | logged in |
| `GET /api/courses/mine`, `POST /api/courses` | instructor |
| `PUT / DELETE /api/courses/{id}` | owner |
| `POST /api/courses/{id}/contents`, `DELETE /api/courses/{id}/contents/{contentId}` | owner |
| `GET /api/courses/{id}/students` | owner |
| `POST /api/enrollments`, `GET /api/enrollments/mine` | student |
| `POST / DELETE /api/enrollments/{id}/contents/{contentId}/complete` | student |
| `GET /api/notifications`, `GET /api/notifications/unread-count`, `POST /api/notifications/{id}/read`, `POST /api/notifications/read-all` | logged in |

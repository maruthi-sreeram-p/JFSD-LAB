import { Navigate, Outlet, Route, Routes } from 'react-router-dom';
import { useAuth } from './AuthContext.jsx';
import Navbar from './components/Navbar.jsx';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import Courses from './pages/Courses.jsx';
import MyLearning from './pages/MyLearning.jsx';
import Profile from './pages/Profile.jsx';
import Notifications from './pages/Notifications.jsx';
import InstructorCourses from './pages/InstructorCourses.jsx';
import CourseStudents from './pages/CourseStudents.jsx';

// pages that need a logged in user
function RequireAuth() {
  const { user, loading } = useAuth();
  if (loading) {
    return <p className="center muted">Loading...</p>;
  }
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  return (
    <>
      <Navbar />
      <main className="container">
        <Outlet />
      </main>
    </>
  );
}

// pages for one role only (the server checks this too)
function RequireRole({ role }) {
  const { user } = useAuth();
  return user.role === role ? <Outlet /> : <Navigate to="/" replace />;
}

// "/" sends each role to its own start page
function Home() {
  const { user, loading } = useAuth();
  if (loading) {
    return <p className="center muted">Loading...</p>;
  }
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  return <Navigate to={user.role === 'INSTRUCTOR' ? '/instructor/courses' : '/courses'} replace />;
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route element={<RequireAuth />}>
        <Route path="/courses" element={<Courses />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/notifications" element={<Notifications />} />

        <Route element={<RequireRole role="STUDENT" />}>
          <Route path="/my-learning" element={<MyLearning />} />
        </Route>

        <Route element={<RequireRole role="INSTRUCTOR" />}>
          <Route path="/instructor/courses" element={<InstructorCourses />} />
          <Route path="/instructor/courses/:id/students" element={<CourseStudents />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

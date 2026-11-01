# Case Study B - Smart Health Monitoring System (SHMS)

Spring Boot REST API (`backend`) with a React front end (`frontend`). Doctors watch the vital signs of their patients on a dashboard that refreshes by itself.

## Modules

| Module | Backend | React page |
|--------|---------|------------|
| Patient - register and update personal and medical details | `PatientService`, `PatientController` | Patients, Patient page |
| Vital stats - heart rate, blood pressure, oxygen level, temperature | `VitalService`, `VitalReading` | "Record vital signs" and "Reading history" on the patient page |
| Alert - raised when a value is outside the safe range | `SafeRanges`, `AlertService` | Alerts, open alerts on the dashboard |
| Doctor - assign doctors to patients, dashboard | `DoctorService` | Doctors, Doctor dashboard |

## Safe ranges

The rules are in `service/SafeRanges.java`. A value outside the safe range raises an alert, a value far outside it raises a critical one.

| Value | Safe range | Critical when |
|-------|-----------|---------------|
| Heart rate | 60 - 100 bpm | below 40 or above 130 |
| Systolic pressure | 90 - 140 mmHg | below 80 or above 180 |
| Diastolic pressure | 60 - 90 mmHg | below 50 or above 120 |
| Oxygen level | 95 - 100 % | below 90 |
| Temperature | 36.1 - 37.5 C | below 35.0 or above 39.0 |

These are general adult values; a doctor may want different limits for a particular patient.

## How it behaves

- Saving a reading checks every value and creates one alert for each value that is out of range. The answer to the request lists the alerts, and the page shows them.
- The dashboard belongs to one doctor: it lists the doctor's patients with their latest vitals, the sickest first (critical, warning, stable, no readings), and the alerts nobody has acknowledged yet. The page asks the backend again every 5 seconds, so a new reading shows up without reloading.
- A patient's status on the dashboard comes from the latest reading, so it goes back to stable when the values are normal again; old alerts stay open until a doctor acknowledges them.

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

Open http://localhost:5173. First add a doctor (Doctors), register a patient (Patients), open the patient and choose the doctor, then record readings and watch the Doctor dashboard.

The data is kept in `backend/data` (H2 database file). To use MySQL change the `spring.datasource` / `ddl` lines in `application.properties` and add the MySQL driver to `pom.xml`.

## API

| Method and path | Purpose |
|-----------------|---------|
| `GET /api/patients`, `GET /api/patients/{id}` | list / read patients |
| `POST /api/patients`, `PUT /api/patients/{id}` | register / update details |
| `PUT /api/patients/{id}/doctor` | assign a doctor (`{"doctorId": null}` removes it) |
| `POST /api/patients/{id}/vitals` | record a reading, returns the reading and the alerts it raised |
| `GET /api/patients/{id}/vitals?limit=20` | reading history, newest first |
| `GET /api/doctors`, `POST /api/doctors` | list / add doctors |
| `GET /api/doctors/{id}/dashboard` | patients with latest vitals + open alerts |
| `GET /api/alerts?doctorId=&patientId=&open=true` | alerts |
| `POST /api/alerts/{id}/acknowledge` | mark an alert as seen |

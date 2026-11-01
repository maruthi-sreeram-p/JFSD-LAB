import { useState } from 'react';

const EMPTY = {
  name: '',
  age: '',
  gender: 'Female',
  contact: '',
  address: '',
  bloodGroup: '',
  medicalHistory: '',
  allergies: '',
};

// personal and medical details of a patient - used to register and to update
export default function PatientForm({ initial, submitLabel, onSubmit, onCancel }) {
  const [form, setForm] = useState({ ...EMPTY, ...initial });
  const [busy, setBusy] = useState(false);

  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setBusy(true);
    try {
      await onSubmit({ ...form, age: Number(form.age) });
    } finally {
      setBusy(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h4>Personal details</h4>
      <div className="form-grid">
        <label>
          Name
          <input name="name" value={form.name} onChange={change} required />
        </label>
        <label>
          Age
          <input name="age" type="number" min="0" max="130" value={form.age} onChange={change} required />
        </label>
        <label>
          Gender
          <select name="gender" value={form.gender} onChange={change}>
            <option>Female</option>
            <option>Male</option>
            <option>Other</option>
          </select>
        </label>
        <label>
          Contact number
          <input name="contact" value={form.contact || ''} onChange={change} />
        </label>
      </div>
      <label>
        Address
        <input name="address" value={form.address || ''} onChange={change} />
      </label>

      <h4>Medical details</h4>
      <label>
        Blood group
        <select name="bloodGroup" value={form.bloodGroup || ''} onChange={change}>
          <option value="">Not known</option>
          {['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'].map((g) => (
            <option key={g}>{g}</option>
          ))}
        </select>
      </label>
      <label>
        Medical history
        <textarea name="medicalHistory" rows="3" value={form.medicalHistory || ''} onChange={change} />
      </label>
      <label>
        Allergies
        <input name="allergies" value={form.allergies || ''} onChange={change} />
      </label>

      <div className="actions">
        <button className="btn" disabled={busy}>
          {busy ? 'Saving...' : submitLabel}
        </button>
        {onCancel && (
          <button type="button" className="btn ghost" onClick={onCancel}>
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}

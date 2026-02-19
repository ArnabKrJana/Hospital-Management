INSERT INTO doctor (id, full_name, specialization, email)
VALUES
    (gen_random_uuid(), 'Dr. Arnab Roy', 'CARDIOLOGY', 'arnab.cardio@gmail.com'),
    (gen_random_uuid(), 'Dr. Sen Gupta', 'NEUROLOGY', 'sen.neuro@gmail.com');

INSERT INTO patient (id, first_name, last_name, gender, email)
VALUES
    (gen_random_uuid(), 'Rahul', 'Sharma', 'MALE', 'rahul@gmail.com');

INSERT INTO department (id, department_name, doctor_department_head_id)
VALUES
    (gen_random_uuid(), 'Cardiology',
     (SELECT id FROM doctor WHERE email='arnab.cardio@gmail.com'));

INSERT INTO insurance (id, provider, policy_number, issued, insurance_date, patient_insurance_id)
VALUES
    (gen_random_uuid(), 'HDFC', 123456789, true, NOW(),
     (SELECT id FROM patient WHERE email='rahul@gmail.com'));

INSERT INTO appointment (
    id,
    category,
    reason_description,
    appointment_date_time,
    patient_appointment_id,
    doctor_appointment_id
)
VALUES
    (
        gen_random_uuid(),
        'GENERAL_CHECKUP',
        'Regular heart checkup',
        NOW() + INTERVAL '2 days',
        (SELECT id FROM patient WHERE email='rahul@gmail.com'),
        (SELECT id FROM doctor WHERE email='arnab.cardio@gmail.com')
    );

INSERT INTO doctor_department_table (department_id, doctor_id)
VALUES
    (
        (SELECT id FROM department WHERE department_name='Cardiology'),
        (SELECT id FROM doctor WHERE email='arnab.cardio@gmail.com')
    );

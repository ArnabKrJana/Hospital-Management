-- ===================================================================================
-- 1. INSERT DOCTORS (15 Records)
-- Enums: CARDIOLOGY, NEUROLOGY, ORTHOPEDICS, PEDIATRICS, DERMATOLOGY, GYNECOLOGY, 
-- PSYCHIATRY, ONCOLOGY, RADIOLOGY, GENERAL_SURGERY
-- ===================================================================================
INSERT INTO doctor (id, full_name, specialization, email)
VALUES (gen_random_uuid(), 'Dr. Arnab Roy', 'CARDIOLOGY', 'arnab.cardio@gmail.com'),
       (gen_random_uuid(), 'Dr. Sen Gupta', 'NEUROLOGY', 'sen.neuro@gmail.com'),
       (gen_random_uuid(), 'Dr. Priya Das', 'ORTHOPEDICS', 'priya.ortho@gmail.com'),
       (gen_random_uuid(), 'Dr. John Smith', 'PEDIATRICS', 'john.peds@gmail.com'),
       (gen_random_uuid(), 'Dr. Emily Stone', 'DERMATOLOGY', 'emily.derma@gmail.com'),
       (gen_random_uuid(), 'Dr. Michael Brown', 'GYNECOLOGY', 'michael.gyn@gmail.com'),
       (gen_random_uuid(), 'Dr. Sarah Wilson', 'PSYCHIATRY', 'sarah.psych@gmail.com'),
       (gen_random_uuid(), 'Dr. David Lee', 'ONCOLOGY', 'david.onco@gmail.com'),
       (gen_random_uuid(), 'Dr. Linda Chen', 'RADIOLOGY', 'linda.radio@gmail.com'),
       (gen_random_uuid(), 'Dr. Robert King', 'GENERAL_SURGERY', 'robert.surg@gmail.com'),
       -- Staff Doctors (Not Heads)
       (gen_random_uuid(), 'Dr. Alice Cooper', 'CARDIOLOGY', 'alice.c@gmail.com'),
       (gen_random_uuid(), 'Dr. Bob Marley', 'NEUROLOGY', 'bob.m@gmail.com'),
       (gen_random_uuid(), 'Dr. Charlie Puth', 'ORTHOPEDICS', 'charlie.p@gmail.com'),
       (gen_random_uuid(), 'Dr. Dua Lipa', 'PEDIATRICS', 'dua.l@gmail.com'),
       (gen_random_uuid(), 'Dr. Elton John', 'GENERAL_SURGERY', 'elton.j@gmail.com');

-- ===================================================================================
-- 2. INSERT DEPARTMENTS (10 Records)
-- Each department requires a "Head Doctor". We link this using the emails above.
-- ===================================================================================
INSERT INTO department (id, department_name, doctor_department_head_id)
VALUES (gen_random_uuid(), 'Cardiology', (SELECT id FROM doctor WHERE email = 'arnab.cardio@gmail.com')),
       (gen_random_uuid(), 'Neurology', (SELECT id FROM doctor WHERE email = 'sen.neuro@gmail.com')),
       (gen_random_uuid(), 'Orthopedics', (SELECT id FROM doctor WHERE email = 'priya.ortho@gmail.com')),
       (gen_random_uuid(), 'Pediatrics', (SELECT id FROM doctor WHERE email = 'john.peds@gmail.com')),
       (gen_random_uuid(), 'Dermatology', (SELECT id FROM doctor WHERE email = 'emily.derma@gmail.com')),
       (gen_random_uuid(), 'Gynecology', (SELECT id FROM doctor WHERE email = 'michael.gyn@gmail.com')),
       (gen_random_uuid(), 'Psychiatry', (SELECT id FROM doctor WHERE email = 'sarah.psych@gmail.com')),
       (gen_random_uuid(), 'Oncology', (SELECT id FROM doctor WHERE email = 'david.onco@gmail.com')),
       (gen_random_uuid(), 'Radiology', (SELECT id FROM doctor WHERE email = 'linda.radio@gmail.com')),
       (gen_random_uuid(), 'General Surgery', (SELECT id FROM doctor WHERE email = 'robert.surg@gmail.com'));

-- ===================================================================================
-- 3. INSERT PATIENTS (12 Records)
-- Enums: MALE, FEMALE, OTHERS
-- ===================================================================================
INSERT INTO patient (id, first_name, last_name, gender, email, registration_date_time)
VALUES (gen_random_uuid(), 'Rahul', 'Sharma', 'MALE', 'rahul@gmail.com', NOW()),
       (gen_random_uuid(), 'Anjali', 'Verma', 'FEMALE', 'anjali@gmail.com', NOW()),
       (gen_random_uuid(), 'Rohan', 'Das', 'MALE', 'rohan@gmail.com', NOW()),
       (gen_random_uuid(), 'Priya', 'Singh', 'FEMALE', 'priya@gmail.com', NOW()),
       (gen_random_uuid(), 'Amit', 'Patel', 'MALE', 'amit@gmail.com', NOW()),
       (gen_random_uuid(), 'Sneha', 'Gupta', 'FEMALE', 'sneha@gmail.com', NOW()),
       (gen_random_uuid(), 'Vikram', 'Malhotra', 'MALE', 'vikram@gmail.com', NOW()),
       (gen_random_uuid(), 'Neha', 'Kapoor', 'FEMALE', 'neha@gmail.com', NOW()),
       (gen_random_uuid(), 'Arjun', 'Reddy', 'MALE', 'arjun@gmail.com', NOW()),
       (gen_random_uuid(), 'Pooja', 'Mishra', 'FEMALE', 'pooja@gmail.com', NOW()),
       (gen_random_uuid(), 'Karan', 'Johar', 'MALE', 'karan@gmail.com', NOW()),
       (gen_random_uuid(), 'Deepika', 'Padukone', 'FEMALE', 'deepika@gmail.com', NOW());

-- ===================================================================================
-- 4. INSERT INSURANCE (12 Records - 1 per Patient)
-- Enums: HDFC, ICICIC, LIC
-- Note: 'patient_insurance_id' is the OneToOne JoinColumn in the Insurance entity.
-- ===================================================================================
INSERT INTO insurance (id, provider, policy_number, issued, insurance_date, patient_insurance_id)
VALUES (gen_random_uuid(), 'HDFC', 100000001, true, NOW(), (SELECT id FROM patient WHERE email = 'rahul@gmail.com')),
       (gen_random_uuid(), 'ICICIC', 200000002, true, NOW(), (SELECT id FROM patient WHERE email = 'anjali@gmail.com')),
       (gen_random_uuid(), 'LIC', 300000003, true, NOW(), (SELECT id FROM patient WHERE email = 'rohan@gmail.com')),
       (gen_random_uuid(), 'HDFC', 400000004, true, NOW(), (SELECT id FROM patient WHERE email = 'priya@gmail.com')),
       (gen_random_uuid(), 'ICICIC', 500000005, true, NOW(), (SELECT id FROM patient WHERE email = 'amit@gmail.com')),
       (gen_random_uuid(), 'LIC', 600000006, false, NOW(), (SELECT id FROM patient WHERE email = 'sneha@gmail.com')),
       (gen_random_uuid(), 'HDFC', 700000007, true, NOW(), (SELECT id FROM patient WHERE email = 'vikram@gmail.com')),
       (gen_random_uuid(), 'ICICIC', 800000008, true, NOW(), (SELECT id FROM patient WHERE email = 'neha@gmail.com')),
       (gen_random_uuid(), 'LIC', 900000009, true, NOW(), (SELECT id FROM patient WHERE email = 'arjun@gmail.com')),
       (gen_random_uuid(), 'HDFC', 100000010, true, NOW(), (SELECT id FROM patient WHERE email = 'pooja@gmail.com')),
       (gen_random_uuid(), 'ICICIC', 110000011, false, NOW(), (SELECT id FROM patient WHERE email = 'karan@gmail.com')),
       (gen_random_uuid(), 'LIC', 120000012, true, NOW(), (SELECT id FROM patient WHERE email = 'deepika@gmail.com'));

-- ===================================================================================
-- 5. INSERT APPOINTMENTS (15 Records)
-- Enums: GENERAL_CHECKUP, SPECIALIST_CONSULTATION, EMERGENCY, FOLLOW_UP, SURGERY, DIAGNOSTIC_TEST
-- ===================================================================================
INSERT INTO appointment (id, category, reason_description, appointment_date_time, patient_appointment_id,
                         doctor_appointment_id)
VALUES (gen_random_uuid(), 'GENERAL_CHECKUP', 'Routine heart checkup', NOW() + INTERVAL '1 day',
        (SELECT id FROM patient WHERE email = 'rahul@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'arnab.cardio@gmail.com')),

       (gen_random_uuid(), 'SPECIALIST_CONSULTATION', 'Migraine consultation', NOW() + INTERVAL '2 days',
        (SELECT id FROM patient WHERE email = 'anjali@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'sen.neuro@gmail.com')),

       (gen_random_uuid(), 'EMERGENCY', 'Fractured arm', NOW() - INTERVAL '1 day',
        (SELECT id FROM patient WHERE email = 'rohan@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'priya.ortho@gmail.com')),

       (gen_random_uuid(), 'GENERAL_CHECKUP', 'Child vaccination', NOW() + INTERVAL '3 days',
        (SELECT id FROM patient WHERE email = 'priya@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'john.peds@gmail.com')),

       (gen_random_uuid(), 'DIAGNOSTIC_TEST', 'Skin allergy test', NOW() + INTERVAL '4 days',
        (SELECT id FROM patient WHERE email = 'amit@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'emily.derma@gmail.com')),

       (gen_random_uuid(), 'FOLLOW_UP', 'Post-pregnancy checkup', NOW() + INTERVAL '5 days',
        (SELECT id FROM patient WHERE email = 'sneha@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'michael.gyn@gmail.com')),

       (gen_random_uuid(), 'SPECIALIST_CONSULTATION', 'Anxiety counseling', NOW() + INTERVAL '2 days',
        (SELECT id FROM patient WHERE email = 'vikram@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'sarah.psych@gmail.com')),

       (gen_random_uuid(), 'DIAGNOSTIC_TEST', 'MRI Scan', NOW() + INTERVAL '1 week',
        (SELECT id FROM patient WHERE email = 'neha@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'linda.radio@gmail.com')),

       (gen_random_uuid(), 'SURGERY', 'Appendicitis surgery', NOW() + INTERVAL '2 days',
        (SELECT id FROM patient WHERE email = 'arjun@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'robert.surg@gmail.com')),

       (gen_random_uuid(), 'GENERAL_CHECKUP', 'Regular checkup', NOW() + INTERVAL '10 days',
        (SELECT id FROM patient WHERE email = 'pooja@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'alice.c@gmail.com')),

       (gen_random_uuid(), 'SPECIALIST_CONSULTATION', 'Back pain', NOW() + INTERVAL '3 days',
        (SELECT id FROM patient WHERE email = 'karan@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'charlie.p@gmail.com')),

       (gen_random_uuid(), 'FOLLOW_UP', 'Neurology follow up', NOW() + INTERVAL '6 days',
        (SELECT id FROM patient WHERE email = 'deepika@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'bob.m@gmail.com')),

       (gen_random_uuid(), 'EMERGENCY', 'High fever pediatric', NOW(),
        (SELECT id FROM patient WHERE email = 'rahul@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'dua.l@gmail.com')),

       (gen_random_uuid(), 'SURGERY', 'Knee replacement', NOW() + INTERVAL '2 weeks',
        (SELECT id FROM patient WHERE email = 'amit@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'elton.j@gmail.com')),

       (gen_random_uuid(), 'SPECIALIST_CONSULTATION', 'Heart palpitations', NOW() + INTERVAL '1 day',
        (SELECT id FROM patient WHERE email = 'sneha@gmail.com'),
        (SELECT id FROM doctor WHERE email = 'arnab.cardio@gmail.com'));

-- ===================================================================================
-- 6. INSERT DOCTOR_DEPARTMENT_TABLE (Mapping Table)
-- We must link Head Doctors to their departments, AND Staff doctors to their departments.
-- ===================================================================================

-- 6.1 Link HEAD Doctors to their Departments
INSERT INTO doctor_department_table (department_id, doctor_id)
VALUES ((SELECT id FROM department WHERE department_name = 'Cardiology'),
        (SELECT id FROM doctor WHERE email = 'arnab.cardio@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Neurology'),
        (SELECT id FROM doctor WHERE email = 'sen.neuro@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Orthopedics'),
        (SELECT id FROM doctor WHERE email = 'priya.ortho@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Pediatrics'),
        (SELECT id FROM doctor WHERE email = 'john.peds@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Dermatology'),
        (SELECT id FROM doctor WHERE email = 'emily.derma@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Gynecology'),
        (SELECT id FROM doctor WHERE email = 'michael.gyn@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Psychiatry'),
        (SELECT id FROM doctor WHERE email = 'sarah.psych@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Oncology'),
        (SELECT id FROM doctor WHERE email = 'david.onco@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Radiology'),
        (SELECT id FROM doctor WHERE email = 'linda.radio@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'General Surgery'),
        (SELECT id FROM doctor WHERE email = 'robert.surg@gmail.com'));

-- 6.2 Link STAFF Doctors to their Departments
INSERT INTO doctor_department_table (department_id, doctor_id)
VALUES ((SELECT id FROM department WHERE department_name = 'Cardiology'),
        (SELECT id FROM doctor WHERE email = 'alice.c@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Neurology'),
        (SELECT id FROM doctor WHERE email = 'bob.m@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Orthopedics'),
        (SELECT id FROM doctor WHERE email = 'charlie.p@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'Pediatrics'),
        (SELECT id FROM doctor WHERE email = 'dua.l@gmail.com')),
       ((SELECT id FROM department WHERE department_name = 'General Surgery'),
        (SELECT id FROM doctor WHERE email = 'elton.j@gmail.com'));
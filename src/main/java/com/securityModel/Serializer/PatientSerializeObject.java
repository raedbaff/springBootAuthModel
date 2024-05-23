package com.securityModel.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.securityModel.models.Patient;
import com.securityModel.models.RendezVous;

import java.io.IOException;
import java.util.List;

public class PatientSerializeObject extends StdSerializer<Patient> {
    public PatientSerializeObject() {
        this(null);
    }
    public PatientSerializeObject(Class<Patient> t) {
        super(t);
    }

    @Override
    public void serialize(Patient value,
                          JsonGenerator gen,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {
        Patient patient = new Patient();
        if (value != null) {
            patient.setUsername(value.getUsername());
            patient.setId(value.getId());
            patient.setEmail(value.getEmail());
            patient.setRoles(value.getRoles());
            patient.setPhoto(value.getPhoto());
           // patient.setMyRendezVous(value.getMyRendezVous());
            patient.setAdresse(value.getAdresse());
            patient.setAge(value.getAge());
            patient.setGender(value.getGender());
            patient.setSocialAccount(value.getSocialAccount());

            patient.setMessagesReceived(value.getMessagesReceived());
            patient.setMessagesSent(value.getMessagesSent());

            gen.writeObject(patient);
        }

    }
}
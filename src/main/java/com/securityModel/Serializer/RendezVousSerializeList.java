package com.securityModel.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.securityModel.models.RendezVous;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RendezVousSerializeList extends StdSerializer<List<RendezVous>> {
    public RendezVousSerializeList(){this(null);};
    public RendezVousSerializeList(Class<List<RendezVous>>c){super(c);}

    @Override
    public void serialize(List<RendezVous> value,
                          JsonGenerator gen,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {
        List<RendezVous>ids=new ArrayList<>();
        if(value!=null)
            for(RendezVous val:value){
                RendezVous rd=new RendezVous();
                rd.setId(val.getId());
                rd.setPatientName(val.getPatientName());
                rd.setConfirmed(val.getConfirmed());

                rd.setAppointmentDate(val.getAppointmentDate());
                rd.setAppointmentReason(val.getAppointmentReason());
                ids.add(rd);
            }
        gen.writeObject(ids);

    }
}

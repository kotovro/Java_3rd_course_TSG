package services;

import models.entities.ListItem;
import models.entities.RequestState;
import models.entities.RequestType;
import models.entities.Resident;
import models.repositories.RepositoryProvider;
import models.repositories.interfaces.IResidentRepository;

import java.util.ArrayList;
import java.util.List;

public class ListService {
    public static List<ListItem> getStatusesList() {
        List<ListItem> statusesList = new ArrayList<ListItem>();
        for (RequestState requestState : RequestState.values()) {
            statusesList.add(new ListItem(requestState.name(), requestState.name()));
        }
        return statusesList;
    }
    public static List<ListItem> getTypesList() {
        List<ListItem> typesList = new ArrayList<ListItem>();
        for (RequestType requestType : RequestType.values()) {
            typesList.add(new ListItem(requestType.name(), requestType.name()));
        }
        return typesList;
    }

    public static List<ListItem> getResidentsList() {
        IResidentRepository residentRepository = RepositoryProvider.getInstance().getResidentRepository();
        List<Resident> list = residentRepository.getAllResidents();
        return list.stream().map(r -> {return new ListItem(Integer.toString(r.getResidentId()), r.getName() + " " + r.getSurname());}).toList();
    }
}

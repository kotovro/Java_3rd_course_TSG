package models.entities;

public enum RequestType {
    COLLECTIVE(1),
    SINGLE_PERSON(2);

    private int requestTypeId;

    RequestType(int id) {
        requestTypeId = id;
    }
}

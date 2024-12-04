package team.proximity.request_management.request_management.validations;

public interface Validator<T> {
    void validate(T input);
}

package team.proximity.provider_profile_service.validations;

public interface Validator<T> {
    void validate(T input);
}

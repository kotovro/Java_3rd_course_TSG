package services;

import view.ValidationTypes;
import view.ViewField;
import view.ViewModel;

import java.util.List;

public class ValidatorService {
    public boolean stringNotEmpty(String string) {
        return string != null && !string.trim().isEmpty();
    }

    public boolean intNotNegative(int number) {
        return number >= 0;
    }

    public boolean stringIsValidInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String validate(ViewModel vm) {
        for (ViewField vf: vm.getParameters())
        {
            String validationError = validate(vf.getAttributeValue(), vf.getValidators());
            if (!validationError.isEmpty()) {
                return vf.getAttributeName() +
                        (vf.getAttributeValue().isEmpty() ? " Empty value" : " Value " + vf.getAttributeValue())  +
                        " is invalid: " +  validationError;
            }
        }
        return "";
    }

    public String validate(String parameter, List<ValidationTypes> validationTypesList) {
        boolean valid = true;
        for (ValidationTypes validationType : validationTypesList) {
            switch (validationType) {
                case STRING_VALID_INTEGER:
                {
                    valid = stringIsValidInteger(parameter);
                    break;
                }
                case INT_NOT_NEGATIVE:
                {
                    valid =  stringIsValidInteger(parameter) && intNotNegative(Integer.parseInt(parameter));
                    break;
                }
                case STRING_NOT_EMPTY:
                {
                    valid =  stringNotEmpty(parameter);
                    break;
                }
                case USER_EXISTS:
                {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                return validationType.getErrorMessage();
            }
        }
        return "";
    }
}

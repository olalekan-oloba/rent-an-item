package com.omegalambdang.rentanitem.feature.account.rentor;

import com.omegalambdang.rentanitem.feature.account.Gender;
import com.omegalambdang.rentanitem.validator.enumValidator.Enum;
import com.omegalambdang.rentanitem.validator.sameFieldValidator.Same;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.omegalambdang.rentanitem.constants.ValidationFieldConstants.*;
import static com.omegalambdang.rentanitem.feature.account.Gender.Constants.FEMALE_VALUE;
import static com.omegalambdang.rentanitem.feature.account.Gender.Constants.MALE_VALUE;

@Data
@SuperBuilder
@NoArgsConstructor
@Same(field1 = "password", field2 = "confirmPassword", message = "Password and Confirm Password do not match")
public class SignUpRentorRequestDto  {

    private static final long serialVersionUID = -6196458615228927518L;

    @NotBlank(message = "First name must not be empty")
    @Size(max = NAMES_COL_SIZE,message ="First name must not be more than "+ NAMES_COL_SIZE +" xters")
    private String firstName;

    @Size(max = NAMES_COL_SIZE,message ="Last name must not be more than "+ NAMES_COL_SIZE +" xters")
    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @Email(regexp = EMAIL_REGEX,message = "Email not a valid email format")
    @NotBlank(message = "Email must not be empty")
    @Size(max = EMAIL_COL_SIZE,message ="Email must not be more than "+ EMAIL_COL_SIZE +" xters")
    private String email;

    @Size(max = PHONE_COL_SIZE,message ="Phone must not be more than "+ PHONE_COL_SIZE +" xters")
    @NotBlank(message = "Phone must not be empty")
    private String phone;

    @NotBlank(message = "Gender must not be empty")
    @Enum(enumClass = Gender.class)
    @Schema(allowableValues = MALE_VALUE+" | "+FEMALE_VALUE)
    private String gender;

    @NotBlank(message = "Contact Address must not be empty")
    @Size(max = CONTACT_ADDRESS_COL_SIZE,message ="Contact Address must not be more than "+ CONTACT_ADDRESS_COL_SIZE +" xters")
    private String contactAddress;

    @NotBlank(message = "Account Name must not be empty")
    @Size(max = ACCOUNT_NAME_COL_SIZE,message ="Account Name must not be more than "+ ACCOUNT_NAME_COL_SIZE +" xters")
    private String accountName;

    @NotBlank(message = "Account Number must not be empty")
    @Pattern(regexp=DIGIT_REGEX,message = "Account Number must be a number")
    @Size(min =ACCOUNT_NO_COL_SIZE, max = ACCOUNT_NO_COL_SIZE,message ="Account Number must be "+ACCOUNT_NO_COL_SIZE+" xters long")
    private String accountNo;

    @NotBlank(message = "Bank must not be empty")
    @Pattern(regexp=DIGIT_REGEX,message = "Bank code must be a number")
    @Size(min =BANK_CODE_SIZE, max = BANK_CODE_SIZE,message ="Bank Code must be "+BANK_CODE_SIZE+" xters long")
    private String bankCode;

    @Positive(message = "State must be number greater than zero")
    private int state;

    @NotBlank(message = "Password must not be empty")
    @Pattern(regexp=PASSWORD_REGEX,message = "Password must contain at least one lowercase letter")
    private String password;

    private String confirmPassword;

    private boolean customerAgreement;//TODO: validation

}
